package com.barbershop.mapper.implementation;

import com.barbershop.dto.app_user.AppUserCreationDTO;
import com.barbershop.dto.app_user.AppUserInfoDTO;
import com.barbershop.dto.app_user.AppUserUpdateDTO;
import com.barbershop.mapper.interfaces.AppUserMapper;
import com.barbershop.model.AppUser;

import java.util.List;

public class AppUserMapperImpl implements AppUserMapper {

    @Override
    public AppUser mapAppUserCreationDTOtoAppUser(AppUserCreationDTO appUserCreationDTO) {

        return null;
    }

    @Override
    public AppUser mapAppUserUpdateDTOtoAppUser(AppUserUpdateDTO updateDTO, AppUser appUserOnDB) {

        return null;
    }

    @Override
    public AppUserInfoDTO mapAppUserToInfoDTO(AppUser appUser) {

        return null;
    }

    @Override
    public List<AppUserInfoDTO> mapAppUserToInfoDTO(List<AppUser> appUserList) {

        return List.of();
    }
}
