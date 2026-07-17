package com.dto.product;

import com.enums.ProductCategory;
import com.enums.ProductPresentationUnit;
import jakarta.validation.constraints.*;
import lombok.*;

import static com.launcher.constants.entity_constraints.predicate.ConstraintViolationMessagePredicate.*;
import static com.launcher.constants.entity_constraints.subject.ProductConstraintViolationMessageSubject.*;
import static com.utils.strings.RegexPatterns.NAME_REGEX;
import static com.validation.common.CommonConstants.*;
import static com.validation.product.ProductValidatorConstants.MAX_DISCOUNT_VALUE;
import static com.validation.product.ProductValidatorConstants.MIN_DISCOUNT_VALUE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateDTO implements ProductDTOCommonMethods {

    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = PRODUCT_NAME + INVALID_NAME_SIZE)
    @Pattern(regexp = NAME_REGEX, message = PRODUCT_NAME + DOES_NOT_MATCH_NAME_REGEX)
    private String name;

    @Size(max = MAX_OPTIONAL_DESCRIPTION_LENGTH, message = OPTIONAL_DESCRIPTION + OPTIONAL_TEXT_OR_DESCRIPTION_MAX_SIZE)
    private String optionalDescription;

    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = BRAND_NAME + INVALID_NAME_SIZE)
    @Pattern(regexp = NAME_REGEX, message = BRAND_NAME + DOES_NOT_MATCH_NAME_REGEX)
    private String brandName;

    private ProductPresentationUnit presentationUnit;

    @Positive(message = PRESENTATION_SIZE + POSITIVE)
    private Integer presentationSize;

    @Positive(message = PRODUCT_COST + POSITIVE)
    private Double productCost;

    @Positive(message = PRODUCT_MIN_PRICE + POSITIVE)
    private Double minPrice;

    @Positive(message = PRODUCT_CURRENT_PRICE + POSITIVE)
    private Double currentPrice;

    @Positive(message = PRODUCT_WHOLE_SALE_PRICE + POSITIVE)
    private Double productWholeSalePrice;

    @PositiveOrZero(message = PRODUCT_DISCOUNT_VALUE + HIGHER_OR_EQUALS_THAN_ZERO)
    @DecimalMin(value = MIN_DISCOUNT_VALUE)
    @DecimalMax(value = MAX_DISCOUNT_VALUE)
    private Double maxDiscountPercentage;

    private ProductCategory category;

    @PositiveOrZero(message = PRODUCT_CURRENT_STOCK_LEVEL + HIGHER_OR_EQUALS_THAN_ZERO)
    private Integer currentStockLevel;

    @PositiveOrZero(message = PRODUCT_SAFETY_STOCK_LEVEL + HIGHER_OR_EQUALS_THAN_ZERO)
    private Integer safetyStockLevel;

    private String imageFilePath;
}
