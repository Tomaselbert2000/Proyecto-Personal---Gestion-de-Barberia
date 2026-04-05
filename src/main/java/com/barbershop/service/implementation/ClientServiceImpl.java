package com.barbershop.service.implementation;

import com.barbershop.dto.client.ClientCreationDTO;
import com.barbershop.dto.client.ClientInfoDTO;
import com.barbershop.dto.client.ClientUpdateDTO;
import com.barbershop.exceptions.client.ClientNotFoundException;
import com.barbershop.exceptions.client.DuplicatedEmailException;
import com.barbershop.exceptions.client.DuplicatedNationalIDCardNumberException;
import com.barbershop.exceptions.client.DuplicatedPhoneNumberException;
import com.barbershop.model.Client;
import com.barbershop.service.interfaces.ClientService;
import com.barbershop.mapper.interfaces.ClientMapper;
import com.barbershop.repository.ClientRepository;
import com.barbershop.utils.time.TimeCalculation;
import com.barbershop.validation.client.ClientValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

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
    public List<ClientInfoDTO> getClientList() {

        return mapper.mapClientToInfoDTO(clientRepository.findAll());
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

        LocalDate startDateTimeAfter = TimeCalculation.getStartOfCurrentMonth();
        LocalDate startDateTimeBefore = TimeCalculation.getEndOfCurrentMonth();

        Long clientsRegisteredThisMonth = clientRepository.countByRegistrationDateBetween(startDateTimeAfter, startDateTimeBefore);
        Long clientsTheLastMonth = clientRepository.countByRegistrationDateBetween(startDateTimeAfter.minusMonths(1), startDateTimeBefore.minusMonths(1));

        if (clientsRegisteredThisMonth == 0 && clientsTheLastMonth == 0) {

            return 0L;

        } else if (clientsTheLastMonth == 0) {

            return 100L;
        }

        return ((clientsRegisteredThisMonth - clientsTheLastMonth) * 100) / clientsTheLastMonth;
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
