package com.service.implementation;

import com.dto.client.ClientCreationDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.client.ClientUpdateDTO;
import com.dto.stats.*;
import com.enums.ClientNotesFilter;
import com.enums.RegisteredPhoneFilter;
import com.enums.RegistrationDateRange;
import com.exceptions.client.ClientNotFoundException;
import com.exceptions.client.DuplicatedEmailException;
import com.exceptions.client.DuplicatedNationalIDCardNumberException;
import com.exceptions.client.DuplicatedPhoneNumberException;
import com.mapper.interfaces.ClientMapper;
import com.model.Client;
import com.repository.ClientRepository;
import com.service.interfaces.ClientService;
import com.validation.client.ClientValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static com.dto.stats.EmptyStatDTOFactory.*;
import static com.utils.time.TimeCalculation.getEndOfCurrentMonth;
import static com.utils.time.TimeCalculation.getStartOfCurrentMonth;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientMapper mapper;

    private final ClientRepository clientRepository;

    private final Clock clock;

    private final ClientValidator validator;

    @Override
    @Transactional
    public void registerNewClient(ClientCreationDTO newClient) {

        validator.validateDTO(newClient);

        checkNationalIDCardNumberAvailability(newClient.getNationalIdentityCardNumber());

        checkEmailAvailabilityForCreation(newClient.getEmail());

        checkPhoneNumberListForCreation(newClient.getPhoneNumbersList());

        LocalDate registrationDate = LocalDate.now(clock);

        clientRepository.save(mapper.mapClientCreationDTOtoEntity(newClient, registrationDate));
    }


    @Override
    @Transactional
    public void deleteClient(String nationalIDCardNumber) {

        Client clientToDelete = loadClient(nationalIDCardNumber);

        clientRepository.delete(clientToDelete);
    }

    @Override
    @Transactional
    public void updateClient(String nationalIDCardNumber, ClientUpdateDTO updateDTO) {

        Client clientOnDB = loadClient(nationalIDCardNumber);

        validator.validateDTO(updateDTO);

        checkNationalIDCardNumberAvailability(clientOnDB.getClientID(), updateDTO.getNationalIdentityCardNumber());

        checkEmailAvailabilityForUpdate(updateDTO.getEmail(), clientOnDB.getClientID());

        checkPhoneNumberListForUpdate(updateDTO.getPhoneNumbersList(), clientOnDB.getClientID());

        clientRepository.save(mapper.mapClientUpdateDTOtoEntity(clientOnDB, updateDTO));
    }

    @Override
    public ClientInfoDTO getClientInfo(String nationalIdentityCardNumber) {

        Client clientOnDB = loadClient(nationalIdentityCardNumber);

        return mapper.mapClientToInfoDTO(clientOnDB);
    }

    @Override
    public List<ClientInfoDTO> clientLiveSearchByName(String searchName) {

        return mapper.mapClientToInfoDTO(clientRepository.clientLiveSearchByName(searchName));
    }

    @Override
    public ClientAcquisitionStatsDTO getClientStatsVsLastMonth() {

        Long clientsRegisteredDuringThisMonth = clientRepository.getClientCountByRegistrationDateRange(
                getStartOfCurrentMonth(),
                getEndOfCurrentMonth());

        Long clientsRegisteredTheLastMonth = clientRepository.getClientCountByRegistrationDateRange(
                getStartOfCurrentMonth().minusMonths(1),
                getEndOfCurrentMonth().minusMonths(1)
        );

        ClientAcquisitionStatsDTO clientAcquisitionStatsDTO = ClientAcquisitionStatsDTO.builder()
                .newClientsThisMonth(clientsRegisteredDuringThisMonth)
                .newClientsLastMonth(clientsRegisteredTheLastMonth)
                .build();

        double trendPercentage;

        if (clientAcquisitionStatsDTO.getNewClientsLastMonth() == 0L) {

            trendPercentage = 0.0;

        } else {

            trendPercentage = ((double) clientAcquisitionStatsDTO.getNewClientsThisMonth() * clientAcquisitionStatsDTO.getPercentageVsLastMonth() / 100);
        }

        clientAcquisitionStatsDTO.setPercentageVsLastMonth(trendPercentage);

        return clientAcquisitionStatsDTO;
    }

    @Override
    @SuppressWarnings("ALL")
    public List<ClientInfoDTO> liveSearch(
            String clientName,
            RegistrationDateRange registrationDateRange,
            RegisteredPhoneFilter phoneFilter,
            ClientNotesFilter notesFilter
    ) {

        LocalDate limitRegistrationDate = null;
        Boolean hasPhone = null;
        Boolean hasNotes = null;

        switch (registrationDateRange) {

            case TODOS -> limitRegistrationDate = null;

            case ULTIMOS_30_DIAS -> limitRegistrationDate = LocalDate.now().minusDays(30);

            case ESTE_MES ->
                    limitRegistrationDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);

            case ESTE_AÑO -> limitRegistrationDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        }

        switch (phoneFilter) {

            case TODOS -> hasPhone = null;

            case CON_TELEFONO_REGISTRADO -> hasPhone = true;

            case SIN_TELEFONO_REGISTRADO -> hasPhone = false;
        }

        switch (notesFilter) {

            case TODOS -> hasNotes = null;

            case CON_OBSERVACIONES -> hasNotes = true;

            case SIN_OBSERVACIONES -> hasNotes = false;
        }

        List<Client> clients = clientRepository.liveSearch(clientName, limitRegistrationDate, hasPhone, hasNotes);

        return mapper.mapClientToInfoDTO(clients);
    }

    @Override
    public List<ClientInfoDTO> getClientList() {

        List<Client> clients = clientRepository.findAll();

        return mapper.mapClientToInfoDTO(clients);
    }

    @Override
    public TotalClientsStatsDTO getTotalClientsStats() {

        TotalClientsStatsDTO totalClientsStatsDTO = clientRepository.getTotalClientsStats(getStartOfCurrentMonth(), getEndOfCurrentMonth());

        if (totalClientsStatsDTO.getTotalClientsCount() == null || totalClientsStatsDTO.getClientsRegisteredThisMonth() == null)
            return emptyTotalClientsStatsDTO();

        return totalClientsStatsDTO;
    }

    @Override
    public ClientPhoneNumberStatsDTO getPhoneNumberRegistrationStats() {

        ClientPhoneNumberStatsDTO clientPhoneNumberStatsDTO = clientRepository.getPhoneNumberRegistrationStats();

        if (clientPhoneNumberStatsDTO == null || clientPhoneNumberStatsDTO.getClientsWithAtLeastOnePhoneNumber() == null)
            return emptyClientPhoneNumberStatsDTO();

        return clientPhoneNumberStatsDTO;
    }

    @Override
    public ClientRegistrationTrendStatDTO getClientRegistrationTrendStats() {

        ClientRegistrationTrendStatDTO clientRegistrationTrendStatDTO = clientRepository.getClientRegistrationTrend(
                getStartOfCurrentMonth(),
                getEndOfCurrentMonth(),
                getStartOfCurrentMonth().minusMonths(1),
                getEndOfCurrentMonth().minusMonths(1)
        );

        if (clientRegistrationTrendStatDTO == null || clientRegistrationTrendStatDTO.getClientsRegisteredDuringThisMonth() == null)
            return emptyClientRegistrationTrendStatDTO();

        Long clientsThisMonth = clientRegistrationTrendStatDTO.getClientsRegisteredDuringThisMonth();
        Long clientsLastMonth = clientRegistrationTrendStatDTO.getClientsRegisteredDuringTheLastMonth();

        double calculatedPercentage;

        if (clientsLastMonth != 0L) {

            calculatedPercentage = (((double) (clientsThisMonth - clientsLastMonth) / clientsLastMonth) * 100);

        } else {

            calculatedPercentage = 0.0;
        }

        clientRegistrationTrendStatDTO.setTrendPercentage(calculatedPercentage);

        return clientRegistrationTrendStatDTO;
    }

    @Override
    public ClientNotesStatsDTO getClientNotesStats() {

        ClientNotesStatsDTO clientNotesStatsDTO = clientRepository.getClientsNotesStats();

        if (clientNotesStatsDTO == null || clientNotesStatsDTO.getClientsWithNotes() == null)
            return emptyClientNotesStatsDTO();

        Long notesCount = clientNotesStatsDTO.getClientsWithNotes();
        Long noNotesCount = clientNotesStatsDTO.getClientsWithoutNotes();
        long totalCount = notesCount + noNotesCount;

        double notesPercentage;

        if (totalCount != 0L) {

            notesPercentage = ((double) notesCount / totalCount) * 100;

        } else {

            notesPercentage = 0.0;
        }

        clientNotesStatsDTO.setClientsWithNotesPercentage(notesPercentage);

        return clientNotesStatsDTO;
    }

    private Client loadClient(String nationalIdentityCardNumber) {

        return clientRepository.findByNationalIdentityCardNumber(nationalIdentityCardNumber).orElseThrow(ClientNotFoundException::new);
    }

    private void checkNationalIDCardNumberAvailability(String nationalIdentityCardNumber) {

        if (clientRepository.existsByNationalIdentityCardNumber(nationalIdentityCardNumber))
            throw new DuplicatedNationalIDCardNumberException();
    }

    private void checkNationalIDCardNumberAvailability(Long clientID, String nationalIdentityCardNumber) {

        if (clientRepository.existsByNationalIdentityCardNumberAndClientIDNot(nationalIdentityCardNumber, clientID))
            throw new DuplicatedNationalIDCardNumberException();
    }

    private void checkEmailAvailabilityForCreation(String email) {

        if (clientRepository.existsByEmail(email)) throw new DuplicatedEmailException();
    }

    private void checkEmailAvailabilityForUpdate(String email, Long clientID) {

        if (clientRepository.existsByEmailAndClientIDNot(email, clientID)) throw new DuplicatedEmailException();
    }

    private void checkPhoneNumberListForCreation(List<String> phoneNumbersList) {

        checkIfPhoneListContainsValuesAlreadyRegistered(phoneNumbersList);
    }

    private void checkPhoneNumberListForUpdate(List<String> phoneNumbersList, Long clientID) {

        checkIfPhoneListContainsValuesAlreadyRegistered(phoneNumbersList, clientID);
    }

    private void checkIfPhoneListContainsValuesAlreadyRegistered(List<String> phoneNumbersList) {

        if (clientRepository.existsByAnyPhoneNumberInList(phoneNumbersList)) {
            throw new DuplicatedPhoneNumberException();
        }
    }

    private void checkIfPhoneListContainsValuesAlreadyRegistered(List<String> phoneNumberList, Long clientID) {

        if (clientRepository.existsByAnyPhoneNumberInListAndClientIDNot(phoneNumberList, clientID))

            throw new DuplicatedPhoneNumberException();
    }
}
