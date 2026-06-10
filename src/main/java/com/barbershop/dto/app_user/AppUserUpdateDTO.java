package com.barbershop.dto.app_user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserUpdateDTO {

    private Long appUserId;
    private String name;
    private String password;
}
