package com.barbershop.mapper.implementation;

import com.barbershop.dto.client.ClientCreationDTO;
import com.barbershop.dto.client.ClientInfoDTO;
import com.barbershop.dto.client.ClientUpdateDTO;
import com.barbershop.exceptions.common.NullMapperInputException;
import com.barbershop.mapper.interfaces.ClientMapper;
import com.barbershop.model.Client;
import com.barbershop.utils.strings.StringCleaner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public Client mapClientCreationDTOtoEntity(ClientCreationDTO creationDTO, LocalDate registrationDate) {

        if (creationDTO == null || registrationDate == null) {

            throw new NullMapperInputException();

        } else {

            return Client.builder()
                    .nationalIdentityCardNumber(creationDTO.getNationalIdentityCardNumber().trim())
                    .firstName(StringCleaner.formatAsProperName(creationDTO.getFirstName()))
                    .lastName(StringCleaner.formatAsProperName(creationDTO.getLastName()))
                    .registrationDate(registrationDate)
                    .email(creationDTO.getEmail().trim())
                    .phoneNumbersList(StringCleaner.trimList(creationDTO.getPhoneNumbersList()))
                    .optionalNotes(creationDTO.getOptionalNotes())
                    .build();
        }
    }

    @Override
    public Client mapClientUpdateDTOtoEntity(Client client, ClientUpdateDTO updateDTO) {

        if (client == null || updateDTO == null) {

            throw new NullMapperInputException();

        } else {

            setUpdatedDataOnClient(client, updateDTO);

            return client;
        }
    }

    @Override
    public ClientInfoDTO mapClientoToInfoDTO(Client client) {

        if (client == null) {

            throw new NullMapperInputException();

        } else {

            return ClientInfoDTO.builder()
                    .nationalIdentityCardNumber(client.getNationalIdentityCardNumber())
                    .firstName(client.getFirstName())
                    .lastName(client.getLastName())
                    .registrationDate(client.getRegistrationDate())
                    .optionalNotes(client.getOptionalNotes())
                    .build();
        }
    }

    @Override
    public List<ClientInfoDTO> mapClientToInfoDTO(List<Client> clientList) {

        if (clientList == null) throw new NullMapperInputException();

        return clientList.stream().map(this::mapClientoToInfoDTO).collect(Collectors.toList());

    }

    private void setUpdatedDataOnClient(Client client, ClientUpdateDTO updateDTO) {

        if (updateDTO.getNationalIdentityCardNumber() != null)
            client.setNationalIdentityCardNumber(updateDTO.getNationalIdentityCardNumber().trim());

        if (updateDTO.getFirstName() != null)
            client.setFirstName(StringCleaner.formatAsProperName(updateDTO.getFirstName()));

        if (updateDTO.getLastName() != null)
            client.setLastName(StringCleaner.formatAsProperName(updateDTO.getLastName()));

        if (updateDTO.getEmail() != null) client.setEmail(updateDTO.getEmail().trim());

        if (updateDTO.getPhoneNumbersList() != null) {

            List<String> updatedPhoneList = new ArrayList<>(List.of());

            updatedPhoneList.addAll(updateDTO.getPhoneNumbersList());

            client.setPhoneNumbersList(updatedPhoneList);
        }

        if (updateDTO.getOptionalNotes() != null) client.setOptionalNotes(updateDTO.getOptionalNotes());
    }
}
