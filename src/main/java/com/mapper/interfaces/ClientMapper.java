package com.mapper.interfaces;

import com.dto.client.ClientCreationDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.client.ClientUpdateDTO;
import com.model.Client;

import java.time.LocalDate;
import java.util.List;

public interface ClientMapper {

    Client mapClientCreationDTOtoEntity(ClientCreationDTO dto, LocalDate registrationDate);

    Client mapClientUpdateDTOtoEntity(Client entity, ClientUpdateDTO dto);

    ClientInfoDTO mapClientToInfoDTO(Client entity);

    List<ClientInfoDTO> mapClientToInfoDTO(List<Client> entityList);
}
