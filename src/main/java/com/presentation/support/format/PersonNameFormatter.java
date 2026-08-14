package com.presentation.support.format;

public class PersonNameFormatter {

    public static String fullName(String firstName, String lastName) {

        if (firstName != null && lastName != null) return firstName + " " + lastName;

        return "";
    }

    public static String initials(String firstNameInitial, String lastNameInitial) {

        if (firstNameInitial != null && lastNameInitial != null) return firstNameInitial + "." + lastNameInitial;

        return "";
    }
}
