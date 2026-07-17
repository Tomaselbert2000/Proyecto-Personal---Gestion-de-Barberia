package com.factory;

import com.dto.app_user.AppUserCreationDTO;
import com.dto.app_user.AppUserUpdateDTO;
import com.model.AppUser;

import static com.test_constant.AppUserTestConstants.CreationValidData.*;
import static com.test_constant.AppUserTestConstants.UpdateValidData.*;

public class AppUserTestDataFactory {

    private AppUserTestDataFactory() {
    }

    public static AppUser builValidAppUser1() {

        return AppUser.builder()
                .userId(APP_USER_1_ID)
                .username(APP_USER_1_USERNAME)
                .password(APP_USER_1_PASSWORD)
                .creationTimestamp(APP_USER_1_CREATION_TIMESTAMP)
                .modifiedDate(APP_USER_1_MODIFIED_TIMESTAMP)
                .hasAdminRights(APP_USER_1_DEFAULT_ADMIN_RIGHTS_BOOLEAN)
                .build();
    }

    public static AppUser buildValidAppUser2() {

        return AppUser.builder()
                .userId(APP_USER_2_ID)
                .username(APP_USER_2_USERNAME)
                .password(APP_USER_2_PASSWORD)
                .creationTimestamp(APP_USER_2_CREATION_TIMESTAMP)
                .modifiedDate(APP_USER_2_MODIFIED_TIMESTAMP)
                .hasAdminRights(APP_USER_2_DEFAULT_ADMIN_RIGHTS_BOOLEAN)
                .build();
    }

    public static AppUserCreationDTO buildValidAppUserCreationDTO() {

        return AppUserCreationDTO.builder()
                .username(APP_USER_1_USERNAME)
                .password(APP_USER_1_PASSWORD)
                .hasAdminRights(APP_USER_1_DEFAULT_ADMIN_RIGHTS_BOOLEAN)
                .build();
    }

    public static AppUserUpdateDTO buildValidAppUserUpdateDTO() {

        return AppUserUpdateDTO.builder()
                .username(APP_USER_NEW_USERNAME)
                .password(APP_USER_NEW_PASSWORD)
                .hasAdminRights(APP_USER_NEW_ADMIN_RIGHTS_BOOLEAN)
                .build();
    }
}
