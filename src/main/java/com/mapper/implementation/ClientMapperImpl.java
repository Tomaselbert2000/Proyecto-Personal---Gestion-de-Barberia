package com.mapper.implementation;

import com.dto.client.ClientCreationDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.client.ClientUpdateDTO;
import com.exceptions.common.NullMapperInputException;
import com.mapper.interfaces.ClientMapper;
import com.model.Client;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.utils.strings.StringCleaner.*;

@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public Client mapClientCreationDTOtoEntity(ClientCreationDTO creationDTO, LocalDate registrationDate) {

        if (creationDTO == null || registrationDate == null) {

            throw new NullMapperInputException();

        } else {

            return Client.builder()
                    .nationalIdentityCardNumber(creationDTO.getNationalIdentityCardNumber().trim())
                    .firstName(formatAsProperName(creationDTO.getFirstName()))
                    .lastName(formatAsProperName(creationDTO.getLastName()))
                    .registrationDate(registrationDate)
                    .email(creationDTO.getEmail().trim())
                    .phoneNumbersList(trimList(creationDTO.getPhoneNumbersList()))
                    .optionalNotes(formatAsSentence(creationDTO.getOptionalNotes()))
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
                    .id(client.getClientID())
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
            client.setFirstName(formatAsProperName(updateDTO.getFirstName()));

        if (updateDTO.getLastName() != null)
            client.setLastName(formatAsProperName(updateDTO.getLastName()));

        if (updateDTO.getEmail() != null) client.setEmail(updateDTO.getEmail().trim());

        if (updateDTO.getPhoneNumbersList() != null) {

            List<String> updatedPhoneList = new ArrayList<>(List.of());

            for (String phone : updateDTO.getPhoneNumbersList()) {

                updatedPhoneList.add(phone.trim());
            }

            client.setPhoneNumbersList(updatedPhoneList);
        }

        if (updateDTO.getOptionalNotes() != null) client.setOptionalNotes(updateDTO.getOptionalNotes());
    }
}
