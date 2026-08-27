package com.dto.sale;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class SaleInfoDTO {

    private Long saleID;
    private LocalDateTime dateAndTime;
    private String clientFirstName;
    private String clientLastName;
    private String employeeFirstName;
    private String employeeLastName;
    private String barberServiceName;
    private List<ReceiptItemDTO> receiptItems;
    private Double total;
    private String paymentMethodName;
}
