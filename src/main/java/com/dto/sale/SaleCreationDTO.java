package com.dto.sale;

import com.dto.product.ProductItemDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleCreationDTO {

    @NotNull
    private LocalDateTime dateAndTime;

    @NotNull
    private Long clientID;

    private Long employeeID;

    @NotNull
    private Long paymentMethodID;

    private Long barberServiceID;

    @Valid
    @Builder.Default
    private List<ProductItemDTO> productsDetail = new ArrayList<>();
}