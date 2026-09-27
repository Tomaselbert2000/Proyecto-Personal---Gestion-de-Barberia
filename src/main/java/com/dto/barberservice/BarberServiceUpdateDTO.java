package com.dto.barberservice;

import com.enums.BarberServiceCategory;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.presentation.constants.ConstraintViolationMessages.BarberServiceConstraintSubject.*;
import static com.presentation.constants.ConstraintViolationMessages.MessagePredicates.*;
import static com.utils.strings.RegexPatterns.NAME_REGEX;
import static com.validation.barberservice.BarberServiceValidatorConstants.MAX_INTERNAL_NOTES_LENGTH;
import static com.validation.common.CommonConstants.MAX_NAME_LENGTH;
import static com.validation.common.CommonConstants.MIN_NAME_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class BarberServiceUpdateDTO {

    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = BARBER_SERVICE_NAME + INVALID_NAME_SIZE)
    @Pattern(regexp = NAME_REGEX, message = BARBER_SERVICE_NAME + DOES_NOT_MATCH_NAME_REGEX)
    private String name;

    @Positive(message = BARBER_SERVICE_PRICE + POSITIVE)
    private Double price;

    private BarberServiceCategory serviceCategory;

    @Size(max = MAX_INTERNAL_NOTES_LENGTH, message = BARBER_SERVICE_INTERNAL_NOTES + OPTIONAL_TEXT_OR_DESCRIPTION_MAX_SIZE)
    private String internalNotes;

    private Boolean isCurrentlyActive;
}