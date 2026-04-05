package com.barbershop.dto.barbershopservice;

import com.barbershop.enums.BarberServiceCategory;
import jakarta.validation.constraints.*;
import lombok.*;

import static com.barbershop.utils.strings.RegexPatterns.NAME_REGEX;
import static com.barbershop.validation.common.CommonConstants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarberServiceCreationDTO {

    @NotNull
    @NotBlank
    @Pattern(regexp = NAME_REGEX)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    private String name;

    @NotNull
    @Positive
    private Double price;

    @NotNull
    private BarberServiceCategory serviceCategory;

    @NotNull
    @Size(max = MAX_OPTIONAL_DESCRIPTION_LENGTH)
    private String internalNotes;
}
