package com.service.helper;

import com.dto.barbershopservice.BarberServiceCreationDTO;
import com.dto.barbershopservice.BarberServiceUpdateDTO;
import com.mapper.interfaces.BarberServiceMapper;
import com.model.BarberService;
import com.repository.BarberServiceRepository;
import com.repository.ServicePriceHistoryRepository;
import com.service.interfaces.BarberserviceService;
import com.validation.barberservice.BarberServiceValidator;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class BarberserviceServiceTestHelper {

    public static void registerBarberService(BarberserviceService barberService, BarberServiceCreationDTO creationDTO) {

        barberService.registerNewBarberService(creationDTO);
    }

    public static void updateBarberService(BarberserviceService barberserviceService, BarberService barberService, BarberServiceUpdateDTO updateDTO) {

        barberserviceService.updateService(barberService.getBarbershopServiceID(), updateDTO);
    }

    public static void deleteBarberService(BarberserviceService barberService, BarberService serviceToDelete) {

        barberService.deleteBarberservice(serviceToDelete.getBarbershopServiceID());
    }

    public static void verifyValidatorCreationInteraction(BarberServiceValidator validator, BarberServiceCreationDTO creationDTO) {

        verify(validator).validateDTO(creationDTO);
    }

    public static void verifyValidatorUpdateInteraction(BarberServiceValidator validator, BarberServiceUpdateDTO updateDTO) {

        verify(validator).validateDTO(updateDTO);
    }

    public static void verifyMapperCreationInteraction(BarberServiceMapper mapper, BarberServiceCreationDTO creationDTO) {

        verify(mapper).mapBarberServiceCreationDtoToEntity(creationDTO);
    }

    public static void verifyMapperUpdateInteraction(BarberServiceMapper mapper, BarberService existingService, BarberServiceUpdateDTO updateDTO) {

        verify(mapper).mapBarberServiceUpdateDtoToEntity(existingService, updateDTO);
    }

    public static void verifyMapperCreationNoInteractions(BarberServiceMapper mapper, BarberServiceCreationDTO creationDTO) {

        verify(mapper, never()).mapBarberServiceCreationDtoToEntity(creationDTO);
    }

    public static void verifyMapperUpdateNoInteractions(BarberServiceMapper mapper, BarberService existingService, BarberServiceUpdateDTO updateDTO) {

        verify(mapper, never()).mapBarberServiceUpdateDtoToEntity(existingService, updateDTO);
    }

    public static void verifyThatServicePriceHistory(ServicePriceHistoryRepository servicePriceHistoryRepository) {

        verify(servicePriceHistoryRepository).save(any());
    }

    public static void mockBarberService(BarberServiceRepository barberServiceRepository, BarberService existingService) {

        when(barberServiceRepository.findById(existingService.getBarbershopServiceID())).thenReturn(Optional.of(existingService));
    }

    public static void mockAllBarberServices(BarberServiceRepository barberServiceRepository, BarberService existingService) {

        List<BarberService> serviceList = List.of(existingService);

        when(barberServiceRepository.findAll()).thenReturn(serviceList);
    }

    public static void mockNonExistingBarberService(BarberServiceRepository barberServiceRepository, BarberService existingService) {

        when(barberServiceRepository.findById(existingService.getBarbershopServiceID())).thenReturn(Optional.empty());
    }

    public static <T> void mockValidatorToThrowException(BarberServiceValidator validator, Exception exception, T dto) {

        doThrow(exception).when(validator).validateDTO(dto);
    }
}
