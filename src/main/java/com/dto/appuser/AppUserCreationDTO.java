package com.dto.appuser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.presentation.constants.ConstraintViolationMessages.AppUserConstraintSubject.APP_PASSWORD_STRING;
import static com.presentation.constants.ConstraintViolationMessages.AppUserConstraintSubject.APP_USERNAME_STRING;
import static com.presentation.constants.ConstraintViolationMessages.MessagePredicates.MAX_NAME_SIZE;
import static com.presentation.constants.ConstraintViolationMessages.MessagePredicates.NOT_BLANK;
import static com.presentation.constants.ConstraintViolationMessages.MessagePredicates.NOT_NULL;
import static com.validation.common.CommonConstants.MAX_NAME_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class AppUserCreationDTO {

    @NotNull(message = APP_USERNAME_STRING + NOT_NULL)
    @NotBlank(message = APP_USERNAME_STRING + NOT_BLANK)
    @Size(max = MAX_NAME_LENGTH, message = APP_USERNAME_STRING + MAX_NAME_SIZE)
    private String username;

    @NotNull(message = APP_PASSWORD_STRING + NOT_NULL)
    @NotBlank(message = APP_PASSWORD_STRING + NOT_BLANK)
    private String password;
    private Boolean hasAdminRights;
}
