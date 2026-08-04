package com.validation.product;

import com.dto.product.ProductInputDTO;
import com.exceptions.product.InvalidProductCurrentPriceException;
import com.validation.common.BaseDTOValidator;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class ProductValidator extends BaseDTOValidator {

    public ProductValidator(Validator validatorEngine) {
        super(validatorEngine);
    }

    public <T extends ProductInputDTO> void validateDTO(T dto) {

        super.validateDTO(dto);

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
