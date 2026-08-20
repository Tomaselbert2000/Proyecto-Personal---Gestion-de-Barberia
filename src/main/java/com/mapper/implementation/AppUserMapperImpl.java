package com.mapper.implementation;

import com.dto.appuser.AppUserCreationDTO;
import com.dto.appuser.AppUserInfoDTO;
import com.dto.appuser.AppUserUpdateDTO;
import com.exceptions.common.NullMapperInputException;
import com.mapper.interfaces.AppUserMapper;
import com.model.AppUser;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mapper.helper.MapperHelper.checkIfMapperInputIsNull;

@Component
public class AppUserMapperImpl implements AppUserMapper {

    @Override
    public AppUser mapAppUserCreationDTOtoAppUser(AppUserCreationDTO dto) {

        checkIfMapperInputIsNull(dto);

        return AppUser.builder()
                .username(dto.getUsername().trim())
                .password(dto.getPassword().trim())
                .hasAdminRights(dto.getHasAdminRights())
                .build();
    }

    @Override
    public void mapAppUserUpdateDTOtoAppUser(AppUserUpdateDTO updateDTO, AppUser appUserOnDB) {

        checkIfMapperInputIsNull(updateDTO, appUserOnDB);

        setUpdatedDataOnEntity(appUserOnDB, updateDTO);
    }

    @Override
    public AppUserInfoDTO mapAppUserToInfoDTO(AppUser appUser) {

        checkIfMapperInputIsNull(appUser);

        return AppUserInfoDTO.builder()
                .username(appUser.getUsername())
                .createdAt(appUser.getCreationTimestamp())
                .hasAdminRights(appUser.getHasAdminRights())
                .build();
    }

    @Override
    public List<AppUserInfoDTO> mapAppUserToInfoDTO(List<AppUser> appUserList) {

        checkIfMapperInputIsNull(appUserList);

        return appUserList.stream().map(this::mapAppUserToInfoDTO).collect(Collectors.toList());
    }

    private void setUpdatedDataOnEntity(AppUser appUserOnDB, AppUserUpdateDTO updateDTO) {

        checkIfMapperInputIsNull(appUserOnDB, updateDTO);

        if (updateDTO.getUsername() != null) appUserOnDB.setUsername(updateDTO.getUsername());
        if (updateDTO.getPassword() != null) appUserOnDB.setPassword(updateDTO.getPassword());
        if (updateDTO.getHasAdminRights() != null) appUserOnDB.setHasAdminRights(updateDTO.getHasAdminRights());

        appUserOnDB.setModifiedDate(LocalDateTime.now());
    }
}
