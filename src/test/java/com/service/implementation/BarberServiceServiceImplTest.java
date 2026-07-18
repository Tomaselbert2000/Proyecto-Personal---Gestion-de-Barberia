package com.service.implementation;

import com.abstract_test_class.BaseServiceTest;
import com.dto.barbershopservice.BarberServiceCreationDTO;
import com.dto.barbershopservice.BarberServiceInfoDTO;
import com.dto.barbershopservice.BarberServiceUpdateDTO;
import com.exceptions.barberservice.*;
import com.mapper.implementation.BarberServiceMapperImpl;
import com.mapper.interfaces.BarberServiceMapper;
import com.model.BarberService;
import com.repository.BarberServiceRepository;
import com.repository.ServicePriceHistoryRepository;
import com.validation.barberservice.BarberServiceValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.List;

import static com.factory.BarberServiceTestDataFactory.*;
import static com.service.helper.BarberserviceServiceTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

public class BarberServiceServiceImplTest extends BaseServiceTest<BarberService, BarberServiceRepository> {

    private final BarberService existingService = buildValidBarberService();
    private final BarberServiceCreationDTO creationDTO = buildValidBarberServiceCreationDTO();
    private final BarberServiceUpdateDTO updateDTO = buildValidBarberServiceUpdateDTO();
    @Mock
    private BarberServiceRepository barberServiceRepository;
    @Mock
    private ServicePriceHistoryRepository servicePriceHistoryRepository;
    @Mock
    private BarberServiceValidator validator;
    @Spy
    private final BarberServiceMapper mapper = new BarberServiceMapperImpl();
    @InjectMocks
    private BarberServiceServiceImpl barberServiceImplInstance;

    @Test
    @DisplayName("Dado un DTO de creación de servicio de barbería con datos correctos, deberá ser persistido exitosamente")
    void givenNewBarberServiceWithValidData_WhenCreating_ThenIsSuccessfullyPersisted() {

        registerBarberService(barberServiceImplInstance, creationDTO);

        verifyCreationProcessSuccess();
    }

    @Test
    @DisplayName("Dado un registro exitoso de un servicio, se persistirá un registro en el historial de precios")
    void givenNewBarberServiceWithValidData_WhenCreatingSuccessfully_ThenServicePriceHistoryObjectIsPersisted() {

        registerBarberService(barberServiceImplInstance, creationDTO);

        verifyThatServicePriceHistory(servicePriceHistoryRepository);
    }

    @Test
    @DisplayName("Dado un DTO de creación con 1 o más campos NULL, arrojará NullBarberServiceInputDataException")
    void givenAnyNullValue_WhenCreating_ThenThrows_NullBarberServiceInputDataException() {

        mockValidatorToThrowException(validator, new NullBarberServiceInputDataException(), creationDTO);

        assertThrows(NullBarberServiceInputDataException.class, () -> registerBarberService(barberServiceImplInstance, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de servicio compuesto de un String inválido, la validación deberá fallar y no persistir cambios")
    void givenInvalidBarberServiceName_WhenCreating_ThenValidationShouldFail() {

        mockValidatorToThrowException(validator, new InvalidBarberServiceNameException(), creationDTO);

        assertThrows(InvalidBarberServiceNameException.class, () -> registerBarberService(barberServiceImplInstance, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de servicio que no cumpla límites de longitud mínima o máxima, la validación deberá fallar y no persistir cambios")
    void givenInvalidNameLength_WhenCreating_ThenValidationShouldFail() {

        mockValidatorToThrowException(validator, new InvalidBarberServiceNameLengthException(), creationDTO);

        assertThrows(InvalidBarberServiceNameLengthException.class, () -> registerBarberService(barberServiceImplInstance, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio de servicio inválido, fallará la validación y no se persistirán los cambios")
    void givenInvalidBarberServicePrice_WhenCreating_ThenValidationShouldFail() {

        mockValidatorToThrowException(validator, new NegativeBarberServicePriceException(), creationDTO);

        assertThrows(NegativeBarberServicePriceException.class, () -> registerBarberService(barberServiceImplInstance, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un servicio de barbería previamente registrado, deberá retornar su información mediante mapeo por DTO informativo")
    void givenExistingBarberService_WhenSearching_ThenReturnsAs_InfoDTO() {

        mockBarberService(barberServiceRepository, existingService);

        BarberServiceInfoDTO returnedDTO = barberServiceImplInstance.getBarberServiceInfo(existingService.getBarbershopServiceID());

        verifyInfoDTOAssertions(returnedDTO);
    }

    @Test
    @DisplayName("Dado un ID de servicio de barbería inexistente, arrojará BarberServiceNotFoundException")
    void givenNonExistingBarberService_WhenSearching_ThenThrows_BarberServiceNotFoundException() {

        mockNonExistingBarberService(barberServiceRepository, existingService);

        assertThrows(BarberServiceNotFoundException.class, () -> barberServiceImplInstance.getBarberServiceInfo(existingService.getBarbershopServiceID()));
    }

    @Test
    @DisplayName("Dados N servicios registrados previamente, al obtener todos se retorna una lista mediante mapeo por DTO informativo")
    void given_N_ExistingServices_WhenGettingAll_ThenReturnsAListOfDTO() {

        mockAllBarberServices(barberServiceRepository, existingService);

        List<BarberServiceInfoDTO> returnedList = barberServiceImplInstance.getServicesList();

        verifyInfoDTOListAssertions(returnedList);
    }

    @Test
    @DisplayName("Dado un ID de servicio inexistente, al intentar actualizarlo arrojará BarberServiceNotFoundException")
    void givenNonExistingBarberService_WhenUpdating_ThenThrows_BarberServiceNotFoundException() {

        mockNonExistingBarberService(barberServiceRepository, existingService);

        assertThrows(BarberServiceNotFoundException.class, () -> updateBarberService(barberServiceImplInstance, existingService, updateDTO));

        verifyThatEntityWasNotSaved();
    }

    @Test
    @DisplayName("Dado un servicio registrado previamente y un DTO de actualización con datos válidos, deberán persistirse los cambios exitosamente")
    void givenExistingBarberService_WhenUpdatingWithValidData_ThenIsSuccessfullyPersisted() {

        mockBarberService(barberServiceRepository, existingService);

        updateBarberService(barberServiceImplInstance, existingService, updateDTO);

        verifyUpdateProcessSuccess();
    }

    @Test
    @DisplayName("Dado un servicio de barbería registrado previamente y un DTO de actualización con un nombre inválido," +
            " arrojará InvalidBarberServiceNameException y no ser persistirán los cambios")
    void givenExistingBarberService_WhenUpdatingWithAnInvalidName_ThenBarberServiceIsNotPersisted() {

        mockBarberService(barberServiceRepository, existingService);

        mockValidatorToThrowException(validator, new InvalidBarberServiceNameException(), updateDTO);

        assertThrows(InvalidBarberServiceNameException.class, () -> updateBarberService(barberServiceImplInstance, existingService, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un servicio de barbería registrado previamente y un DTO de actualización con un nombre" +
            " que no cumple límites de longitud máxima o mínima, arrojará InvalidBarberServiceNameLengthException y no se persistirán los cambios")
    void givenExistingBarberService_WhenUpdatingWithInvalidLengthName_ThenThrows_InvalidBarberServiceNameLengthException() {

        mockBarberService(barberServiceRepository, existingService);

        mockValidatorToThrowException(validator, new InvalidBarberServiceNameLengthException(), updateDTO);

        assertThrows(InvalidBarberServiceNameLengthException.class, () -> updateBarberService(barberServiceImplInstance, existingService, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un servicio de barbería registrado previamente y un DTO de actualización con un nuevo precio inválido, arrojará NegativeBarberServicePriceException" +
            " y no se persistirán los cambios")
    void givenInvalidPrice_WhenUpdating_ThenThrows_NegativeBarberServicePriceException() {

        mockBarberService(barberServiceRepository, existingService);

        mockValidatorToThrowException(validator, new NegativeBarberServicePriceException(), updateDTO);

        assertThrows(NegativeBarberServicePriceException.class, () -> updateBarberService(barberServiceImplInstance, existingService, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un servicio de barbería registrado previamente, deberá poder ser eliminado exitosamente")
    void givenExistingBarberService_WhenDeleting_ThenIsSuccessfullyDeleted() {

        mockBarberService(barberServiceRepository, existingService);

        deleteBarberService(barberServiceImplInstance, existingService);

        verifyThatEntityWasDeleted(existingService);
    }

    @Test
    @DisplayName("Dado un servicio de barbería inexistente, al intentar eliminarlo arrojará BarberServiceNotFoundException")
    void givenNonExistingBarberService_WhenDeleting_ThenThrows_BarberServiceNotFoundException() {

        mockNonExistingBarberService(barberServiceRepository, existingService);

        assertThrows(BarberServiceNotFoundException.class, () -> deleteBarberService(barberServiceImplInstance, existingService));

        verifyThatEntityWasNotDeleted(existingService);
    }

    private void verifyCreationProcessSuccess() {
        verifyValidatorCreationInteraction(validator, creationDTO);
        verifyMapperCreationInteraction(mapper, creationDTO);
        verifyThatEntityWasSaved();
    }

    private void verifyCreationProcessFailure() {

        verifyValidatorCreationInteraction(validator, creationDTO);
        verifyMapperCreationNoInteractions(mapper, creationDTO);
        verifyThatEntityWasNotSaved();
    }

    private void verifyUpdateProcessSuccess() {

        verifyValidatorUpdateInteraction(validator, updateDTO);
        verifyMapperUpdateInteraction(mapper, existingService, updateDTO);
        verifyThatEntityWasSaved();
    }

    private void verifyUpdateProcessFailure() {
        verifyValidatorUpdateInteraction(validator, updateDTO);
        verifyMapperUpdateNoInteractions(mapper, existingService, updateDTO);
        verifyThatEntityWasNotSaved();
    }

    private void verifyInfoDTOAssertions(BarberServiceInfoDTO returnedDTO) {
        assertNotNull(returnedDTO);

        assertAll(
                "Verificación de campos",
                () -> assertEquals(returnedDTO.getName(), existingService.getName()),
                () -> assertEquals(returnedDTO.getPrice(), existingService.getPrice()),
                () -> assertEquals(returnedDTO.getCategory(), existingService.getServiceCategory())
        );
    }

    private void verifyInfoDTOListAssertions(List<BarberServiceInfoDTO> returnedList) {
        assertNotNull(returnedList);

        assertAll(
                "Verificación de campos",
                () -> assertEquals(1, returnedList.size()),
                () -> assertEquals(existingService.getName(), returnedList.getFirst().getName()),
                () -> assertEquals(existingService.getPrice(), returnedList.getFirst().getPrice()),
                () -> assertEquals(existingService.getServiceCategory(), returnedList.getFirst().getCategory())
        );
    }

    @Override
    protected BarberServiceRepository getPrimaryRepository() {

        return barberServiceRepository;
    }
}