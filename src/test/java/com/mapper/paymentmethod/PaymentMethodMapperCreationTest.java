package com.mapper.paymentmethod;

import com.barbershop.dto.payment.PaymentMethodCreationDTO;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentMethodMapperCreationTest {

    private PaymentMethodCreationDTO creationDTO;

    private final Clock clock = Clock.fixed(Instant.parse("2026-01-01T15:00:00Z"), ZoneId.of("America/Argentina/Buenos_Aires"));

    private final PaymentMethodMapper mapper = new PaymentMethodMapperImpl(clock);

    @BeforeEach
    void init(){

        creationDTO = PaymentMethodCreationDTO.builder()
                .name("Mercado Pago")
                .description("")
                .priceModifierType(PaymentMethodModifierType.RECARGO)
                .priceModifier(0.02)
                .build();
    }

    @Test
    void givenNameWithSpaces_WhenCreating_ThenIsTrimmed(){

        creationDTO.setName("   Mercado Pago   ");

        PaymentMethod result = mapper.mapPaymentMethodCreationDtoToPaymentMethod(creationDTO);

        assertEquals("Mercado Pago", result.getName());
    }

    @Test
    void givenLowercaseName_WhenCreating_ThenIsCapitalized(){

        creationDTO.setName("mercado pago");

        PaymentMethod result = mapper.mapPaymentMethodCreationDtoToPaymentMethod(creationDTO);

        assertEquals("Mercado Pago", result.getName());
    }

    @Test
    void givenNewPaymentMethod_WhenCreating_ThenIsActiveByDefault(){

        PaymentMethod result = mapper.mapPaymentMethodCreationDtoToPaymentMethod(creationDTO);

        assertTrue(result.getIsActive());
    }

    @Test
    void givenNewPaymentMethod_WhenCreating_ThenCreatedAtDateIsCurrentDate(){

        LocalDate currentDate = LocalDate.now(clock);

        PaymentMethod result = mapper.mapPaymentMethodCreationDtoToPaymentMethod(creationDTO);

        assertEquals(currentDate, result.getCreatedAt());
    }

    @Test
    void givenBlankDescriptionValue_ThenDefaultDescriptionIsApplied(){

        String defaultDescription = "No se proporcionó una descripción.";

        creationDTO.setDescription("");

        PaymentMethod result = mapper.mapPaymentMethodCreationDtoToPaymentMethod(creationDTO);

        assertEquals(defaultDescription, result.getDescription());
    }
}
