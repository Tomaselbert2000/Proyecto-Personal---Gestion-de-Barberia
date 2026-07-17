package com.mapper.appuser;

import com.dto.app_user.AppUserUpdateDTO;
import com.exceptions.common.NullMapperInputException;
import com.mapper.implementation.AppUserMapperImpl;
import com.mapper.interfaces.AppUserMapper;
import com.model.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.factory.AppUserTestDataFactory.builValidAppUser1;
import static com.factory.AppUserTestDataFactory.buildValidAppUserUpdateDTO;
import static com.test_constant.AppUserTestConstants.CreationValidData.*;
import static com.test_constant.AppUserTestConstants.UpdateValidData.*;
import static org.junit.jupiter.api.Assertions.*;

public class AppUserMapperUpdateTest {

    private final AppUserMapper mapper = new AppUserMapperImpl();

    private AppUserUpdateDTO updateDTO;

    private AppUser existingUser;

    @BeforeEach
    void init() {

        existingUser = builValidAppUser1();
        updateDTO = buildValidAppUserUpdateDTO();
    }

    @Test
    @DisplayName("Dado un DTO de actualización NULL, deberá arrojar NullMapperInputException")
    void givenUpdateDTOWithNullAttributes_WhenUpdating_ThenThrows_NullMapperInputDataException() {

        existingUser = null;

        assertThrows(NullMapperInputException.class, this::mapEntityForUpdate);
    }

    @Test
    @DisplayName("Dados cualquiera de los atributos de actualización en NULL, la entidad deberá preservar sus valores actuales")
    void givenUpdateDTOWithNullAttributesWhenUpdatingAppUser_thenEntityKeepsCurrentData() {

        updateDTO.setUsername(null);
        updateDTO.setPassword(null);
        updateDTO.setHasAdminRights(null);

        mapEntityForUpdate();

        assertAll(
                "Verificación de campos",
                () -> assertEquals(APP_USER_1_USERNAME, existingUser.getUsername()),
                () -> assertEquals(APP_USER_1_PASSWORD, existingUser.getPassword()),
                () -> assertEquals(APP_USER_1_DEFAULT_ADMIN_RIGHTS_BOOLEAN, existingUser.getHasAdminRights())
        );
    }

    @Test
    @DisplayName("Dado un mapeado exitoso, la fecha y hora de última modificación se actualizarán automáticamente")
    void givenSuccessfulMappingWhenUpdatingAppUser_thenLastModifiedDate_ShouldBeUpdatedAutomatically() {

        LocalDateTime currentModifiedDate = existingUser.getModifiedDate();

        mapEntityForUpdate();

        assertNotEquals(currentModifiedDate, existingUser.getModifiedDate());
    }

    @Test
    @DisplayName("Dado un mapeado exitoso, la entidad pasará a tener correctamente asignados los nuevos datos")
    void givenSuccessfulMappingWhenUpdatingAppUser_ThenShouldReturnCorrectlyTranslatedDataFromOriginalEntity() {

        mapEntityForUpdate();

        assertAll(
                "Verificación de campos",
                () -> assertEquals(APP_USER_NEW_USERNAME, existingUser.getUsername()),
                () -> assertEquals(APP_USER_NEW_PASSWORD, existingUser.getPassword()),
                () -> assertEquals(APP_USER_NEW_ADMIN_RIGHTS_BOOLEAN, existingUser.getHasAdminRights())
        );
    }

    private void mapEntityForUpdate() {

        mapper.mapAppUserUpdateDTOtoAppUser(updateDTO, existingUser);
    }
}
