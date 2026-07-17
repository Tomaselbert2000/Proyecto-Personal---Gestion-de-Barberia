package com.mapper.interfaces;

import com.dto.payment.PaymentMethodCreationDTO;
import com.dto.payment.PaymentMethodInfoDTO;
import com.dto.payment.PaymentMethodUpdateDTO;
import com.model.PaymentMethod;

import java.util.List;

public interface PaymentMethodMapper {

    PaymentMethod mapPaymentMethodCreationDtoToPaymentMethod(PaymentMethodCreationDTO dto);

    PaymentMethod mapPaymentMethodUpdateDtoToPaymentMethod(PaymentMethod paymentMethod, PaymentMethodUpdateDTO updateDTO);

    PaymentMethodInfoDTO mapPaymentMethodToInfoDTO(PaymentMethod paymentMethod);

    List<PaymentMethodInfoDTO> mapPaymentMethodToInfoDTO(List<PaymentMethod> paymentMethodList);
}
