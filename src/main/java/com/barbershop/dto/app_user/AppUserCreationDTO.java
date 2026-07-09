package com.barbershop.dto.app_user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.barbershop.launcher.constants.entity_constraints.predicate.ConstraintViolationMessagePredicate.NOT_BLANK;
import static com.barbershop.launcher.constants.entity_constraints.subject.AppUserConstraintViolationMessageSubject.APP_PASSWORD_STRING;
import static com.barbershop.launcher.constants.entity_constraints.subject.AppUserConstraintViolationMessageSubject.APP_USERNAME_STRING;
import static com.barbershop.validation.common.CommonConstants.MAX_NAME_LENGTH;

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
}
