package com.service.interfaces;

import com.dto.stats.BarberServiceActiveOnCatalogStatsDTO;
import com.dto.barberservice.BarberServiceCreationDTO;
import com.dto.barberservice.BarberServiceInfoDTO;
import com.dto.barberservice.BarberServiceUpdateDTO;
import com.enums.BarberServiceCategory;

import java.util.List;

public interface BarberserviceService {

    void registerNewBarberService(BarberServiceCreationDTO newBarberService);

    void deleteBarberservice(Long barberServiceID);

    List<BarberServiceInfoDTO> getServicesList();

    void updateService(Long barberServiceID, BarberServiceUpdateDTO updateDTO);

    List<BarberServiceInfoDTO> liveSearch(String name, BarberServiceCategory selectedCategory, Double minPrice, Double maxPrice);

    BarberServiceActiveOnCatalogStatsDTO getActiveOnCatalogStats();
}
