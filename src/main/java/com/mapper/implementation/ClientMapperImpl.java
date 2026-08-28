package com.mapper.implementation;

import com.dto.client.ClientCreationDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.client.ClientUpdateDTO;
import com.mapper.helper.MapperHelper;
import com.mapper.interfaces.ClientMapper;
import com.model.Client;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mapper.helper.MapperHelper.checkIfMapperInputIsNull;
import static com.utils.strings.StringCleaner.*;

@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public Client mapClientCreationDTOtoEntity(ClientCreationDTO creationDTO, LocalDate registrationDate) {

        checkIfMapperInputIsNull(creationDTO, registrationDate);

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

    @Override
    public Client mapClientUpdateDTOtoEntity(Client entity, ClientUpdateDTO dto) {

        checkIfMapperInputIsNull(entity, dto);

        setUpdatedDataOnClient(entity, dto);

        return entity;
    }

    @Override
    public ClientInfoDTO mapClientToInfoDTO(Client entity) {

        checkIfMapperInputIsNull(entity);

        return ClientInfoDTO.builder()
                .id(entity.getClientID())
                .nationalIdentityCardNumber(entity.getNationalIdentityCardNumber())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .registrationDate(entity.getRegistrationDate())
                .email(entity.getEmail())
                .phoneNumbersList(entity.getPhoneNumbersList())
                .optionalNotes(entity.getOptionalNotes())
                .build();
    }

    @Override
    public List<ClientInfoDTO> mapClientToInfoDTO(List<Client> entityList) {

        return MapperHelper.mapList(entityList, this::mapClientToInfoDTO);
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
