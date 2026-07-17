package com.factory;

import com.dto.credentials_management.CredentialsUpdateDTO;

import static com.test_constant.CredentialUpdateTestConstants.CreationValidData.*;

public class CredentialsTestDataFactory {

    private CredentialsTestDataFactory() {
    }

    public static CredentialsUpdateDTO buildValidCredentialsUpdateDTO() {

        return CredentialsUpdateDTO.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .confirmPassword(CONFIRM_PASSWORD)
                .build();
    }
}
