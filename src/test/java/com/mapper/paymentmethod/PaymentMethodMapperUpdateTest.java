package com.mapper.paymentmethod;

import com.dto.payment.PaymentMethodUpdateDTO;
import com.mapper.implementation.PaymentMethodMapperImpl;
import com.mapper.interfaces.PaymentMethodMapper;
import com.model.PaymentMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.PaymentMethodTestDataFactory.buildValidPaymentMethod;
import static com.factory.PaymentMethodTestDataFactory.buildValidPaymentMethodUpdateDTO;
import static com.test_constant.PaymentMethodTestConstants.CreationValidData.*;
import static com.test_constant.PaymentMethodTestConstants.MapperData.PAYMENT_METHOD_TEST_CLOCK;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentMethodMapperUpdateTest {

    private final PaymentMethodMapper mapper = new PaymentMethodMapperImpl(PAYMENT_METHOD_TEST_CLOCK);

    private final PaymentMethod existingPaymentMethod = buildValidPaymentMethod();
    private final PaymentMethodUpdateDTO updateDTO = buildValidPaymentMethodUpdateDTO();

    @Test
    @DisplayName("Debería mantener el nombre actual si se proporciona un valor null para newName")
    void givenNullNewName_WhenUpdating_ThenEntityKeepsCurrentName() {

        updateDTO.setNewName(null);

        PaymentMethod result = mapEntity(existingPaymentMethod, updateDTO);

        assertEquals(PAYMENT_METHOD_NAME, result.getName());
    }

    @Test
    @DisplayName("Debería mantener la descripción actual si se proporciona un valor null para newDescription")
    void givenNullNewDescription_WhenUpdating_ThenEntityKeepsCurrentDescription() {

        updateDTO.setNewDescription(null);

        PaymentMethod result = mapEntity(existingPaymentMethod, updateDTO);

        assertEquals(PAYMENT_METHOD_DESCRIPTION, result.getDescription());
    }

    @Test
    @DisplayName("Debería mantener el valor isActive actual si se proporciona un valor null para newIsActiveValue")
    void givenNullIsActiveValue_WhenUpdating_ThenEntityKeepsCurrentIsActiveValue() {

        Boolean currentIsActiveValue = true;
        updateDTO.setIsActive(null);

        PaymentMethod result = mapEntity(existingPaymentMethod, updateDTO);

        assertEquals(currentIsActiveValue, result.getIsActive());
    }

    @Test
    @DisplayName("Debería mantener el tipo de modificador actual si se proporciona un valor null para newModifierType")
    void givenNullNewModifierType_WhenUpdating_ThenEntityKeepsCurrentPriceModifier() {

        updateDTO.setNewModifierType(null);

        PaymentMethod result = mapEntity(existingPaymentMethod, updateDTO);

        assertEquals(PAYMENT_METHOD_MODIFIER_TYPE, result.getModifierType());
    }

    @Test
    @DisplayName("Debería mantener el modificador de precio actual si se proporciona un valor null para priceModifier")
    void givenNullNewPriceModifier_WhenUpdating_ThenEntityKeepsCurrentPriceModifier() {

        updateDTO.setPriceModifier(null);

        PaymentMethod result = mapEntity(existingPaymentMethod, updateDTO);

        assertEquals(PAYMENT_METHOD_PRICE_MODIFIER_VALUE, result.getPriceModifier());
    }

    private PaymentMethod mapEntity(PaymentMethod existingPaymentMethod, PaymentMethodUpdateDTO updateDTO) {

        return mapper.mapPaymentMethodUpdateDtoToPaymentMethod(existingPaymentMethod, updateDTO);
    }
}
