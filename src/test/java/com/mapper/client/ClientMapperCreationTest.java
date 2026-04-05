package com.mapper.client;

import com.barbershop.dto.client.ClientCreationDTO;
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

public class ClientMapperCreationTest {

    private final ClientMapper clientMapper = new ClientMapperImpl();

    private ClientCreationDTO createDTO;

    @BeforeEach
    void init() {

        createDTO = ClientCreationDTO.builder()
                .nationalIdentityCardNumber("1234567")
                .firstName("Tomas Gabriel")
                .lastName("Elbert")
                .email("tomas@gmail.com")
                .phoneNumbersList(new ArrayList<>(List.of("1122334455")))
                .build();
    }

    @Test
    void givenNationalIdentityCardNumberWithSpaces_WhenCreating_ThenIsTrimmed() {

        String nationalIDCardNumberWithSpaces = "   1122334   ";

        createDTO.setNationalIdentityCardNumber(nationalIDCardNumberWithSpaces);

        Client clientMapped = clientMapper.mapClientCreationDTOtoEntity(createDTO, LocalDate.now());

        assertEquals("1122334", clientMapped.getNationalIdentityCardNumber());
    }

    @Test
    void givenFirstNameWithSpaces_WhenCreating_ThenIsTrimmed() {

        String nameWithSpaces = "   Tomas Gabriel   ";

        createDTO.setFirstName(nameWithSpaces);

        Client mappedClient = clientMapper.mapClientCreationDTOtoEntity(createDTO, LocalDate.now());

        assertEquals("Tomas Gabriel", mappedClient.getFirstName());
    }

    @Test
    void givenLowercaseFirstName_WhenCreating_ThenIsFullyCapitalized() {

        String lowercaseFirstName = "tomas gabriel";

        createDTO.setFirstName(lowercaseFirstName);

        Client mappedClient = clientMapper.mapClientCreationDTOtoEntity(createDTO, LocalDate.now());

        assertEquals("Tomas Gabriel", mappedClient.getFirstName());
    }

    @Test
    void givenLastNameWith_Spaces_WhenCreating_ThenIsTrimmed() {

        String lastNameWithSpaces = "   Elbert   ";

        createDTO.setLastName(lastNameWithSpaces);

        Client mappedClient = clientMapper.mapClientCreationDTOtoEntity(createDTO, LocalDate.now());

        assertEquals("Elbert", mappedClient.getLastName());
    }

    @Test
    void givenLowercaseLastName_WhenCreating_ThenIsFullyCapitalized() {

        String lowercaseLastName = "elbert";

        createDTO.setLastName(lowercaseLastName);

        Client mappedClient = clientMapper.mapClientCreationDTOtoEntity(createDTO, LocalDate.now());

        assertEquals("Elbert", mappedClient.getLastName());
    }

    @Test
    void givenEmailWithSpaces_WhenCreating_ThenIsTrimmed() {

        String emailWithSpaces = "   tomas@gmail.com   ";

        createDTO.setEmail(emailWithSpaces);

        Client mappedClient = clientMapper.mapClientCreationDTOtoEntity(createDTO, LocalDate.now());

        assertEquals("tomas@gmail.com", mappedClient.getEmail());
    }

    @Test
    void givenPhoneNumberWithSpaces_WhenCreating_ThenIsTrimmed() {

        String phoneWithSpaces = "   1122334455   ";

        createDTO.getPhoneNumbersList().clear();
        createDTO.getPhoneNumbersList().add(phoneWithSpaces);

        Client mappedClient = clientMapper.mapClientCreationDTOtoEntity(createDTO, LocalDate.now());

        assertTrue(mappedClient.getPhoneNumbersList().stream().anyMatch(ph -> ph.equals("1122334455")));
    }
}
