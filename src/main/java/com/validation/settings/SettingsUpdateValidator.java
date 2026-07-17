package com.validation.settings;

import com.dto.settings.SettingsUpdateDTO;
import com.exceptions.settings.InvalidServiceHourException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.validation.common.CommonValidationFunctions.checkIfDtoIsNull;
import static com.validation.common.CommonValidationFunctions.validateAnnotationConstraints;

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
