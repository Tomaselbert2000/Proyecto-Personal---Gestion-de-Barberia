package com.impl;

import com.barbershop.dto.client.ClientCreationDTO;
import com.barbershop.dto.client.ClientInfoDTO;
import com.barbershop.dto.client.ClientUpdateDTO;
import com.barbershop.exceptions.client.*;
import com.barbershop.mapper.implementation.ClientMapperImpl;
import com.barbershop.mapper.interfaces.ClientMapper;
import com.barbershop.model.Client;
import com.barbershop.repository.ClientRepository;
import com.barbershop.service.implementation.ClientServiceImpl;
import com.barbershop.validation.client.ClientValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    ClientRepository clientRepository;

    @Mock
    ClientValidator validator;

    private static final Instant INSTANT = Instant.parse("2026-01-01T10:00:00Z");
    private static final ZoneId ZONE_ID = ZoneId.of("America/Argentina/Buenos_Aires");

    @Mock
    private Clock clock;

    @Spy
    ClientMapper mapper = new ClientMapperImpl();

    @Captor
    ArgumentCaptor<Client> captor;

    @InjectMocks
    private ClientServiceImpl clientService;

    private static final Long CLIENT_ID = 1L;
    private static final String CREATION_DTO_FIRST_NAME = "Tomas Gabriel";
    private static final String CREATION_DTO_LAST_NAME = "Elbert";
    private static final String CREATION_DTO_NATIONAL_ID_CARD_NUMBER = "1234567";
    private static final LocalDate REGISTRATION_DATE = INSTANT.atZone(ZONE_ID).toLocalDate();
    private static final String CREATION_DTO_EMAIL = "tomas@gmail.com";
    private static final List<String> CREATION_DTO_PHONE_LIST = List.of("1122334455");

    private static final String UPDATE_DTO_FIRST_NAME = "Juan";
    private static final String UPDATE_DTO_LAST_NAME = "Perez";
    private static final String UPDATE_DTO_NATIONAL_ID_CARD_NUMBER = "7654321";
    private static final String UPDATE_DTO_EMAIL = "juanperez@gmail.com";
    private static final List<String> UPDATE_DTO_PHONE_LIST = List.of("2244668800");


    private static final LocalDate CREATION_DATE = INSTANT.atZone(ZONE_ID).toLocalDate();

    private Client client;
    private ClientCreationDTO creationDTO;
    private ClientUpdateDTO updateDTO;
    private ClientInfoDTO infoDTO;

    @BeforeEach
    void init() {

        setupClock();
        setupClient();
        setupCreationDTO();
        setupUpdateDTO();
        setupInfoDTO();
    }

    @Test
    @DisplayName("Dado un DTO de creación con datos válidos, deberá ser persistido exitosamente")
    void givenNewClientWithValidData_WhenCreating_ThenIsSuccessfullyPersisted() {

        registerClient();

        verifyValidatorCreationInteraction();
        verifyMapperCreationInteraction();
        verifyThatClientWasRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con 1 o más valores NULL, la validación deberá fallar y el cliente no será persistido")
    void givenAnyNullValue_WhenCreating_ThenValidationShouldFail() {

        doThrow(NullClientInputDataException.class).when(validator).validateDTO(creationDTO);

        assertThrows(NullClientInputDataException.class, this::registerClient);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatClientWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un DNI ya registrado, la validación deberá fallar y el cliente no será persistido")
    void givenExistingNationalIDCardNumber_WhenCreating_ThenValidationShouldFail() {

        mockThatNationalIDCardNumberWillCauseConflict();

        assertThrows(DuplicatedNationalIDCardNumberException.class, this::registerClient);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatClientWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un DNI que contiene caractéres inválidos, la validación fallará y el cliente no será persistido")
    void givenNationalIDCardNumberWithInvalidCharacters_WhenCreating_ThenValidationShouldFail() {

        doThrow(InvalidNationalIDCardNumberException.class).when(validator).validateDTO(creationDTO);

        assertThrows(InvalidNationalIDCardNumberException.class, this::registerClient);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatClientWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un DNI que no cumpla límites de longitud, la validación deberá fallar y el cliente no será persistido")
    void givenNationalIDCardNumberWithInvalidLength_WhenCreating_ThenValidationShouldFail() {

        doThrow(InvalidNationalIDCardNumberLengthException.class).when(validator).validateDTO(creationDTO);

        assertThrows(InvalidNationalIDCardNumberLengthException.class, this::registerClient);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatClientWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con 1 o más nombres que contengan caractéres inválidos, la validación deberá fallar y el cliente no será persistido")
    void givenInvalidFirstOrLastName_WhenCreating_ThenValidationShouldFail() {

        doThrow(InvalidClientNameException.class).when(validator).validateDTO(creationDTO);

        assertThrows(InvalidClientNameException.class, this::registerClient);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatClientWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un email ya registrado, la validación deberá fallar y el cliente no será persistido")
    void givenExistingEmail_WhenCreating_ThenValidationShoulFail() {

        mockThatEmailWillCauseConflict();

        assertThrows(DuplicatedEmailException.class, this::registerClient);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatClientWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un email inválido, la validación deberá fallar y el cliente no será persistido")
    void givenEmailWithInvalidCharacters_WhenCreating_ThenValidationShouldFail() {

        doThrow(InvalidEmailException.class).when(validator).validateDTO(creationDTO);

        assertThrows(InvalidEmailException.class, this::registerClient);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatClientWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación cuyo valor DNI esté en blanco, la validación fallará y el cliente no será persistido")
    void givenBlankNationalIDCardNumber_WhenCreating_ThenValidationShouldFail() {

        doThrow(BlankClientNationalIDCardNumberException.class).when(validator).validateDTO(creationDTO);

        assertThrows(BlankClientNationalIDCardNumberException.class, this::registerClient);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatClientWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con 1 o más nombres en blanco, la validación fallará y el cliente no será persistido")
    void givenAnyBlankName_WhenCreating_ThenValidationShouldFail() {

        doThrow(BlankClientNameException.class).when(validator).validateDTO(creationDTO);

        assertThrows(BlankClientNameException.class, this::registerClient);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatClientWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un email en blanco, la validación fallará y el cliente no será persistido")
    void givenBlankEmail_WhenCreating_ThenValidationShouldFail() {

        doThrow(BlankClientEmailException.class).when(validator).validateDTO(creationDTO);

        assertThrows(BlankClientEmailException.class, this::registerClient);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatClientWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación que incluya una lista de números de teléfono con 1 o más valores inválidos, la validación fallará y el cliente no será persistido")
    void givenInvalidPhone_WhenCreating_ThenClientIsNotPersisted() {

        doThrow(InvalidPhoneNumberException.class).when(validator).validateDTO(creationDTO);

        assertThrows(InvalidPhoneNumberException.class, this::registerClient);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatClientWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación que incluya una lista de números de teléfono con 1 o más valores que no respeten longitudes válidas" +
            ", la validación fallará y el cliente no será persistido")
    void givenInvalidPhoneLength_WhenCreating_ThenClientIsNotPersisted() {

        doThrow(InvalidPhoneNumberLengthException.class).when(validator).validateDTO(creationDTO);

        assertThrows(InvalidPhoneNumberLengthException.class, this::registerClient);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatClientWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación que incluya una lista de números de teléfono vacía, la validación fallará y el cliente no será persistido")
    void givenEmptyPhoneList_WhenCreating_ThenClientIsNotPersisted() {

        doThrow(EmptyPhoneNumbersListException.class).when(validator).validateDTO(creationDTO);

        assertThrows(EmptyPhoneNumbersListException.class, this::registerClient);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatClientWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un cliente previamente registrado, deberá retornar su información mediante DTO informativo")
    void givenExistingClient_WhenSearching_ThenReturnsItsInformation() {

        mockClientByNationalIDCardNumber();
        mockMapperToReturnInfoDTO();

        ClientInfoDTO returnedDTO = clientService.getClientInfo(CREATION_DTO_NATIONAL_ID_CARD_NUMBER);

        assertAll(
                "Verificación de campos",
                () -> assertNotNull(returnedDTO),
                () -> assertEquals(returnedDTO.getNationalIdentityCardNumber(), client.getNationalIdentityCardNumber()),
                () -> assertEquals(returnedDTO.getFirstName(), client.getFirstName()),
                () -> assertEquals(returnedDTO.getLastName(), client.getLastName()),
                () -> assertEquals(returnedDTO.getRegistrationDate(), client.getRegistrationDate())
        );
    }

    @Test
    @DisplayName("Dado un DNI de cliente no registrado, al buscarlo arrojará ClientNotFoundException")
    void givenNonExistingClient_WhenSearching_ThenThrows_ClientNotFoundException() {

        mockThatNationalIDCardNumberDoesNotExist();

        assertThrows(ClientNotFoundException.class, () -> clientService.getClientInfo(CREATION_DTO_NATIONAL_ID_CARD_NUMBER));
    }

    @Test
    @DisplayName("Dado un cliente registrado previamente y un DTO de actualización con información correcta, deberá persistir los cambios exitosamente")
    void givenExistingClient_WhenUpdatingWithValidData_ThenIsSuccessfullyPersisted() {

        mockClientByNationalIDCardNumber();

        updateClient();

        captureClient();

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateInteraction();

        Client clientCaptured = captor.getValue();

        assertAll(
                "Verificación de campos",
                () -> assertNotNull(clientCaptured),
                () -> assertEquals(UPDATE_DTO_NATIONAL_ID_CARD_NUMBER, clientCaptured.getNationalIdentityCardNumber()),
                () -> assertEquals(UPDATE_DTO_FIRST_NAME, clientCaptured.getFirstName()),
                () -> assertEquals(UPDATE_DTO_LAST_NAME, clientCaptured.getLastName()),
                () -> assertEquals(UPDATE_DTO_EMAIL, clientCaptured.getEmail()),
                () -> assertEquals(UPDATE_DTO_PHONE_LIST, clientCaptured.getPhoneNumbersList())
        );
    }

    @Test
    @DisplayName("Dado un DNI de cliente inexistente, al intentar actualizarlo deberá arrojar ClientNotFoundException")
    void givenNonExistingClient_WhenUpdating_ThenThrows_ClientNotFoundException() {

        mockThatNationalIDCardNumberDoesNotExist();

        assertThrows(ClientNotFoundException.class, this::updateClient);

        verifyMapperUpdateNoInteractions();
        verifyThatClientWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un DNI ya registrado, la validación deberá fallar y no se persistirán cambios")
    void givenExistingClient_WhenUpdatingWithExistingNationalIDCardNumber_ThenValidationShouldFail() {

        mockClientByNationalIDCardNumber();
        mockThatNationalIDCardNumberWillCauseConflictOnUpdate();

        assertThrows(DuplicatedNationalIDCardNumberException.class, this::updateClient);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatClientWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un DNI inválido, la validación deberá fallar y no se persistirán los cambios")
    void givenInvalidNationalIDCardNumber_WhenUpdating_ThenValidationShouldFail() {

        mockClientByNationalIDCardNumber();

        doThrow(InvalidNationalIDCardNumberException.class).when(validator).validateDTO(updateDTO);

        assertThrows(InvalidNationalIDCardNumberException.class, this::updateClient);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatClientWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un DNI en blanco, la validación fallará y no se persistirán los cambios")
    void givenBlankNationalIDCardNumber_WhenUpdating_ThenClientIsNotPersisted() {

        mockClientByNationalIDCardNumber();

        doThrow(BlankClientNationalIDCardNumberException.class).when(validator).validateDTO(updateDTO);

        assertThrows(BlankClientNationalIDCardNumberException.class, this::updateClient);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatClientWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con nombres en blanco, la validación fallará y no se persistirán los cambios")
    void givenBlankNames_WhenUpdating_ThenClientIsNotPersisted() {

        mockClientByNationalIDCardNumber();

        doThrow(BlankClientNameException.class).when(validator).validateDTO(updateDTO);

        assertThrows(BlankClientNameException.class, this::updateClient);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatClientWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un email ya registrado en otro cliente, la validación fallará y no ser persistirán cambios")
    void givenExistingEmail_WhenUpdating_ThenClientIsNotPersisted() {

        mockThatEmailWillCauseConflictOnUpdate();
        mockClientByNationalIDCardNumber();

        assertThrows(DuplicatedEmailException.class, this::updateClient);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatClientWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una lista de números de teléfono con 1 o más números inválidos, la validación fallará y no se persistirán cambios")
    void givenInvalidPhone_WhenUpdating_ThenClientIsNotPersisted() {

        mockClientByNationalIDCardNumber();

        doThrow(InvalidPhoneNumberException.class).when(validator).validateDTO(updateDTO);

        assertThrows(InvalidPhoneNumberException.class, this::updateClient);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatClientWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una lista de números de teléfono con 1 o más números que no respeten longitudes" +
            ", la validación fallará y no se persistirán cambios")
    void givenInvalidPhoneLength_WhenUpdating_ThenClientIsNotPersisted() {

        mockClientByNationalIDCardNumber();

        doThrow(InvalidPhoneNumberLengthException.class).when(validator).validateDTO(updateDTO);

        assertThrows(InvalidPhoneNumberLengthException.class, this::updateClient);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatClientWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una lista de números de teléfono vacía, la validación fallará y no se persistirán cambios")
    void givenEmptyPhoneList_WhenUpdating_ThenClientIsNotPersisted() {

        mockClientByNationalIDCardNumber();

        doThrow(EmptyPhoneNumbersListException.class).when(validator).validateDTO(updateDTO);

        assertThrows(EmptyPhoneNumbersListException.class, this::updateClient);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatClientWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un email inválido, la validación fallará y no se persistirán cambios")
    void givenInvalidEmail_WhenUpdating_ThenClientIsNotPersisted() {

        mockClientByNationalIDCardNumber();

        doThrow(InvalidEmailException.class).when(validator).validateDTO(updateDTO);

        assertThrows(InvalidEmailException.class, this::updateClient);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatClientWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un cliente registrado previamente, deberá poder ser eliminado exitosamente")
    void givenExistingClient_WhenDeleting_ThenIsSuccessufullyDeleted() {

        mockClientByNationalIDCardNumber();

        deleteClient();

        verifyThatClientWasDeleted();
    }

    @Test
    @DisplayName("Dado un DNI de cliente no registrado, al intentar eliminarlo arrojará ClientNotFoundException")
    void givenNonExistingClient_WhenDeleting_ThenThrows_ClientNotFoundException() {

        mockThatNationalIDCardNumberDoesNotExist();

        assertThrows(ClientNotFoundException.class, this::deleteClient);

        verifyThatClientWasNotDeleted();
    }

    private void setupClock() {

        lenient().when(clock.instant()).thenReturn(INSTANT);
        lenient().when(clock.getZone()).thenReturn(ZONE_ID);
    }

    private void setupClient() {

        client = Client.builder()
                .clientID(CLIENT_ID)
                .nationalIdentityCardNumber(CREATION_DTO_NATIONAL_ID_CARD_NUMBER)
                .firstName(CREATION_DTO_FIRST_NAME)
                .lastName(CREATION_DTO_LAST_NAME)
                .registrationDate(REGISTRATION_DATE)
                .email(CREATION_DTO_EMAIL)
                .phoneNumbersList(CREATION_DTO_PHONE_LIST)
                .build();
    }

    private void setupCreationDTO() {

        creationDTO = ClientCreationDTO.builder()
                .nationalIdentityCardNumber(CREATION_DTO_NATIONAL_ID_CARD_NUMBER)
                .firstName(CREATION_DTO_FIRST_NAME)
                .lastName(CREATION_DTO_LAST_NAME)
                .email(CREATION_DTO_EMAIL)
                .phoneNumbersList(CREATION_DTO_PHONE_LIST)
                .build();
    }

    private void setupUpdateDTO() {

        updateDTO = ClientUpdateDTO.builder()
                .nationalIdentityCardNumber(UPDATE_DTO_NATIONAL_ID_CARD_NUMBER)
                .firstName(UPDATE_DTO_FIRST_NAME)
                .lastName(UPDATE_DTO_LAST_NAME)
                .email(UPDATE_DTO_EMAIL)
                .phoneNumbersList(UPDATE_DTO_PHONE_LIST)
                .build();
    }

    private void setupInfoDTO() {

        infoDTO = ClientInfoDTO.builder()
                .nationalIdentityCardNumber(client.getNationalIdentityCardNumber())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .registrationDate(client.getRegistrationDate())
                .build();
    }

    private void registerClient() {

        clientService.registerNewClient(creationDTO);
    }

    private void updateClient() {

        clientService.updateClient(CREATION_DTO_NATIONAL_ID_CARD_NUMBER, updateDTO);
    }

    private void deleteClient() {

        clientService.deleteClient(CREATION_DTO_NATIONAL_ID_CARD_NUMBER);
    }

    private void captureClient() {

        verify(clientRepository).save(captor.capture());
    }

    private void verifyValidatorCreationInteraction() {

        verify(validator).validateDTO(creationDTO);
    }

    private void verifyValidatorUpdateInteraction() {

        verify(validator).validateDTO(updateDTO);
    }

    private void verifyMapperCreationInteraction() {

        verify(mapper).mapClientCreationDTOtoEntity(creationDTO, CREATION_DATE);
    }

    private void verifyMapperUpdateInteraction() {

        verify(mapper).mapClientUpdateDTOtoEntity(client, updateDTO);
    }

    private void verifyMapperCreationNoInteractions() {

        verify(mapper, never()).mapClientCreationDTOtoEntity(creationDTO, CREATION_DATE);
    }

    private void verifyMapperUpdateNoInteractions() {

        verify(mapper, never()).mapClientUpdateDTOtoEntity(client, updateDTO);
    }

    private void verifyThatClientWasRegistered() {

        verify(clientRepository, times(1)).save(any());
    }

    private void verifyThatClientWasNotRegistered() {

        verify(clientRepository, never()).save(any());
    }

    private void verifyThatClientWasNotUpdated() {

        verify(clientRepository, never()).save(client);
    }

    private void verifyThatClientWasDeleted() {

        verify(clientRepository, times(1)).delete(any());
    }

    private void verifyThatClientWasNotDeleted() {

        verify(clientRepository, never()).delete(any());
    }

    private void mockThatNationalIDCardNumberWillCauseConflict() {

        when(clientRepository.existsByNationalIdentityCardNumber(CREATION_DTO_NATIONAL_ID_CARD_NUMBER)).thenReturn(true);
    }

    private void mockThatNationalIDCardNumberWillCauseConflictOnUpdate() {

        when(clientRepository.existsByNationalIdentityCardNumberAndClientIDNot(UPDATE_DTO_NATIONAL_ID_CARD_NUMBER, CLIENT_ID)).thenReturn(true);
    }

    private void mockThatNationalIDCardNumberDoesNotExist() {

        when(clientRepository.findByNationalIdentityCardNumber(CREATION_DTO_NATIONAL_ID_CARD_NUMBER)).thenReturn(Optional.empty());
    }

    private void mockThatEmailWillCauseConflict() {

        when(clientRepository.existsByEmail(CREATION_DTO_EMAIL)).thenReturn(true);
    }

    private void mockThatEmailWillCauseConflictOnUpdate() {

        when(clientRepository.existsByEmailAndClientIDNot(UPDATE_DTO_EMAIL, CLIENT_ID)).thenReturn(true);
    }

    private void mockClientByNationalIDCardNumber() {

        when(clientRepository.findByNationalIdentityCardNumber(CREATION_DTO_NATIONAL_ID_CARD_NUMBER)).thenReturn(Optional.of(client));
    }

    private void mockMapperToReturnInfoDTO() {

        when(mapper.mapClientoToInfoDTO(client)).thenReturn(infoDTO);
    }
}
