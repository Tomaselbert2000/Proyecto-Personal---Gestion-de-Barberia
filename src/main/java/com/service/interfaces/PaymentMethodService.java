package com.service.interfaces;

import com.dto.paymentmethod.PaymentMethodCreationDTO;
import com.dto.paymentmethod.PaymentMethodInfoDTO;
import com.dto.paymentmethod.PaymentMethodUpdateDTO;
import com.enums.PaymentMethodModifierType;
import com.enums.PaymentMethodStatus;

import java.util.List;

public interface PaymentMethodService {

    void registerNewPaymentMethod(PaymentMethodCreationDTO dto);

    void deletePaymentMethod(Long id);

    PaymentMethodInfoDTO getPaymentMethod(Long id);

    List<PaymentMethodInfoDTO> getPaymentMethodsList();

    void updatePaymentMethod(Long id, PaymentMethodUpdateDTO dto);

    List<PaymentMethodInfoDTO> liveSearch(String name, PaymentMethodStatus status, PaymentMethodModifierType modifierType);

    Long getPaymentMethodCountMarkedAsActive();

    void togglePaymentMethodStatus(String name);

    List<String> getNames();
}
