package com.barbershop.service.interfaces;

import com.barbershop.dto.barbershopservice.BarberServiceCreationDTO;
import com.barbershop.dto.barbershopservice.BarberServiceInfoDTO;
import com.barbershop.dto.barbershopservice.BarberServiceUpdateDTO;
import com.barbershop.enums.BarberServiceCategory;

import java.util.List;

public interface BarberserviceService {

    void registerNewBarberService(BarberServiceCreationDTO newBarberService);

    void deleteBarberservice(Long barberServiceID);

    BarberServiceInfoDTO getBarberServiceInfo(Long barberServiceID);

    List<BarberServiceInfoDTO> getServicesList();

    void updateService(Long barberServiceID, BarberServiceUpdateDTO updateDTO);

    Long getServiceCount();

    Long calculateServicesCreatedThisMonthVsLastMonth();

    Double getAveragePrice();

    Double getAveragePricePercentageVsLastMonth();

    Long getCategoryCount();

    Double getHighestPrice();

    Double getLowestPrice();

    List<BarberServiceInfoDTO> liveSearch(String name, BarberServiceCategory selectedCategory, Double minPrice, Double maxPrice);
}
