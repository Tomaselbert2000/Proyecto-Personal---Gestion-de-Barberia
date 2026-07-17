package com.dto.barbershopservice;

import com.enums.BarberServiceCategory;
import jakarta.validation.constraints.*;
import lombok.*;

import static com.launcher.constants.entity_constraints.predicate.ConstraintViolationMessagePredicate.*;
import static com.launcher.constants.entity_constraints.subject.BarberServiceConstraintViolationMessageSubject.*;
import static com.utils.strings.RegexPatterns.NAME_REGEX;
import static com.validation.common.CommonConstants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarberServiceCreationDTO {

    @NotNull(message = BARBER_SERVICE_NAME + NOT_NULL)
    @NotBlank(message = BARBER_SERVICE_NAME + NOT_BLANK)
    @Pattern(regexp = NAME_REGEX, message = BARBER_SERVICE_NAME + DOES_NOT_MATCH_NAME_REGEX)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = BARBER_SERVICE_NAME + INVALID_NAME_SIZE)
    private String name;

    @NotNull(message = BARBER_SERVICE_PRICE + NOT_NULL)
    @Positive(message = BARBER_SERVICE_PRICE + POSITIVE)
    private Double price;

    @NotNull(message = BARBER_SERVICE_CATEGORY + NOT_NULL)
    private BarberServiceCategory serviceCategory;

    @NotNull(message = BARBER_SERVICE_INTERNAL_NOTES + NOT_NULL)
    @Size(max = MAX_OPTIONAL_DESCRIPTION_LENGTH, message = BARBER_SERVICE_INTERNAL_NOTES + OPTIONAL_TEXT_OR_DESCRIPTION_MAX_SIZE)
    private String internalNotes;
}
