package com.dto.stats;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AverageSaleTicketStatDTO {

    public AverageSaleTicketStatDTO(Double averageTicket, Long extraSoldUnits) {

        this.averageTicket = BigDecimal.valueOf(averageTicket);
        this.extraSoldUnits = extraSoldUnits;
    }

    @Builder.Default
    private BigDecimal averageTicket = BigDecimal.valueOf(0.0);

    @Builder.Default
    private Long extraSoldUnits = 0L;
}
