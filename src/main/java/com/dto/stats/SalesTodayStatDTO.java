package com.dto.stats;

import lombok.*;

import static com.presentation.constants.StringResource.DisplayString.NO_DATA;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesTodayStatDTO {

    @Builder.Default
    private Long salesRegisteredToday = 0L;

    @Builder.Default
    private String mostPopularBarberService = NO_DATA;
}
