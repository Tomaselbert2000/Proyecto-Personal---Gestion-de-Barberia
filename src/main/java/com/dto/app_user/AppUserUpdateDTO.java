package com.dto.app_user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.launcher.constants.ConstraintViolationMessages.AppUserConstraintSubject.APP_PASSWORD_STRING;
import static com.launcher.constants.ConstraintViolationMessages.AppUserConstraintSubject.APP_USERNAME_STRING;
import static com.launcher.constants.ConstraintViolationMessages.MessagePredicates.*;
import static com.validation.common.CommonConstants.MAX_NAME_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserUpdateDTO {

    @Size(min = 1, message = APP_USERNAME_STRING + NOT_BLANK)
    @Size(max = MAX_NAME_LENGTH, message = APP_USERNAME_STRING + INVALID_NAME_SIZE)
    @Pattern(regexp = ".*\\S.*", message = APP_USERNAME_STRING + CANNOT_BE_SPACES_ONLY)
    private String username;

    @Size(min = 1, message = APP_USERNAME_STRING + NOT_BLANK)
    @Pattern(regexp = ".*\\S.*", message = APP_PASSWORD_STRING + CANNOT_BE_SPACES_ONLY)
    private String password;

    private Boolean hasAdminRights;
}
