package com.mapper.interfaces;

import com.dto.appuser.AppUserCreationDTO;
import com.dto.appuser.AppUserInfoDTO;
import com.dto.appuser.AppUserUpdateDTO;
import com.model.AppUser;

import java.util.List;

public interface AppUserMapper {

    AppUser mapAppUserCreationDTOtoAppUser(AppUserCreationDTO appUserCreationDTO);

    void mapAppUserUpdateDTOtoAppUser(AppUserUpdateDTO updateDTO, AppUser appUserOnDB);

    AppUserInfoDTO mapAppUserToInfoDTO(AppUser appUser);

    List<AppUserInfoDTO> mapAppUserToInfoDTO(List<AppUser> appUserList);
}
