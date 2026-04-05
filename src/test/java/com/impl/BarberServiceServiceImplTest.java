package com.impl;

import com.barbershop.dto.barbershopservice.BarberServiceCreationDTO;
import com.barbershop.dto.barbershopservice.BarberServiceInfoDTO;
import com.barbershop.dto.barbershopservice.BarberServiceUpdateDTO;
import com.barbershop.enums.BarberServiceCategory;
import com.barbershop.exceptions.barberservice.*;
import com.barbershop.mapper.implementation.BarberServiceMapperImpl;
import com.barbershop.mapper.interfaces.BarberServiceMapper;
import com.barbershop.model.BarberService;
import com.barbershop.repository.BarberServiceRepository;
import com.barbershop.service.implementation.BarberServiceServiceImpl;
import com.barbershop.validation.barberservice.BarberServiceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BarberServiceServiceImplTest {

    @Mock
    BarberServiceRepository repository;

    @Mock
    BarberServiceValidator validator;

    @Spy
    BarberServiceMapper mapper = new BarberServiceMapperImpl();

    @InjectMocks
    private BarberServiceServiceImpl barberService;

    private BarberService service;
    private BarberServiceCreationDTO creationDTO;
    private BarberServiceUpdateDTO updateDTO;
    private BarberServiceInfoDTO infoDTO;

    private static final Long BARBER_SERVICE_ID = 1L;
    private static final Double BARBER_SERVICE_PRICE = 25000.0;
    private static final String BARBER_SERVICE_NAME = "Servicio corte + barba";
    private static final String UPDATE_DTO_NAME = "Servicio de corte de pelo completo (incluyendo barba)";
    private static final Double UPDATE_DTO_PRICE = 35000.0;

    @BeforeEach
    void init() {

        setupBarberService();
        setupCreationDTO();
        setupInfoDTO();
        setupUpdateDTO();
    }

    @Test
    @DisplayName("Dado un DTO de creación de servicio de barbería con datos correctos, deberá ser persistido exitosamente")
    void givenNewBarberServiceWithValidData_WhenCreating_ThenIsSuccessfullyPersisted() {

        registerBarberService();

        verifyValidatorCreationInteraction();
        verifyMapperCreationInteraction();
        verifyThatBarberServiceWasRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con 1 o más campos NULL, arrojará NullBarberServiceInputDataException")
    void givenAnyNullValue_WhenCreating_ThenThrows_NullBarberServiceInputDataException() {

        doThrow(NullBarberServiceInputDataException.class).when(validator).validateDTO(creationDTO);

        assertThrows(NullBarberServiceInputDataException.class, this::registerBarberService);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatBarberServiceWasNotPersisted();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de servicio compuesto de un String inválido, la validación deberá fallar y no persistir cambios")
    void givenInvalidBarberServiceName_WhenCreating_ThenValidationShouldFail() {

        doThrow(InvalidBarberServiceNameException.class).when(validator).validateDTO(creationDTO);

        assertThrows(InvalidBarberServiceNameException.class, this::registerBarberService);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatBarberServiceWasNotPersisted();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de servicio que no cumpla límites de longitud mínima o máxima, la validación deberá fallar y no persistir cambios")
    void givenInvalidNameLength_WhenCreating_ThenValidationShouldFail() {

        doThrow(InvalidBarberServiceNameLengthException.class).when(validator).validateDTO(creationDTO);

        assertThrows(InvalidBarberServiceNameLengthException.class, this::registerBarberService);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatBarberServiceWasNotPersisted();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio de servicio inválido, fallará la validación y no se persistirán los cambios")
    void givenInvalidBarberServicePrice_WhenCreating_ThenValidationShouldFail() {

        doThrow(NegativeBarberServicePriceException.class).when(validator).validateDTO(creationDTO);

        assertThrows(NegativeBarberServicePriceException.class, this::registerBarberService);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatBarberServiceWasNotPersisted();
    }

    @Test
    @DisplayName("Dado un servicio de barbería previamente registrado, deberá retornar su información mediante mapeo por DTO informativo")
    void givenExistingBarberService_WhenSearching_ThenReturnsAs_InfoDTO() {

        mockBarberService();
        mockMapperToReturnInfoDTO();

        BarberServiceInfoDTO returnedDTO = barberService.getBarberServiceInfo(BARBER_SERVICE_ID);

        assertAll(
                "Verificación de campos",
                () -> assertNotNull(returnedDTO),
                () -> assertEquals(returnedDTO.getName(), service.getName()),
                () -> assertEquals(returnedDTO.getPrice(), service.getPrice()),
                () -> assertEquals(returnedDTO.getCategory(), service.getServiceCategory())
        );
    }

    @Test
    @DisplayName("Dado un ID de servicio de barbería inexistente, arrojará BarberServiceNotFoundException")
    void givenNonExistingBarberService_WhenSearching_ThenThrows_BarberServiceNotFoundException() {

        mockNonExistingBarberService();

        assertThrows(BarberServiceNotFoundException.class, () -> barberService.getBarberServiceInfo(BARBER_SERVICE_ID));
    }

    @Test
    @DisplayName("Dados N servicios registrados previamente, al obtener todos se retorna una lista mediante mapeo por DTO informativo")
    void given_N_ExistingServices_WhenGettingAll_ThenReturnsAListOfDTO() {

        mockAllBarberServices();

        mockMapperToReturnInfoDTO_List();

        List<BarberServiceInfoDTO> returnedList = barberService.getServicesList();

        assertAll(
                "Verificación de campos",
                () -> assertNotNull(returnedList),
                () -> assertEquals(1, returnedList.size()),
                () -> assertEquals(service.getName(), returnedList.getFirst().getName()),
                () -> assertEquals(service.getPrice(), returnedList.getFirst().getPrice()),
                () -> assertEquals(service.getServiceCategory(), returnedList.getFirst().getCategory())
        );
    }

    @Test
    @DisplayName("Dado un ID de servicio inexistente, al intentar actualizarlo arrojará BarberServiceNotFoundException")
    void givenNonExistingBarberService_WhenUpdating_ThenThrows_BarberServiceNotFoundException() {

        mockNonExistingBarberService();

        assertThrows(BarberServiceNotFoundException.class, this::updateBarberService);

        verifyThatBarberServiceWasNotPersisted();
    }

    @Test
    @DisplayName("Dado un servicio registrado previamente y un DTO de actualización con datos válidos, deberán persistirse los cambios exitosamente")
    void givenExistingBarberService_WhenUpdatingWithValidData_ThenIsSuccessfullyPersisted() {

        mockBarberService();

        updateBarberService();

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateInteraction();
        verifyThatBarberServiceWasRegistered();
    }

    @Test
    @DisplayName("Dado un servicio de barbería registrado previamente y un DTO de actualización con un nombre inválido," +
            " arrojará InvalidBarberServiceNameException y no ser persistirán los cambios")
    void givenExistingBarberService_WhenUpdatingWithAnInvalidName_ThenBarberServiceIsNotPersisted() {

        mockBarberService();

        doThrow(InvalidBarberServiceNameException.class).when(validator).validateDTO(updateDTO);

        assertThrows(InvalidBarberServiceNameException.class, this::updateBarberService);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatBarberServiceWasNotPersisted();
    }

    @Test
    @DisplayName("Dado un servicio de barbería registrado previamente y un DTO de actualización con un nombre" +
            " que no cumple límites de longitud máxima o mínima, arrojará InvalidBarberServiceNameLengthException y no se persistirán los cambios")
    void givenExistingBarberService_WhenUpdatingWithInvalidLengthName_ThenThrows_InvalidBarberServiceNameLengthException() {

        mockBarberService();

        doThrow(InvalidBarberServiceNameLengthException.class).when(validator).validateDTO(updateDTO);

        assertThrows(InvalidBarberServiceNameLengthException.class, this::updateBarberService);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatBarberServiceWasNotPersisted();
    }

    @Test
    @DisplayName("Dado un servicio de barbería registrado previamente y un DTO de actualización con un nuevo precio inválido, arrojará NegativeBarberServicePriceException" +
            " y no se persistirán los cambios")
    void givenInvalidPrice_WhenUpdating_ThenThrows_NegativeBarberServicePriceException() {

        mockBarberService();

        doThrow(NegativeBarberServicePriceException.class).when(validator).validateDTO(updateDTO);

        assertThrows(NegativeBarberServicePriceException.class, this::updateBarberService);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatBarberServiceWasNotPersisted();
    }

    @Test
    @DisplayName("Dado un servicio de barbería registrado previamente, deberá poder ser eliminado exitosamente")
    void givenExistingBarberService_WhenDeleting_ThenIsSuccessfullyDeleted() {

        mockBarberService();

        deleteBarberService();

        verifyThatBarberServiceWasDeleted();
    }

    @Test
    @DisplayName("Dado un servicio de barbería inexistente, al intentar eliminarlo arrojará BarberServiceNotFoundException")
    void givenNonExistingBarberService_WhenDeleting_ThenThrows_BarberServiceNotFoundException() {

        mockNonExistingBarberService();

        assertThrows(BarberServiceNotFoundException.class, this::deleteBarberService);

        verifyThatBarberServiceWasNotDeleted();
    }

    private void setupBarberService() {

        service = BarberService.builder()
                .barbershopServiceID(BARBER_SERVICE_ID)
                .name(BARBER_SERVICE_NAME)
                .price(BARBER_SERVICE_PRICE)
                .serviceCategory(BarberServiceCategory.CORTE_Y_BARBA)
                .build();
    }

    private void setupCreationDTO() {

        creationDTO = BarberServiceCreationDTO.builder()
                .name(BARBER_SERVICE_NAME)
                .price(BARBER_SERVICE_PRICE)
                .serviceCategory(BarberServiceCategory.CORTE_Y_BARBA)
                .build();
    }

    private void setupUpdateDTO() {

        updateDTO = BarberServiceUpdateDTO.builder()
                .name(UPDATE_DTO_NAME)
                .price(UPDATE_DTO_PRICE)
                .serviceCategory(BarberServiceCategory.CORTE_Y_BARBA)
                .build();
    }

    private void setupInfoDTO() {

        infoDTO = BarberServiceInfoDTO.builder()
                .name(service.getName())
                .price(service.getPrice())
                .category(service.getServiceCategory())
                .build();
    }

    private void registerBarberService() {

        barberService.registerNewBarberService(creationDTO);
    }

    private void updateBarberService() {

        barberService.updateService(BARBER_SERVICE_ID, updateDTO);
    }

    private void deleteBarberService() {

        barberService.deleteBarberservice(BARBER_SERVICE_ID);
    }

    private void verifyValidatorCreationInteraction() {

        verify(validator).validateDTO(creationDTO);
    }

    private void verifyValidatorUpdateInteraction() {

        verify(validator).validateDTO(updateDTO);
    }

    private void verifyMapperCreationInteraction() {

        verify(mapper).mapBarberServiceCreationDtoToEntity(creationDTO);
    }

    private void verifyMapperUpdateInteraction() {

        verify(mapper).mapBarberServiceUpdateDtoToEntity(service, updateDTO);
    }

    private void verifyMapperCreationNoInteractions() {

        verify(mapper, never()).mapBarberServiceCreationDtoToEntity(creationDTO);
    }

    private void verifyMapperUpdateNoInteractions() {

        verify(mapper, never()).mapBarberServiceUpdateDtoToEntity(service, updateDTO);
    }

    private void verifyThatBarberServiceWasRegistered() {

        verify(repository, times(1)).save(any());
    }

    private void verifyThatBarberServiceWasNotPersisted() {

        verify(repository, never()).save(any());
    }

    private void verifyThatBarberServiceWasDeleted() {

        verify(repository, times(1)).delete(any());
    }

    private void verifyThatBarberServiceWasNotDeleted() {

        verify(repository, never()).delete(any());
    }

    private void mockBarberService() {

        when(repository.findById(BARBER_SERVICE_ID)).thenReturn(Optional.of(service));
    }

    private void mockAllBarberServices() {

        List<BarberService> serviceList = List.of(service);

        when(repository.findAll()).thenReturn(serviceList);
    }

    private void mockNonExistingBarberService() {

        when(repository.findById(BARBER_SERVICE_ID)).thenReturn(Optional.empty());
    }

    private void mockMapperToReturnInfoDTO() {

        when(mapper.mapBarberServiceToInfoDto(service)).thenReturn(infoDTO);
    }

    private void mockMapperToReturnInfoDTO_List() {

        List<BarberService> serviceList = List.of(service);
        List<BarberServiceInfoDTO> infoDTOList = List.of(infoDTO);

        when(mapper.mapBarberServiceToInfoDto(serviceList)).thenReturn(infoDTOList);
    }
}
