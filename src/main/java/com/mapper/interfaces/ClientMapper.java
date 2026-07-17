package com.mapper.interfaces;

import com.dto.client.ClientCreationDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.client.ClientUpdateDTO;
import com.model.Client;

import java.time.LocalDate;
import java.util.List;

public interface ClientMapper {

    Client mapClientCreationDTOtoEntity(ClientCreationDTO dto, LocalDate registrationDate);

    Client mapClientUpdateDTOtoEntity(Client client, ClientUpdateDTO updateDTO);

    ClientInfoDTO mapClientoToInfoDTO(Client client);

    List<ClientInfoDTO> mapClientToInfoDTO(List<Client> clientList);
}
