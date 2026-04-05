package com.barbershop.validation.product;

import com.barbershop.dto.product.ProductDTOCommonMethods;
import com.barbershop.exceptions.product.*;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.barbershop.validation.common.CommonValidationFunctions.checkIfDtoIsNull;
import static com.barbershop.validation.common.CommonValidationFunctions.validateAnnotationConstraints;

@Component
@RequiredArgsConstructor
public class ProductValidator {

    private final Validator validatorEngine;

    public <T extends ProductDTOCommonMethods> void validateDTO(T dto) {

        checkIfDtoIsNull(dto);

        validateAnnotationConstraints(validatorEngine, dto);

        validatePriceCostLogic(
                dto.getProductCost(),
                dto.getCurrentPrice()
        );
    }

    private void validatePriceCostLogic(
            Double productCost,
            Double currentPrice
    ) {

        if (productCost != null) {

            if (currentPrice != null) {

                if (currentPrice <= productCost) throw new InvalidProductCurrentPriceException();
            }
        }
    }
}
