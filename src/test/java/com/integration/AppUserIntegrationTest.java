package com.integration;

import com.abstract_test_class.BaseIntegrationTest;
import com.dto.appuser.AppUserCreationDTO;
import com.dto.appuser.AppUserInfoDTO;
import com.dto.appuser.AppUserUpdateDTO;
import com.dto.credentials.CredentialsUpdateDTO;
import com.exceptions.appuser.AppUserNotFoundException;
import com.exceptions.appuser.UsernameTakenException;
import com.exceptions.common.NullDTOException;
import com.model.AppUser;
import com.repository.AppUserRepository;
import com.service.interfaces.AppUserService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

import static com.factory.AppUserTestDataFactory.buildValidAppUserCreationDTO;
import static com.factory.AppUserTestDataFactory.buildValidAppUserUpdateDTO;
import static com.test_constant.AppUserTestConstants.CreationValidData.*;
import static com.test_constant.AppUserTestConstants.InvalidData.APP_USERNAME_WITH_INVALID_SIZE;
import static com.test_constant.AppUserTestConstants.UpdateValidData.*;
import static org.junit.jupiter.api.Assertions.*;

public class AppUserIntegrationTest extends BaseIntegrationTest<AppUserRepository, AppUserCreationDTO, AppUserUpdateDTO, AppUserInfoDTO, AppUserService, AppUser> {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @BeforeEach
    protected void init() {

        creationDTO = buildValidAppUserCreationDTO();
        updateDTO = buildValidAppUserUpdateDTO();
    }

    @Override
    protected void createDefaultEntity() {

        service.createAppUser(creationDTO);
    }

    @Override
    protected void deleteDefaultEntity(Long id) {

        service.deleteAppUser(id);
    }

    @Override
    protected void updateEntity(Long id, AppUserUpdateDTO appUserUpdateDTO) {

        service.updateAppUser(id, appUserUpdateDTO);
    }

    @Override
    protected AppUser getFirstEntityOnRepoList() {

        return repository.findAll().getFirst();
    }

    @Override
    protected AppUser reloadEntity(Long id) {

        return repository.findById(id).orElseThrow();
    }

    @Override
    protected AppUserInfoDTO getInfoDTO(Long id) {

        return service.getAppUserById(getFirstEntityOnRepoList().getUserId());
    }

    @Override
    protected Integer countEntityOcurrencesOnRepo() {

        return repository.findAll().size();
    }

    @Test
    @DisplayName("Dado un DTO de creación con datos válidos, el usuario deberá persistirse correctamente en la base de datos")
    void givenCreationDTOWithValidData_WhenCreating_ThenAppUserIsPersisted() {

        createDefaultEntity();

        AppUser persistedAppUser = getFirstEntityOnRepoList();

        assertAll(
                "Verificación de campos",
                () -> assertEquals(1, countEntityOcurrencesOnRepo()),
                () -> assertNotNull(persistedAppUser.getUserId()),
                () -> assertEquals(APP_USER_1_USERNAME, persistedAppUser.getUsername()),
                () -> assertEquals(APP_USER_1_DEFAULT_ADMIN_RIGHTS_BOOLEAN, persistedAppUser.getHasAdminRights()),
                () -> assertNotNull(persistedAppUser.getCreationTimestamp()));
    }

    @Test
    @DisplayName("Dado un DTO de creación con datos válidos, la contraseña deberá almacenarse cifrada y no en texto plano")
    void givenCreationDTOWithValidData_WhenCreating_ThenPasswordIsStoredEncoded() {

        createDefaultEntity();

        AppUser persistedAppUser = getFirstEntityOnRepoList();

        assertAll(
                "Verificación de cifrado",
                () -> assertNotEquals(APP_USER_1_PASSWORD, persistedAppUser.getPassword()),
                () -> assertTrue(passwordEncoder.matches(APP_USER_1_PASSWORD, persistedAppUser.getPassword())));
    }

    @Test
    @DisplayName("Dado un DTO de creación con espacios alrededor del username y de la contraseña, ambos valores deberán persistirse recortados")
    void givenCreationDTOWithSurroundedBySpacesValues_WhenCreating_ThenValuesArePersistedTrimmed() {

        creationDTO.setUsername("   " + APP_USER_2_USERNAME + "   ");
        creationDTO.setPassword("   " + APP_USER_1_PASSWORD + "   ");

        createDefaultEntity();

        AppUser persistedAppUser = getFirstEntityOnRepoList();

        assertAll(
                "Verificación de recorte",
                () -> assertEquals(APP_USER_2_USERNAME, persistedAppUser.getUsername()),
                () -> assertTrue(passwordEncoder.matches(APP_USER_1_PASSWORD, persistedAppUser.getPassword())));
    }

    @Test
    @DisplayName("Dado un DTO de creación con un username ya registrado, deberá arrojar UsernameTakenException y no persistir el nuevo usuario")
    void givenCreationDTOWithTakenUsername_WhenCreating_ThenThrowsUsernameTakenExceptionAndAppUserIsNotPersisted() {

        createDefaultEntity();

        AppUserCreationDTO duplicatedUsernameDTO = buildValidAppUserCreationDTO();

        assertThrows(UsernameTakenException.class, () -> service.createAppUser(duplicatedUsernameDTO));

        assertEquals(1, countEntityOcurrencesOnRepo());
    }

    @Test
    @DisplayName("Dado un DTO de creación NULL, deberá arrojar NullDTOException y no persistir ningún usuario")
    void givenNullCreationDTO_WhenCreating_ThenThrowsNullDTOExceptionAndAppUserIsNotPersisted() {

        assertThrows(NullDTOException.class, () -> service.createAppUser(null));

        assertEntityTableIsEmpty();
    }

    @Test
    @DisplayName("Dado un DTO de creación con username NULL, deberá arrojar ConstraintViolationException y no persistir ningún usuario")
    void givenCreationDTOWithNullUsername_WhenCreating_ThenThrowsConstraintViolationExceptionAndAppUserIsNotPersisted() {

        creationDTO.setUsername(null);

        assertThrows(ConstraintViolationException.class, this::createDefaultEntity);

        assertEntityTableIsEmpty();
    }

    @Test
    @DisplayName("Dado un DTO de creación con contraseña en blanco, deberá arrojar ConstraintViolationException y no persistir ningún usuario")
    void givenCreationDTOWithBlankPassword_WhenCreating_ThenThrowsConstraintViolationExceptionAndAppUserIsNotPersisted() {

        creationDTO.setPassword("");

        assertThrows(ConstraintViolationException.class, this::createDefaultEntity);

        assertEntityTableIsEmpty();
    }

    @Test
    @DisplayName("Dado un DTO de creación con username que supera la longitud máxima permitida, deberá arrojar ConstraintViolationException y no persistir ningún usuario")
    void givenCreationDTOWithUsernameLongerThanMaxAllowed_WhenCreating_ThenThrowsConstraintViolationExceptionAndAppUserIsNotPersisted() {

        creationDTO.setUsername(APP_USERNAME_WITH_INVALID_SIZE);

        assertThrows(ConstraintViolationException.class, this::createDefaultEntity);

        assertEntityTableIsEmpty();
    }

    @Test
    @DisplayName("Dado un usuario existente, deberá exponer su información completa a través del DTO informativo")
    void givenExistingAppUser_WhenRetrievingById_ThenReturnsInfoDTOWithItsData() {

        createDefaultEntity();

        AppUser persistedAppUser = getFirstEntityOnRepoList();

        AppUserInfoDTO infoDTO = service.getAppUserById(persistedAppUser.getUserId());

        assertAll(
                "Verificación de campos",
                () -> assertNotNull(infoDTO),
                () -> assertEquals(APP_USER_1_USERNAME, infoDTO.getUsername()),
                () -> assertEquals(APP_USER_1_DEFAULT_ADMIN_RIGHTS_BOOLEAN, infoDTO.getHasAdminRights()),
                () -> assertNotNull(infoDTO.getCreatedAt()));
    }

    @Test
    @DisplayName("Dado un ID de usuario inexistente, al intentar obtener su información deberá arrojar AppUserNotFoundException")
    void givenNonExistingAppUser_WhenRetrievingById_ThenThrowsAppUserNotFoundException() {

        assertThrows(AppUserNotFoundException.class, () -> service.getAppUserById(-1L));
    }

    @Test
    @DisplayName("Dado un usuario existente, deberá poder actualizarse con datos válidos persistiéndose todos los cambios")
    void givenExistingAppUser_WhenUpdatingWithValidData_ThenAllChangesArePersisted() {

        createDefaultEntity();

        AppUser persistedAppUser = getFirstEntityOnRepoList();

        service.updateAppUser(persistedAppUser.getUserId(), updateDTO);

        AppUser updatedAppUser = reloadEntity(persistedAppUser.getUserId());

        assertAll(
                "Verificación de campos",
                () -> assertEquals(APP_USER_NEW_USERNAME, updatedAppUser.getUsername()),
                () -> assertEquals(APP_USER_NEW_ADMIN_RIGHTS_BOOLEAN, updatedAppUser.getHasAdminRights()),
                () -> assertNotNull(updatedAppUser.getModifiedDate()),
                () -> assertNotEquals(APP_USER_1_PASSWORD, updatedAppUser.getPassword()),
                () -> assertTrue(passwordEncoder.matches(APP_USER_NEW_PASSWORD, updatedAppUser.getPassword())),
                () -> assertFalse(passwordEncoder.matches(APP_USER_1_PASSWORD, updatedAppUser.getPassword())));
    }

    @Test
    @DisplayName("Dado un usuario existente, un DTO de actualización que solo indique derechos de administración deberá conservar username y contraseña originales")
    void givenExistingAppUser_WhenUpdatingOnlyAdminRights_ThenUsernameAndPasswordRemainUnchanged() {

        createDefaultEntity();

        AppUser persistedAppUser = getFirstEntityOnRepoList();
        String originalEncodedPassword = persistedAppUser.getPassword();

        AppUserUpdateDTO adminRightsOnlyDTO = AppUserUpdateDTO.builder()
                .hasAdminRights(APP_USER_NEW_ADMIN_RIGHTS_BOOLEAN)
                .build();

        service.updateAppUser(persistedAppUser.getUserId(), adminRightsOnlyDTO);

        AppUser updatedAppUser = reloadEntity(persistedAppUser.getUserId());

        assertAll(
                "Verificación de campos",
                () -> assertEquals(APP_USER_1_USERNAME, updatedAppUser.getUsername()),
                () -> assertEquals(originalEncodedPassword, updatedAppUser.getPassword()),
                () -> assertEquals(APP_USER_NEW_ADMIN_RIGHTS_BOOLEAN, updatedAppUser.getHasAdminRights()));
    }

    @Test
    @DisplayName("Dado un usuario existente, un DTO de actualización con contraseña NULL deberá conservar la contraseña almacenada")
    void givenExistingAppUser_WhenUpdatingWithNullPassword_ThenStoredPasswordRemainsUnchanged() {

        createDefaultEntity();

        AppUser persistedAppUser = getFirstEntityOnRepoList();
        String originalEncodedPassword = persistedAppUser.getPassword();

        updateDTO.setPassword(null);

        service.updateAppUser(persistedAppUser.getUserId(), updateDTO);

        AppUser updatedAppUser = reloadEntity(persistedAppUser.getUserId());

        assertAll(
                "Verificación de contraseña",
                () -> assertEquals(originalEncodedPassword, updatedAppUser.getPassword()),
                () -> assertTrue(passwordEncoder.matches(APP_USER_1_PASSWORD, updatedAppUser.getPassword())),
                () -> assertEquals(APP_USER_NEW_USERNAME, updatedAppUser.getUsername()));
    }

    @Test
    @DisplayName("Dado un usuario existente, un DTO de actualización con todos sus campos NULL no modificará ningún dato almacenado")
    void givenExistingAppUser_WhenUpdatingWithAllNullFields_ThenStoredDataRemainsUnchanged() {

        createDefaultEntity();

        AppUser persistedAppUser = getFirstEntityOnRepoList();
        String originalEncodedPassword = persistedAppUser.getPassword();

        AppUserUpdateDTO emptyUpdateDTO = AppUserUpdateDTO.builder().build();

        updateEntity(persistedAppUser.getUserId(), emptyUpdateDTO);

        AppUser updatedAppUser = reloadEntity(persistedAppUser.getUserId());

        assertAll(
                "Verificación de campos",
                () -> assertEquals(APP_USER_1_USERNAME, updatedAppUser.getUsername()),
                () -> assertEquals(originalEncodedPassword, updatedAppUser.getPassword()),
                () -> assertEquals(APP_USER_1_DEFAULT_ADMIN_RIGHTS_BOOLEAN, updatedAppUser.getHasAdminRights()));
    }

    @Test
    @DisplayName("Dado un usuario existente, un DTO de actualización que mantenga su propio username no deberá arrojar UsernameTakenException")
    void givenExistingAppUser_WhenUpdatingWithItsOwnUsername_ThenUpdateIsAppliedSuccessfully() {

        createDefaultEntity();

        AppUser persistedAppUser = getFirstEntityOnRepoList();

        updateDTO.setUsername(APP_USER_1_USERNAME);

        assertDoesNotThrow(() -> updateEntity(persistedAppUser.getUserId(), updateDTO));

        AppUser updatedAppUser = reloadEntity(persistedAppUser.getUserId());

        assertAll(
                "Verificación de campos",
                () -> assertEquals(APP_USER_1_USERNAME, updatedAppUser.getUsername()),
                () -> assertEquals(APP_USER_NEW_ADMIN_RIGHTS_BOOLEAN, updatedAppUser.getHasAdminRights()));
    }

    @Test
    @DisplayName("Dado un usuario existente y un username utilizado por otro usuario, deberá arrojar UsernameTakenException sin persistir los cambios")
    void givenExistingAppUser_WhenUpdatingWithUsernameInUseByAnotherUser_ThenThrowsUsernameTakenExceptionAndChangesAreNotPersisted() {

        createDefaultEntity();

        AppUser persistedAppUser = getFirstEntityOnRepoList();

        createSecondAppUser();

        updateDTO.setUsername(APP_USER_2_USERNAME);

        assertThrows(UsernameTakenException.class, () -> updateEntity(persistedAppUser.getUserId(), updateDTO));

        AppUser unchangedAppUser = reloadEntity(persistedAppUser.getUserId());

        assertAll(
                "Verificación de campos",
                () -> assertEquals(APP_USER_1_USERNAME, unchangedAppUser.getUsername()),
                () -> assertEquals(APP_USER_1_DEFAULT_ADMIN_RIGHTS_BOOLEAN, unchangedAppUser.getHasAdminRights()),
                () -> assertEquals(2, countEntityOcurrencesOnRepo()));
    }

    @Test
    @DisplayName("Dado un ID de usuario inexistente, al intentar actualizarlo deberá arrojar AppUserNotFoundException")
    void givenNonExistingAppUser_WhenUpdating_ThenThrowsAppUserNotFoundException() {

        assertThrows(AppUserNotFoundException.class, () -> service.updateAppUser(-1L, updateDTO));
    }

    @Test
    @DisplayName("Dado un ID de usuario inexistente y un DTO de actualización NULL, deberá arrojar AppUserNotFoundException")
    void givenNonExistingAppUser_WhenUpdatingWithNullDTO_ThenThrowsAppUserNotFoundException() {

        assertThrows(AppUserNotFoundException.class, () -> service.updateAppUser(-1L, null));
    }

    @Test
    @DisplayName("Dado un usuario existente, un DTO de actualización NULL deberá arrojar NullDTOException sin modificar el usuario almacenado")
    void givenExistingAppUser_WhenUpdatingWithNullDTO_ThenThrowsNullDTOExceptionAndStoredDataRemainsUnchanged() {

        createDefaultEntity();

        AppUser persistedAppUser = getFirstEntityOnRepoList();

        String originalEncodedPassword = persistedAppUser.getPassword();

        assertThrows(NullDTOException.class, () -> updateEntity(persistedAppUser.getUserId(), null));

        AppUser unchangedAppUser = reloadEntity(persistedAppUser.getUserId());

        assertAll(
                "Verificación de campos",
                () -> assertEquals(APP_USER_1_USERNAME, unchangedAppUser.getUsername()),
                () -> assertEquals(originalEncodedPassword, unchangedAppUser.getPassword()));
    }

    @Test
    @DisplayName("Dado un usuario existente, un DTO de actualización con username en blanco deberá arrojar ConstraintViolationException sin modificar el usuario almacenado")
    void givenExistingAppUser_WhenUpdatingWithBlankUsername_ThenThrowsConstraintViolationExceptionAndStoredDataRemainsUnchanged() {

        createDefaultEntity();

        AppUser persistedAppUser = getFirstEntityOnRepoList();

        String originalEncodedPassword = persistedAppUser.getPassword();

        updateDTO.setUsername("");

        assertThrows(ConstraintViolationException.class, () -> updateEntity(persistedAppUser.getUserId(), updateDTO));

        AppUser unchangedAppUser = reloadEntity(persistedAppUser.getUserId());

        assertAll(
                "Verificación de campos",
                () -> assertEquals(APP_USER_1_USERNAME, unchangedAppUser.getUsername()),
                () -> assertEquals(originalEncodedPassword, unchangedAppUser.getPassword()));
    }

    @Test
    @DisplayName("Dado un usuario existente, un DTO de actualización con username compuesto solo por espacios deberá arrojar ConstraintViolationException")
    void givenExistingAppUser_WhenUpdatingWithSpacesOnlyUsername_ThenThrowsConstraintViolationException() {

        createDefaultEntity();

        AppUser persistedAppUser = getFirstEntityOnRepoList();

        updateDTO.setUsername("     ");

        assertThrows(ConstraintViolationException.class, () -> updateEntity(persistedAppUser.getUserId(), updateDTO));

        AppUser unchangedAppUser = reloadEntity(persistedAppUser.getUserId());

        assertEquals(APP_USER_1_USERNAME, unchangedAppUser.getUsername());
    }

    @Test
    @DisplayName("Dado un usuario existente, un DTO de actualización con username que supera la longitud máxima deberá arrojar ConstraintViolationException")
    void givenExistingAppUser_WhenUpdatingWithUsernameLongerThanMaxAllowed_ThenThrowsConstraintViolationException() {

        createDefaultEntity();

        AppUser persistedAppUser = getFirstEntityOnRepoList();

        updateDTO.setUsername(APP_USERNAME_WITH_INVALID_SIZE);

        assertThrows(ConstraintViolationException.class, () -> updateEntity(persistedAppUser.getUserId(), updateDTO));

        AppUser unchangedAppUser = reloadEntity(persistedAppUser.getUserId());

        assertEquals(APP_USER_1_USERNAME, unchangedAppUser.getUsername());
    }

    @Test
    @DisplayName("Dado un usuario existente, deberá poder eliminarse correctamente de la base de datos")
    void givenExistingAppUser_WhenDeleting_ThenAppUserIsRemoved() {

        createDefaultEntity();

        AppUser persistedAppUser = getFirstEntityOnRepoList();

        deleteDefaultEntity(persistedAppUser.getUserId());

        assertEntityTableIsEmpty();
    }

    @Test
    @DisplayName("Dado un ID de usuario inexistente, al intentar eliminarlo deberá arrojar AppUserNotFoundException")
    void givenNonExistingAppUser_WhenDeleting_ThenThrowsAppUserNotFoundException() {

        assertThrows(AppUserNotFoundException.class, () -> service.deleteAppUser(-1L));

        assertEntityTableIsEmpty();
    }

    @Test
    @DisplayName("Dado un usuario existente y credenciales correctas, el inicio de sesión deberá retornar true")
    void givenExistingAppUserWithCorrectCredentials_WhenSigningIn_ThenReturnsTrue() {

        createDefaultEntity();

        assertTrue(service.signIn(APP_USER_1_USERNAME, APP_USER_1_PASSWORD));
    }

    @Test
    @DisplayName("Dado un usuario existente y una contraseña incorrecta, el inicio de sesión deberá retornar false")
    void givenExistingAppUserWithWrongPassword_WhenSigningIn_ThenReturnsFalse() {

        createDefaultEntity();

        assertFalse(service.signIn(APP_USER_1_USERNAME, "contrasenaIncorrecta"));
    }

    @Test
    @DisplayName("Dado un username inexistente, el inicio de sesión deberá arrojar AppUserNotFoundException")
    void givenNonExistingUsername_WhenSigningIn_ThenThrowsAppUserNotFoundException() {

        createDefaultEntity();

        assertThrows(AppUserNotFoundException.class, () -> service.signIn("usuarioInexistente", APP_USER_1_PASSWORD));
    }

    @Test
    @DisplayName("Dado un usuario existente con credenciales correctas, deberá poder validarlas aunque la contraseña almacenada esté cifrada")
    void givenExistingAppUserWithEncodedPassword_WhenSigningInWithStoredCredentials_ThenReturnsTrue() {

        createDefaultEntity();

        AppUser persistedAppUser = getFirstEntityOnRepoList();

        assertAll(
                "Verificación de credenciales",
                () -> assertNotEquals(APP_USER_1_PASSWORD, persistedAppUser.getPassword()),
                () -> assertTrue(service.signIn(APP_USER_1_USERNAME, APP_USER_1_PASSWORD)));
    }

    @Test
    @DisplayName("Dado un usuario administrador existente, la carga de su cuenta deberá otorgarle el rol ADMIN")
    void givenExistingAdminAppUser_WhenLoadingByUsername_ThenGrantsAdminRole() {

        createSecondAppUser();

        UserDetails loadedUser = userDetailsService.loadUserByUsername(APP_USER_2_USERNAME);

        assertAll(
                "Verificación de cuenta",
                () -> assertEquals(APP_USER_2_USERNAME, loadedUser.getUsername()),
                () -> assertTrue(loadedUser.isEnabled()),
                () -> assertTrue(hasAuthority(loadedUser, "ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Dado un usuario sin derechos de administración, la carga de su cuenta deberá otorgarle el rol USER")
    void givenExistingNonAdminAppUser_WhenLoadingByUsername_ThenGrantsUserRole() {

        createDefaultEntity();

        UserDetails loadedUser = userDetailsService.loadUserByUsername(APP_USER_1_USERNAME);

        assertAll(
                "Verificación de cuenta",
                () -> assertEquals(APP_USER_1_USERNAME, loadedUser.getUsername()),
                () -> assertTrue(hasAuthority(loadedUser, "ROLE_USER")),
                () -> assertFalse(hasAuthority(loadedUser, "ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Dado un username inexistente, la carga de su cuenta deberá arrojar UsernameNotFoundException")
    void givenNonExistingUsername_WhenLoadingByUsername_ThenThrowsUsernameNotFoundException() {

        createDefaultEntity();

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("usuarioInexistente"));
    }

    @Test
    @DisplayName("Dado un usuario existente, la contraseña cargada en su cuenta deberá coincidir con la almacenada en base de datos")
    void givenExistingAppUser_WhenLoadingByUsername_ThenReturnsStoredEncodedPassword() {

        createDefaultEntity();

        AppUser persistedAppUser = getFirstEntityOnRepoList();

        UserDetails loadedUser = userDetailsService.loadUserByUsername(APP_USER_1_USERNAME);

        assertAll(
                "Verificación de contraseña",
                () -> assertEquals(persistedAppUser.getPassword(), loadedUser.getPassword()),
                () -> assertTrue(passwordEncoder.matches(APP_USER_1_PASSWORD, loadedUser.getPassword())));
    }

    @Test
    @DisplayName("Dado un usuario existente, la actualización de credenciales con datos válidos deberá persistir los cambios")
    void givenExistingAppUser_WhenUpdatingCredentials_ThenCredentialsAreUpdated() {

        createDefaultEntity();

        AppUser persistedAppUser = getFirstEntityOnRepoList();

        CredentialsUpdateDTO credentialsUpdateDTO = CredentialsUpdateDTO.builder()
                .username(APP_USER_1_USERNAME)
                .password(APP_USER_1_PASSWORD)
                .confirmPassword(APP_USER_1_PASSWORD)
                .build();

        service.updateCredentials(APP_USER_1_USERNAME, credentialsUpdateDTO);

        AppUser updatedAppUser = reloadEntity(persistedAppUser.getUserId());

        assertAll(
                "Verificación de credenciales",
                () -> assertNotEquals(APP_USER_1_PASSWORD, updatedAppUser.getPassword()),
                () -> assertTrue(passwordEncoder.matches(APP_USER_1_PASSWORD, updatedAppUser.getPassword()))
        );
    }

    private void createSecondAppUser() {

        AppUserCreationDTO secondAppUserDTO = buildValidAppUserCreationDTO();

        secondAppUserDTO.setUsername(APP_USER_2_USERNAME);
        secondAppUserDTO.setPassword(APP_USER_2_PASSWORD);
        secondAppUserDTO.setHasAdminRights(APP_USER_2_DEFAULT_ADMIN_RIGHTS_BOOLEAN);

        service.createAppUser(secondAppUserDTO);
    }

    private boolean hasAuthority(UserDetails loadedUser, String expectedAuthority) {

        return loadedUser.getAuthorities().stream()
                .anyMatch(authority -> Objects.equals(authority.getAuthority(), expectedAuthority));
    }
}
