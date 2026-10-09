package com.factory;

import com.dto.client.ClientCreationDTO;
import com.dto.client.ClientUpdateDTO;
import com.model.Client;

import static com.test_constant.ClientTestConstants.CreationValidData.*;
import static com.test_constant.ClientTestConstants.MapperData.*;
import static com.test_constant.ClientTestConstants.SecondValidClientData.*;
import static com.test_constant.ClientTestConstants.UpdateValidData.*;

public class ClientTestDataFactory {

    private ClientTestDataFactory() {
    }

    public static ClientCreationDTO buildValidClientCreationDTO() {

        return ClientCreationDTO.builder()
                .nationalIdentityCardNumber(NATIONAL_ID_CARD_NUMBER)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .phoneNumbersList(PHONE_LIST)
                .optionalNotes(OPTIONAL_NOTES)
                .build();
    }

    public static ClientUpdateDTO buildValidClientUpdateDTO() {

        return ClientUpdateDTO.builder()
                .nationalIdentityCardNumber(NEW_NATIONAL_ID_CARD_NUMBER)
                .firstName(NEW_FIRST_NAME)
                .lastName(NEW_LAST_NAME)
                .email(NEW_EMAIL)
                .phoneNumbersList(NEW_PHONE_LIST)
                .optionalNotes(NEW_OPTIONAL_NOTES)
                .build();
    }

    public static Client buildValidClient() {

        return Client.builder()
                .clientID(CLIENT_ID)
                .nationalIdentityCardNumber(NATIONAL_ID_CARD_NUMBER)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .registrationDate(REGISTRATION_DATE)
                .email(EMAIL)
                .phoneNumbersList(PHONE_LIST)
                .build();
    }

    public static Client buildAnotherValidClient(){

        return Client.builder()
                .clientID(ANOTHER_ID)
                .nationalIdentityCardNumber(SECOND_CLIENT_NATIONAL_ID_CARD_NUMBER)
                .firstName(SECOND_CLIENT_FIRST_NAME)
                .lastName(SECOND_CLIENT_LAST_NAME)
                .registrationDate(REGISTRATION_DATE)
                .email(SECOND_CLIENT_EMAIL)
                .phoneNumbersList(SECOND_CLIENT_PHONE_LIST)
                .optionalNotes(SECOND_CLIENT_OPTIONAL_NOTES)
                .build();
    }
}
