package com.validation.settings;

import com.dto.settings.SettingsUpdateDTO;
import com.exceptions.settings.InvalidServiceHourException;
import com.validation.common.BaseDTOValidator;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class SettingsUpdateValidator extends BaseDTOValidator {

    public SettingsUpdateValidator(Validator validatorEngine) {
        super(validatorEngine);
    }

    public void validateDTO(SettingsUpdateDTO settingsUpdateDTO) {

        super.validateDTO(settingsUpdateDTO);

        checkIfOpeningAndClosingTimeAreValid(settingsUpdateDTO);
    }

    private void checkIfOpeningAndClosingTimeAreValid(SettingsUpdateDTO settingsUpdateDTO) {

        if (settingsUpdateDTO.getOpeningHour() != null && settingsUpdateDTO.getClosingHour() != null) {

            if (settingsUpdateDTO.getClosingHour().isBefore(settingsUpdateDTO.getOpeningHour()))
                throw new InvalidServiceHourException();
        }
    }
}
