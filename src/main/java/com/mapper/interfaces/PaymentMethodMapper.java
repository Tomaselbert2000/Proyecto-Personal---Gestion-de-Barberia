package com.mapper.interfaces;

import com.dto.paymentmethod.PaymentMethodCreationDTO;
import com.dto.paymentmethod.PaymentMethodInfoDTO;
import com.dto.paymentmethod.PaymentMethodUpdateDTO;
import com.model.PaymentMethod;

import java.util.List;

public interface PaymentMethodMapper {

    PaymentMethod mapPaymentMethodCreationDtoToPaymentMethod(PaymentMethodCreationDTO dto);

    PaymentMethod mapPaymentMethodUpdateDtoToPaymentMethod(PaymentMethod paymentMethod, PaymentMethodUpdateDTO updateDTO);

    PaymentMethodInfoDTO mapPaymentMethodToInfoDTO(PaymentMethod paymentMethod);

    List<PaymentMethodInfoDTO> mapPaymentMethodToInfoDTO(List<PaymentMethod> paymentMethodList);
}
