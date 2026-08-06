package com.service.interfaces;

import com.dto.client.ClientCreationDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.client.ClientUpdateDTO;
import com.dto.stats.ClientAcquisitionStatsDTO;

import java.util.List;

public interface ClientService {

    void registerNewClient(ClientCreationDTO newClient);

    void deleteClient(String nationalIDCardNumber);

    void updateClient(String nationalIDCardNumber, ClientUpdateDTO client);

    ClientInfoDTO getClientInfo(String nationalIdentityCardNumber);

    Long getClientsRegisteredQuantity();

    Long calculatePercentageOfClientsVsLastMonth();

    List<ClientInfoDTO> clientLiveSearchByName(String searchName);

    ClientAcquisitionStatsDTO getClientStatsVsLastMonth();
}
