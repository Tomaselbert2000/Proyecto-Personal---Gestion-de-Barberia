package com.dto.sale;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class SaleInfoDTO {

    private LocalDateTime dateAndTime;
    private String clientFirstName;
    private String clientLastName;
    private String barberServiceName;
    private Double total;
    private String paymentMethodName;
}
