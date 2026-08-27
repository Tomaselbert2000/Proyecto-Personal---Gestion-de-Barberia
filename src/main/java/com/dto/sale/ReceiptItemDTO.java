package com.dto.sale;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptItemDTO {

    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
}
