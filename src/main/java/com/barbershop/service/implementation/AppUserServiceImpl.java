package com.barbershop.service.implementation;

import com.barbershop.dto.app_user.AppUserCreationDTO;
import com.barbershop.dto.app_user.AppUserUpdateDTO;
import com.barbershop.model.AppUser;
import com.barbershop.service.interfaces.AppUserService;
import org.springframework.transaction.annotation.Transactional;

public class AppUserServiceImpl implements AppUserService {

    @Override
    @Transactional
    public void createAppUser(AppUserCreationDTO appUserCreationDTO) {

    }

    @Override
    @Transactional
    public void deleteAppUser(Long appUserId) {

    }

    @Override
    public AppUser getAppUserById(Long appUserId) {

        return null;
    }

    @Override
    @Transactional
    public AppUser updateAppUser(Long appUserId, AppUserUpdateDTO appUserUpdateDTO) {

        return null;
    }
}
