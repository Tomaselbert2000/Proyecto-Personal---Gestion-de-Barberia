package com.service.implementation;

import com.dto.client.ClientCreationDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.client.ClientUpdateDTO;
import com.dto.stats.ClientAcquisitionStatsDTO;
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

import static com.dto.stats.EmptyStatDTOFactory.emptyClientAcquisitionStatsDTO;
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

        return mapper.mapClientoToInfoDTO(clientOnDB);
    }

    @Override
    public Long getClientsRegisteredQuantity() {

        return clientRepository.count();
    }

    @Override
    public Long calculatePercentageOfClientsVsLastMonth() {

        LocalDate startDateTimeAfter = getStartOfCurrentMonth();
        LocalDate startDateTimeBefore = getEndOfCurrentMonth();

        Long clientsRegisteredThisMonth = clientRepository.countByRegistrationDateBetween(startDateTimeAfter, startDateTimeBefore);
        Long clientsTheLastMonth = clientRepository.countByRegistrationDateBetween(startDateTimeAfter.minusMonths(1), startDateTimeBefore.minusMonths(1));

        if (clientsRegisteredThisMonth == 0 && clientsTheLastMonth == 0) {

            return 0L;

        } else if (clientsTheLastMonth == 0) {

            return 100L;
        }

        return ((clientsRegisteredThisMonth - clientsTheLastMonth) * 100) / clientsTheLastMonth;
    }

    @Override
    public List<ClientInfoDTO> clientLiveSearchByName(String searchName) {

        return mapper.mapClientToInfoDTO(clientRepository.clientLiveSearchByName(searchName));
    }

    @Override
    public ClientAcquisitionStatsDTO getClientStatsVsLastMonth() {

        List<ClientAcquisitionStatsDTO> clientAcquisitionStatsDTOS = clientRepository.getClientStats(
                getStartOfCurrentMonth(),
                getEndOfCurrentMonth(),
                getStartOfCurrentMonth().minusMonths(1),
                getEndOfCurrentMonth().minusMonths(1));

        if (!clientAcquisitionStatsDTOS.isEmpty()) {

            ClientAcquisitionStatsDTO statsDTO = clientAcquisitionStatsDTOS.getFirst();

            Double trendPercentage = ((double) statsDTO.getNewClientsThisMonth() * statsDTO.getPercentageVsLastMonth() / 100);

            statsDTO.setPercentageVsLastMonth(trendPercentage);

            return statsDTO;

        } else {

            return emptyClientAcquisitionStatsDTO();
        }
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
