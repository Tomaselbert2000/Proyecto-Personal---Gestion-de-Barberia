package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class ClientPhoneNumberStatsDTO {

    @Builder.Default
    private Long clientsWithAtLeastOnePhoneNumber = 0L;
    @Builder.Default
    private Long clientsWithoutPhoneNumber = 0L;
}
