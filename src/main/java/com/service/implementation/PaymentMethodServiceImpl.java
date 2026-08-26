package com.service.implementation;

import com.dto.paymentmethod.PaymentMethodCreationDTO;
import com.dto.paymentmethod.PaymentMethodInfoDTO;
import com.dto.paymentmethod.PaymentMethodUpdateDTO;
import com.enums.PaymentMethodModifierType;
import com.enums.PaymentMethodStatus;
import com.exceptions.paymentmethod.DuplicatedPaymentMethodNameException;
import com.exceptions.paymentmethod.PaymentMethodNotFoundException;
import com.mapper.interfaces.PaymentMethodMapper;
import com.model.PaymentMethod;
import com.repository.PaymentMethodRepository;
import com.service.interfaces.PaymentMethodService;
import com.validation.payment.PaymentMethodValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodValidator validator;
    private final PaymentMethodMapper mapper;

    @Override
    @Transactional
    public void registerNewPaymentMethod(PaymentMethodCreationDTO creationDTO) {

        validator.validateDTO(creationDTO);

        checkNameAvailability(creationDTO.getName());

        PaymentMethod mappedEntity = mapper.mapPaymentMethodCreationDtoToPaymentMethod(creationDTO);

        paymentMethodRepository.save(mappedEntity);
    }

    @Override
    @Transactional
    public void deletePaymentMethod(Long paymentMethodID) {

        PaymentMethod paymentMethodOnDB = loadPaymentMethodByID(paymentMethodID);

        paymentMethodRepository.delete(paymentMethodOnDB);
    }

    @Override
    public PaymentMethodInfoDTO getPaymentMethod(Long paymentMethodID) {

        PaymentMethod paymentMethodOnDB = loadPaymentMethodByID(paymentMethodID);

        return mapper.mapPaymentMethodToInfoDTO(paymentMethodOnDB);
    }

    @Override
    public List<PaymentMethodInfoDTO> getPaymentMethodsList() {

        List<PaymentMethod> paymentMethodListOnDB = paymentMethodRepository.findAll();

        return mapper.mapPaymentMethodToInfoDTO(paymentMethodListOnDB);
    }

    @Override
    @Transactional
    public void updatePaymentMethod(Long paymentMethodID, PaymentMethodUpdateDTO updateDTO) {

        PaymentMethod paymentMethodOnDB = loadPaymentMethodByID(paymentMethodID);

        validator.validateDTO(updateDTO);

        checkNameAvailability(updateDTO.getNewName(), paymentMethodID);

        paymentMethodRepository.save(mapper.mapPaymentMethodUpdateDtoToPaymentMethod(paymentMethodOnDB, updateDTO));
    }

    @Override
    public List<PaymentMethodInfoDTO> paymentMethodLiveSearch(String paymentName, PaymentMethodStatus status, PaymentMethodModifierType modifierType) {

        Boolean isActiveValueToSearch = null;

        switch (status) {

            case INACTIVO -> isActiveValueToSearch = false;

            case ACTIVO -> isActiveValueToSearch = true;
        }

        List<PaymentMethod> filteredList = paymentMethodRepository.paymentMethodLiveSearch(paymentName, isActiveValueToSearch, modifierType);

        return mapper.mapPaymentMethodToInfoDTO(filteredList);
    }

    @Override
    public Long getPaymentMethodCountMarkedAsActive() {

        return paymentMethodRepository.getCountMarkedAsActive();
    }

    @Override
    @Transactional
    public void togglePaymentMethodStatus(String name) {

        PaymentMethod existingPaymentMethod = loadPaymentMethodByName(name);

        if (existingPaymentMethod != null) {

            existingPaymentMethod.setIsActive(!existingPaymentMethod.getIsActive());
            paymentMethodRepository.save(existingPaymentMethod);
        }
    }

    @Override
    public List<String> getNames() {

        List<String> names = new ArrayList<>();

        for (PaymentMethod paymentMethod : paymentMethodRepository.findAll()) {

            names.add(paymentMethod.getName());
        }

        return names;
    }

    private PaymentMethod loadPaymentMethodByName(String name) {

        return paymentMethodRepository.findPaymentMethodByName(name);
    }

    private PaymentMethod loadPaymentMethodByID(Long paymentMethodID) {

        return paymentMethodRepository.findById(paymentMethodID).orElseThrow(PaymentMethodNotFoundException::new);
    }

    private void checkNameAvailability(String name) {

        if (paymentMethodRepository.existsByName(name)) throw new DuplicatedPaymentMethodNameException();
    }

    private void checkNameAvailability(String newName, Long paymentMethodID) {

        if (paymentMethodRepository.existsByNameAndPaymentMethodIDNot(newName, paymentMethodID))
            throw new DuplicatedPaymentMethodNameException();
    }
}
