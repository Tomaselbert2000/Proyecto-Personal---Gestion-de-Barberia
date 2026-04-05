package com.mapper.paymentmethod;

import com.barbershop.dto.payment.PaymentMethodUpdateDTO;
import com.barbershop.enums.PaymentMethodModifierType;
import com.barbershop.mapper.implementation.PaymentMethodMapperImpl;
import com.barbershop.mapper.interfaces.PaymentMethodMapper;
import com.barbershop.model.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentMethodMapperUpdateTest {

    private final Clock clock = Clock.fixed(Instant.parse("2026-01-01T15:00:00Z"), ZoneId.of("America/Argentina/Buenos_Aires"));

    private final PaymentMethodMapper mapper = new PaymentMethodMapperImpl(clock);

    private PaymentMethod existingPaymentMethod;
    private PaymentMethodUpdateDTO updateDTO;

    @BeforeEach
    void init() {

        existingPaymentMethod = PaymentMethod.builder()
                .paymentMethodID(1L)
                .name("Mercado Pago")
                .description("Pasarela de pagos provista por Mercado Libre SRL")
                .isActive(true)
                .createdAt(LocalDate.of(2026, 1, 1))
                .modifierType(PaymentMethodModifierType.NINGUNO)
                .priceModifier(0.0)
                .build();

        updateDTO = PaymentMethodUpdateDTO.builder()
                .newName("Mastercard")
                .newDescription("Pasarela de pagos de Mastercard")
                .isActive(false)
                .newModifierType(PaymentMethodModifierType.RECARGO)
                .priceModifier(0.01)
                .build();
    }

    @Test
    void givenNullNewName_WhenUpdating_ThenEntityKeepsCurrentName() {

        String currentName = "Mercado Pago";
        updateDTO.setNewName(null);

        PaymentMethod result = mapper.mapPaymentMethodUpdateDtoToPaymentMethod(existingPaymentMethod, updateDTO);

        assertEquals(currentName, result.getName());
    }

    @Test
    void givenNullNewDescription_WhenUpdating_ThenEntityKeepsCurrentDescription() {

        String currentDescription = "Pasarela de pagos provista por Mercado Libre SRL";
        updateDTO.setNewDescription(null);

        PaymentMethod result = mapper.mapPaymentMethodUpdateDtoToPaymentMethod(existingPaymentMethod, updateDTO);

        assertEquals(currentDescription, result.getDescription());
    }

    @Test
    void givenNullIsActiveValue_WhenUpdating_ThenEntityKeepsCurrentIsActiveValue(){

        Boolean currentIsActiveValue = true;
        updateDTO.setIsActive(null);

        PaymentMethod result = mapper.mapPaymentMethodUpdateDtoToPaymentMethod(existingPaymentMethod, updateDTO);

        assertEquals(currentIsActiveValue, result.getIsActive());
    }

    @Test
    void givenNullNewModifierType_WhenUpdating_ThenEntityKeepsCurrentPriceModifier(){

        PaymentMethodModifierType currentPriceModifier = PaymentMethodModifierType.NINGUNO;
        updateDTO.setNewModifierType(null);

        PaymentMethod result = mapper.mapPaymentMethodUpdateDtoToPaymentMethod(existingPaymentMethod, updateDTO);

        assertEquals(currentPriceModifier, result.getModifierType());
    }

    @Test
    void givenNullNewPriceModifier_WhenUpdating_ThenEntityKeepsCurrentPriceModifier(){

        Double currentPriceModifier = 0.0;
        updateDTO.setPriceModifier(null);

        PaymentMethod result = mapper.mapPaymentMethodUpdateDtoToPaymentMethod(existingPaymentMethod, updateDTO);

        assertEquals(currentPriceModifier, result.getPriceModifier());
    }
}
