package com.test_constant;

import java.time.LocalDateTime;

import static com.utils.strings.RegexPatterns.returnLoremIpsum;

public final class AppUserTestConstants {

    private AppUserTestConstants() {
    }

    public static final class CreationValidData {

        public static final Long APP_USER_1_ID = 1L;
        public static final String APP_USER_1_USERNAME = "admin123";
        public static final String APP_USER_1_PASSWORD = "LaTerceraBarbershop2026!";
        public static final Boolean APP_USER_1_DEFAULT_ADMIN_RIGHTS_BOOLEAN = false;
        public static final LocalDateTime APP_USER_1_CREATION_TIMESTAMP = LocalDateTime.now();
        public static final LocalDateTime APP_USER_1_MODIFIED_TIMESTAMP = APP_USER_1_CREATION_TIMESTAMP.plusMinutes(30);
        public static final LocalDateTime APP_USER_2_MODIFIED_TIMESTAMP = APP_USER_1_MODIFIED_TIMESTAMP.plusMinutes(45);
        public static final Long APP_USER_2_ID = 2L;
        public static final String APP_USER_2_USERNAME = "user";
        public static final String APP_USER_2_PASSWORD = "password12345";
        public static final Boolean APP_USER_2_DEFAULT_ADMIN_RIGHTS_BOOLEAN = true;
        public static final LocalDateTime APP_USER_2_CREATION_TIMESTAMP = APP_USER_1_CREATION_TIMESTAMP.plusMinutes(15);
    }

    public static final class UpdateValidData {

        public static final String APP_USER_NEW_USERNAME = "newAdmin";
        public static final String APP_USER_NEW_PASSWORD = "this is a new password";
        public static final Boolean APP_USER_NEW_ADMIN_RIGHTS_BOOLEAN = true;
    }

    public static final class InvalidData {

        public static final String APP_USERNAME_WITH_INVALID_SIZE = returnLoremIpsum();
        public static final String USERNAME_WITH_SPACES = "   admin123   ";
        public static final String PASSWORD_WITH_SPACES = "   LaTerceraBarbershop2026!   ";
    }
}
