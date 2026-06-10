package com.barbershop.service.interfaces;

import com.barbershop.dto.app_user.AppUserCreationDTO;
import com.barbershop.dto.app_user.AppUserUpdateDTO;
import com.barbershop.model.AppUser;

public interface AppUserService {

    void createAppUser(AppUserCreationDTO appUserCreationDTO);

    void deleteAppUser(Long appUserId);

    AppUser getAppUserById(Long appUserId);

    AppUser updateAppUser(Long appUserId, AppUserUpdateDTO appUserUpdateDTO);
}
