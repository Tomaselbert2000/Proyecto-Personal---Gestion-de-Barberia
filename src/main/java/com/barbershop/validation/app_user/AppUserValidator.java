package com.barbershop.validation.app_user;

import com.barbershop.dto.app_user.AppUserCreationDTO;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.barbershop.validation.common.CommonValidationFunctions.checkIfDtoIsNull;
import static com.barbershop.validation.common.CommonValidationFunctions.validateAnnotationConstraints;

@Component
@RequiredArgsConstructor
public class AppUserValidator {

    private final Validator validatorEngine;

    public void validateForCreation(AppUserCreationDTO creationDTO) {

        checkIfDtoIsNull(creationDTO);

        validateAnnotationConstraints(validatorEngine, creationDTO);
    }
}
