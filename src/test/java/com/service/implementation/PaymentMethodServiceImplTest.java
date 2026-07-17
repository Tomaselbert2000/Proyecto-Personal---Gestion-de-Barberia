package com.service.implementation;

import com.abstract_test_class.BaseServiceTest;
import com.dto.payment.PaymentMethodCreationDTO;
import com.dto.payment.PaymentMethodInfoDTO;
import com.dto.payment.PaymentMethodUpdateDTO;
import com.exceptions.paymentmethod.*;
import com.mapper.implementation.PaymentMethodMapperImpl;
import com.mapper.interfaces.PaymentMethodMapper;
import com.model.PaymentMethod;
import com.repository.PaymentMethodRepository;
import com.validation.payment.PaymentMethodValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;

import static com.factory.PaymentMethodTestDataFactory.*;
import static com.service.helper.PaymentMethodServiceTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PaymentMethodServiceImplTest extends BaseServiceTest<PaymentMethod, PaymentMethodRepository> {

    private static final Clock clock = Clock.systemDefaultZone();
    private static final String DEFAULT_DESCRIPTION = PaymentMethodMapperImpl.DEFAULT_DESCRIPTION;
    private static final Boolean DEFAULT_IS_ACTIVE_VALUE = PaymentMethodMapperImpl.DEFAULT_IS_ACTIVE_VALUE;
    private final PaymentMethod paymentMethod = buildValidPaymentMethod();
    private final PaymentMethodCreationDTO creationDTO = buildValidPaymentMethodCreationDTO();
    private final PaymentMethodUpdateDTO updateDTO = buildValidPaymentMethodUpdateDTO();
    @Captor
    ArgumentCaptor<PaymentMethod> captor;
    @Mock
    private PaymentMethodRepository paymentMethodRepository;
    @Mock
    private PaymentMethodValidator validator;
    @Spy
    private PaymentMethodMapper mapper = new PaymentMethodMapperImpl(clock);
    @InjectMocks
    private PaymentMethodServiceImpl paymentMethodService;

    @Test
    @DisplayName("Dado un DTO de creación con datos correctos, deberá ser persistido exitosamente")
    void givenDTOwithValidData_WhenCreating_ThenIsSuccessufullyPersisted() {

        registerPaymentMethod(paymentMethodService, creationDTO);

        capturePayment(paymentMethodRepository, captor);

        verifyCreationProcessSuccess();

        PaymentMethod capturedPayment = captor.getValue();

        verifyCreationDTOAssertions(capturedPayment);
    }

    @Test
    @DisplayName("Dado un DTO de creación con cualquiera de sus valores en NULL, la validación fallará y el medio de pago no será persistido")
    void givenDTOwithAnyNULL_WhenCreating_ThenPaymentMethodIsNotPersisted() {

        mockValidatorToThrowException(validator, new NullPaymentMethodInputDataException(), creationDTO);

        assertThrows(NullPaymentMethodInputDataException.class, () -> registerPaymentMethod(paymentMethodService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre en blanco, la validación fallará y el medio de pago no será persistido")
    void givenBlankName_WhenCreating_ThenPaymentMethodIsNotPersisted() {

        mockValidatorToThrowException(validator, new BlankPaymentMethodNameException(), creationDTO);

        assertThrows(BlankPaymentMethodNameException.class, () -> registerPaymentMethod(paymentMethodService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre compuesto por caractéres inválidos, la validación fallará y el medio de pago no será persistido")
    void givenInvalidName_WhenCreating_ThenPaymentMethodIsNotPersisted() {

        mockValidatorToThrowException(validator, new InvalidPaymentMethodNameException(), creationDTO);

        assertThrows(InvalidPaymentMethodNameException.class, () -> registerPaymentMethod(paymentMethodService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con una descripción en blanco, la validación será exitosa y el medio de pago se persistirá con una descripción por defecto")
    void givenBlankDescription_WhenCreating_ThenIsSuccessfullyReplacedByADefaultDescription() {

        creationDTO.setDescription("");

        registerPaymentMethod(paymentMethodService, creationDTO);

        capturePayment(paymentMethodRepository, captor);

        PaymentMethod capturedPayment = captor.getValue();

        assertEquals(DEFAULT_DESCRIPTION, capturedPayment.getDescription());
    }

    @Test
    @DisplayName("Dado un DTO de creación con datos válidos, al persistirse el medio de pago su estado de actividad por defecto deberá ser Activo")
    void givenDTOwithValidData_WhenCreating_ThenDefaultActiveValueIsTrue() {

        registerPaymentMethod(paymentMethodService, creationDTO);

        capturePayment(paymentMethodRepository, captor);

        PaymentMethod capturedPayment = captor.getValue();

        assertEquals(DEFAULT_IS_ACTIVE_VALUE, capturedPayment.getIsActive());
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre válido pero que ya fue registrado anteriormente, la validación fallará y el medio de pago no será persistido")
    void givenDuplicatedName_WhenCreating_ThenThrows_DuplicatedPaymentMethodNameException() {

        mockThatNameWillCauseConflict(paymentMethodRepository, paymentMethod);

        assertThrows(DuplicatedPaymentMethodNameException.class, () -> registerPaymentMethod(paymentMethodService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un medio de pago previamente registrado, deberá retornar su información mediante mapeo por DTO informativo")
    void givenExistingPaymentMethod_WhenSearching_ThenReturnsItsInformation() {

        mockPaymentMethod(paymentMethodRepository, paymentMethod);

        PaymentMethodInfoDTO returnedDTO = paymentMethodService.getPaymentMethod(paymentMethod.getPaymentMethodID());

        verifyInfoDTOAssertions(returnedDTO);
    }

    @Test
    @DisplayName("Dado un ID de medio de pago inexistente, al buscarlo arrojará PaymentMethodNotFoundException")
    void givenNonExistingPaymentMethod_WhenSearching_ThenThrows_PaymentMethodNotFoundException() {

        mockThatnPaymentMethodDoesNotExist(paymentMethodRepository, paymentMethod);

        assertThrows(PaymentMethodNotFoundException.class, () -> paymentMethodService.getPaymentMethod(paymentMethod.getPaymentMethodID()));
    }

    @Test
    @DisplayName("Dado un medio de pago previamente registrado, deberá poder ser eliminado exitosamente")
    void givenExistingPaymentMethod_WhenDeleting_ThenIsSuccessufullyErased() {

        mockPaymentMethod(paymentMethodRepository, paymentMethod);

        deletePaymentMethod(paymentMethodService, paymentMethod);

        verifyThatEntityWasDeleted(paymentMethod);
    }

    @Test
    @DisplayName("Dado un ID de medio de pago inexistente, al intentar eliminarlo arrojará PaymentMethodNotFoundException")
    void givenNonExistingPaymentMethod_WhenDeleting_ThenThrows_PaymentMethodNotFoundException() {

        mockThatnPaymentMethodDoesNotExist(paymentMethodRepository, paymentMethod);

        assertThrows(PaymentMethodNotFoundException.class, () -> deletePaymentMethod(paymentMethodService, paymentMethod));

        verifyThatEntityWasNotDeleted(paymentMethod);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con datos correctos, se deberán persistir los cambios exitosamente")
    void givenExistingPaymentMethod_WhenUpdatingWithValidData_ThenIsSuccessfullyPersisted() {

        mockPaymentMethod(paymentMethodRepository, paymentMethod);

        updatePaymentMethod(paymentMethodService, paymentMethod, updateDTO);

        capturePayment(paymentMethodRepository, captor);

        verifyUpdateProcessSuccess();

        PaymentMethod capturedPayment = captor.getValue();

        assertAll(
                "Verifación de campos",
                () -> assertNotNull(capturedPayment),
                () -> assertEquals(updateDTO.getNewName(), capturedPayment.getName()),
                () -> assertEquals(updateDTO.getNewDescription(), capturedPayment.getDescription()),
                () -> assertEquals(updateDTO.getIsActive(), capturedPayment.getIsActive()),
                () -> assertEquals(updateDTO.getNewModifierType(), capturedPayment.getModifierType()),
                () -> assertEquals(updateDTO.getPriceModifier(), capturedPayment.getPriceModifier())
        );
    }

    @Test
    @DisplayName("Dado un ID de medio de pago inexistente, al intentar actualizarlo, arrojará PaymentMethodNotFoundException")
    void givenNonExistingPaymentMethod_WhenUpdating_ThenThrows_PaymentMethodNotFoundException() {

        mockThatnPaymentMethodDoesNotExist(paymentMethodRepository, paymentMethod);

        assertThrows(PaymentMethodNotFoundException.class, () -> updatePaymentMethod(paymentMethodService, paymentMethod, updateDTO));

        verifyThatEntityWasNotSaved();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre en blanco, la validación fallará y arrojará BlankPaymentMethodNameException")
    void givenBlankName_WhenUpdating_ThenThrows_BlankPaymentMethodNameException() {

        mockPaymentMethod(paymentMethodRepository, paymentMethod);

        mockValidatorToThrowException(validator, new BlankPaymentMethodNameException(), updateDTO);

        assertThrows(BlankPaymentMethodNameException.class, () -> updatePaymentMethod(paymentMethodService, paymentMethod, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre compuesto por caractéres inválidos, la validación fallará y arrojará InvalidPaymentMethodNameException")
    void givenInvalidName_WhenUpdating_ThenThrows_InvalidPaymentMethodNameException() {

        mockPaymentMethod(paymentMethodRepository, paymentMethod);

        mockValidatorToThrowException(validator, new InvalidPaymentMethodNameException(), updateDTO);

        assertThrows(InvalidPaymentMethodNameException.class, () -> updatePaymentMethod(paymentMethodService, paymentMethod, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla longitud mínima o máxima, la validación fallará y arrojará InvalidPaymentMethodNameLengthException")
    void givenNameTooShort_WhenUpdating_ThenThrows_InvalidPaymentMethodNameLengthException() {

        mockPaymentMethod(paymentMethodRepository, paymentMethod);

        mockValidatorToThrowException(validator, new InvalidPaymentMethodNameLengthException(), updateDTO);

        assertThrows(InvalidPaymentMethodNameLengthException.class, () -> updatePaymentMethod(paymentMethodService, paymentMethod, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una descripción en blanco, se usará la descripción por defecto")
    void givenBlankDescription_WhenUpdating_ThenDefaultDescriptionIsPersisted() {

        mockPaymentMethod(paymentMethodRepository, paymentMethod);

        updateDTO.setNewDescription("");

        updatePaymentMethod(paymentMethodService, paymentMethod, updateDTO);

        capturePayment(paymentMethodRepository, captor);

        verifyUpdateProcessSuccess();

        PaymentMethod capturedPayment = captor.getValue();

        assertEquals(PaymentMethodMapperImpl.DEFAULT_DESCRIPTION, capturedPayment.getDescription());
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una descripción que no cumpla límite de longitud, la validación fallará y arrojará InvalidPaymentMethodDescriptionLengthException")
    void givenDescriptionTooLong_WhenUpdating_ThenThrows_InvalidPaymentMethodDescriptionLengthException() {

        mockPaymentMethod(paymentMethodRepository, paymentMethod);

        mockValidatorToThrowException(validator, new InvalidPaymentMethodDescriptionLengthException(), updateDTO);

        assertThrows(InvalidPaymentMethodDescriptionLengthException.class, () -> updatePaymentMethod(paymentMethodService, paymentMethod, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un valor modificador de precio menor que cero, la validación fallará y arrojará InvalidDecimalValueException")
    void givenPriceModifierValue_WhenUpdating_ThenThrows_InvalidDecimalValueException() {

        mockPaymentMethod(paymentMethodRepository, paymentMethod);

        mockValidatorToThrowException(validator, new InvalidDecimalValueException(), updateDTO);

        assertThrows(InvalidDecimalValueException.class, () -> updatePaymentMethod(paymentMethodService, paymentMethod, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre ya registrado en otro medio de pago, la validación fallará y arrojará DuplicatedPaymentMethodNameException")
    void givenDuplicatedName_WhenUpdating_ThenThrows_DuplicatedPaymentMethodNameException() {

        mockPaymentMethod(paymentMethodRepository, paymentMethod);
        mockThatNameWillCauseConflictOnUpdate(paymentMethodRepository, paymentMethod);

        assertThrows(DuplicatedPaymentMethodNameException.class, () -> updatePaymentMethod(paymentMethodService, paymentMethod, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Override
    protected PaymentMethodRepository getPrimaryRepository() {

        return paymentMethodRepository;
    }

    private void verifyCreationDTOAssertions(PaymentMethod capturedPayment) {

        assertNotNull(capturedPayment);

        assertAll(
                "Verifación de campos",
                () -> assertEquals(paymentMethod.getName(), capturedPayment.getName()),
                () -> assertEquals(paymentMethod.getDescription(), capturedPayment.getDescription()),
                () -> assertEquals(paymentMethod.getModifierType(), capturedPayment.getModifierType()),
                () -> assertEquals(paymentMethod.getPriceModifier(), capturedPayment.getPriceModifier())
        );
    }

    private void verifyInfoDTOAssertions(PaymentMethodInfoDTO returnedDTO) {

        assertNotNull(returnedDTO);

        assertAll(
                "Verificación de campos",
                () -> assertEquals(paymentMethod.getName(), returnedDTO.getName()),
                () -> assertEquals(paymentMethod.getDescription(), returnedDTO.getDescription()),
                () -> assertEquals(paymentMethod.getModifierType(), returnedDTO.getModifierType()),
                () -> assertEquals(paymentMethod.getPriceModifier(), returnedDTO.getPriceModifier())
        );
    }

    private void verifyCreationProcessSuccess() {

        verifyValidatorCreationInteraction(validator, creationDTO);
        verifyMapperCreationInteraction(mapper, creationDTO);
        verifyThatEntityWasSaved();
    }

    private void verifyCreationProcessFailure() {

        verifyValidatorCreationInteraction(validator, creationDTO);
        verifyMapperCreationNoInteraction(mapper, creationDTO);
        verifyThatEntityWasNotSaved();
    }

    private void verifyUpdateProcessSuccess() {

        verifyValidatorUpdateInteraction(validator, updateDTO);
        verifyMapperUpdateInteraction(mapper, paymentMethod, updateDTO);
        verifyThatEntityWasSaved();
    }

    private void verifyUpdateProcessFailure() {

        verifyValidatorUpdateInteraction(validator, updateDTO);
        verifyMapperUpdateNoInteraction(mapper, paymentMethod, updateDTO);
        verifyThatEntityWasNotSaved();
    }
}