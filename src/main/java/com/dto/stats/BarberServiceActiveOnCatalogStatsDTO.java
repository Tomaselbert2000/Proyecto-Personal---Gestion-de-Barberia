package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class BarberServiceActiveOnCatalogStatsDTO {

    @Builder.Default
    private Long amountOfActiveServices = 0L;
    @Builder.Default
    private Long amountOfDifferentCategories = 0L;
}
