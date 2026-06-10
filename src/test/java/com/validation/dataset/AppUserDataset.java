package com.validation.dataset;

import static com.barbershop.utils.strings.RegexPatterns.returnLoremIpsum;

public final class AppUserDataset {
    
    public static final String APP_USERNAME = "admin123";
    public static final String APP_PASSWORD = "LaTerceraBarbershop2026!";

    public static final String APP_USERNAME_WITH_INVALID_SIZE = returnLoremIpsum();
}
