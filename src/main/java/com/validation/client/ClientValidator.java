package com.validation.client;

import com.dto.client.ClientInputDTO;
import com.exceptions.client.DuplicatedPhoneInListException;
import com.validation.common.BaseDTOValidator;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class ClientValidator extends BaseDTOValidator {

    public ClientValidator(Validator validatorEngine) {
        super(validatorEngine);
    }

    public <T extends ClientInputDTO> void validateDTO(T dto) {

        super.validateDTO(dto);

        validatePhoneNumberList(dto);
    }

    private <T extends ClientInputDTO> void validatePhoneNumberList(T dto) {

        if (dto != null && dto.getPhoneNumbersList() != null && dto.getPhoneNumbersList().stream().distinct().count() < dto.getPhoneNumbersList().size()) {

            throw new DuplicatedPhoneInListException();
        }
    }
}
