package com.mapper.paymentmethod;

import com.dto.payment.PaymentMethodInfoDTO;
import com.enums.PaymentMethodStatus;
import com.mapper.implementation.PaymentMethodMapperImpl;
import com.mapper.interfaces.PaymentMethodMapper;
import com.model.PaymentMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.PaymentMethodTestDataFactory.buildValidPaymentMethod;
import static com.test_constant.PaymentMethodTestConstants.MapperData.PAYMENT_METHOD_TEST_CLOCK;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentMethodMapperInfoTest {

    private final PaymentMethodMapper mapper = new PaymentMethodMapperImpl(PAYMENT_METHOD_TEST_CLOCK);
    private final PaymentMethod existingPaymentMethod = buildValidPaymentMethod();

    @Test
    @DisplayName("Dado un medio de pago existente y activo, el atributo currentStatus será igual a ACTIVO")
    void givenExistingPaymentMethod_WhenGettingItsInformation_ItsCurrentStatusIsMappedAsACTIVO() {

        PaymentMethodInfoDTO infoDTO = mapEntity(existingPaymentMethod);

        assertEquals(PaymentMethodStatus.ACTIVO, infoDTO.getCurrentStatus());
    }

    @Test
    @DisplayName("Dado un medio de pago existente e inactivo, el atributo currentStatus será igual a INACTIVO")
    void givenExistingPaymentMethodMarkedAsInactive_WhenGettingItsInformation_ItsCurrentStatusIsMappedAsINACTIVO() {

        existingPaymentMethod.setIsActive(false);

        PaymentMethodInfoDTO infoDTO = mapEntity(existingPaymentMethod);

        assertEquals(PaymentMethodStatus.INACTIVO, infoDTO.getCurrentStatus());
    }

    private PaymentMethodInfoDTO mapEntity(PaymentMethod paymentMethod) {

        return mapper.mapPaymentMethodToInfoDTO(paymentMethod);
    }
}
