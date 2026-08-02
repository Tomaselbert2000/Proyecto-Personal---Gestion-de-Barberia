package com.service.implementation;

import com.dto.stats.BarberServiceActiveOnCatalogStatsDTO;
import com.dto.barberservice.BarberServiceCreationDTO;
import com.dto.barberservice.BarberServiceInfoDTO;
import com.dto.barberservice.BarberServiceUpdateDTO;
import com.enums.BarberServiceCategory;
import com.exceptions.barberservice.BarberServiceNotFoundException;
import com.exceptions.barberservice.DuplicatedBarberServiceNameException;
import com.mapper.interfaces.BarberServiceMapper;
import com.model.BarberService;
import com.model.ServicePriceHistory;
import com.repository.BarberServiceRepository;
import com.repository.ServicePriceHistoryRepository;
import com.service.interfaces.BarberserviceService;
import com.validation.barberservice.BarberServiceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
    public List<BarberServiceInfoDTO> liveSearch(String name, BarberServiceCategory selectedCategory, Double minPrice, Double maxPrice) {

        List<BarberService> barberServices = barberServiceRepository.liveSearchWithFilters(name, selectedCategory, minPrice, maxPrice);

        return barberServiceMapper.mapBarberServiceToInfoDto(barberServices);
    }

    @Override
    public BarberServiceActiveOnCatalogStatsDTO getActiveOnCatalogStats() {

        BarberServiceActiveOnCatalogStatsDTO activeStats = barberServiceRepository.getActiveBarberServicesStats();

        if (activeStats != null) return activeStats;

        return emptyActiveOnCatalogStatsDTO();
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

    private BarberServiceActiveOnCatalogStatsDTO emptyActiveOnCatalogStatsDTO() {

        return BarberServiceActiveOnCatalogStatsDTO.builder()
                .amountOfActiveServices(0L)
                .amountOfDifferentCategories(0L)
                .build();
    }
}
