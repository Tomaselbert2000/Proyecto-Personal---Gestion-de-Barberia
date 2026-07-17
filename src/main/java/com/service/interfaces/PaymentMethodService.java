package com.service.interfaces;

import com.dto.payment.PaymentMethodCreationDTO;
import com.dto.payment.PaymentMethodInfoDTO;
import com.dto.payment.PaymentMethodUpdateDTO;

import java.util.List;

public interface PaymentMethodService {

    void registerNewPaymentMethod(PaymentMethodCreationDTO creationDTO);

    void deletePaymentMethod(Long paymentMethodID);

    PaymentMethodInfoDTO getPaymentMethod(Long paymentMethodID);

    List<PaymentMethodInfoDTO> getPaymentMethodsList();

    void updatePaymentMethod(Long paymentMethodID, PaymentMethodUpdateDTO updateDTO);
}
