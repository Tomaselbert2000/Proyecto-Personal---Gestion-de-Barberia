package com.mapper.client;

import com.dto.client.ClientUpdateDTO;
import com.mapper.implementation.ClientMapperImpl;
import com.mapper.interfaces.ClientMapper;
import com.model.Client;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.ClientTestDataFactory.buildValidClient;
import static com.factory.ClientTestDataFactory.buildValidClientUpdateDTO;
import static com.test_constant.ClientTestConstants.CreationValidData.*;
import static com.test_constant.ClientTestConstants.MapperData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientMapperUpdateTest {

    private final ClientMapper clientMapper = new ClientMapperImpl();
    private final ClientUpdateDTO updateDTO = buildValidClientUpdateDTO();
    private final Client existingClient = buildValidClient();

    @Test
    @DisplayName("Dado un DTO de actualización con un DNI que contenga espacios en blanco, al mapear serán eliminados")
    void givenNationalIdentityCardNumberWithSpaces_WhenUpdating_ThenIsTrimmed() {

        updateDTO.setNationalIdentityCardNumber(NATIONAL_ID_CARD_NUMBER_WITH_SPACES);

        Client updatedClient = mapEntity(updateDTO, existingClient);
        assertEquals(NATIONAL_ID_CARD_NUMBER, updatedClient.getNationalIdentityCardNumber());
    }

    @Test
    @DisplayName("Dado un nombre con espacios en blanco, al mapear serán eliminados")
    void givenFirstNameWithSpaces_WhenUpdating_ThenIsTrimmed() {

        updateDTO.setFirstName(FIRST_NAME_WITH_SPACES);

        Client updatedClient = mapEntity(updateDTO, existingClient);
        assertEquals(FIRST_NAME, updatedClient.getFirstName());
    }

    @Test
    @DisplayName("Dado un nombre en minúsculas, al mapear se convertirá a mayúscula")
    void givenLowerCaseFirstName_WhenUpdating_ThenIsFullyCapitalized() {

        updateDTO.setFirstName(LOWERCASE_FIRST_NAME);

        Client updatedClient = mapEntity(updateDTO, existingClient);
        assertEquals(FIRST_NAME, updatedClient.getFirstName());
    }

    @Test
    @DisplayName("Dado un apellido con espacios en blanco, al mapear serán eliminados")
    void givenLastNameWithSpaces_WhenUpdating_ThenIsTrimmed() {

        updateDTO.setLastName(LOWERCASE_LAST_NAME);

        Client updatedClient = mapEntity(updateDTO, existingClient);
        assertEquals(LAST_NAME, updatedClient.getLastName());
    }

    @Test
    @DisplayName("Dado un apellido en minúsculas, al mapear se convertirá a mayúscula")
    void givenLowercaseLastName_WhenUpdating_ThenIsFullyCapitalized() {

        updateDTO.setLastName(LOWERCASE_LAST_NAME);
        Client updatedClient = mapEntity(updateDTO, existingClient);
        assertEquals(LAST_NAME, updatedClient.getLastName());
    }

    @Test
    @DisplayName("Dado un correo electrónico con espacios en blanco, al mapear serán eliminados")
    void givenEmailWithSpaces_WhenUpdating_ThenIsTrimmed() {

        updateDTO.setEmail(EMAIL_WITH_SPACES);
        Client updatedClient = mapEntity(updateDTO, existingClient);
        assertEquals(EMAIL, updatedClient.getEmail());
    }

    @Test
    @DisplayName("Dado un número de teléfono con espacios en blanco, al mapear serán eliminados")
    void givenPhoneNumberValueWithSpaces_WhenUpdating_ThenIsTrimmed() {

        updateDTO.setPhoneNumbersList(PHONE_LIST_THAT_CONTAINS_PHONE_WITH_SPACES);
        Client updatedClient = mapEntity(updateDTO, existingClient);

        assertEquals(PHONE_LIST.getFirst(), updatedClient.getPhoneNumbersList().getFirst());
    }

    private Client mapEntity(ClientUpdateDTO updateDTO, Client existingClient) {

        return clientMapper.mapClientUpdateDTOtoEntity(existingClient, updateDTO);
    }
}
