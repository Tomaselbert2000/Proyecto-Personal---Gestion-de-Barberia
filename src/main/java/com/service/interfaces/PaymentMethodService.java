package com.service.interfaces;

import com.dto.paymentmethod.PaymentMethodCreationDTO;
import com.dto.paymentmethod.PaymentMethodInfoDTO;
import com.dto.paymentmethod.PaymentMethodUpdateDTO;
import com.enums.PaymentMethodModifierType;
import com.enums.PaymentMethodStatus;

import java.util.List;

public interface PaymentMethodService {

    void registerNewPaymentMethod(PaymentMethodCreationDTO creationDTO);

    void deletePaymentMethod(Long paymentMethodID);

    List<PaymentMethodInfoDTO> getPaymentMethodsList();

    void updatePaymentMethod(Long paymentMethodID, PaymentMethodUpdateDTO updateDTO);

    List<PaymentMethodInfoDTO> paymentMethodLiveSearch(String paymentName, PaymentMethodStatus status, PaymentMethodModifierType modifierType);

    Long getPaymentMethodCountMarkedAsActive();

    void togglePaymentMethodStatus(String name);
}
