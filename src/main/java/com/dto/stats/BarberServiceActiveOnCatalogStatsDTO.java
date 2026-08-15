package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class BarberServiceActiveOnCatalogStatsDTO {

    private Long amountOfActiveServices;
    private Long amountOfDifferentCategories;
}
