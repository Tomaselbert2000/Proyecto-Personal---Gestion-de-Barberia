package com.dto.credentials;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import static com.presentation.constants.ConstraintViolationMessages.AppUserConstraintSubject.APP_CONFIRM_PASSWORD_STRING;
import static com.presentation.constants.ConstraintViolationMessages.AppUserConstraintSubject.APP_PASSWORD_STRING;
import static com.presentation.constants.ConstraintViolationMessages.AppUserConstraintSubject.APP_USERNAME_STRING;
import static com.presentation.constants.ConstraintViolationMessages.MessagePredicates.NOT_BLANK;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class CredentialsUpdateDTO {

    @NotBlank(message = APP_USERNAME_STRING + NOT_BLANK)
    private String username;

    @NotBlank(message = APP_PASSWORD_STRING + NOT_BLANK)
    private String password;

    @NotBlank(message = APP_CONFIRM_PASSWORD_STRING + NOT_BLANK)
    private String confirmPassword;
}
