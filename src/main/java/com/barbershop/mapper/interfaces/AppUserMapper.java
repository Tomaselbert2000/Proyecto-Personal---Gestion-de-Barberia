package com.barbershop.mapper.interfaces;

import com.barbershop.dto.app_user.AppUserCreationDTO;
import com.barbershop.dto.app_user.AppUserInfoDTO;
import com.barbershop.dto.app_user.AppUserUpdateDTO;
import com.barbershop.model.AppUser;

import java.util.List;

public interface AppUserMapper {

    AppUser mapAppUserCreationDTOtoAppUser(AppUserCreationDTO appUserCreationDTO);

    AppUser mapAppUserUpdateDTOtoAppUser(AppUserUpdateDTO updateDTO, AppUser appUserOnDB);

    AppUserInfoDTO mapAppUserToInfoDTO(AppUser appUser);

    List<AppUserInfoDTO> mapAppUserToInfoDTO(List<AppUser> appUserList);
}
