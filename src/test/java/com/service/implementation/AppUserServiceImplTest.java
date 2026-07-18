package com.service.implementation;

import com.abstract_test_class.BaseServiceTest;
import com.dto.app_user.AppUserCreationDTO;
import com.dto.app_user.AppUserInfoDTO;
import com.dto.app_user.AppUserUpdateDTO;
import com.exceptions.appuser.AppUserNotFoundException;
import com.exceptions.appuser.UsernameTakenException;
import com.exceptions.common.NullDTOException;
import com.mapper.implementation.AppUserMapperImpl;
import com.mapper.interfaces.AppUserMapper;
import com.model.AppUser;
import com.repository.AppUserRepository;
import com.validation.app_user.AppUserValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.factory.AppUserTestDataFactory.*;
import static com.service.helper.AppUserServiceTestHelper.*;
import static com.service.helper.ServiceTestVerificationHelper.verifyEntityNotSaved;
import static com.service.helper.ServiceTestVerificationHelper.verifyEntitySaved;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

public class AppUserServiceImplTest extends BaseServiceTest<AppUser, AppUserRepository> {

    @Mock
    private AppUserRepository repository;

    @Mock
    private AppUserValidator validator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    private final AppUserMapper mapper = new AppUserMapperImpl();

    @InjectMocks
    private AppUserServiceImpl appUserService;

    @Captor
    private ArgumentCaptor<AppUser> captor;

    private final AppUserCreationDTO creationDTO = buildValidAppUserCreationDTO();
    private final AppUserUpdateDTO updateDTO = buildValidAppUserUpdateDTO();
    private final AppUser existingAppUser = builValidAppUser1();

    @Test
    @DisplayName("Dado un DTO de creación con datos correctos, deberá ser persistido correctamente")
    void givenValidUserDTO_WhenCreating_ThenIsPersisted() {

        String encodedPassword = "EncodedPassword";

        mockPasswordEncoder(passwordEncoder, encodedPassword);

        registerNewUser(appUserService, creationDTO);

        verifyCreationProcessSuccess();

        AppUser capturedUser = captureAppUser(repository, captor);

        checkCreationProcessAssertions(capturedUser, encodedPassword);
    }

    @Test
    @DisplayName("Dado cualquier valor NULL ingresado dentro del DTO de creación de usuario, el nuevo usuario no será persistido")
    void givenNullValues_WhenCreating_ThenAppUserIsNotPersisted() {

        mockValidatorToThrowException(validator, new NullDTOException(), creationDTO);

        assertThrows(NullDTOException.class, () -> registerNewUser(appUserService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de usuario ya existente, la entidad no será persistida")
    void givenInvalidData_WhenCreating_ThenEntityIsNotPersisted() {

        mockThatUsernameIsTakenOnCreation(repository, creationDTO.getUsername());

        assertThrows(UsernameTakenException.class, () -> registerNewUser(appUserService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un usuario ya registrado, deberá poder ser eliminado exitosamente")
    void givenExistingUser_WhenDeleting_ThenUserIsSuccessfullyDeleted() {

        mockExistingAppUser(repository, existingAppUser);

        deleteAppUser(appUserService, existingAppUser);

        verifyThatEntityWasDeleted(existingAppUser);
    }

    @Test
    @DisplayName("Dado un ID de usuario inexistente, al intentar eliminarlo arrojará AppUserNotFoundException y no se efectuarán cambios en ninguna entidad")
    void givenNonExistingAppUserID_WhenDeleting_ThenThrowsAppUserNotFoundExceptionAndNothingIsModifierNorDeleted() {

        mockNonExistingAppUser(repository, existingAppUser);

        assertThrows(AppUserNotFoundException.class, () -> deleteAppUser(appUserService, existingAppUser));

        verifyThatEntityWasNotDeleted(existingAppUser);
    }

    @Test
    @DisplayName("Dado un usuario ya registrado, deberá exponer su información mediante un DTO informativo")
    void givenExistingUser_ThenItsInformationIsMappedAsInfoDTO() {

        mockExistingAppUser(repository, existingAppUser);

        AppUserInfoDTO infoDTO = appUserService.getAppUserById(existingAppUser.getUserId());

        checkInfoDTOAssertions(infoDTO);
    }

    @Test
    @DisplayName("Dado un usuario ya registrado, deberá poder actualizar su información exitosamente")
    void givenExistingUser_WhenUpdatingWithValidData_ThenIsSuccessfullyPersisted() {

        mockExistingAppUser(repository, existingAppUser);

        updateAppUser(appUserService, updateDTO, existingAppUser);

        verifyUpdateProcessSuccess();

        AppUser capturedAppUser = captureAppUser(repository, captor);

        checkUpdateAssertions(capturedAppUser);
    }

    @Test
    @DisplayName("Dado un DTO de actualización que modifique el nombre de usuario asignado por uno ya utilizado por otro usuario, arrojará UsernameTakenException y no persistirá cambios")
    void givenExistingUser_WhenUpdatingWithUsernameStringAlreadyInUseByAnotherUser_ThenThrowsUsernameTakenException() {

        mockExistingAppUser(repository, existingAppUser);

        mockThatUsernameIsTakenOnUpdate(repository, updateDTO.getUsername(), existingAppUser.getUserId());

        assertThrows(UsernameTakenException.class, () -> updateAppUser(appUserService, updateDTO, existingAppUser));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un usuario inexistente, al intentar actualizarlo arrojará AppUserNotFoundException y no modificará ninguna entidad")
    void givenNonExistingAppUser_WhenUpdating_ThenThrowsAppUserNotFoundExceptionAndNothingIsModifierNorDeleted() {

        mockNonExistingAppUser(repository, existingAppUser);

        assertThrows(AppUserNotFoundException.class, () -> updateAppUser(appUserService, updateDTO, existingAppUser));

        verifyThatEntityWasNotDeleted(existingAppUser);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un string de contraseña en NULL, la contraseña actualmente almacenada no será modificada")
    void givenUpdateDTOWithNullPasswordString_WhenUpdating_ThenPasswordEncoderIsNotCalled() {

        mockExistingAppUser(repository, existingAppUser);

        updateDTO.setPassword(null);

        updateAppUser(appUserService, updateDTO, existingAppUser);

        verifyUpdateProcessSuccess();

        verifyPasswordEncoderNoInteraction(passwordEncoder);
    }

    @Override
    protected AppUserRepository getPrimaryRepository() {

        return repository;
    }

    private void checkCreationProcessAssertions(AppUser capturedUser, String encodedPassword) {

        assertNotNull(capturedUser);

        assertAll(
                "Verificación de campos",
                () -> assertEquals(creationDTO.getUsername(), capturedUser.getUsername()),
                () -> assertEquals(creationDTO.getHasAdminRights(), capturedUser.getHasAdminRights()),
                () -> assertEquals(encodedPassword, capturedUser.getPassword())
        );
    }

    private void checkInfoDTOAssertions(AppUserInfoDTO infoDTO) {

        assertNotNull(infoDTO);

        assertAll(
                "Verificación de campos",
                () -> assertEquals(infoDTO.getUsername(), existingAppUser.getUsername()),
                () -> assertEquals(infoDTO.getCreatedAt(), existingAppUser.getCreationTimestamp()),
                () -> assertEquals(infoDTO.getHasAdminRights(), existingAppUser.getHasAdminRights())
        );
    }

    private void checkUpdateAssertions(AppUser capturedAppUser) {

        assertNotNull(capturedAppUser);

        assertAll(
                "Verificación de campos",
                () -> assertEquals(updateDTO.getUsername(), capturedAppUser.getUsername()),
                () -> assertEquals(updateDTO.getHasAdminRights(), capturedAppUser.getHasAdminRights())
        );
    }

    private void verifyCreationProcessFailure() {

        verify(validator).validateDTO(creationDTO);
        verifyMapperCreationNoInteractions(mapper, creationDTO);
        verifyEntityNotSaved(repository);
    }

    private void verifyCreationProcessSuccess() {

        verifyValidatorCreationInteraction(validator, creationDTO);
        verifyMapperCreationInteraction(mapper, creationDTO);
        verifyEntitySaved(repository);
    }

    private void verifyUpdateProcessSuccess() {

        verifyValidatorUpdateInteraction(validator, updateDTO);
        verifyMapperUpdateInteraction(mapper, existingAppUser, updateDTO);
        verifyThatEntityWasSaved();
    }

    private void verifyUpdateProcessFailure() {

        verifyValidatorUpdateInteraction(validator, updateDTO);
        verifyMapperUpdateNoInteraction(mapper, existingAppUser, updateDTO);
        verifyThatEntityWasNotSaved();
    }
}