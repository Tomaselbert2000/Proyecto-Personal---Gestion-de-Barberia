package com.factory;

import com.dto.payment.PaymentMethodCreationDTO;
import com.dto.payment.PaymentMethodUpdateDTO;
import com.model.PaymentMethod;

import static com.test_constant.PaymentMethodTestConstants.CreationValidData.*;
import static com.test_constant.PaymentMethodTestConstants.UpdateValidData.*;

public class PaymentMethodTestDataFactory {

    public static PaymentMethodCreationDTO buildValidPaymentMethodCreationDTO() {

        return PaymentMethodCreationDTO.builder()
                .name(PAYMENT_METHOD_NAME)
                .description(PAYMENT_METHOD_DESCRIPTION)
                .priceModifierType(PAYMENT_METHOD_MODIFIER_TYPE)
                .priceModifier(PAYMENT_METHOD_PRICE_MODIFIER_VALUE)
                .build();
    }

    public static PaymentMethodUpdateDTO buildValidPaymentMethodUpdateDTO() {

        return PaymentMethodUpdateDTO.builder()
                .newName(UPDATED_PAYMENT_METHOD_NAME)
                .newDescription(UPDATED_PAYMENT_METHOD_DESCRIPTION)
                .isActive(PAYMENT_METHOD_IS_ACTIVE)
                .newModifierType(UPDATED_PAYMENT_METHOD_MODIFIER_TYPE)
                .priceModifier(UPDATED_PRICE_MODIFIER_VALUE)
                .build();
    }

    public static PaymentMethod buildValidPaymentMethod() {

        return PaymentMethod.builder()
                .paymentMethodID(PAYMENT_METHOD_ID)
                .name(PAYMENT_METHOD_NAME)
                .description(PAYMENT_METHOD_DESCRIPTION)
                .isActive(PAYMENT_METHOD_IS_ACTIVE)
                .createdAt(REGISTERED_DATE)
                .modifierType(PAYMENT_METHOD_MODIFIER_TYPE)
                .priceModifier(PAYMENT_METHOD_PRICE_MODIFIER_VALUE)
                .build();
    }
}