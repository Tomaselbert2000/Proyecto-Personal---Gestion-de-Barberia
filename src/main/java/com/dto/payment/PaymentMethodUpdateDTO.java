package com.dto.payment;

import com.enums.PaymentMethodModifierType;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.launcher.constants.entity_constraints.predicate.ConstraintViolationMessagePredicate.*;
import static com.launcher.constants.entity_constraints.subject.PaymentMethodConstraintViolationMessageSubject.*;
import static com.utils.strings.RegexPatterns.NAME_REGEX;
import static com.validation.common.CommonConstants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodUpdateDTO {

    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = PAYMENT_METHOD_NAME + INVALID_NAME_SIZE)
    @Pattern(regexp = NAME_REGEX, message = PAYMENT_METHOD_NAME + DOES_NOT_MATCH_NAME_REGEX)
    private String newName;

    @Size(max = MAX_OPTIONAL_DESCRIPTION_LENGTH, message = PAYMENT_METHOD_DESCRIPTION + OPTIONAL_TEXT_OR_DESCRIPTION_MAX_SIZE)
    private String newDescription;

    private Boolean isActive;

    private PaymentMethodModifierType newModifierType;

    @PositiveOrZero(message = PAYMENT_METHOD_PRICE_MODIFIER + HIGHER_OR_EQUALS_THAN_ZERO)
    private Double priceModifier;
}
