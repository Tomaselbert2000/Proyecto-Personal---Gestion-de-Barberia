package com.service.helper;

import com.dto.client.ClientCreationDTO;
import com.dto.client.ClientDTOCommonMethods;
import com.dto.client.ClientUpdateDTO;
import com.mapper.interfaces.ClientMapper;
import com.model.Client;
import com.repository.ClientRepository;
import com.service.interfaces.ClientService;
import com.validation.client.ClientValidator;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ClientServiceTestHelper {

    public static void registerClient(ClientService clientService, ClientCreationDTO creationDTO) {

        clientService.registerNewClient(creationDTO);
    }

    public static void updateClient(ClientService clientService, Client client, ClientUpdateDTO updateDTO) {

        clientService.updateClient(client.getNationalIdentityCardNumber(), updateDTO);
    }

    public static void deleteClient(ClientService clientService, Client client) {

        clientService.deleteClient(client.getNationalIdentityCardNumber());
    }

    public static void captureClient(ClientRepository clientRepository, ArgumentCaptor<Client> captor) {

        verify(clientRepository).save(captor.capture());
    }

    public static void verifyValidatorCreationInteraction(ClientValidator validator, ClientCreationDTO creationDTO) {

        verify(validator).validateDTO(creationDTO);
    }

    public static void verifyValidatorUpdateInteraction(ClientValidator validator, ClientUpdateDTO updateDTO) {

        verify(validator).validateDTO(updateDTO);
    }

    public static void verifyMapperCreationInteraction(ClientMapper mapper, ClientCreationDTO creationDTO, LocalDate creationDate) {

        verify(mapper).mapClientCreationDTOtoEntity(creationDTO, creationDate);
    }

    public static void verifyMapperUpdateInteraction(ClientMapper mapper, Client client, ClientUpdateDTO updateDTO) {

        verify(mapper).mapClientUpdateDTOtoEntity(client, updateDTO);
    }

    public static void verifyMapperCreationNoInteractions(ClientMapper mapper, ClientCreationDTO creationDTO, LocalDate creationDate) {

        verify(mapper, never()).mapClientCreationDTOtoEntity(creationDTO, creationDate);
    }

    public static void verifyMapperUpdateNoInteractions(ClientMapper mapper, Client client, ClientUpdateDTO updateDTO) {

        verify(mapper, never()).mapClientUpdateDTOtoEntity(client, updateDTO);
    }

    public static void mockThatNationalIDCardNumberWillCauseConflict(ClientRepository clientRepository, Client client) {

        when(clientRepository.existsByNationalIdentityCardNumber(client.getNationalIdentityCardNumber())).thenReturn(true);
    }

    public static void mockThatNationalIDCardNumberWillCauseConflictOnUpdate(ClientRepository clientRepository, Client client, ClientUpdateDTO updateDTO) {

        when(clientRepository.existsByNationalIdentityCardNumberAndClientIDNot(updateDTO.getNationalIdentityCardNumber(), client.getClientID())).thenReturn(true);
    }

    public static void mockThatNationalIDCardNumberDoesNotExist(ClientRepository clientRepository, Client client) {

        when(clientRepository.findByNationalIdentityCardNumber(client.getNationalIdentityCardNumber())).thenReturn(Optional.empty());
    }

    public static void mockThatEmailWillCauseConflict(ClientRepository clientRepository, Client client) {

        when(clientRepository.existsByEmail(client.getEmail())).thenReturn(true);
    }

    public static void mockThatEmailWillCauseConflictOnUpdate(ClientRepository clientRepository, Client client, ClientUpdateDTO updateDTO) {

        when(clientRepository.existsByEmailAndClientIDNot(updateDTO.getEmail(), client.getClientID())).thenReturn(true);
    }

    public static void mockClientByNationalIDCardNumber(ClientRepository clientRepository, Client client) {

        when(clientRepository.findByNationalIdentityCardNumber(client.getNationalIdentityCardNumber())).thenReturn(Optional.of(client));
    }

    public static <T extends ClientDTOCommonMethods> void mockValidatorToThrowException(ClientValidator validator, Exception exception, T dto) {

        doThrow(exception).when(validator).validateDTO(dto);
    }
}
