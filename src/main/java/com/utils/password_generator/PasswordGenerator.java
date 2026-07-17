package com.utils.password_generator;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.shuffle;

public final class PasswordGenerator {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+[]{}|";
    private static final String NUMBERS = "0123456789";
    private static final int MIN_MANDATORY_CHARACTERS = 4;

    private static final String ALL_CHARS = UPPERCASE + LOWERCASE + SPECIAL_CHARS + NUMBERS;

    private PasswordGenerator() {
    }

    public static String generatePassword(int length) {

        if (length < MIN_MANDATORY_CHARACTERS) throw new IllegalArgumentException();

        SecureRandom secureRandomInstance = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();

        char uppercase = UPPERCASE.charAt(secureRandomInstance.nextInt(UPPERCASE.length()));
        char lowercase = LOWERCASE.charAt(secureRandomInstance.nextInt(LOWERCASE.length()));
        char special = SPECIAL_CHARS.charAt(secureRandomInstance.nextInt(SPECIAL_CHARS.length()));
        char number = NUMBERS.charAt(secureRandomInstance.nextInt(NUMBERS.length()));

        List<Character> mandatoryChars = List.of(uppercase, lowercase, special, number);

        List<Character> passwordCharacters = new ArrayList<>(mandatoryChars);

        for (int i = 0; i < length - mandatoryChars.size(); i++) {

            char randomCharacter = ALL_CHARS.charAt(secureRandomInstance.nextInt(ALL_CHARS.length()));

            passwordCharacters.add(randomCharacter);
        }

        shuffle(passwordCharacters);

        for (char character : passwordCharacters) {

            stringBuilder.append(character);
        }

        return stringBuilder.toString();
    }
}