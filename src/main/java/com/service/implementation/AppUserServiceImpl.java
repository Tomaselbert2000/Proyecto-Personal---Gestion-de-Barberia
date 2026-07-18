package com.service.implementation;

import com.dto.app_user.AppUserCreationDTO;
import com.dto.app_user.AppUserInfoDTO;
import com.dto.app_user.AppUserUpdateDTO;
import com.exceptions.appuser.AppUserNotFoundException;
import com.exceptions.appuser.UsernameTakenException;
import com.mapper.interfaces.AppUserMapper;
import com.model.AppUser;
import com.repository.AppUserRepository;
import com.service.interfaces.AppUserService;
import com.validation.app_user.AppUserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;
    private final AppUserValidator validator;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void createAppUser(AppUserCreationDTO appUserCreationDTO) {

        validator.validateDTO(appUserCreationDTO);

        checkIfUsernameIsAlreadyTaken(appUserCreationDTO.getUsername());

        AppUser newUser = appUserMapper.mapAppUserCreationDTOtoAppUser(appUserCreationDTO);

        String encodedPassword = encodePassword(newUser.getPassword());

        newUser.setPassword(encodedPassword);

        appUserRepository.save(newUser);
    }

    @Override
    @Transactional
    public void deleteAppUser(Long appUserId) {

        AppUser appUserOnDB = loadAppUser(appUserId);

        appUserRepository.delete(appUserOnDB);
    }

    @Override
    public AppUserInfoDTO getAppUserById(Long appUserId) {

        AppUser appUserOnDB = loadAppUser(appUserId);

        return appUserMapper.mapAppUserToInfoDTO(appUserOnDB);
    }

    @Override
    @Transactional
    public void updateAppUser(Long appUserId, AppUserUpdateDTO updateDTO) {

        AppUser existingAppUser = loadAppUser(appUserId);

        validator.validateDTO(updateDTO);

        checkIfUsernameIsAlreadyTakenForUpdate(updateDTO.getUsername(), existingAppUser.getUserId());

        appUserMapper.mapAppUserUpdateDTOtoAppUser(updateDTO, existingAppUser);

        checkIfPasswordChangedAndEncryptIt(updateDTO, existingAppUser);

        appUserRepository.save(existingAppUser);
    }

    @Override
    public Boolean signIn(String username, String password) {

        AppUser userOnDB = appUserRepository.findByUsername(username);

        if (userOnDB == null) return false;

        return passwordEncoder.matches(password, userOnDB.getPassword());
    }

    private AppUser loadAppUser(Long appUserId) {

        return appUserRepository.findById(appUserId).orElseThrow(AppUserNotFoundException::new);
    }

    private void checkIfUsernameIsAlreadyTaken(String username) {

        if (username != null) {

            if (appUserRepository.existsByUsername(username)) throw new UsernameTakenException();
        }
    }

    private void checkIfUsernameIsAlreadyTakenForUpdate(String username, Long userId) {

        if (username != null) {

            if (appUserRepository.existsByUsernameAndUserIdNot(username, userId)) throw new UsernameTakenException();
        }
    }

    private String encodePassword(String password) {

        if (password != null) {

            return passwordEncoder.encode(password);
        }

        return "";
    }

    private void checkIfPasswordChangedAndEncryptIt(AppUserUpdateDTO updateDTO, AppUser existingAppUser) {

        if (updateDTO.getPassword() != null && !updateDTO.getPassword().isEmpty()) {

            String encodedPassword = encodePassword(updateDTO.getPassword());

            existingAppUser.setPassword(encodedPassword);
        }
    }
}
