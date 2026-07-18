package com.service.implementation;

import com.abstract_test_class.BaseServiceTest;
import com.dto.client.ClientCreationDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.client.ClientUpdateDTO;
import com.exceptions.client.*;
import com.mapper.implementation.ClientMapperImpl;
import com.mapper.interfaces.ClientMapper;
import com.model.Client;
import com.repository.ClientRepository;
import com.validation.client.ClientValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.Clock;
import java.time.LocalDate;

import static com.factory.ClientTestDataFactory.*;
import static com.service.helper.ClientServiceTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

public class ClientServiceImplTest extends BaseServiceTest<Client, ClientRepository> {

    private final Client client = buildValidClient();
    private final ClientCreationDTO creationDTO = buildValidClientCreationDTO();
    private final ClientUpdateDTO updateDTO = buildValidClientUpdateDTO();
    @Spy
    private final Clock clock = Clock.systemDefaultZone();
    @Spy
    private final ClientMapper mapper = new ClientMapperImpl();
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientValidator validator;
    @Captor
    private ArgumentCaptor<Client> captor;
    @InjectMocks
    private ClientServiceImpl clientService;

    @Override
    protected ClientRepository getPrimaryRepository() {

        return clientRepository;
    }

    @Test
    @DisplayName("Dado un DTO de creación con datos válidos, deberá ser persistido exitosamente")
    void givenNewClientWithValidData_WhenCreating_ThenIsSuccessfullyPersisted() {

        registerClient(clientService, creationDTO);

        verifyCreationProcessSuccess();
    }

    @Test
    @DisplayName("Dado un DTO de creación con 1 o más valores NULL, la validación deberá fallar y el cliente no será persistido")
    void givenAnyNullValue_WhenCreating_ThenValidationShouldFail() {

        mockValidatorToThrowException(validator, new NullClientInputDataException(), creationDTO);

        assertThrows(NullClientInputDataException.class, (() -> registerClient(clientService, creationDTO)));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un DNI ya registrado, la validación deberá fallar y el cliente no será persistido")
    void givenExistingNationalIDCardNumber_WhenCreating_ThenValidationShouldFail() {

        mockThatNationalIDCardNumberWillCauseConflict(clientRepository, client);

        assertThrows(DuplicatedNationalIDCardNumberException.class, () -> registerClient(clientService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un DNI que contiene caractéres inválidos, la validación fallará y el cliente no será persistido")
    void givenNationalIDCardNumberWithInvalidCharacters_WhenCreating_ThenValidationShouldFail() {

        mockValidatorToThrowException(validator, new InvalidNationalIDCardNumberException(), creationDTO);

        assertThrows(InvalidNationalIDCardNumberException.class, () -> registerClient(clientService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un DNI que no cumpla límites de longitud, la validación deberá fallar y el cliente no será persistido")
    void givenNationalIDCardNumberWithInvalidLength_WhenCreating_ThenValidationShouldFail() {

        mockValidatorToThrowException(validator, new InvalidNationalIDCardNumberLengthException(), creationDTO);

        assertThrows(InvalidNationalIDCardNumberLengthException.class, () -> registerClient(clientService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con 1 o más nombres que contengan caractéres inválidos, la validación deberá fallar y el cliente no será persistido")
    void givenInvalidFirstOrLastName_WhenCreating_ThenValidationShouldFail() {

        mockValidatorToThrowException(validator, new InvalidClientNameException(), creationDTO);

        assertThrows(InvalidClientNameException.class, () -> registerClient(clientService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un email ya registrado, la validación deberá fallar y el cliente no será persistido")
    void givenExistingEmail_WhenCreating_ThenValidationShoulFail() {

        mockThatEmailWillCauseConflict(clientRepository, client);

        assertThrows(DuplicatedEmailException.class, () -> registerClient(clientService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un email inválido, la validación deberá fallar y el cliente no será persistido")
    void givenEmailWithInvalidCharacters_WhenCreating_ThenValidationShouldFail() {

        mockValidatorToThrowException(validator, new InvalidEmailException(), creationDTO);

        assertThrows(InvalidEmailException.class, () -> registerClient(clientService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación cuyo valor DNI esté en blanco, la validación fallará y el cliente no será persistido")
    void givenBlankNationalIDCardNumber_WhenCreating_ThenValidationShouldFail() {

        mockValidatorToThrowException(validator, new BlankClientNationalIDCardNumberException(), creationDTO);

        assertThrows(BlankClientNationalIDCardNumberException.class, () -> registerClient(clientService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con 1 o más nombres en blanco, la validación fallará y el cliente no será persistido")
    void givenAnyBlankName_WhenCreating_ThenValidationShouldFail() {

        mockValidatorToThrowException(validator, new BlankClientNameException(), creationDTO);

        assertThrows(BlankClientNameException.class, () -> registerClient(clientService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un email en blanco, la validación fallará y el cliente no será persistido")
    void givenBlankEmail_WhenCreating_ThenValidationShouldFail() {

        mockValidatorToThrowException(validator, new BlankClientEmailException(), creationDTO);

        assertThrows(BlankClientEmailException.class, () -> registerClient(clientService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación que incluya una lista de números de teléfono con 1 o más valores inválidos, la validación fallará y el cliente no será persistido")
    void givenInvalidPhone_WhenCreating_ThenClientIsNotPersisted() {

        mockValidatorToThrowException(validator, new InvalidPhoneNumberException(), creationDTO);

        assertThrows(InvalidPhoneNumberException.class, () -> registerClient(clientService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación que incluya una lista de números de teléfono con 1 o más valores que no respeten longitudes válidas" +
            ", la validación fallará y el cliente no será persistido")
    void givenInvalidPhoneLength_WhenCreating_ThenClientIsNotPersisted() {

        mockValidatorToThrowException(validator, new InvalidPhoneNumberLengthException(), creationDTO);

        assertThrows(InvalidPhoneNumberLengthException.class, () -> registerClient(clientService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación que incluya una lista de números de teléfono vacía, la validación fallará y el cliente no será persistido")
    void givenEmptyPhoneList_WhenCreating_ThenClientIsNotPersisted() {

        mockValidatorToThrowException(validator, new EmptyPhoneNumbersListException(), creationDTO);

        assertThrows(EmptyPhoneNumbersListException.class, () -> registerClient(clientService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un cliente previamente registrado, deberá retornar su información mediante DTO informativo")
    void givenExistingClient_WhenSearching_ThenReturnsItsInformation() {

        mockClientByNationalIDCardNumber(clientRepository, client);

        ClientInfoDTO returnedDTO = clientService.getClientInfo(creationDTO.getNationalIdentityCardNumber());

        verifyInfoDTOAssertions(returnedDTO);
    }

    @Test
    @DisplayName("Dado un DNI de cliente no registrado, al buscarlo arrojará ClientNotFoundException")
    void givenNonExistingClient_WhenSearching_ThenThrows_ClientNotFoundException() {

        mockThatNationalIDCardNumberDoesNotExist(clientRepository, client);

        assertThrows(ClientNotFoundException.class, () -> clientService.getClientInfo(client.getNationalIdentityCardNumber()));
    }

    @Test
    @DisplayName("Dado un cliente registrado previamente y un DTO de actualización con información correcta, deberá persistir los cambios exitosamente")
    void givenExistingClient_WhenUpdatingWithValidData_ThenIsSuccessfullyPersisted() {

        mockClientByNationalIDCardNumber(clientRepository, client);

        updateClient(clientService, client, updateDTO);

        captureClient(clientRepository, captor);

        verifyValidatorUpdateInteraction(validator, updateDTO);
        verifyMapperUpdateInteraction(mapper, client, updateDTO);

        Client clientCaptured = captor.getValue();

        assertNotNull(clientCaptured);

        verifyUpdatDTOAssertions(clientCaptured);
    }

    @Test
    @DisplayName("Dado un DNI de cliente inexistente, al intentar actualizarlo deberá arrojar ClientNotFoundException")
    void givenNonExistingClient_WhenUpdating_ThenThrows_ClientNotFoundException() {

        mockThatNationalIDCardNumberDoesNotExist(clientRepository, client);

        assertThrows(ClientNotFoundException.class, () -> updateClient(clientService, client, updateDTO));

        verifyMapperUpdateNoInteractions(mapper, client, updateDTO);
        verifyThatEntityWasNotSaved();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un DNI ya registrado, la validación deberá fallar y no se persistirán cambios")
    void givenExistingClient_WhenUpdatingWithExistingNationalIDCardNumber_ThenValidationShouldFail() {

        mockClientByNationalIDCardNumber(clientRepository, client);
        mockThatNationalIDCardNumberWillCauseConflictOnUpdate(clientRepository, client, updateDTO);

        assertThrows(DuplicatedNationalIDCardNumberException.class, () -> updateClient(clientService, client, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un DNI inválido, la validación deberá fallar y no se persistirán los cambios")
    void givenInvalidNationalIDCardNumber_WhenUpdating_ThenValidationShouldFail() {

        mockClientByNationalIDCardNumber(clientRepository, client);

        mockValidatorToThrowException(validator, new InvalidNationalIDCardNumberException(), updateDTO);

        assertThrows(InvalidNationalIDCardNumberException.class, () -> updateClient(clientService, client, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un DNI en blanco, la validación fallará y no se persistirán los cambios")
    void givenBlankNationalIDCardNumber_WhenUpdating_ThenClientIsNotPersisted() {

        mockClientByNationalIDCardNumber(clientRepository, client);

        mockValidatorToThrowException(validator, new BlankClientNationalIDCardNumberException(), updateDTO);

        assertThrows(BlankClientNationalIDCardNumberException.class, () -> updateClient(clientService, client, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con nombres en blanco, la validación fallará y no se persistirán los cambios")
    void givenBlankNames_WhenUpdating_ThenClientIsNotPersisted() {

        mockClientByNationalIDCardNumber(clientRepository, client);

        mockValidatorToThrowException(validator, new BlankClientNameException(), updateDTO);

        assertThrows(BlankClientNameException.class, () -> updateClient(clientService, client, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un email ya registrado en otro cliente, la validación fallará y no ser persistirán cambios")
    void givenExistingEmail_WhenUpdating_ThenClientIsNotPersisted() {

        mockThatEmailWillCauseConflictOnUpdate(clientRepository, client, updateDTO);
        mockClientByNationalIDCardNumber(clientRepository, client);

        assertThrows(DuplicatedEmailException.class, () -> updateClient(clientService, client, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una lista de números de teléfono con 1 o más números inválidos, la validación fallará y no se persistirán cambios")
    void givenInvalidPhone_WhenUpdating_ThenClientIsNotPersisted() {

        mockClientByNationalIDCardNumber(clientRepository, client);

        mockValidatorToThrowException(validator, new InvalidPhoneNumberException(), updateDTO);

        assertThrows(InvalidPhoneNumberException.class, () -> updateClient(clientService, client, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una lista de números de teléfono con 1 o más números que no respeten longitudes" +
            ", la validación fallará y no se persistirán cambios")
    void givenInvalidPhoneLength_WhenUpdating_ThenClientIsNotPersisted() {

        mockClientByNationalIDCardNumber(clientRepository, client);

        mockValidatorToThrowException(validator, new InvalidPhoneNumberLengthException(), updateDTO);

        assertThrows(InvalidPhoneNumberLengthException.class, () -> updateClient(clientService, client, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una lista de números de teléfono vacía, la validación fallará y no se persistirán cambios")
    void givenEmptyPhoneList_WhenUpdating_ThenClientIsNotPersisted() {

        mockClientByNationalIDCardNumber(clientRepository, client);

        mockValidatorToThrowException(validator, new EmptyPhoneNumbersListException(), updateDTO);

        assertThrows(EmptyPhoneNumbersListException.class, () -> updateClient(clientService, client, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un email inválido, la validación fallará y no se persistirán cambios")
    void givenInvalidEmail_WhenUpdating_ThenClientIsNotPersisted() {

        mockClientByNationalIDCardNumber(clientRepository, client);

        mockValidatorToThrowException(validator, new InvalidEmailException(), updateDTO);

        assertThrows(InvalidEmailException.class, () -> updateClient(clientService, client, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un cliente registrado previamente, deberá poder ser eliminado exitosamente")
    void givenExistingClient_WhenDeleting_ThenIsSuccessufullyDeleted() {

        mockClientByNationalIDCardNumber(clientRepository, client);

        deleteClient(clientService, client);

        verifyThatEntityWasDeleted(client);
    }

    @Test
    @DisplayName("Dado un DNI de cliente no registrado, al intentar eliminarlo arrojará ClientNotFoundException")
    void givenNonExistingClient_WhenDeleting_ThenThrows_ClientNotFoundException() {

        mockThatNationalIDCardNumberDoesNotExist(clientRepository, client);

        assertThrows(ClientNotFoundException.class, () -> deleteClient(clientService, client));

        verifyThatEntityWasNotDeleted(client);
    }

    private void verifyCreationProcessFailure() {

        verifyValidatorCreationInteraction(validator, creationDTO);
        verifyMapperUpdateNoInteractions(mapper, client, updateDTO);
        verifyThatEntityWasNotSaved();
    }

    private void verifyCreationProcessSuccess() {

        verifyValidatorCreationInteraction(validator, creationDTO);
        verifyMapperCreationInteraction(mapper, creationDTO, LocalDate.now(clock));
        verifyThatEntityWasSaved();
    }

    private void verifyUpdateProcessFailure() {
        verifyValidatorUpdateInteraction(validator, updateDTO);
        verifyMapperUpdateNoInteractions(mapper, client, updateDTO);
        verifyThatEntityWasNotSaved();
    }

    private void verifyUpdatDTOAssertions(Client clientCaptured) {
        assertAll(
                "Verificación de campos",
                () -> assertEquals(updateDTO.getNationalIdentityCardNumber(), clientCaptured.getNationalIdentityCardNumber()),
                () -> assertEquals(updateDTO.getFirstName(), clientCaptured.getFirstName()),
                () -> assertEquals(updateDTO.getLastName(), clientCaptured.getLastName()),
                () -> assertEquals(updateDTO.getEmail(), clientCaptured.getEmail()),
                () -> assertEquals(updateDTO.getPhoneNumbersList(), clientCaptured.getPhoneNumbersList())
        );
    }

    private void verifyInfoDTOAssertions(ClientInfoDTO returnedDTO) {
        assertNotNull(returnedDTO);

        assertAll(
                "Verificación de campos",
                () -> assertEquals(returnedDTO.getNationalIdentityCardNumber(), client.getNationalIdentityCardNumber()),
                () -> assertEquals(returnedDTO.getFirstName(), client.getFirstName()),
                () -> assertEquals(returnedDTO.getLastName(), client.getLastName()),
                () -> assertEquals(returnedDTO.getRegistrationDate(), client.getRegistrationDate())
        );
    }
}
