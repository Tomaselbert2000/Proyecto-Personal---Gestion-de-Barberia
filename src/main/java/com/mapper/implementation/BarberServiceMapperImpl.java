package com.mapper.implementation;

import com.dto.barberservice.BarberServiceCreationDTO;
import com.dto.barberservice.BarberServiceInfoDTO;
import com.dto.barberservice.BarberServiceUpdateDTO;
import com.exceptions.common.NullMapperInputException;
import com.mapper.interfaces.BarberServiceMapper;
import com.model.BarberService;
import com.utils.strings.StringCleaner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mapper.helper.MapperHelper.checkIfMapperInputIsNull;

@Component
public class BarberServiceMapperImpl implements BarberServiceMapper {

    @Override
    public BarberService mapBarberServiceCreationDtoToEntity(BarberServiceCreationDTO dto) {

        checkIfMapperInputIsNull(dto);

        LocalDateTime registrationTimestamp = LocalDateTime.now();

        String internalNotes = "";

        if (dto.getInternalNotes() != null) internalNotes = StringCleaner.formatAsSentence(dto.getInternalNotes());

        return BarberService.builder()
                .name(StringCleaner.formatAsSentence(dto.getName()))
                .price(dto.getPrice())
                .serviceCategory(dto.getServiceCategory())
                .registrationTimestamp(registrationTimestamp)
                .internalNotes(internalNotes)
                .isCurrentlyActive(true)
                .build();
    }

    @Override
    public BarberService mapBarberServiceUpdateDtoToEntity(BarberService barberService, BarberServiceUpdateDTO updateDTO) {

        checkIfMapperInputIsNull(updateDTO, barberService);

        setUpdatedDataOnBarberService(updateDTO, barberService);

        return barberService;
    }

    @Override
    public BarberServiceInfoDTO mapBarberServiceToInfoDto(BarberService barberService) {

        checkIfMapperInputIsNull(barberService);

        return BarberServiceInfoDTO.builder()
                .barberServiceId(barberService.getBarbershopServiceID())
                .name(barberService.getName())
                .price(barberService.getPrice())
                .category(barberService.getServiceCategory())
                .internalNotes(barberService.getInternalNotes() == null ? "" : barberService.getInternalNotes())
                .build();
    }

    @Override
    public List<BarberServiceInfoDTO> mapBarberServiceToInfoDto(List<BarberService> serviceList) {

        checkIfMapperInputIsNull(serviceList);

        return serviceList.stream().map(this::mapBarberServiceToInfoDto).collect(Collectors.toList());

    }

    private void setUpdatedDataOnBarberService(BarberServiceUpdateDTO updateDTO, BarberService barberService) {

        if (updateDTO.getName() != null) barberService.setName(StringCleaner.formatAsSentence(updateDTO.getName()));

        if (updateDTO.getPrice() != null) barberService.setPrice(updateDTO.getPrice());

        if (updateDTO.getServiceCategory() != null) barberService.setServiceCategory(updateDTO.getServiceCategory());

        if (updateDTO.getInternalNotes() != null)
            barberService.setInternalNotes(StringCleaner.formatAsSentence(updateDTO.getInternalNotes()));

        if (updateDTO.getIsCurrentlyActive() != null)
            barberService.setIsCurrentlyActive(updateDTO.getIsCurrentlyActive());

        LocalDateTime modificationTimestamp = LocalDateTime.now();

        barberService.setModifiedDate(modificationTimestamp);
    }
}
