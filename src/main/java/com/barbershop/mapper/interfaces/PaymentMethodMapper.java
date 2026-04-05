package com.barbershop.mapper.interfaces;

import com.barbershop.dto.payment.PaymentMethodCreationDTO;
import com.barbershop.dto.payment.PaymentMethodInfoDTO;
import com.barbershop.dto.payment.PaymentMethodUpdateDTO;
import com.barbershop.model.PaymentMethod;

import java.util.List;

public interface PaymentMethodMapper {

    PaymentMethod mapPaymentMethodCreationDtoToPaymentMethod(PaymentMethodCreationDTO dto);

    PaymentMethod mapPaymentMethodUpdateDtoToPaymentMethod(PaymentMethod paymentMethod, PaymentMethodUpdateDTO updateDTO);

    PaymentMethodInfoDTO mapPaymentMethodToInfoDTO(PaymentMethod paymentMethod);

    List<PaymentMethodInfoDTO> mapPaymentMethodToInfoDTO(List<PaymentMethod> paymentMethodList);
}
