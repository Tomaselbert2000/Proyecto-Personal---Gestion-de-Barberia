package com.service.helper;

import com.dto.app_user.AppUserCreationDTO;
import com.dto.app_user.AppUserUpdateDTO;
import com.mapper.interfaces.AppUserMapper;
import com.model.AppUser;
import com.repository.AppUserRepository;
import com.service.interfaces.AppUserService;
import com.validation.app_user.AppUserValidator;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class AppUserServiceTestHelper {

    public static void mockPasswordEncoder(PasswordEncoder passwordEncoder, String encodedPassword) {

        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
    }

    public static void verifyValidatorCreationInteraction(AppUserValidator validator, AppUserCreationDTO inputDTO) {

        verify(validator).validateDTO(inputDTO);
    }

    public static void verifyValidatorUpdateInteraction(AppUserValidator validator, AppUserUpdateDTO updateDTO) {

        verify(validator).validateDTO(updateDTO);
    }

    public static void verifyMapperCreationInteraction(AppUserMapper mapper, AppUserCreationDTO inputDTO) {

        verify(mapper).mapAppUserCreationDTOtoAppUser(inputDTO);
    }

    public static void verifyMapperCreationNoInteractions(AppUserMapper mapper, AppUserCreationDTO inputDTO) {

        verify(mapper, never()).mapAppUserCreationDTOtoAppUser(inputDTO);
    }

    public static void verifyMapperUpdateInteraction(AppUserMapper mapper, AppUser existingAppUser, AppUserUpdateDTO updateDTO) {

        verify(mapper).mapAppUserUpdateDTOtoAppUser(updateDTO, existingAppUser);
    }

    public static void verifyMapperUpdateNoInteraction(AppUserMapper mapper, AppUser existingUser, AppUserUpdateDTO updateDTO) {

        verify(mapper, never()).mapAppUserUpdateDTOtoAppUser(updateDTO, existingUser);
    }

    public static void verifyPasswordEncoderNoInteraction(PasswordEncoder passwordEncoder) {

        verify(passwordEncoder, never()).encode(anyString());
    }

    public static AppUser captureAppUser(AppUserRepository appUserRepository, ArgumentCaptor<AppUser> captor) {

        verify(appUserRepository).save(captor.capture());

        return captor.getValue();
    }

    public static <T> void mockValidatorToThrowException(AppUserValidator validator, Exception exception, T inputDTO) {

        doThrow(exception).when(validator).validateDTO(inputDTO);
    }

    public static void mockThatUsernameIsTakenOnCreation(AppUserRepository repository, String username) {

        when(repository.existsByUsername(username)).thenReturn(true);
    }

    public static void mockThatUsernameIsTakenOnUpdate(AppUserRepository repository, String username, Long existingUserId) {

        when(repository.existsByUsernameAndUserIdNot(username, existingUserId)).thenReturn(true);
    }

    public static void mockExistingAppUser(AppUserRepository appUserRepository, AppUser appUser) {

        when(appUserRepository.findById(appUser.getUserId())).thenReturn(Optional.of(appUser));
    }

    public static void mockNonExistingAppUser(AppUserRepository appUserRepository, AppUser appUser) {

        when(appUserRepository.findById(appUser.getUserId())).thenReturn(Optional.empty());
    }

    public static void registerNewUser(AppUserService appUserService, AppUserCreationDTO inputDTO) {

        appUserService.createAppUser(inputDTO);
    }

    public static void deleteAppUser(AppUserService appUserService, AppUser appUser) {

        appUserService.deleteAppUser(appUser.getUserId());
    }

    public static void updateAppUser(AppUserService appUserService, AppUserUpdateDTO updateDTO, AppUser appUser) {

        appUserService.updateAppUser(appUser.getUserId(), updateDTO);
    }
}
