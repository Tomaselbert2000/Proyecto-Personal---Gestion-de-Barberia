package com.validation.dataset;

import com.barbershop.dto.product.ProductItemDTO;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static com.barbershop.validation.common.CommonValidationFunctions.generateClockInstance;

public class SaleTestDataset {

    public static final Instant INSTANT = Instant.parse("2026-01-01T15:00:00Z");
    public static final ZoneId ZONE_ID = ZoneId.of("America/Argentina/Buenos_Aires");

    private static final Clock clock = generateClockInstance(INSTANT, ZONE_ID);

    public static final Long CLIENT_ID = 1L;
    public static final Long EMPLOYEE_ID = 999L;
    public static final Long PAYMENT_METHOD_ID = 10L;
    public static final Long BARBER_SERVICE_ID = 100L;
    public static final LocalDateTime REGISTRATION_TIMESTAMP = LocalDateTime.of(2026, 1, 1, 11, 45);

    public static final LocalDateTime REGISTRATION_TIMESTAMP_IN_TIME_RANGE_LIMIT = LocalDateTime.now(clock).minusHours(23);
    public static final LocalDateTime REGISTRATION_TIMESTAMP_OUTSIDE_RANGE_LIMIT = LocalDateTime.now(clock).minusHours(25);
    public static final LocalDateTime INVALID_REGISTRATION_TIMESTAMP = LocalDateTime.now(clock).plusDays(1);

    public static final List<ProductItemDTO> NON_EMPTY_LIST = List.of(new ProductItemDTO(1L, 6));
    public static final List<ProductItemDTO> EMPTY_LIST = List.of();

    public static final ProductItemDTO PRODUCT_WITH_NULL_ID = new ProductItemDTO(null, 5);
    public static final ProductItemDTO PRODUCT_WITH_NULL_QUANTITY = new ProductItemDTO(1L, null);
    public static final ProductItemDTO PRODUCT_WITH_QUANTITY_LOWER_THAN_ONE = new ProductItemDTO(1L, -5);

    public static final List<ProductItemDTO> LIST_WITH_NULL_PRODUCT = List.of(PRODUCT_WITH_NULL_ID);
    public static final List<ProductItemDTO> LIST_WITH_NULL_QUANTITY = List.of(PRODUCT_WITH_NULL_QUANTITY);
    public static final List<ProductItemDTO> LIST_WITH_NEGATIVE_QUANTITY = List.of(PRODUCT_WITH_QUANTITY_LOWER_THAN_ONE);
}
