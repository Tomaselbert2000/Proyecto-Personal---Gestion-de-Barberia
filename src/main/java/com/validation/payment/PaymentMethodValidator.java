package com.validation.payment;

import com.validation.common.BaseDTOValidator;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodValidator extends BaseDTOValidator {

    public PaymentMethodValidator(Validator validatorEngine) {
        super(validatorEngine);
    }
}
