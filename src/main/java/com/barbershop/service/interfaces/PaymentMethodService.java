package com.barbershop.service.interfaces;

import com.barbershop.dto.payment.PaymentMethodCreationDTO;
import com.barbershop.dto.payment.PaymentMethodInfoDTO;
import com.barbershop.dto.payment.PaymentMethodUpdateDTO;

import java.util.List;

public interface PaymentMethodService {

    void registerNewPaymentMethod(PaymentMethodCreationDTO creationDTO);

    void deletePaymentMethod(Long paymentMethodID);

    PaymentMethodInfoDTO getPaymentMethod(Long paymentMethodID);

    List<PaymentMethodInfoDTO> getPaymentMethodsList();

    void updatePaymentMethod(Long paymentMethodID, PaymentMethodUpdateDTO updateDTO);
}
