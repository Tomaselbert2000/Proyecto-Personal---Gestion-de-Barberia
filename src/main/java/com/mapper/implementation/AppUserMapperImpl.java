package com.mapper.implementation;

import com.dto.app_user.AppUserCreationDTO;
import com.dto.app_user.AppUserInfoDTO;
import com.dto.app_user.AppUserUpdateDTO;
import com.exceptions.common.NullMapperInputException;
import com.mapper.interfaces.AppUserMapper;
import com.model.AppUser;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppUserMapperImpl implements AppUserMapper {

    @Override
    public AppUser mapAppUserCreationDTOtoAppUser(AppUserCreationDTO appUserCreationDTO) {

        if (appUserCreationDTO == null) throw new NullMapperInputException();

        return AppUser.builder()
                .username(appUserCreationDTO.getUsername().trim())
                .password(appUserCreationDTO.getPassword().trim())
                .hasAdminRights(appUserCreationDTO.getHasAdminRights())
                .build();
    }

    @Override
    public void mapAppUserUpdateDTOtoAppUser(AppUserUpdateDTO updateDTO, AppUser appUserOnDB) {

        if (updateDTO == null || appUserOnDB == null) throw new NullMapperInputException();

        setUpdatedDataOnEntity(appUserOnDB, updateDTO);

    }

    @Override
    public AppUserInfoDTO mapAppUserToInfoDTO(AppUser appUser) {

        if (appUser == null) throw new NullMapperInputException();

        return AppUserInfoDTO.builder()
                .username(appUser.getUsername())
                .createdAt(appUser.getCreationTimestamp())
                .hasAdminRights(appUser.getHasAdminRights())
                .build();
    }

    @Override
    public List<AppUserInfoDTO> mapAppUserToInfoDTO(List<AppUser> appUserList) {

        return appUserList.stream().map(this::mapAppUserToInfoDTO).collect(Collectors.toList());
    }

    private void setUpdatedDataOnEntity(AppUser appUserOnDB, AppUserUpdateDTO updateDTO) {

        if (updateDTO.getUsername() != null) appUserOnDB.setUsername(updateDTO.getUsername());
        if (updateDTO.getPassword() != null) appUserOnDB.setPassword(updateDTO.getPassword());
        if (updateDTO.getHasAdminRights() != null) appUserOnDB.setHasAdminRights(updateDTO.getHasAdminRights());
        appUserOnDB.setModifiedDate(LocalDateTime.now());
    }
}
