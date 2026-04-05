package com.barbershop.dto.barbershopservice;

import com.barbershop.enums.BarberServiceCategory;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.barbershop.utils.strings.RegexPatterns.NAME_REGEX;
import static com.barbershop.validation.barberservice.BarberServiceValidatorConstants.MAX_INTERNAL_NOTES_LENGTH;
import static com.barbershop.validation.common.CommonConstants.MAX_NAME_LENGTH;
import static com.barbershop.validation.common.CommonConstants.MIN_NAME_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarberServiceUpdateDTO {

    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    @Pattern(regexp = NAME_REGEX)
    private String name;

    @Positive
    private Double price;
    private BarberServiceCategory serviceCategory;

    @Size(max = MAX_INTERNAL_NOTES_LENGTH)
    private String internalNotes;
}
