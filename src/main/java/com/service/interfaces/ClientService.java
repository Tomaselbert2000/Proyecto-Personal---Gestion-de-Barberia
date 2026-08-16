package com.service.interfaces;

import com.dto.client.ClientCreationDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.client.ClientUpdateDTO;
import com.dto.stats.*;
import com.enums.ClientNotesFilter;
import com.enums.RegisteredPhoneFilter;
import com.enums.RegistrationDateRange;

import java.util.List;

public interface ClientService {

    void registerNewClient(ClientCreationDTO newClient);

    void deleteClient(String nationalIDCardNumber);

    void updateClient(String nationalIDCardNumber, ClientUpdateDTO client);

    ClientInfoDTO getClientInfo(String nationalIdentityCardNumber);

    List<ClientInfoDTO> clientLiveSearchByName(String searchName);

    ClientAcquisitionStatsDTO getClientStatsVsLastMonth();

    List<ClientInfoDTO> liveSearch(
            String clientName,
            RegistrationDateRange registrationDateRange,
            RegisteredPhoneFilter phoneFilter,
            ClientNotesFilter notesFilter
    );

    List<ClientInfoDTO> getClientList();

    TotalClientsStatsDTO getTotalClientsStats();

    ClientPhoneNumberStatsDTO getPhoneNumberRegistrationStats();

    ClientRegistrationTrendStatDTO getClientRegistrationTrendStats();

    ClientNotesStatsDTO getClientNotesStats();
}
