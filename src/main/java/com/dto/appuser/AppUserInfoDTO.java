package com.dto.appuser;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class AppUserInfoDTO {

    private String username;
    private LocalDateTime createdAt;
    private Boolean hasAdminRights;
}
