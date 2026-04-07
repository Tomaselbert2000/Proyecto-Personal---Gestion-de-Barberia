package com.barbershop.dto.product;

import com.barbershop.enums.ProductCategory;
import com.barbershop.enums.ProductPresentationUnit;
import jakarta.validation.constraints.*;
import lombok.*;

import static com.barbershop.launcher.constants.entity_constraints.GenericConstraintViolationConstants.*;
import static com.barbershop.launcher.constants.entity_constraints.ProductConstraintViolationMessageStarters.*;
import static com.barbershop.utils.strings.RegexPatterns.NAME_REGEX;
import static com.barbershop.validation.common.CommonConstants.*;
import static com.barbershop.validation.product.ProductValidatorConstants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateDTO implements ProductDTOCommonMethods{

    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    @Pattern(regexp = NAME_REGEX)
    private String name;

    @Size(max = MAX_OPTIONAL_DESCRIPTION_LENGTH)
    private String optionalDescription;

    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
    @Pattern(regexp = NAME_REGEX)
    private String brandName;

    private ProductPresentationUnit presentationUnit;

    @Positive
    private Integer presentationSize;

    @Positive
    private Double productCost;

    @Positive
    private Double minPrice;

    @Positive
    private Double currentPrice;

    @Positive
    private Double productWholeSalePrice;

    @PositiveOrZero
    @DecimalMin(value = MIN_DISCOUNT_VALUE)
    @DecimalMax(value = MAX_DISCOUNT_VALUE)
    private Double maxDiscountPercentage;

    private ProductCategory category;

    @PositiveOrZero
    private Integer currentStockLevel;

    @PositiveOrZero
    private Integer safetyStockLevel;

    private String imageFilePath;
}
