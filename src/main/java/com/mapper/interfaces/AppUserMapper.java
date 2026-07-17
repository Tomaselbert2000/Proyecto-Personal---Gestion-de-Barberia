package com.mapper.interfaces;

import com.dto.app_user.AppUserCreationDTO;
import com.dto.app_user.AppUserInfoDTO;
import com.dto.app_user.AppUserUpdateDTO;
import com.model.AppUser;

import java.util.List;

public interface AppUserMapper {

    AppUser mapAppUserCreationDTOtoAppUser(AppUserCreationDTO appUserCreationDTO);

    void mapAppUserUpdateDTOtoAppUser(AppUserUpdateDTO updateDTO, AppUser appUserOnDB);

    AppUserInfoDTO mapAppUserToInfoDTO(AppUser appUser);

    List<AppUserInfoDTO> mapAppUserToInfoDTO(List<AppUser> appUserList);
}
