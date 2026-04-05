package com.mapper.client;

import com.barbershop.dto.client.ClientUpdateDTO;
import com.barbershop.mapper.implementation.ClientMapperImpl;
import com.barbershop.mapper.interfaces.ClientMapper;
import com.barbershop.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientMapperUpdateTest {

    private final ClientMapper clientMapper = new ClientMapperImpl();

    private ClientUpdateDTO updateDTO;
    private Client clientToUpdate;

    @BeforeEach
    void init() {

        clientToUpdate = Client.builder().clientID(1L)
                .nationalIdentityCardNumber("1111111")
                .firstName("Nombre")
                .lastName("Apellido")
                .registrationDate(LocalDate.of(2026, 1, 1))
                .email("abcdef@hotmail.com")
                .phoneNumbersList(new ArrayList<>(List.of("0000000000")))
                .build();

        updateDTO = new ClientUpdateDTO();
    }

    @Test
    void givenNationalIdentityCardNumberWithSpaces_WhenUpdating_ThenIsTrimmed() {

        String nationalIdCardNumberWithSpaces = "  1122334   ";

        updateDTO.setNationalIdentityCardNumber(nationalIdCardNumberWithSpaces);

        Client updatedClient = clientMapper.mapClientUpdateDTOtoEntity(clientToUpdate, updateDTO);

        assertEquals("1122334", updatedClient.getNationalIdentityCardNumber());
    }

    @Test
    void givenFirstNameWithSpaces_WhenUpdating_ThenIsTrimmed() {

        String firstNameWithSpaces = "   Tomas   ";

        updateDTO.setFirstName(firstNameWithSpaces);

        Client updatedClient = clientMapper.mapClientUpdateDTOtoEntity(clientToUpdate, updateDTO);

        assertEquals("Tomas", updatedClient.getFirstName());
    }

    @Test
    void givenLowerCaseFirstName_WhenUpdating_ThenIsFullyCapitalized() {

        String lowercaseFirstName = "tomas gabriel";

        updateDTO.setFirstName(lowercaseFirstName);

        Client updatedClient = clientMapper.mapClientUpdateDTOtoEntity(clientToUpdate, updateDTO);

        assertEquals("Tomas Gabriel", updatedClient.getFirstName());
    }

    @Test
    void givenLastNameWithSpaces_WhenUpdating_ThenIsTrimmed() {

        String lastNameWithSpaces = "   Elbert   ";

        updateDTO.setLastName(lastNameWithSpaces);

        Client updatedClient = clientMapper.mapClientUpdateDTOtoEntity(clientToUpdate, updateDTO);

        assertEquals("Elbert", updatedClient.getLastName());
    }

    @Test
    void givenLowercaseLastName_WhenUpdating_ThenIsFullyCapitalized() {

        String lowercaseLastName = "elbert";

        updateDTO.setLastName(lowercaseLastName);

        Client updatedClient = clientMapper.mapClientUpdateDTOtoEntity(clientToUpdate, updateDTO);

        assertEquals("Elbert", updatedClient.getLastName());
    }

    @Test
    void givenEmailWithSpaces_WhenUpdating_ThenIsTrimmed() {

        String emailWithSpaces = "   nuevoemail@gmail.com   ";

        updateDTO.setEmail(emailWithSpaces);

        Client updatedClient = clientMapper.mapClientUpdateDTOtoEntity(clientToUpdate, updateDTO);

        assertEquals("nuevoemail@gmail.com", updatedClient.getEmail());
    }

    @Test
    void givenPhoneNumberValueWithSpaces_WhenUpdating_ThenIsTrimmed() {

        String phoneWithSpaces = "   1168433212   ";

        updateDTO.setPhoneNumbersList(new ArrayList<>(List.of(phoneWithSpaces)));

        Client updatedClient = clientMapper.mapClientUpdateDTOtoEntity(clientToUpdate, updateDTO);

        assertTrue(updatedClient.getPhoneNumbersList().stream().anyMatch(ph -> ph.equals("1168433212")));
    }
}
