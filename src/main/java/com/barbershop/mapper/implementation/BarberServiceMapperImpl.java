package com.barbershop.mapper.implementation;

import com.barbershop.dto.barbershopservice.BarberServiceCreationDTO;
import com.barbershop.dto.barbershopservice.BarberServiceInfoDTO;
import com.barbershop.dto.barbershopservice.BarberServiceUpdateDTO;
import com.barbershop.exceptions.common.NullMapperInputException;
import com.barbershop.mapper.interfaces.BarberServiceMapper;
import com.barbershop.model.BarberService;
import com.barbershop.utils.strings.StringCleaner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BarberServiceMapperImpl implements BarberServiceMapper {

    private static final String INTERNAL_NOTES_DEFAULT_VALUE = "";

    @Override
    public BarberService mapBarberServiceCreationDtoToEntity(BarberServiceCreationDTO creationDTO) {

        if (creationDTO == null) throw new NullMapperInputException();

        LocalDateTime registrationTimestamp = LocalDateTime.now();

        String internalNotes;

        if(creationDTO.getInternalNotes() != null){

            internalNotes = StringCleaner.formatAsSentence(creationDTO.getInternalNotes());

        }else {

            internalNotes = INTERNAL_NOTES_DEFAULT_VALUE;
        }

        return BarberService.builder()
                .name(StringCleaner.formatAsSentence(creationDTO.getName()))
                .price(creationDTO.getPrice())
                .serviceCategory(creationDTO.getServiceCategory())
                .registrationTimestamp(registrationTimestamp)
                .internalNotes(internalNotes)
                .build();
    }

    @Override
    public BarberService mapBarberServiceUpdateDtoToEntity(BarberService barberService, BarberServiceUpdateDTO updateDTO) {

        if (updateDTO == null || barberService == null) throw new NullMapperInputException();

        setUpdatedDataOnBarberService(updateDTO, barberService);

        return barberService;
    }

    @Override
    public BarberServiceInfoDTO mapBarberServiceToInfoDto(BarberService barberService) {

        if (barberService == null) throw new NullMapperInputException();

        return BarberServiceInfoDTO.builder()
                .barberServiceId(barberService.getBarbershopServiceID())
                .name(barberService.getName())
                .price(barberService.getPrice())
                .category(barberService.getServiceCategory())
                .internalNotes(barberService.getInternalNotes() == null ? INTERNAL_NOTES_DEFAULT_VALUE : barberService.getInternalNotes())
                .build();
    }

    @Override
    public List<BarberServiceInfoDTO> mapBarberServiceToInfoDto(List<BarberService> serviceList) {

        if (serviceList == null) throw new NullMapperInputException();

        return serviceList.stream().map(this::mapBarberServiceToInfoDto).collect(Collectors.toList());

    }

    private void setUpdatedDataOnBarberService(BarberServiceUpdateDTO updateDTO, BarberService barberService) {

        if (updateDTO.getName() != null) barberService.setName(StringCleaner.formatAsSentence(updateDTO.getName()));

        if (updateDTO.getPrice() != null) barberService.setPrice(updateDTO.getPrice());

        if (updateDTO.getServiceCategory() != null) barberService.setServiceCategory(updateDTO.getServiceCategory());

        if (updateDTO.getInternalNotes() != null) barberService.setInternalNotes(StringCleaner.formatAsSentence(updateDTO.getInternalNotes()));

        LocalDateTime modificationTimestamp = LocalDateTime.now();

        barberService.setModifiedDate(modificationTimestamp);
    }
}
