package com.barbershop.service.implementation;

import com.barbershop.dto.barbershopservice.BarberServiceCreationDTO;
import com.barbershop.dto.barbershopservice.BarberServiceInfoDTO;
import com.barbershop.dto.barbershopservice.BarberServiceUpdateDTO;
import com.barbershop.enums.BarberServiceCategory;
import com.barbershop.exceptions.barberservice.BarberServiceNotFoundException;
import com.barbershop.exceptions.barberservice.DuplicatedBarberServiceNameException;
import com.barbershop.model.ServicePriceHistory;
import com.barbershop.repository.ServicePriceHistoryRepository;
import com.barbershop.service.interfaces.BarberserviceService;
import com.barbershop.mapper.interfaces.BarberServiceMapper;
import com.barbershop.model.BarberService;
import com.barbershop.repository.BarberServiceRepository;
import com.barbershop.validation.barberservice.BarberServiceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.barbershop.utils.time.TimeCalculation.*;

@Service
@RequiredArgsConstructor
public class BarberServiceServiceImpl implements BarberserviceService {

    private final BarberServiceRepository barberServiceRepository;

    private final ServicePriceHistoryRepository servicePriceHistoryRepository;

    private final BarberServiceMapper barberServiceMapper;

    private final BarberServiceValidator barberServiceValidator;

    @Override
    @Transactional
    public void registerNewBarberService(BarberServiceCreationDTO creationDTO) {

        barberServiceValidator.validateDTO(creationDTO);

        checkIfNameIsAlreadyRegisteredWhenCreating(creationDTO.getName());

        BarberService serviceToRegister = barberServiceMapper.mapBarberServiceCreationDtoToEntity(creationDTO);

        barberServiceRepository.save(serviceToRegister);

        saveNewServicePriceHistoryInstance(serviceToRegister, serviceToRegister.getPrice());
    }

    @Override
    @Transactional
    public void deleteBarberservice(Long barberServiceID) {

        BarberService serviceOnDB = loadBarberService(barberServiceID);

        barberServiceRepository.delete(serviceOnDB);
    }

    @Override
    public BarberServiceInfoDTO getBarberServiceInfo(Long barberServiceID) {

        BarberService serviceOnDB = loadBarberService(barberServiceID);

        return barberServiceMapper.mapBarberServiceToInfoDto(serviceOnDB);
    }

    @Override
    public List<BarberServiceInfoDTO> getServicesList() {

        return barberServiceMapper.mapBarberServiceToInfoDto(barberServiceRepository.findAll());
    }

    @Override
    @Transactional
    public void updateService(Long barberServiceID, BarberServiceUpdateDTO updateDTO) {

        BarberService serviceOnDB = loadBarberService(barberServiceID);

        barberServiceValidator.validateDTO(updateDTO);

        checkIfNameIsAlreadyRegisteredWhenUpdating(barberServiceID, updateDTO.getName());

        if (!serviceOnDB.getPrice().equals(updateDTO.getPrice())) {

            saveNewServicePriceHistoryInstance(serviceOnDB, updateDTO.getPrice());
        }

        barberServiceRepository.save(barberServiceMapper.mapBarberServiceUpdateDtoToEntity(serviceOnDB, updateDTO));
    }

    @Override
    public Long getServiceCount() {

        return barberServiceRepository.count();
    }

    @Override
    public Long calculateServicesCreatedThisMonthVsLastMonth() {

        LocalDateTime creationDateTimeAfter = getStartOfCurrentMonth().atStartOfDay();
        LocalDateTime creationDateTimeBefore = getEndOfCurrentMonth().atTime(LAST_SECOND_OF_DAY);

        return barberServiceRepository.countByRegistrationTimestampBetween(creationDateTimeAfter, creationDateTimeBefore);
    }

    @Override
    public Double getAveragePrice() {

        return barberServiceRepository.getPriceAverage();
    }

    @Override
    public Double getAveragePricePercentageVsLastMonth() {

        LocalDateTime endOfLastMonth = getEndOfLastMonth();

        Double currentAveragePrice = getAveragePrice();
        Double averagePriceLastMonth = servicePriceHistoryRepository.averageBarberServicePriceUntilDate(endOfLastMonth);

        if (averagePriceLastMonth != null && averagePriceLastMonth != 0.0) {

            return ((currentAveragePrice - averagePriceLastMonth) / averagePriceLastMonth) * 100;
        }

        return 0.0;
    }

    @Override
    public Long getCategoryCount() {

        return barberServiceRepository.countDistinctByServiceCategoryIsNotNull();
    }

    @Override
    public Double getHighestPrice() {

        return barberServiceRepository.getHighestPrice();
    }

    @Override
    public Double getLowestPrice() {

        return barberServiceRepository.getLowestPrice();
    }

    @Override
    public List<BarberServiceInfoDTO> liveSearch(String name, BarberServiceCategory selectedCategory, Double minPrice, Double maxPrice) {

        List<BarberService> barberServices = barberServiceRepository.liveSearchWithFilters(name, selectedCategory, minPrice, maxPrice);

        return barberServiceMapper.mapBarberServiceToInfoDto(barberServices);
    }

    private void checkIfNameIsAlreadyRegisteredWhenCreating(String name) {

        if (barberServiceRepository.existsByNameIgnoreCase(name)) throw new DuplicatedBarberServiceNameException();
    }

    private void checkIfNameIsAlreadyRegisteredWhenUpdating(Long barberServiceID, String name) {

        if (barberServiceRepository.existsByNameAndBarbershopServiceIDNot(name, barberServiceID))
            throw new DuplicatedBarberServiceNameException();
    }

    private BarberService loadBarberService(Long barberServiceID) {

        return barberServiceRepository.findById(barberServiceID).orElseThrow(BarberServiceNotFoundException::new);
    }

    private void saveNewServicePriceHistoryInstance(BarberService serviceOnDB, Double priceAtMoment) {

        LocalDateTime now = LocalDateTime.now();

        ServicePriceHistory servicePriceHistory = ServicePriceHistory.builder()
                .barberService(serviceOnDB)
                .priceAtMoment(priceAtMoment)
                .timestamp(now)
                .build();

        servicePriceHistoryRepository.save(servicePriceHistory);
    }
}
