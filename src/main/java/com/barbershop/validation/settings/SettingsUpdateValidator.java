package com.barbershop.validation.settings;

import com.barbershop.dto.settings.SettingsUpdateDTO;
import com.barbershop.exceptions.settings.InvalidServiceHourException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.barbershop.validation.common.CommonValidationFunctions.checkIfDtoIsNull;
import static com.barbershop.validation.common.CommonValidationFunctions.validateAnnotationConstraints;

@Component
@RequiredArgsConstructor
public class SettingsUpdateValidator {

    private final Validator validatorEngine;

    public void validateDTO(SettingsUpdateDTO settingsUpdateDTO) {

        checkIfDtoIsNull(settingsUpdateDTO);

        validateAnnotationConstraints(validatorEngine, settingsUpdateDTO);

        checkIfOpeningAndClosingTimeAreValid(settingsUpdateDTO);
    }

    private void checkIfOpeningAndClosingTimeAreValid(SettingsUpdateDTO settingsUpdateDTO) {

        if (settingsUpdateDTO.getOpeningHour() != null && settingsUpdateDTO.getClosingHour() != null) {

            if (settingsUpdateDTO.getClosingHour().isBefore(settingsUpdateDTO.getOpeningHour()))
                throw new InvalidServiceHourException();
        }
    }
}
