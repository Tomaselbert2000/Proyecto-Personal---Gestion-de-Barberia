package com.dto.sale;

import com.dto.product.ProductItemDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.presentation.constants.ConstraintViolationMessages.MessagePredicates.NOT_NULL;
import static com.presentation.constants.ConstraintViolationMessages.SaleConstraintSubject.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class SaleCreationDTO {

    private LocalDateTime dateAndTime;

    @NotNull(message = SALE_CLIENT_ID + NOT_NULL)
    private Long clientID;

    @NotNull(message = SALE_EMPLOYEE_ID + NOT_NULL)
    private Long employeeID;

    @NotNull(message = SALE_PAYMENT_METHOD + NOT_NULL)
    private Long paymentMethodID;

    private Long barberServiceID;

    @Valid
    @Builder.Default
    private List<ProductItemDTO> productsDetail = new ArrayList<>();
}