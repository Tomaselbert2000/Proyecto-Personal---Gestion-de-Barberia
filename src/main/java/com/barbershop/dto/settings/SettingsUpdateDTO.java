package com.barbershop.dto.settings;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalTime;

import static com.barbershop.launcher.constants.entity_constraints.predicate.ConstraintViolationMessagePredicate.*;
import static com.barbershop.launcher.constants.entity_constraints.subject.SettingsUpdateMessageSubject.*;
import static com.barbershop.utils.strings.RegexPatterns.*;
import static com.barbershop.validation.common.CommonConstants.MAX_NAME_LENGTH;
import static com.barbershop.validation.common.CommonConstants.MAX_OPTIONAL_DESCRIPTION_LENGTH;
import static com.barbershop.validation.common.CommonConstants.MAX_PHONE_LENGTH;
import static com.barbershop.validation.common.CommonConstants.MIN_PHONE_LENGTH;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettingsUpdateDTO {

    private String themeSelected;

    @Size(max = MAX_NAME_LENGTH, message = BARBER_SHOP_NAME + INVALID_NAME_SIZE)
    @Pattern(regexp = NAME_REGEX, message = BARBER_SHOP_NAME + DOES_NOT_MATCH_NAME_REGEX)
    private String name;

    @Size(min = MIN_PHONE_LENGTH, max = MAX_PHONE_LENGTH, message = BARBER_SHOP_PHONE + INVALID_PHONE_SIZE)
    @Pattern(regexp = PHONE_REGEX, message = BARBER_SHOP_PHONE + DOES_NOT_MATCH_PHONE_REGEX)
    private String phone;

    @Pattern(regexp = EMAIL_REGEX, message = BARBER_SHOP_EMAIL + DOES_NOT_MATCH_EMAIL_REGEX)
    private String email;

    @Size(max = MAX_OPTIONAL_DESCRIPTION_LENGTH, message = BARBER_SHOP_ADDRESS + OPTIONAL_TEXT_MAX_SIZE)
    @Pattern(regexp = ADDRESS_REGEX, message = BARBER_SHOP_ADDRESS + DOES_NOT_MATCH_ADDRESS_REGEX)
    private String address;

    private LocalTime openingHour;
    private LocalTime closingHour;

    private Boolean newAppointmentNotificationEnabled;
    private Boolean clientReminderNotificationEnabled;
    private Boolean lowStockNotificationEnabled;
    private Boolean workplaceChangesNotificationEnabled;
}
