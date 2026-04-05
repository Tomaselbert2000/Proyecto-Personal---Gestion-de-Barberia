package com.barbershop.mapper.interfaces;

import com.barbershop.dto.client.ClientCreationDTO;
import com.barbershop.dto.client.ClientInfoDTO;
import com.barbershop.dto.client.ClientUpdateDTO;
import com.barbershop.model.Client;

import java.time.LocalDate;
import java.util.List;

public interface ClientMapper {

    Client mapClientCreationDTOtoEntity(ClientCreationDTO dto, LocalDate registrationDate);

    Client mapClientUpdateDTOtoEntity(Client client, ClientUpdateDTO updateDTO);

    ClientInfoDTO mapClientoToInfoDTO(Client client);

    List<ClientInfoDTO> mapClientToInfoDTO(List<Client> clientList);
}
