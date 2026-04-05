package com.impl;

import com.barbershop.dto.payment.PaymentMethodCreationDTO;
import com.barbershop.dto.payment.PaymentMethodInfoDTO;
import com.barbershop.dto.payment.PaymentMethodUpdateDTO;
import com.barbershop.enums.PaymentMethodModifierType;
import com.barbershop.exceptions.paymentmethod.*;
import com.barbershop.mapper.implementation.PaymentMethodMapperImpl;
import com.barbershop.mapper.interfaces.PaymentMethodMapper;
import com.barbershop.model.PaymentMethod;
import com.barbershop.repository.PaymentMethodRepository;
import com.barbershop.service.implementation.PaymentMethodServiceImpl;
import com.barbershop.validation.payment.PaymentMethodValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentMethodServiceImplTest {

    private static final Instant INSTANT = Instant.parse("2026-01-01T10:00:00Z");
    private static final ZoneId ZONE_ID = ZoneId.of("America/Argentina/Buenos_Aires");
    private static final Clock CLOCK = Clock.fixed(INSTANT, ZONE_ID);

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private PaymentMethodValidator validator;

    @Spy
    private PaymentMethodMapper mapper = new PaymentMethodMapperImpl(CLOCK);

    @InjectMocks
    private PaymentMethodServiceImpl paymentMethodService;

    @Captor
    ArgumentCaptor<PaymentMethod> captor;

    private PaymentMethod paymentMethod;
    private PaymentMethodCreationDTO creationDTO;
    private PaymentMethodUpdateDTO updateDTO;

    private static final String NAME = "Mercado Pago";
    private static final String DESCRIPTION = "Pasarela de pagos de Mercado Libre SRL";
    private static final PaymentMethodModifierType MODIFIER_TYPE = PaymentMethodModifierType.RECARGO;
    private static final Double PRICE_MODIFIER = 0.06;

    private static final String UPDATE_NAME = "Mercado Pago SRL";
    private static final String UPDATE_DESCRIPTION = "Nueva descripcion";
    private static final PaymentMethodModifierType UPDATE_MODIFIER_TYPE = PaymentMethodModifierType.NINGUNO;
    private static final Double UPDATE_PRICE_MODIFIER = 0.0;
    private static final Boolean UPDATE_IS_ACTIVE_VALUE = false;

    private static final Long PAYMENT_METHOD_ID = 1L;
    private static final LocalDate CREATION_DATE = LocalDate.now(CLOCK);
    private static final String DEFAULT_DESCRIPTION = PaymentMethodMapperImpl.DEFAULT_DESCRIPTION;
    private static final Boolean DEFAULT_IS_ACTIVE_VALUE = PaymentMethodMapperImpl.DEFAULT_IS_ACTIVE_VALUE;

    @BeforeEach
    void init() {

        setupCreationDTO();
        setupUpdateDTO();
        setupPaymentMethod();
    }

    @Test
    @DisplayName("Dado un DTO de creación con datos correctos, deberá ser persistido exitosamente")
    void givenDTOwithValidData_WhenCreating_ThenIsSuccessufullyPersisted() {

        registerPaymentMethod();

        capturePayment();

        verifyValidatorCreationInteraction();
        verifyMapperCreationInteraction();
        verifyThatPaymentMethodWasRegistered();

        PaymentMethod capturedPayment = captor.getValue();

        assertAll(
                "Verifación de campos",
                () -> assertNotNull(capturedPayment),
                () -> assertEquals(NAME, capturedPayment.getName()),
                () -> assertEquals(DESCRIPTION, capturedPayment.getDescription()),
                () -> assertEquals(MODIFIER_TYPE, capturedPayment.getModifierType()),
                () -> assertEquals(PRICE_MODIFIER, capturedPayment.getPriceModifier())
        );
    }

    @Test
    @DisplayName("Dado un DTO de creación con cualquiera de sus valores en NULL, la validación fallará y el medio de pago no será persistido")
    void givenDTOwithAnyNULL_WhenCreating_ThenPaymentMethodIsNotPersisted() {

        doThrow(NullPaymentMethodInputDataException.class).when(validator).validateForCreation(creationDTO);

        assertThrows(NullPaymentMethodInputDataException.class, this::registerPaymentMethod);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteraction();
        verifyThatPaymentMethodWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre en blanco, la validación fallará y el medio de pago no será persistido")
    void givenBlankName_WhenCreating_ThenPaymentMethodIsNotPersisted() {

        doThrow(BlankPaymentMethodNameException.class).when(validator).validateForCreation(creationDTO);

        assertThrows(BlankPaymentMethodNameException.class, this::registerPaymentMethod);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteraction();
        verifyThatPaymentMethodWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre compuesto por caractéres inválidos, la validación fallará y el medio de pago no será persistido")
    void givenInvalidName_WhenCreating_ThenPaymentMethodIsNotPersisted() {

        doThrow(InvalidPaymentMethodNameException.class).when(validator).validateForCreation(creationDTO);

        assertThrows(InvalidPaymentMethodNameException.class, this::registerPaymentMethod);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteraction();
        verifyThatPaymentMethodWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con una descripción en blanco, la validación será exitosa y el medio de pago se persistirá con una descripción por defecto")
    void givenBlankDescription_WhenCreating_ThenIsSuccessfullyReplacedByADefaultDescription() {

        creationDTO.setDescription("");

        registerPaymentMethod();

        capturePayment();

        PaymentMethod capturedPayment = captor.getValue();

        assertEquals(DEFAULT_DESCRIPTION, capturedPayment.getDescription());
    }

    @Test
    @DisplayName("Dado un DTO de creación con datos válidos, al persistirse el medio de pago su estado de actividad por defecto deberá ser Activo")
    void givenDTOwithValidData_WhenCreating_ThenDefaultActiveValueIsTrue() {

        registerPaymentMethod();

        capturePayment();

        PaymentMethod capturedPayment = captor.getValue();

        assertEquals(DEFAULT_IS_ACTIVE_VALUE, capturedPayment.getIsActive());
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre válido pero que ya fue registrado anteriormente, la validación fallará y el medio de pago no será persistido")
    void givenDuplicatedName_WhenCreating_ThenThrows_DuplicatedPaymentMethodNameException() {

        mockThatNameWillCauseConflict();

        assertThrows(DuplicatedPaymentMethodNameException.class, this::registerPaymentMethod);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteraction();
        verifyThatPaymentMethodWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un medio de pago previamente registrado, deberá retornar su información mediante mapeo por DTO informativo")
    void givenExistingPaymentMethod_WhenSearching_ThenReturnsItsInformation() {

        mockPaymentMethod();

        PaymentMethodInfoDTO returnedDTO = paymentMethodService.getPaymentMethod(PAYMENT_METHOD_ID);

        assertAll(
                "Verificación de campos",
                () -> assertNotNull(returnedDTO),
                () -> assertEquals(NAME, returnedDTO.getName()),
                () -> assertEquals(DESCRIPTION, returnedDTO.getDescription()),
                () -> assertEquals(MODIFIER_TYPE, returnedDTO.getModifierType()),
                () -> assertEquals(PRICE_MODIFIER, returnedDTO.getPriceModifier())
        );
    }

    @Test
    @DisplayName("Dado un ID de medio de pago inexistente, al buscarlo arrojará PaymentMethodNotFoundException")
    void givenNonExistingPaymentMethod_WhenSearching_ThenThrows_PaymentMethodNotFoundException() {

        mockThatnPaymentMethodDoesNotExist();

        assertThrows(PaymentMethodNotFoundException.class, () -> paymentMethodService.getPaymentMethod(PAYMENT_METHOD_ID));
    }

    @Test
    @DisplayName("Dado un medio de pago previamente registrado, deberá poder ser eliminado exitosamente")
    void givenExistingPaymentMethod_WhenDeleting_ThenIsSuccessufullyErased() {

        mockPaymentMethod();

        deletePaymentMethod();

        verifyThatPaymentMethodWasDeleted();
    }

    @Test
    @DisplayName("Dado un ID de medio de pago inexistente, al intentar eliminarlo arrojará PaymentMethodNotFoundException")
    void givenNonExistingPaymentMethod_WhenDeleting_ThenThrows_PaymentMethodNotFoundException() {

        mockThatnPaymentMethodDoesNotExist();

        assertThrows(PaymentMethodNotFoundException.class, this::deletePaymentMethod);

        verifyThatPaymentMethodWasNotDeleted();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con datos correctos, se deberán persistir los cambios exitosamente")
    void givenExistingPaymentMethod_WhenUpdatingWithValidData_ThenIsSuccessfullyPersisted() {

        mockPaymentMethod();

        updatePaymentMethod();

        capturePayment();

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateInteraction();
        verifyThatPaymentMethodWasUpdated();

        PaymentMethod capturedPayment = captor.getValue();

        assertAll(
                "Verifación de campos",
                () -> assertNotNull(capturedPayment),
                () -> assertEquals(UPDATE_NAME, capturedPayment.getName()),
                () -> assertEquals(UPDATE_DESCRIPTION, capturedPayment.getDescription()),
                () -> assertEquals(UPDATE_IS_ACTIVE_VALUE, capturedPayment.getIsActive()),
                () -> assertEquals(UPDATE_MODIFIER_TYPE, capturedPayment.getModifierType()),
                () -> assertEquals(UPDATE_PRICE_MODIFIER, capturedPayment.getPriceModifier())
        );
    }

    @Test
    @DisplayName("Dado un ID de medio de pago inexistente, al intentar actualizarlo, arrojará PaymentMethodNotFoundException")
    void givenNonExistingPaymentMethod_WhenUpdating_ThenThrows_PaymentMethodNotFoundException() {

        mockThatnPaymentMethodDoesNotExist();

        assertThrows(PaymentMethodNotFoundException.class, this::updatePaymentMethod);

        verifyThatPaymentMethodWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre en blanco, la validación fallará y arrojará BlankPaymentMethodNameException")
    void givenBlankName_WhenUpdating_ThenThrows_BlankPaymentMethodNameException() {

        mockPaymentMethod();

        doThrow(BlankPaymentMethodNameException.class).when(validator).validateForUpdate(updateDTO);

        assertThrows(BlankPaymentMethodNameException.class, this::updatePaymentMethod);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteraction();
        verifyThatPaymentMethodWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre compuesto por caractéres inválidos, la validación fallará y arrojará InvalidPaymentMethodNameException")
    void givenInvalidName_WhenUpdating_ThenThrows_InvalidPaymentMethodNameException() {

        mockPaymentMethod();

        doThrow(InvalidPaymentMethodNameException.class).when(validator).validateForUpdate(updateDTO);

        assertThrows(InvalidPaymentMethodNameException.class, this::updatePaymentMethod);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteraction();
        verifyThatPaymentMethodWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre que no cumpla longitud mínima o máxima, la validación fallará y arrojará InvalidPaymentMethodNameLengthException")
    void givenNameTooShort_WhenUpdating_ThenThrows_InvalidPaymentMethodNameLengthException() {

        mockPaymentMethod();

        doThrow(InvalidPaymentMethodNameLengthException.class).when(validator).validateForUpdate(updateDTO);

        assertThrows(InvalidPaymentMethodNameLengthException.class, this::updatePaymentMethod);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteraction();
        verifyThatPaymentMethodWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una descripción en blanco, se usará la descripción por defecto")
    void givenBlankDescription_WhenUpdating_ThenDefaultDescriptionIsPersisted() {

        mockPaymentMethod();

        updateDTO.setNewDescription("");

        updatePaymentMethod();

        capturePayment();

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateInteraction();
        verifyThatPaymentMethodWasUpdated();

        PaymentMethod capturedPayment = captor.getValue();

        assertEquals(PaymentMethodMapperImpl.DEFAULT_DESCRIPTION, capturedPayment.getDescription());
    }

    @Test
    @DisplayName("Dado un DTO de actualización con una descripción que no cumpla límite de longitud, la validación fallará y arrojará InvalidPaymentMethodDescriptionLengthException")
    void givenDescriptionTooLong_WhenUpdating_ThenThrows_InvalidPaymentMethodDescriptionLengthException() {

        mockPaymentMethod();

        doThrow(InvalidPaymentMethodDescriptionLengthException.class).when(validator).validateForUpdate(updateDTO);

        assertThrows(InvalidPaymentMethodDescriptionLengthException.class, this::updatePaymentMethod);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteraction();
        verifyThatPaymentMethodWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un valor modificador de precio menor que cero, la validación fallará y arrojará InvalidDecimalValueException")
    void givenPriceModifierValue_WhenUpdating_ThenThrows_InvalidDecimalValueException() {

        mockPaymentMethod();

        doThrow(InvalidDecimalValueException.class).when(validator).validateForUpdate(updateDTO);

        assertThrows(InvalidDecimalValueException.class, this::updatePaymentMethod);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteraction();
        verifyThatPaymentMethodWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre ya registrado en otro medio de pago, la validación fallará y arrojará DuplicatedPaymentMethodNameException")
    void givenDuplicatedName_WhenUpdating_ThenThrows_DuplicatedPaymentMethodNameException(){

        mockPaymentMethod();
        mockThatNameWillCauseConflictOnUpdate();

        assertThrows(DuplicatedPaymentMethodNameException.class, this::updatePaymentMethod);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteraction();
        verifyThatPaymentMethodWasNotUpdated();
    }

    private void setupPaymentMethod() {

        paymentMethod = PaymentMethod.builder()
                .paymentMethodID(PAYMENT_METHOD_ID)
                .name(NAME)
                .description(DESCRIPTION)
                .isActive(DEFAULT_IS_ACTIVE_VALUE)
                .createdAt(CREATION_DATE)
                .modifierType(MODIFIER_TYPE)
                .priceModifier(PRICE_MODIFIER)
                .build();
    }

    private void setupCreationDTO() {

        creationDTO = PaymentMethodCreationDTO.builder()
                .name(NAME)
                .description(DESCRIPTION)
                .priceModifierType(MODIFIER_TYPE)
                .priceModifier(PRICE_MODIFIER)
                .build();
    }

    private void setupUpdateDTO() {

        updateDTO = PaymentMethodUpdateDTO.builder()
                .newName(UPDATE_NAME)
                .newDescription(UPDATE_DESCRIPTION)
                .isActive(UPDATE_IS_ACTIVE_VALUE)
                .newModifierType(UPDATE_MODIFIER_TYPE)
                .priceModifier(UPDATE_PRICE_MODIFIER)
                .build();
    }

    private void capturePayment() {

        verify(paymentMethodRepository).save(captor.capture());
    }

    private void registerPaymentMethod() {

        paymentMethodService.registerNewPaymentMethod(creationDTO);
    }

    private void deletePaymentMethod() {

        paymentMethodService.deletePaymentMethod(PAYMENT_METHOD_ID);
    }

    private void updatePaymentMethod() {

        paymentMethodService.updatePaymentMethod(PAYMENT_METHOD_ID, updateDTO);
    }

    private void verifyValidatorCreationInteraction() {

        verify(validator).validateForCreation(creationDTO);
    }

    private void verifyValidatorUpdateInteraction() {

        verify(validator).validateForUpdate(updateDTO);
    }

    private void verifyMapperCreationInteraction() {

        verify(mapper).mapPaymentMethodCreationDtoToPaymentMethod(creationDTO);
    }

    private void verifyMapperCreationNoInteraction() {

        verify(mapper, never()).mapPaymentMethodCreationDtoToPaymentMethod(creationDTO);
    }

    private void verifyMapperUpdateInteraction() {

        verify(mapper).mapPaymentMethodUpdateDtoToPaymentMethod(paymentMethod, updateDTO);
    }

    private void verifyMapperUpdateNoInteraction() {

        verify(mapper, never()).mapPaymentMethodUpdateDtoToPaymentMethod(paymentMethod, updateDTO);
    }

    private void verifyThatPaymentMethodWasRegistered() {

        verify(paymentMethodRepository, times(1)).save(any());
    }

    private void verifyThatPaymentMethodWasNotRegistered() {

        verify(paymentMethodRepository, never()).save(any());
    }

    private void verifyThatPaymentMethodWasDeleted() {

        verify(paymentMethodRepository, times(1)).delete(paymentMethod);
    }

    private void verifyThatPaymentMethodWasNotDeleted() {

        verify(paymentMethodRepository, never()).delete(paymentMethod);
    }

    private void verifyThatPaymentMethodWasUpdated() {

        verify(paymentMethodRepository, times(1)).save(paymentMethod);
    }

    private void verifyThatPaymentMethodWasNotUpdated() {

        verify(paymentMethodRepository, never()).save(paymentMethod);
    }

    private void mockThatNameWillCauseConflict() {

        when(paymentMethodRepository.existsByName(NAME)).thenReturn(true);
    }

    private void mockThatNameWillCauseConflictOnUpdate() {

        when(paymentMethodRepository.existsByNameAndPaymentMethodIDNot(UPDATE_NAME, PAYMENT_METHOD_ID)).thenReturn(true);
    }

    private void mockPaymentMethod() {

        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(paymentMethod));
    }

    private void mockThatnPaymentMethodDoesNotExist() {

        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.empty());
    }
}
