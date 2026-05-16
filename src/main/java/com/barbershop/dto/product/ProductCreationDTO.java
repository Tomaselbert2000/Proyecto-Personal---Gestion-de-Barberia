package com.barbershop.dto.product;

import com.barbershop.enums.ProductCategory;
import com.barbershop.enums.ProductPresentationUnit;
import jakarta.validation.constraints.*;
import lombok.*;

import static com.barbershop.launcher.constants.entity_constraints.ConstraintViolationMessagePredicate.*;
import static com.barbershop.launcher.constants.entity_constraints.ProductConstraintViolationMessageSubject.*;
import static com.barbershop.utils.strings.RegexPatterns.NAME_REGEX;
import static com.barbershop.validation.common.CommonConstants.*;
import static com.barbershop.validation.product.ProductValidatorConstants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreationDTO implements ProductDTOCommonMethods {

    @NotBlank(message = PRODUCT_NAME + NOT_BLANK)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = PRODUCT_NAME + INVALID_NAME_SIZE)
    @Pattern(regexp = NAME_REGEX, message = PRODUCT_NAME + DOES_NOT_MATCH_REGEX)
    private String name;

    @Size(max = MAX_OPTIONAL_DESCRIPTION_LENGTH, message = OPTIONAL_DESCRIPTION + OPTIONAL_TEXT_MAX_SIZE)
    private String optionalDescription;

    @NotBlank(message = BRAND_NAME + NOT_BLANK)
    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = BRAND_NAME + INVALID_NAME_SIZE)
    @Pattern(regexp = NAME_REGEX, message = BRAND_NAME + DOES_NOT_MATCH_REGEX)
    private String brandName;

    @NotNull(message = PRESENTATION_UNIT + NOT_NULL)
    private ProductPresentationUnit presentationUnit;

    @NotNull(message = PRESENTATION_SIZE + NOT_NULL)
    @Positive(message = PRESENTATION_SIZE + POSITIVE)
    private Integer presentationSize;

    @NotNull(message = PRODUCT_COST + NOT_NULL)
    @Positive(message = PRODUCT_COST + POSITIVE)
    private Double productCost;

    @NotNull(message = PRODUCT_MIN_PRICE + NOT_NULL)
    @Positive(message = PRODUCT_MIN_PRICE + POSITIVE)
    private Double minPrice;

    @NotNull(message = PRODUCT_CURRENT_PRICE + NOT_NULL)
    @Positive(message = PRODUCT_CURRENT_PRICE + POSITIVE)
    private Double currentPrice;

    @NotNull(message = PRODUCT_WHOLE_SALE_PRICE + NOT_NULL)
    @Positive(message = PRODUCT_WHOLE_SALE_PRICE + POSITIVE)
    private Double productWholeSalePrice;

    @NotNull(message = PRODUCT_DISCOUNT_VALUE + NOT_NULL)
    @DecimalMin(value = MIN_DISCOUNT_VALUE, message = PRODUCT_DISCOUNT_VALUE + HIGHER_OR_EQUALS_THAN_ZERO)
    @DecimalMax(value = MAX_DISCOUNT_VALUE, message = PRODUCT_DISCOUNT_VALUE + LOWER_OR_EQUALS_THAN_ONE)
    private Double maxDiscountPercentage;

    @NotNull(message = PRODUCT_CATEGORY + NOT_NULL)
    private ProductCategory category;

    @NotNull(message = PRODUCT_CURRENT_STOCK_LEVEL + NOT_NULL)
    @PositiveOrZero(message = PRODUCT_CURRENT_STOCK_LEVEL + HIGHER_OR_EQUALS_THAN_ZERO)
    private Integer currentStockLevel;

    @NotNull(message = PRODUCT_SAFETY_STOCK_LEVEL + NOT_NULL)
    @PositiveOrZero(message = PRODUCT_SAFETY_STOCK_LEVEL + HIGHER_OR_EQUALS_THAN_ZERO)
    private Integer safetyStockLevel;

    @NotNull(message = IMAGE_FILE_PATH + NOT_NULL)
    private String imageFilePath;
}
