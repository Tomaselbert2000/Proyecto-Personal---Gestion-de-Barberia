package com.mapper.appuser;

import com.dto.app_user.AppUserInfoDTO;
import com.mapper.implementation.AppUserMapperImpl;
import com.mapper.interfaces.AppUserMapper;
import com.model.AppUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.factory.AppUserTestDataFactory.builValidAppUser1;
import static com.factory.AppUserTestDataFactory.buildValidAppUser2;
import static com.test_constant.AppUserTestConstants.CreationValidData.*;
import static org.junit.jupiter.api.Assertions.*;

public class AppUserMapperInfoTest {

    private final AppUserMapper mapper = new AppUserMapperImpl();

    private final AppUser existingUser = builValidAppUser1();
    private final AppUser anotherExistingUser = buildValidAppUser2();
    private final List<AppUser> userList = List.of(existingUser, anotherExistingUser);

    @Test
    @DisplayName("Dado un usuario registrado, se deberá mapear correctamente a DTO informativo")
    void givenAnExistingUser_ThenShouldBeMappedCorrectly() {

        AppUserInfoDTO appUserMappedAsDTO = mapper.mapAppUserToInfoDTO(existingUser);

        assertAll(
                "Verificación de campos",
                () -> assertNotNull(appUserMappedAsDTO),
                () -> assertEquals(APP_USER_1_USERNAME, appUserMappedAsDTO.getUsername()),
                () -> assertEquals(APP_USER_1_CREATION_TIMESTAMP, appUserMappedAsDTO.getCreatedAt()),
                () -> assertEquals(APP_USER_1_DEFAULT_ADMIN_RIGHTS_BOOLEAN, appUserMappedAsDTO.getHasAdminRights())
        );
    }

    @Test
    @DisplayName("Dados N usuarios registrados, se deberán mapear correctamente como lista de DTOs informativos")
    void given_N_UsersRegistered_ThenShouldBeMappedCorrectlyAsDTOList() {

        List<AppUserInfoDTO> infoDTOList = mapper.mapAppUserToInfoDTO(userList);

        assertAll(
                "Verificación de campos",
                () -> assertNotNull(infoDTOList),
                () -> assertFalse(infoDTOList.isEmpty()),

                () -> assertTrue(infoDTOList.stream().anyMatch(appUserInfoDTO ->
                        appUserInfoDTO.getUsername().equals(APP_USER_1_USERNAME) || appUserInfoDTO.getUsername().equals(APP_USER_2_USERNAME))),

                () -> assertTrue(infoDTOList.stream().anyMatch(appUserInfoDTO ->
                        appUserInfoDTO.getCreatedAt().equals(APP_USER_1_CREATION_TIMESTAMP) || appUserInfoDTO.getCreatedAt().equals(APP_USER_2_CREATION_TIMESTAMP))),

                () -> assertTrue(infoDTOList.stream().anyMatch(appUserInfoDTO ->
                        appUserInfoDTO.getHasAdminRights().equals(APP_USER_1_DEFAULT_ADMIN_RIGHTS_BOOLEAN) || appUserInfoDTO.getHasAdminRights().equals(APP_USER_2_DEFAULT_ADMIN_RIGHTS_BOOLEAN)))
        );
    }
}
