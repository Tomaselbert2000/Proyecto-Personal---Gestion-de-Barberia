package com.barbershop.validation.credentials;

import com.barbershop.dto.credentials_management.CredentialsUpdateDTO;
import com.barbershop.exceptions.credentials.PasswordMismatchException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.barbershop.validation.common.CommonValidationFunctions.checkIfDtoIsNull;
import static com.barbershop.validation.common.CommonValidationFunctions.validateAnnotationConstraints;

@Component
@RequiredArgsConstructor
public class CredentialsUpdateValidator {

    private final Validator validatorEngine;

    public void validateDTO(CredentialsUpdateDTO dto) {

        checkIfDtoIsNull(dto);

        validateAnnotationConstraints(validatorEngine, dto);

        checkIfPasswordsMatch(dto);
    }

    private void checkIfPasswordsMatch(CredentialsUpdateDTO dto) {

        if (!dto.getPassword().equals(dto.getConfirmPassword())) throw new PasswordMismatchException();
    }
}
