package com.validation.client;

import com.dto.client.ClientDTOCommonMethods;
import com.exceptions.client.DuplicatedPhoneInListException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.validation.common.CommonValidationFunctions.checkIfDtoIsNull;
import static com.validation.common.CommonValidationFunctions.validateAnnotationConstraints;

@Component
@RequiredArgsConstructor
public class ClientValidator {

    private final Validator validatorEngine;

    public <T extends ClientDTOCommonMethods> void validateDTO(T dto) {

        checkIfDtoIsNull(dto);

        validateAnnotationConstraints(validatorEngine, dto);

        validatePhoneNumberList(dto);
    }

    private <T extends ClientDTOCommonMethods> void validatePhoneNumberList(T dto) {

        if (dto != null && dto.getPhoneNumbersList() != null && dto.getPhoneNumbersList().stream().distinct().count() < dto.getPhoneNumbersList().size()) {

            throw new DuplicatedPhoneInListException();
        }
    }
}
