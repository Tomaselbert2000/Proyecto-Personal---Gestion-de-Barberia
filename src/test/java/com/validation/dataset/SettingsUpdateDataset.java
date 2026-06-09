package com.validation.dataset;

import java.time.LocalTime;

import static com.barbershop.utils.strings.RegexPatterns.returnLoremIpsum;

public final class SettingsUpdateDataset {

    public static final String UPDATE_BARBER_SHOP_NAME = "La Tercera Barbershop";
    public static final String UPDATE_BARBER_SHOP_PHONE = "+54 1122334455";
    public static final String UPDATE_BARBER_SHOP_EMAIL = "barber@tercerbarbershop.com";
    public static final String UPDATE_BARBER_SHOP_ADDRESS = "Av. San Martín 123, Buenos Aires";
    public static final LocalTime UPDATE_OPENING_TIME = LocalTime.of(9, 0);
    public static final LocalTime UPDATE_CLOSE_TIME = LocalTime.of(18, 0);
    public static final Boolean DEFAULT_UPDATE_CHECKBOX_STATUS = true;

    public static final String UPDATE_BARBER_SHOP_INVALID_NAME = "La Tercer@ Barbersh0p";
    public static final String UPDATE_BARBER_SHOP_INVALID_PHONE = "+54 9 112233AA55";
    public static final String UPDATE_BARBER_SHOP_INVALID_EMAIL = "barber•µ•ßæð”@ſ€ðſ@↓@tercerbarbershop.com.";
    public static final String UPDATE_BARBER_SHOP_INVALID_ADDRESS = "Av. San Martín 1ðſ@€ðſ€ŋđððß„¢»ſ";

    public static final String UPDATE_BUSINESS_NAME_LONGER_THAN_MAX_LENGTH = returnLoremIpsum();
    public static final String UPDATE_PHONE_NUMBER_LARGER_THAN_MAX_LENGTH = "+54 9 1122334455667788";

    private SettingsUpdateDataset() {
    }
}
