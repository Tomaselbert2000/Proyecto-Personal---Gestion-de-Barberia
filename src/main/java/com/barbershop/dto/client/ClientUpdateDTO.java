package com.barbershop.dto.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

import static com.barbershop.utils.strings.RegexPatterns.*;
import static com.barbershop.validation.client.ClientValidatorConstants.*;
import static com.barbershop.validation.common.CommonConstants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientUpdateDTO implements ClientDTOCommonMethods {

    @Size(min = MIN_NATIONAL_ID_CARD_NUMBER_LENGTH, max = MAX_NATIONAL_ID_CARD_NUMBER_LENGTH)
    @Pattern(regexp = NATIONAL_ID_CARD_NUMBER_REGEX)
    private String nationalIdentityCardNumber;

    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    @Pattern(regexp = NAME_REGEX)
    private String firstName;

    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    @Pattern(regexp = NAME_REGEX)
    private String lastName;

    @Pattern(regexp = EMAIL_REGEX)
    private String email;

    @Size(min = MIN_PHONE_LIST_LENGTH_FOR_UPDATE)
    private List<@NotBlank @Size(min = MIN_PHONE_LENGTH, max = MAX_PHONE_LENGTH) @Pattern(regexp = PHONE_REGEX) String> phoneNumbersList;

    @Size(max = MAX_OPTIONAL_DESCRIPTION_LENGTH)
    private String optionalNotes;
}
