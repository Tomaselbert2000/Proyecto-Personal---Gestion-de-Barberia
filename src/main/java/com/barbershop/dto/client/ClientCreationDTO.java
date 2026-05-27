package com.barbershop.dto.client;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

import static com.barbershop.launcher.constants.entity_constraints.ClientConstraintViolationMessageSubject.*;
import static com.barbershop.launcher.constants.entity_constraints.ConstraintViolationMessagePredicate.*;
import static com.barbershop.utils.strings.RegexPatterns.*;
import static com.barbershop.validation.client.ClientValidatorConstants.*;
import static com.barbershop.validation.common.CommonConstants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientCreationDTO implements ClientDTOCommonMethods {

    @NotBlank(message = CLIENT_NATIONAL_ID_CARD_NUMBER + NOT_BLANK)
    @Size(min = MIN_NATIONAL_ID_CARD_NUMBER_LENGTH, max = MAX_NATIONAL_ID_CARD_NUMBER_LENGTH, message = CLIENT_NATIONAL_ID_CARD_NUMBER + INVALID_NAME_SIZE)
    @Pattern(regexp = NATIONAL_ID_CARD_NUMBER_REGEX, message = CLIENT_NATIONAL_ID_CARD_NUMBER + DOES_NOT_MATCH_NAME_REGEX)
    private String nationalIdentityCardNumber;

    @NotBlank(message = CLIENT_FIRST_NAME + NOT_BLANK)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = CLIENT_FIRST_NAME + INVALID_NAME_SIZE)
    @Pattern(regexp = NAME_REGEX, message = CLIENT_FIRST_NAME + DOES_NOT_MATCH_NAME_REGEX)
    private String firstName;

    @NotBlank(message = CLIENT_LAST_NAME + NOT_BLANK)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = CLIENT_LAST_NAME + INVALID_NAME_SIZE)
    @Pattern(regexp = NAME_REGEX, message = CLIENT_LAST_NAME + DOES_NOT_MATCH_NAME_REGEX)
    private String lastName;

    @NotBlank(message = CLIENT_EMAIL + NOT_BLANK)
    @Pattern(regexp = EMAIL_REGEX, message = CLIENT_EMAIL + DOES_NOT_MATCH_EMAIL_REGEX)
    private String email;

    @NotEmpty
    private List<@NotBlank(message = CLIENT_PHONE + NOT_BLANK) @Size(min = MIN_PHONE_LENGTH, max = MAX_PHONE_LENGTH) @Pattern(regexp = PHONE_REGEX, message = CLIENT_PHONE + DOES_NOT_MATCH_PHONE_REGEX) String> phoneNumbersList;

    @NotNull
    @Size(max = MAX_OPTIONAL_DESCRIPTION_LENGTH, message = CLIENT_OPTIONAL_NOTES + OPTIONAL_TEXT_MAX_SIZE)
    private String optionalNotes;
}
