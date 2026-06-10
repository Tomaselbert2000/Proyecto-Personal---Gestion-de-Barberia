package com.barbershop.dto.app_user;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserInfoDTO {

    private String username;
    private LocalDateTime createdAt;
    private Boolean hasAdminRights;
}
