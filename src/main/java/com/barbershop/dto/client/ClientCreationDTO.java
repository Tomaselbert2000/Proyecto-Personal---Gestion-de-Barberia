package com.barbershop.dto.client;

import jakarta.validation.constraints.*;
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
public class ClientCreationDTO implements ClientDTOCommonMethods{

    @NotBlank
    @Size(min = MIN_NATIONAL_ID_CARD_NUMBER_LENGTH, max = MAX_NATIONAL_ID_CARD_NUMBER_LENGTH)
    @Pattern(regexp = NATIONAL_ID_CARD_NUMBER_REGEX)
    private String nationalIdentityCardNumber;

    @NotBlank
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    @Pattern(regexp = NAME_REGEX)
    private String firstName;

    @NotBlank
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    @Pattern(regexp = NAME_REGEX)
    private String lastName;

    @NotBlank
    @Pattern(regexp = EMAIL_REGEX)
    private String email;

    @NotEmpty
    private List<@NotBlank @Size(min = MIN_PHONE_LENGTH, max = MAX_PHONE_LENGTH) @Pattern(regexp = PHONE_REGEX) String> phoneNumbersList;

    @NotNull
    @Size(max = MAX_OPTIONAL_DESCRIPTION_LENGTH)
    private String optionalNotes;
}
