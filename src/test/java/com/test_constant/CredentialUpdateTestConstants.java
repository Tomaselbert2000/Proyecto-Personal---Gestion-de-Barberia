package com.test_constant;

public final class CredentialUpdateTestConstants {

    private CredentialUpdateTestConstants() {
    }

    public static final class CreationValidData {

        public static final String USERNAME = "admin";
        public static final String PASSWORD = "LaTerceraBarberShop2026!";
        public static final String CONFIRM_PASSWORD = "LaTerceraBarberShop2026!";
    }

    public static final class InvalidData {

        public static final String INVALID_CONFIRM_PASSWORD = "ABC123#$%&";
    }
}
