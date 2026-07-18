package com.service.interfaces;

import com.dto.app_user.AppUserCreationDTO;
import com.dto.app_user.AppUserInfoDTO;
import com.dto.app_user.AppUserUpdateDTO;

public interface AppUserService {

    void createAppUser(AppUserCreationDTO appUserCreationDTO);

    void deleteAppUser(Long appUserId);

    AppUserInfoDTO getAppUserById(Long appUserId);

    void updateAppUser(Long appUserId, AppUserUpdateDTO appUserUpdateDTO);

    Boolean signIn(String username, String password);

}
