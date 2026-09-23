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

import static com.presentation.constants.StringResource.DisplayString.PAYMENT_METHOD_COMBOBOX_NO_FILTER;

@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodValidator validator;
    private final PaymentMethodMapper mapper;

    @Override
    @Transactional
    public void registerNewPaymentMethod(PaymentMethodCreationDTO dto) {

        validator.validateDTO(dto);

        checkNameAvailability(dto.getName());

        PaymentMethod mappedEntity = mapper.mapPaymentMethodCreationDtoToEntity(dto);

        paymentMethodRepository.save(mappedEntity);
    }

    @Override
    @Transactional
    public void deletePaymentMethod(Long id) {

        PaymentMethod paymentMethodOnDB = loadPaymentMethodByID(id);

        paymentMethodRepository.delete(paymentMethodOnDB);
    }

    @Override
    public PaymentMethodInfoDTO getPaymentMethod(Long id) {

        PaymentMethod paymentMethodOnDB = loadPaymentMethodByID(id);

        return mapper.mapPaymentMethodToInfoDTO(paymentMethodOnDB);
    }

    @Override
    public List<PaymentMethodInfoDTO> getPaymentMethodsList() {

        List<PaymentMethod> paymentMethodListOnDB = paymentMethodRepository.findAll();

        return mapper.mapPaymentMethodToInfoDTO(paymentMethodListOnDB);
    }

    @Override
    @Transactional
    public void updatePaymentMethod(Long id, PaymentMethodUpdateDTO dto) {

        PaymentMethod paymentMethodOnDB = loadPaymentMethodByID(id);

        validator.validateDTO(dto);

        checkNameAvailability(dto.getNewName(), id);

        paymentMethodRepository.save(mapper.mapPaymentMethodUpdateDtoToEntity(paymentMethodOnDB, dto));
    }

    @Override
    public List<PaymentMethodInfoDTO> liveSearch(String name, PaymentMethodStatus status, PaymentMethodModifierType modifierType) {

        Boolean isActiveValueToSearch = null;

        switch (status) {

            case INACTIVO -> isActiveValueToSearch = false;

            case ACTIVO -> isActiveValueToSearch = true;
        }

        List<PaymentMethod> filteredList = paymentMethodRepository.paymentMethodLiveSearch(name, isActiveValueToSearch, modifierType);

        return mapper.mapPaymentMethodToInfoDTO(filteredList);
    }

    @Override
    public Long getPaymentMethodCountMarkedAsActive() {

        return paymentMethodRepository.countByIsActiveTrue();
    }

    @Override
    @Transactional
    public void togglePaymentMethodStatus(String name) {

        PaymentMethod existingPaymentMethod = loadPaymentMethodByName(name);

        existingPaymentMethod.setIsActive(!existingPaymentMethod.getIsActive());
        paymentMethodRepository.save(existingPaymentMethod);
    }

    @Override
    public List<String> getNames() {

        List<String> names = new ArrayList<>();

        names.addFirst(PAYMENT_METHOD_COMBOBOX_NO_FILTER);

        for (PaymentMethod paymentMethod : paymentMethodRepository.findAll()) {

            names.add(paymentMethod.getName());
        }

        return names;
    }

    private PaymentMethod loadPaymentMethodByName(String name) {

        return paymentMethodRepository.findPaymentMethodByName(name).orElseThrow(PaymentMethodNotFoundException::new);
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
