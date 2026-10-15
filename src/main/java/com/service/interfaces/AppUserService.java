package com.service.interfaces;

import com.dto.appuser.AppUserCreationDTO;
import com.dto.appuser.AppUserInfoDTO;
import com.dto.appuser.AppUserUpdateDTO;
import com.dto.credentials.CredentialsUpdateDTO;

public interface AppUserService {

    void createAppUser(AppUserCreationDTO appUserCreationDTO);

    void deleteAppUser(Long appUserId);

    AppUserInfoDTO getAppUserById(Long appUserId);

    void updateAppUser(Long appUserId, AppUserUpdateDTO appUserUpdateDTO);

    void updateCredentials(String currentUsername, CredentialsUpdateDTO credentialsUpdateDTO);

    Boolean signIn(String username, String password);

}
