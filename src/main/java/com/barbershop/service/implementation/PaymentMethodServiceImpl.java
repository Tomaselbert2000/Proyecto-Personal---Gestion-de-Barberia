package com.barbershop.service.implementation;

import com.barbershop.dto.payment.PaymentMethodCreationDTO;
import com.barbershop.dto.payment.PaymentMethodInfoDTO;
import com.barbershop.dto.payment.PaymentMethodUpdateDTO;
import com.barbershop.exceptions.paymentmethod.DuplicatedPaymentMethodNameException;
import com.barbershop.exceptions.paymentmethod.PaymentMethodNotFoundException;
import com.barbershop.mapper.interfaces.PaymentMethodMapper;
import com.barbershop.model.PaymentMethod;
import com.barbershop.repository.PaymentMethodRepository;
import com.barbershop.service.interfaces.PaymentMethodService;
import com.barbershop.validation.payment.PaymentMethodValidator;
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

        validator.validateForCreation(creationDTO);

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

        validator.validateForUpdate(updateDTO);

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
