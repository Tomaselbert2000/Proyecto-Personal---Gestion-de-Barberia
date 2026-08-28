package com.mapper.interfaces;

import com.dto.barberservice.BarberServiceCreationDTO;
import com.dto.barberservice.BarberServiceInfoDTO;
import com.dto.barberservice.BarberServiceUpdateDTO;
import com.model.BarberService;

import java.util.List;

public interface BarberServiceMapper {

    BarberService mapBarberServiceCreationDtoToEntity(BarberServiceCreationDTO dto);

    BarberService mapBarberServiceUpdateDtoToEntity(BarberService entity, BarberServiceUpdateDTO dto);

    BarberServiceInfoDTO mapBarberServiceToInfoDto(BarberService entity);

    List<BarberServiceInfoDTO> mapBarberServiceToInfoDto(List<BarberService> entityList);
}
