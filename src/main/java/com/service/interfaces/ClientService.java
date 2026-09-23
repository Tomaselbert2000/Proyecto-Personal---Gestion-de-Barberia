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

    void registerNewClient(ClientCreationDTO dto);

    void deleteClient(String nicn);

    void deleteClient(Long id);

    void updateClient(String nicn, ClientUpdateDTO dto);

    void updateClient(Long clientID, ClientUpdateDTO dto);

    ClientInfoDTO getClientInfo(String nicn);

    ClientInfoDTO getClientInfo(Long id);

    List<ClientInfoDTO> clientLiveSearchByName(String name);

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
