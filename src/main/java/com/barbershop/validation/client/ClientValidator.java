package com.barbershop.validation.client;

import com.barbershop.dto.client.ClientDTOCommonMethods;
import com.barbershop.exceptions.client.*;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.barbershop.validation.common.CommonValidationFunctions.checkIfDtoIsNull;
import static com.barbershop.validation.common.CommonValidationFunctions.validateAnnotationConstraints;

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
