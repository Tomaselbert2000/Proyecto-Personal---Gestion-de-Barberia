package com.dto.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

import static com.presentation.constants.ConstraintViolationMessages.ClientConstraintSubject.*;
import static com.presentation.constants.ConstraintViolationMessages.MessagePredicates.*;
import static com.utils.strings.RegexPatterns.*;
import static com.validation.client.ClientValidatorConstants.*;
import static com.validation.common.CommonConstants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class ClientUpdateDTO implements ClientInputDTO {

    @Size(min = MIN_NATIONAL_ID_CARD_NUMBER_LENGTH, max = MAX_NATIONAL_ID_CARD_NUMBER_LENGTH, message = CLIENT_NATIONAL_ID_CARD_NUMBER + INVALID_NICN_SIZE)
    @Pattern(regexp = NATIONAL_ID_CARD_NUMBER_REGEX, message = CLIENT_NATIONAL_ID_CARD_NUMBER + DOES_NOT_MATCH_PHONE_REGEX)
    private String nationalIdentityCardNumber;

    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = CLIENT_FIRST_NAME + INVALID_NAME_SIZE)
    @Pattern(regexp = NAME_REGEX, message = CLIENT_FIRST_NAME + DOES_NOT_MATCH_NAME_REGEX)
    private String firstName;

    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = CLIENT_LAST_NAME + INVALID_NAME_SIZE)
    @Pattern(regexp = NAME_REGEX, message = CLIENT_LAST_NAME + DOES_NOT_MATCH_NAME_REGEX)
    private String lastName;

    @Pattern(regexp = EMAIL_REGEX, message = CLIENT_EMAIL + DOES_NOT_MATCH_EMAIL_REGEX)
    private String email;

    @Size(min = MIN_PHONE_LIST_LENGTH_FOR_UPDATE, message = CLIENT_PHONE + NOT_BLANK)
    private List<@NotBlank(message = CLIENT_PHONE + NOT_BLANK) @Size(min = MIN_PHONE_LENGTH, max = MAX_PHONE_LENGTH, message = CLIENT_PHONE + INVALID_PHONE_SIZE) @Pattern(regexp = PHONE_REGEX, message = CLIENT_PHONE + DOES_NOT_MATCH_PHONE_REGEX) String> phoneNumbersList;

    @Size(max = MAX_OPTIONAL_DESCRIPTION_LENGTH, message = CLIENT_OPTIONAL_NOTES + OPTIONAL_TEXT_OR_DESCRIPTION_MAX_SIZE)
    private String optionalNotes;
}