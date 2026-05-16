package com.barbershop.dto.barbershopservice;

import com.barbershop.enums.BarberServiceCategory;
import jakarta.validation.constraints.*;
import lombok.*;

import static com.barbershop.launcher.constants.entity_constraints.BarberServiceConstraintViolationMessageSubject.*;
import static com.barbershop.launcher.constants.entity_constraints.ConstraintViolationMessagePredicate.*;
import static com.barbershop.utils.strings.RegexPatterns.NAME_REGEX;
import static com.barbershop.validation.common.CommonConstants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarberServiceCreationDTO {

    @NotNull(message = BARBER_SERVICE_NAME + NOT_NULL)
    @NotBlank(message = BARBER_SERVICE_NAME + NOT_BLANK)
    @Pattern(regexp = NAME_REGEX, message = BARBER_SERVICE_NAME + DOES_NOT_MATCH_REGEX)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = BARBER_SERVICE_NAME + INVALID_NAME_SIZE)
    private String name;

    @NotNull(message = BARBER_SERVICE_PRICE + NOT_NULL)
    @Positive(message = BARBER_SERVICE_PRICE + POSITIVE)
    private Double price;

    @NotNull(message = BARBER_SERVICE_CATEGORY + NOT_NULL)
    private BarberServiceCategory serviceCategory;

    @NotNull(message = BARBER_SERVICE_INTERNAL_NOTES + NOT_NULL)
    @Size(max = MAX_OPTIONAL_DESCRIPTION_LENGTH, message = BARBER_SERVICE_INTERNAL_NOTES + OPTIONAL_TEXT_MAX_SIZE)
    private String internalNotes;
}
