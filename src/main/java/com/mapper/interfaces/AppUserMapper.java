package com.mapper.interfaces;

import com.dto.appuser.AppUserCreationDTO;
import com.dto.appuser.AppUserInfoDTO;
import com.dto.appuser.AppUserUpdateDTO;
import com.model.AppUser;

import java.util.List;

public interface AppUserMapper {

    AppUser mapAppUserCreationDTOtoEntity(AppUserCreationDTO dto);

    void mapAppUserUpdateDTOtoEntity(AppUserUpdateDTO dto, AppUser entity);

    AppUserInfoDTO mapAppUserToInfoDTO(AppUser entity);

    List<AppUserInfoDTO> mapAppUserToInfoDTO(List<AppUser> entityList);
}
