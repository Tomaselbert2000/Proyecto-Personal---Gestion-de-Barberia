package com.service.implementation;

import com.dto.app_user.AppUserCreationDTO;
import com.exceptions.appuser.UsernameTakenException;
import com.exceptions.common.NullDTOException;
import com.mapper.interfaces.AppUserMapper;
import com.model.AppUser;
import com.repository.AppUserRepository;
import com.validation.app_user.AppUserValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static com.service.helper.ServiceTestVerificationHelper.verifyEntityNotSaved;
import static com.service.helper.ServiceTestVerificationHelper.verifyEntitySaved;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AppUserServiceImplTest {

    AppUserCreationDTO inputDTO;
    @Mock
    private AppUserRepository repository;
    @Mock
    private AppUserValidator validator;
    @Spy
    private AppUserMapper mapper;
    @InjectMocks
    private AppUserServiceImpl appUserService;
    @Captor
    private ArgumentCaptor<AppUser> captor;

    @Test
    @DisplayName("Dado un DTO de creación con datos correctos, deberá ser persistido correctamente")
    void givenValidUserDTO_WhenCreating_ThenIsPersisted() {

        registerNewUser();

        verify(validator).validateDTO(inputDTO);
        verifyMapperCreationInteraction();
        verifyEntitySaved(repository);
    }

    @Test
    @DisplayName("Dado cualquier valor NULL ingresado dentro del DTO de creación de usuario, el nuevo usuario no será persistido")
    void givenNullValues_WhenCreating_ThenAppUserIsNotPersisted() {

        doThrow(NullDTOException.class).when(validator).validateDTO(inputDTO);

        assertThrows(NullDTOException.class, this::registerNewUser);

        verify(validator).validateDTO(inputDTO);
        verifyMapperCreationNoInteractions();
        verifyEntityNotSaved(repository);
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre de usuario ya existente, la entidad no será persistida")
    void givenInvalidData_WhenCreating_ThenEntityIsNotPersisted() {

        doThrow(UsernameTakenException.class).when(validator).validateDTO(inputDTO);

        assertThrows(UsernameTakenException.class, this::registerNewUser);

        verify(validator).validateDTO(inputDTO);
        verifyMapperCreationNoInteractions();
        verifyEntityNotSaved(repository);
    }

    @Test
    @DisplayName("Dado un usuario ya registrado, deberá poder ser eliminado exitosamente")
    void givenExistingUser_WhenDeleting_ThenUserIsSuccessfullyDeleted() {


    }

    private void verifyMapperCreationInteraction() {

        verify(mapper).mapAppUserCreationDTOtoAppUser(inputDTO);
    }

    private void verifyMapperCreationNoInteractions() {

        verify(mapper, never()).mapAppUserCreationDTOtoAppUser(any());
    }

    private void registerNewUser() {

        appUserService.createAppUser(inputDTO);
    }
}