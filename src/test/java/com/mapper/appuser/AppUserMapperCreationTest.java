package com.mapper.appuser;

import com.dto.app_user.AppUserCreationDTO;
import com.exceptions.common.NullMapperInputException;
import com.mapper.implementation.AppUserMapperImpl;
import com.mapper.interfaces.AppUserMapper;
import com.model.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.factory.AppUserTestDataFactory.buildValidAppUserCreationDTO;
import static com.test_constant.AppUserTestConstants.CreationValidData.*;
import static com.test_constant.AppUserTestConstants.InvalidData.PASSWORD_WITH_SPACES;
import static com.test_constant.AppUserTestConstants.InvalidData.USERNAME_WITH_SPACES;
import static org.junit.jupiter.api.Assertions.*;

public class AppUserMapperCreationTest {

    private final AppUserMapper mapper = new AppUserMapperImpl();
    private AppUserCreationDTO creationDTO = new AppUserCreationDTO();
    private AppUser mappedUser;

    @BeforeEach
    void init() {

        creationDTO = buildValidAppUserCreationDTO();
    }

    @Test
    @DisplayName("Dado un DTO de creación NULL, el mapeo fallará y arrojará NullMapperInputDataException")
    void givenNullDTO_WhenCreating_ThenThrows_NullMapperInputDataException() {

        creationDTO = null;

        assertThrows(NullMapperInputException.class, this::mapAppUser);
    }

    @Test
    @DisplayName("Dado un DTO de creación con datos correctos, se deberá mapear correctamente")
    void givenCreationDTOWithValidData_shouldMapCorrectly() {

        mappedUser = mapAppUser();

        assertAll(
                "Verificación de campos",
                () -> assertEquals(APP_USER_1_USERNAME, mappedUser.getUsername()),
                () -> assertEquals(APP_USER_1_PASSWORD, mappedUser.getPassword()),
                () -> assertEquals(APP_USER_1_DEFAULT_ADMIN_RIGHTS_BOOLEAN, mappedUser.getHasAdminRights())
        );
    }

    @Test
    @DisplayName("Dado un nombre de usuario con espacios innecesarios, deberán ser eliminados al mapear la entidad")
    void givenUsernameWithUnnecessarySpaces_ThenShouldBeTrimmed() {

        creationDTO.setUsername(USERNAME_WITH_SPACES);

        mappedUser = mapAppUser();

        assertEquals(APP_USER_1_USERNAME, mappedUser.getUsername());
    }

    @Test
    @DisplayName("Dada una contraseña de usuario con espacios innecesarios, deberán ser eliminados al mapear la entidad")
    void givenPasswordWithUnnecessarySpaces_ThenShouldBeTrimmed() {

        creationDTO.setPassword(PASSWORD_WITH_SPACES);

        mappedUser = mapAppUser();

        assertEquals(APP_USER_1_PASSWORD, mappedUser.getPassword());
    }

    private AppUser mapAppUser() {

        mappedUser = mapper.mapAppUserCreationDTOtoAppUser(creationDTO);

        return mappedUser;
    }
}
