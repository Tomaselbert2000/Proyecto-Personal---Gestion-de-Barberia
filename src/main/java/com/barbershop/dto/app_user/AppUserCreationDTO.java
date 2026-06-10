package com.barbershop.dto.app_user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.barbershop.validation.common.CommonConstants.MAX_NAME_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserCreationDTO {

    @NotNull
    @NotBlank
    @Size(max = MAX_NAME_LENGTH)
    private String username;

    @NotNull
    @NotBlank
    private String password;
}
