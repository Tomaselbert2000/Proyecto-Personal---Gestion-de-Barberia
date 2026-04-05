package com.barbershop.dto.sale;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleInfoDTO {

    private LocalDateTime dateAndTime;
    private String clientFirstName;
    private String clientLastName;
    private String barberServiceName;
    private Double total;
    private String paymentMethodName;
}
