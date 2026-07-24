package com.dto.app_user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.launcher.constants.ConstraintViolationMessages.AppUserConstraintSubject.APP_PASSWORD_STRING;
import static com.launcher.constants.ConstraintViolationMessages.AppUserConstraintSubject.APP_USERNAME_STRING;
import static com.launcher.constants.ConstraintViolationMessages.MessagePredicates.NOT_BLANK;
import static com.validation.common.CommonConstants.MAX_NAME_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserCreationDTO {

    @NotNull
    @NotBlank(message = APP_USERNAME_STRING + NOT_BLANK)
    @Size(max = MAX_NAME_LENGTH)
    private String username;

    @NotNull
    @NotBlank(message = APP_PASSWORD_STRING + NOT_BLANK)
    private String password;

    @NotNull
    private Boolean hasAdminRights;
}
