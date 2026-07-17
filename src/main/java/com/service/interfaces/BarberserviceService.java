package com.service.interfaces;

import com.dto.barbershopservice.BarberServiceCreationDTO;
import com.dto.barbershopservice.BarberServiceInfoDTO;
import com.dto.barbershopservice.BarberServiceUpdateDTO;
import com.enums.BarberServiceCategory;

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
