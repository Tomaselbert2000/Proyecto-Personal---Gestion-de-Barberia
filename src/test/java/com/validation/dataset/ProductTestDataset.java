package com.validation.dataset;

import com.barbershop.enums.ProductCategory;
import com.barbershop.enums.ProductPresentationUnit;

public class ProductTestDataset {

    public static final String NAME = "Cera para modelar";
    public static final String OPTIONAL_DESCRIPTION = "Cera para modelado de cabello, terminación mate";
    public static final String BRAND_NAME = "Marca de cera";
    public static final Integer PRESENTATION_SIZE = 150;
    public static final ProductPresentationUnit UNIT = ProductPresentationUnit.GRAMOS;
    public static final Double COST = 2500.0;
    public static final Double MIN_PRICE = 2750.0;
    public static final Double CURRENT_PRICE = 2950.0;
    public static final Double WHOLE_SALE_PRICE = 2680.0;
    public static final Double MAX_DISCOUNT_PERCENTAGE_VALUE = 0.4;
    public static final ProductCategory CATEGORY = ProductCategory.CERA;
    public static final Integer CURRENT_STOCK_LEVEL = 150;
    public static final Integer SAFETY_STOCK_LEVEL = 40;
    public static final String FILE_PATH = "/image/test_image.jpg";

    public static final String INVALID_NAME = "Sh@mp00 paR@ pŧlo";
    public static final String INVALID_BRAND_NAME = "M@rca n0 valida";
    public static final Integer INVALID_SIZE = -100;
    public static final String OPTIONAL_DESCRIPTION_TOO_LONG = "QnqLqvZhrqZNFMRMKzKBRaxBxfdTTwMyqTVydegVQXJJaMTKSfVqCPZGeGczDZepktnBKcgptvqJxtfmHhjmmcEBFuWTHHQCAyhiJKZEDRaKdFncbYMBeBjyLchMyHRmapZMZcgmHZTybewtiebHmZTUDqtifjyeQuDqMCxQLWHZkTKnQeCZTgYNZMauCyCmdeYyaWtnyRmDqRHrSYNBvkEKydvANJwMjTEcvCxGEEEaMZwqWikkGkHqhDXLUxikumwNCvCnnZtkFSFEznDLPVPH";
    public static final String NAME_TOO_SHORT = "abc";
    public static final String NAME_TOO_LONG = "FUMzLuBzrUtcycKBpbjdyCcAaNRUZYhnwGqguTyHidnDShRPTKgfcQbHvHjUNWAzHGvvaxcbDWaTLvZGidmXAABudxxcxRBFeGkLdkPPpTrVUvgpFnxrFnGj";
    public static final Double NEGATIVE_COST = -2500.0;
    public static final Double NEGATIVE_PRICE = -3250.0;
    public static final Double DISCOUNT_PERCENTAGE_LOWER_THAN_ZERO = -0.5;
    public static final Double DISCOUNT_PERCENTAGE_HIGHER_THAN_ONE = 1.5;
}
