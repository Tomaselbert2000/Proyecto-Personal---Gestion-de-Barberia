package com.mapper.interfaces;

import com.dto.paymentmethod.PaymentMethodCreationDTO;
import com.dto.paymentmethod.PaymentMethodInfoDTO;
import com.dto.paymentmethod.PaymentMethodUpdateDTO;
import com.model.PaymentMethod;

import java.util.List;

public interface PaymentMethodMapper {

    PaymentMethod mapPaymentMethodCreationDtoToEntity(PaymentMethodCreationDTO dto);

    PaymentMethod mapPaymentMethodUpdateDtoToEntity(PaymentMethod entity, PaymentMethodUpdateDTO dto);

    PaymentMethodInfoDTO mapPaymentMethodToInfoDTO(PaymentMethod entity);

    List<PaymentMethodInfoDTO> mapPaymentMethodToInfoDTO(List<PaymentMethod> entityList);
}
