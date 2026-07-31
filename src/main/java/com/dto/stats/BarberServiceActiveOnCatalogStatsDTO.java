package com.dto.stats;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarberServiceActiveOnCatalogStatsDTO {

    private Long amountOfActiveServices;
    private Long amountOfDifferentCategories;
}
