package com.barbershop.validation.payment;

import com.barbershop.dto.payment.PaymentMethodCreationDTO;
import com.barbershop.dto.payment.PaymentMethodUpdateDTO;
import com.barbershop.enums.PaymentMethodModifierType;
import com.barbershop.exceptions.common.NullDTOException;
import com.barbershop.exceptions.paymentmethod.*;
import com.barbershop.utils.strings.RegexPatterns;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodValidator {

    private static final Integer MIN_NAME_LENGTH = 4;
    private static final Integer MAX_NAME_LENGTH = 100;
    private static final Double MIN_DECIMAL_VALUE = 0.0;
    private static final Integer MAX_DESCRIPTION_LENGTH = 256;


    public void validateForCreation(PaymentMethodCreationDTO creationDTO) {

        if (creationDTO == null) throw new NullDTOException();

        validateNonNullAttributes(
                creationDTO.getName(),
                creationDTO.getDescription(),
                creationDTO.getPriceModifierType(),
                creationDTO.getPriceModifier()
        );

        checkIfNameIsBlank(creationDTO.getName());

        checkNameSyntax(creationDTO.getName());

        checkDescriptionSyntax(creationDTO.getDescription());

        checkIfPriceModifierIsLowerThanZero(creationDTO.getPriceModifier());
    }

    public void validateForUpdate(PaymentMethodUpdateDTO updateDTO) {

        if (updateDTO == null) throw new NullDTOException();

        checkIfNameIsBlank(updateDTO.getNewName());

        checkNameSyntax(updateDTO.getNewName());

        checkDescriptionSyntax(updateDTO.getNewDescription());

        checkIfPriceModifierIsLowerThanZero(updateDTO.getPriceModifier());
    }

    private void validateNonNullAttributes(String name, String description, PaymentMethodModifierType priceModifierType, Double priceModifier) {

        if (name == null || description == null || priceModifier == null || priceModifierType == null)
            throw new NullPaymentMethodInputDataException();
    }

    private void checkIfNameIsBlank(String name) {

        if (name != null && name.isBlank()) throw new BlankPaymentMethodNameException();
    }

    private void checkNameSyntax(String name) {

        if (name != null) {

            if (!name.matches(RegexPatterns.NAME_REGEX)) throw new InvalidPaymentMethodNameException();

            if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH)
                throw new InvalidPaymentMethodNameLengthException();
        }
    }

    private void checkDescriptionSyntax(String description) {

        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH)
            throw new InvalidPaymentMethodDescriptionLengthException();
    }

    private void checkIfPriceModifierIsLowerThanZero(Double priceModifierValue) {

        if (priceModifierValue != null && priceModifierValue < MIN_DECIMAL_VALUE)
            throw new InvalidDecimalValueException();
    }
}
