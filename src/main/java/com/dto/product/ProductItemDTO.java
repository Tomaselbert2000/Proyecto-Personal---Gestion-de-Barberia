package com.dto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import static com.presentation.constants.ConstraintViolationMessages.MessagePredicates.NOT_NULL;
import static com.presentation.constants.ConstraintViolationMessages.MessagePredicates.POSITIVE;
import static com.presentation.constants.ConstraintViolationMessages.ProductConstraintSubject.PRODUCT_ID;
import static com.presentation.constants.ConstraintViolationMessages.ProductConstraintSubject.PRODUCT_ITEM_QUANTITY;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class ProductItemDTO {

    @NotNull(message = PRODUCT_ID + NOT_NULL)
    private Long productID;

    @NotNull(message = PRODUCT_ITEM_QUANTITY + NOT_NULL)
    @Positive(message = PRODUCT_ITEM_QUANTITY + POSITIVE)
    private Integer quantity;
}
