package com.test_constant;

import com.enums.ProductCategory;
import com.enums.ProductPresentationUnit;

import java.time.LocalDateTime;

import static com.service.helper.ServiceTestVerificationHelper.addTabs;
import static com.service.helper.ServiceTestVerificationHelper.returnAsLowercase;
import static com.test_constant.ProductTestConstants.CreationValidData.*;

public final class ProductTestConstants {

    private ProductTestConstants() {
    }

    public static final class CreationValidData {

        public static final Long PRODUCT_ID = 1L;
        public static final LocalDateTime PRODUCT_CREATION_DATE = LocalDateTime.of(2026, 1, 1, 12, 30);
        public static final String PRODUCT_NAME = "Cera para modelar";
        public static final String PRODUCT_OPTIONAL_DESCRIPTION = "Cera para modelado de cabello, terminación mate";
        public static final String PRODUCT_BRAND_NAME = "Marca de cera";
        public static final Integer PRODUCT_PRESENTATION_SIZE = 150;
        public static final ProductPresentationUnit PRODUCT_PRESENTATION_UNIT = ProductPresentationUnit.GRAMOS;
        public static final Double PRODUCT_COST = 2500.0;
        public static final Double PRODUCT_MIN_PRICE = 2750.0;
        public static final Double PRODUCT_CURRENT_PRICE = 2950.0;
        public static final Double PRODUCT_WHOLE_SALE_PRICE = 2680.0;
        public static final Double PRODUCT_MAX_DISCOUNT_PERCENTAGE_VALUE = 0.4;
        public static final ProductCategory PRODUCT_CATEGORY = ProductCategory.CERA;
        public static final Integer PRODUCT_CURRENT_STOCK_LEVEL = 150;
        public static final Integer PRODUCT_SAFETY_STOCK_LEVEL = 40;
        public static final String PRODUCT_FILE_PATH = "/image/test_image.jpg";
    }

    public static final class UpdateValidData {

        public static final String NEW_PRODUCT_NAME = "Cera para modelar mate premium";
        public static final String NEW_OPTIONAL_DESCRIPTION = "Cera para modelar con terminación Premium, aroma Cherry";
        public static final String NEW_PRODUCT_BRAND_NAME = "American Crew";
        public static final Integer NEW_PRODUCT_PRESENTATION_SIZE = 125;
        public static final ProductPresentationUnit NEW_PRODUCT_PRESENTATION_UNIT = ProductPresentationUnit.KILOGRAMOS;
        public static final Double NEW_PRODUCT_COST = 2800.0;
        public static final Double NEW_PRODUCT_MIN_PRICE = 2880.5;
        public static final Double NEW_PRODUCT_CURRENT_PRICE = 3200.0;
        public static final Double NEW_PRODUCT_WHOLE_SALE_PRICE = 3050.5;
        public static final Double NEW_PRODUCT_MAX_DISCOUNT_PERCENTAGE_VALUE = 0.36;
        public static final ProductCategory NEW_PRODUCT_CATEGORY = ProductCategory.CERA;
        public static final Integer NEW_PRODUCT_CURRENT_STOCK_LEVEL = 175;
        public static final Integer NEW_PRODUCT_SAFETY_STOCK_LEVEL = 55;
        public static final String NEW_PRODUCT_FILE_PATH = "/image/test_image.jpg";
    }

    public static final class MapperData {

        public static final String PRODUCT_NAME_WITH_SPACES = addTabs(PRODUCT_NAME);
        public static final String PRODUCT_BRAND_NAME_WITH_SPACES = addTabs(PRODUCT_BRAND_NAME);
        public static final String PRODUCT_OPTIONAL_DESCRIPTION_WITH_SPACES = addTabs(PRODUCT_OPTIONAL_DESCRIPTION);

        public static final String LOWERCASE_PRODUCT_NAME = returnAsLowercase(PRODUCT_NAME);
        public static final String LOWERCASE_BRAND_NAME = returnAsLowercase(PRODUCT_BRAND_NAME);
    }

    public static final class InvalidData {

        public static final String PRODUCT_INVALID_NAME = "Sh@mp00 paR@ pŧlo";
        public static final String PRODUCT_INVALID_BRAND_NAME = "M@rca n0 valida";
        public static final Integer PRODUCT_INVALID_SIZE = -100;
        public static final String PRODUCT_OPTIONAL_DESCRIPTION_TOO_LONG = "QnqLqvZhrqZNFMRMKzKBRaxBxfdTTwMyqTVydegVQXJJaMTKSfVqCPZGeGczDZepktnBKcgptvqJxtfmHhjmmcEBFuWTHHQCAyhiJKZEDRaKdFncbYMBeBjyLchMyHRmapZMZcgmHZTybewtiebHmZTUDqtifjyeQuDqMCxQLWHZkTKnQeCZTgYNZMauCyCmdeYyaWtnyRmDqRHrSYNBvkEKydvANJwMjTEcvCxGEEEaMZwqWikkGkHqhDXLUxikumwNCvCnnZtkFSFEznDLPVPH";
        public static final String PRODUCT_NAME_TOO_SHORT = "abc";
        public static final String PRODUCT_NAME_TOO_LONG = "FUMzLuBzrUtcycKBpbjdyCcAaNRUZYhnwGqguTyHidnDShRPTKgfcQbHvHjUNWAzHGvvaxcbDWaTLvZGidmXAABudxxcxRBFeGkLdkPPpTrVUvgpFnxrFnGj";
        public static final Double PRODUCT_NEGATIVE_COST = -2500.0;
        public static final Double PRODUCT_NEGATIVE_PRICE = -3250.0;
        public static final Double PRODUCT_DISCOUNT_PERCENTAGE_LOWER_THAN_ZERO = -0.5;
        public static final Double PRODUCT_DISCOUNT_PERCENTAGE_HIGHER_THAN_ONE = 1.5;
    }
}
