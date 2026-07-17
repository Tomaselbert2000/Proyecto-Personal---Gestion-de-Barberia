package com.mapper.interfaces;

import com.dto.barbershopservice.BarberServiceCreationDTO;
import com.dto.barbershopservice.BarberServiceInfoDTO;
import com.dto.barbershopservice.BarberServiceUpdateDTO;
import com.model.BarberService;

import java.util.List;

public interface BarberServiceMapper {

    BarberService mapBarberServiceCreationDtoToEntity(BarberServiceCreationDTO dto);

    BarberService mapBarberServiceUpdateDtoToEntity(BarberService entity, BarberServiceUpdateDTO dto);

    BarberServiceInfoDTO mapBarberServiceToInfoDto(BarberService service);

    List<BarberServiceInfoDTO> mapBarberServiceToInfoDto(List<BarberService> serviceList);
}
