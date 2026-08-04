package com.validation.credentials;

import com.dto.credentials.CredentialsUpdateDTO;
import com.exceptions.credentials.PasswordMismatchException;
import com.validation.common.BaseDTOValidator;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class CredentialsUpdateValidator extends BaseDTOValidator {

    public CredentialsUpdateValidator(Validator validatorEngine) {
        super(validatorEngine);
    }

    public void validateDTO(CredentialsUpdateDTO dto) {

        super.validateDTO(dto);

        checkIfPasswordsMatch(dto);
    }

    private void checkIfPasswordsMatch(CredentialsUpdateDTO dto) {

        if (!dto.getPassword().equals(dto.getConfirmPassword())) throw new PasswordMismatchException();
    }
}
