package com.barbershop.dto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductItemDTO {

    @NotNull
    private Long productID;

    @NotNull
    @Positive
    private Integer quantity;
}
