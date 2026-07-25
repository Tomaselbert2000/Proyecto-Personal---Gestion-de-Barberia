package com.service.interfaces;

import com.dto.payment.PaymentMethodCreationDTO;
import com.dto.payment.PaymentMethodInfoDTO;
import com.dto.payment.PaymentMethodUpdateDTO;
import com.enums.PaymentMethodModifierType;
import com.enums.PaymentMethodStatus;

import java.util.List;

public interface PaymentMethodService {

    void registerNewPaymentMethod(PaymentMethodCreationDTO creationDTO);

    void deletePaymentMethod(Long paymentMethodID);

    PaymentMethodInfoDTO getPaymentMethod(Long paymentMethodID);

    List<PaymentMethodInfoDTO> getPaymentMethodsList();

    void updatePaymentMethod(Long paymentMethodID, PaymentMethodUpdateDTO updateDTO);

    List<PaymentMethodInfoDTO> paymentMethodLiveSearch(String paymentName, PaymentMethodStatus status, PaymentMethodModifierType modifierType);

    Long getPaymentMethodCount();

    Long getPaymentMethodCountMarkedAsActive();

    Long getPaymentMethodCountMarkedAsInactive();

    Long getpaymentMethodCountMarkedAsOtherThanNINGUNO();

    void togglePaymentMethodStatus(String name);
}
