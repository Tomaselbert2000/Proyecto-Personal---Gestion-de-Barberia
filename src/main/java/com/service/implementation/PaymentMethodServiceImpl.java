package com.service.implementation;

import com.dto.payment.PaymentMethodCreationDTO;
import com.dto.payment.PaymentMethodInfoDTO;
import com.dto.payment.PaymentMethodUpdateDTO;
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

        PaymentMethod paymentMethodOnDB = loadPaymentMethod(paymentMethodID);

        paymentMethodRepository.delete(paymentMethodOnDB);
    }

    @Override
    public PaymentMethodInfoDTO getPaymentMethod(Long paymentMethodID) {

        PaymentMethod paymentMethodOnDB = loadPaymentMethod(paymentMethodID);

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

        PaymentMethod paymentMethodOnDB = loadPaymentMethod(paymentMethodID);

        validator.validateDTO(updateDTO);

        checkNameAvailability(updateDTO.getNewName(), paymentMethodID);

        paymentMethodRepository.save(mapper.mapPaymentMethodUpdateDtoToPaymentMethod(paymentMethodOnDB, updateDTO));
    }

    private PaymentMethod loadPaymentMethod(Long paymentMethodID) {

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
