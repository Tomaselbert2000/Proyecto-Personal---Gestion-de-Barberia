package com.mapper.implementation;

import com.dto.appuser.AppUserCreationDTO;
import com.dto.appuser.AppUserInfoDTO;
import com.dto.appuser.AppUserUpdateDTO;
import com.mapper.helper.MapperHelper;
import com.mapper.interfaces.AppUserMapper;
import com.model.AppUser;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.mapper.helper.MapperHelper.checkIfMapperInputIsNull;

@Component
public class AppUserMapperImpl implements AppUserMapper {

    @Override
    public AppUser mapAppUserCreationDTOtoEntity(AppUserCreationDTO dto) {

        checkIfMapperInputIsNull(dto);

        return AppUser.builder()
                .username(dto.getUsername().trim())
                .password(dto.getPassword().trim())
                .hasAdminRights(dto.getHasAdminRights())
                .build();
    }

    @Override
    public void mapAppUserUpdateDTOtoEntity(AppUserUpdateDTO dto, AppUser entity) {

        checkIfMapperInputIsNull(dto, entity);

        setUpdatedDataOnEntity(entity, dto);
    }

    @Override
    public AppUserInfoDTO mapAppUserToInfoDTO(AppUser entity) {

        checkIfMapperInputIsNull(entity);

        return AppUserInfoDTO.builder()
                .username(entity.getUsername())
                .createdAt(entity.getCreationTimestamp())
                .hasAdminRights(entity.getHasAdminRights())
                .build();
    }

    @Override
    public List<AppUserInfoDTO> mapAppUserToInfoDTO(List<AppUser> entityList) {

        return MapperHelper.mapList(entityList, this::mapAppUserToInfoDTO);
    }

    private void setUpdatedDataOnEntity(AppUser appUserOnDB, AppUserUpdateDTO updateDTO) {

        if (updateDTO.getUsername() != null) appUserOnDB.setUsername(updateDTO.getUsername());
        if (updateDTO.getPassword() != null) appUserOnDB.setPassword(updateDTO.getPassword());
        if (updateDTO.getHasAdminRights() != null) appUserOnDB.setHasAdminRights(updateDTO.getHasAdminRights());

        appUserOnDB.setModifiedDate(LocalDateTime.now());
    }
}
