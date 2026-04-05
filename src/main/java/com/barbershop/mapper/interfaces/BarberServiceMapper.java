package com.barbershop.mapper.interfaces;

import com.barbershop.dto.barbershopservice.BarberServiceCreationDTO;
import com.barbershop.dto.barbershopservice.BarberServiceInfoDTO;
import com.barbershop.dto.barbershopservice.BarberServiceUpdateDTO;
import com.barbershop.model.BarberService;

import java.util.List;

public interface BarberServiceMapper {

    BarberService mapBarberServiceCreationDtoToEntity(BarberServiceCreationDTO dto);

    BarberService mapBarberServiceUpdateDtoToEntity(BarberService entity, BarberServiceUpdateDTO dto);

    BarberServiceInfoDTO mapBarberServiceToInfoDto(BarberService service);

    List<BarberServiceInfoDTO> mapBarberServiceToInfoDto(List<BarberService> serviceList);
}
