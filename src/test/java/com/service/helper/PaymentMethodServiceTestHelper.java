package com.service.helper;

import com.dto.payment.PaymentMethodCreationDTO;
import com.dto.payment.PaymentMethodUpdateDTO;
import com.mapper.interfaces.PaymentMethodMapper;
import com.model.PaymentMethod;
import com.repository.PaymentMethodRepository;
import com.service.interfaces.PaymentMethodService;
import com.validation.payment.PaymentMethodValidator;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class PaymentMethodServiceTestHelper {

    public static void capturePayment(PaymentMethodRepository paymentMethodRepository, ArgumentCaptor<PaymentMethod> captor) {

        verify(paymentMethodRepository).save(captor.capture());
    }

    public static void registerPaymentMethod(PaymentMethodService paymentMethodService, PaymentMethodCreationDTO creationDTO) {

        paymentMethodService.registerNewPaymentMethod(creationDTO);
    }

    public static void deletePaymentMethod(PaymentMethodService paymentMethodService, PaymentMethod paymentMethod) {

        paymentMethodService.deletePaymentMethod(paymentMethod.getPaymentMethodID());
    }

    public static void updatePaymentMethod(PaymentMethodService paymentMethodService, PaymentMethod existingPaymentMethod, PaymentMethodUpdateDTO updateDTO) {

        paymentMethodService.updatePaymentMethod(existingPaymentMethod.getPaymentMethodID(), updateDTO);
    }

    public static void verifyValidatorCreationInteraction(PaymentMethodValidator validator, PaymentMethodCreationDTO creationDTO) {

        verify(validator).validateDTO(creationDTO);
    }

    public static void verifyValidatorUpdateInteraction(PaymentMethodValidator validator, PaymentMethodUpdateDTO updateDTO) {

        verify(validator).validateDTO(updateDTO);
    }

    public static void verifyMapperCreationInteraction(PaymentMethodMapper mapper, PaymentMethodCreationDTO creationDTO) {

        verify(mapper).mapPaymentMethodCreationDtoToPaymentMethod(creationDTO);
    }

    public static void verifyMapperCreationNoInteraction(PaymentMethodMapper mapper, PaymentMethodCreationDTO creationDTO) {

        verify(mapper, never()).mapPaymentMethodCreationDtoToPaymentMethod(creationDTO);
    }

    public static void verifyMapperUpdateInteraction(PaymentMethodMapper mapper, PaymentMethod paymentMethod, PaymentMethodUpdateDTO updateDTO) {

        verify(mapper).mapPaymentMethodUpdateDtoToPaymentMethod(paymentMethod, updateDTO);
    }

    public static void verifyMapperUpdateNoInteraction(PaymentMethodMapper mapper, PaymentMethod paymentMethod, PaymentMethodUpdateDTO updateDTO) {

        verify(mapper, never()).mapPaymentMethodUpdateDtoToPaymentMethod(paymentMethod, updateDTO);
    }

    public static void mockThatNameWillCauseConflict(PaymentMethodRepository paymentMethodRepository, PaymentMethod paymentMethod) {

        when(paymentMethodRepository.existsByName(paymentMethod.getName())).thenReturn(true);
    }

    public static void mockThatNameWillCauseConflictOnUpdate(PaymentMethodRepository paymentMethodRepository, PaymentMethod paymentMethod) {

        when(paymentMethodRepository.existsByNameAndPaymentMethodIDNot(paymentMethod.getName(), paymentMethod.getPaymentMethodID())).thenReturn(true);
    }

    public static void mockPaymentMethod(PaymentMethodRepository paymentMethodRepository, PaymentMethod paymentMethod) {

        when(paymentMethodRepository.findById(paymentMethod.getPaymentMethodID())).thenReturn(Optional.of(paymentMethod));
    }

    public static void mockThatnPaymentMethodDoesNotExist(PaymentMethodRepository paymentMethodRepository, PaymentMethod paymentMethod) {

        when(paymentMethodRepository.findById(paymentMethod.getPaymentMethodID())).thenReturn(Optional.empty());
    }

    public static <T> void mockValidatorToThrowException(PaymentMethodValidator validator, Exception exception, T dto) {

        doThrow(exception).when(validator).validateDTO(dto);
    }
}
