package com.barbershop.service.interfaces;

import com.barbershop.dto.client.ClientCreationDTO;
import com.barbershop.dto.client.ClientInfoDTO;
import com.barbershop.dto.client.ClientUpdateDTO;

import java.util.List;

public interface ClientService {

    void registerNewClient(ClientCreationDTO newClient);

    void deleteClient(String nationalIDCardNumber);

    List<ClientInfoDTO> getClientList();

    void updateClient(String nationalIDCardNumber, ClientUpdateDTO client);

    ClientInfoDTO getClientInfo(String nationalIdentityCardNumber);

    Long getClientsRegisteredQuantity();

    Long calculatePercentageOfClientsVsLastMonth();
}
