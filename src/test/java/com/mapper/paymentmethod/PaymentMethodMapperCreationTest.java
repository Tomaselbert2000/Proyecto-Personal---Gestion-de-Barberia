package com.mapper.paymentmethod;

import com.dto.payment.PaymentMethodCreationDTO;
import com.mapper.implementation.PaymentMethodMapperImpl;
import com.mapper.interfaces.PaymentMethodMapper;
import com.model.PaymentMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.factory.PaymentMethodTestDataFactory.buildValidPaymentMethodCreationDTO;
import static com.test_constant.PaymentMethodTestConstants.CreationValidData.PAYMENT_METHOD_NAME;
import static com.test_constant.PaymentMethodTestConstants.MapperData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentMethodMapperCreationTest {

    private final PaymentMethodMapper mapper = new PaymentMethodMapperImpl(PAYMENT_METHOD_TEST_CLOCK);
    private final PaymentMethodCreationDTO creationDTO = buildValidPaymentMethodCreationDTO();
    private final LocalDate currentDate = LocalDate.now(PAYMENT_METHOD_TEST_CLOCK);

    @Test
    @DisplayName("Verifica que el nombre con espacios se trimea correctamente")
    void givenNameWithSpaces_WhenCreating_ThenIsTrimmed() {

        creationDTO.setName(PAYMENT_METHOD_NAME_WITH_SPACES);

        PaymentMethod result = mapEntity(creationDTO);

        assertEquals(PAYMENT_METHOD_NAME, result.getName());
    }

    @Test
    @DisplayName("Verifica que el nombre en minúsculas se capitaliza correctamente")
    void givenLowercaseName_WhenCreating_ThenIsCapitalized() {

        creationDTO.setName(LOWERCASE_PAYMENT_METHOD_NAME);

        PaymentMethod result = mapEntity(creationDTO);

        assertEquals(PAYMENT_METHOD_NAME, result.getName());
    }

    @Test
    @DisplayName("Verifica que un nuevo método de pago está activo por defecto")
    void givenNewPaymentMethod_WhenCreating_ThenIsActiveByDefault() {

        PaymentMethod result = mapEntity(creationDTO);

        assertTrue(result.getIsActive());
    }

    @Test
    @DisplayName("Verifica que la fecha de creación del método de pago es la fecha actual")
    void givenNewPaymentMethod_WhenCreating_ThenCreatedAtDateIsCurrentDate() {

        PaymentMethod result = mapEntity(creationDTO);

        assertEquals(currentDate, result.getCreatedAt());
    }

    @Test
    @DisplayName("Verifica que se aplica la descripción por defecto cuando no se proporciona una")
    void givenBlankDescriptionValue_ThenDefaultDescriptionIsApplied() {

        String defaultDescription = "No se proporcionó una descripción.";

        creationDTO.setDescription("");

        PaymentMethod result = mapEntity(creationDTO);

        assertEquals(defaultDescription, result.getDescription());
    }

    private PaymentMethod mapEntity(PaymentMethodCreationDTO creationDTO) {

        return mapper.mapPaymentMethodCreationDtoToPaymentMethod(creationDTO);
    }
}
