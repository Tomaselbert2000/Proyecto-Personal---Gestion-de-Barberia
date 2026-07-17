package com.factory;

import com.dto.sale.SaleCreationDTO;
import com.model.Sale;
import com.model.SaleItem;

import java.util.List;

import static com.factory.BarberServiceTestDataFactory.buildValidBarberService;
import static com.factory.ClientTestDataFactory.buildValidClient;
import static com.factory.EmployeeTestDataFactory.buildValidEmployee;
import static com.factory.PaymentMethodTestDataFactory.buildValidPaymentMethod;
import static com.factory.ProductTestDataFactory.buildValidProduct;
import static com.test_constant.SaleTestConstants.CreationValidData.*;

public class SaleTestDataFactory {

    private SaleTestDataFactory() {
    }

    public static SaleCreationDTO buildValidSaleCreationDTO() {

        return SaleCreationDTO.builder()
                .dateAndTime(REGISTRATION_TIMESTAMP)
                .clientID(CLIENT_ID)
                .employeeID(EMPLOYEE_ID)
                .paymentMethodID(PAYMENT_METHOD_ID)
                .barberServiceID(BARBER_SERVICE_ID)
                .productsDetail(NON_EMPTY_LIST).
                build();
    }

    public static SaleItem buildValidSaleItem() {

        return SaleItem.builder()
                .id(SALE_ITEM_ID)
                .product(buildValidProduct())
                .quantity(SALE_ITEM_QUANTITY)
                .unitPrice(SALE_ITEM_UNIT_PRICE)
                .sale(ASSOCIATED_SALE)
                .build();
    }

    public static List<SaleItem> buildValidListSaleItems() {

        return List.of(
                buildValidSaleItem(),
                SaleItem.builder()
                        .id(SALE_ITEM_ID + 1)
                        .product(buildValidProduct())
                        .quantity(SALE_ITEM_QUANTITY + 1)
                        .unitPrice(SALE_ITEM_UNIT_PRICE + 5.0)
                        .sale(ASSOCIATED_SALE)
                        .build()
        );
    }

    public static Sale buildValidSale() {

        return Sale.builder()
                .saleID(SALE_ID)
                .dateAndTime(REGISTRATION_TIMESTAMP)
                .client(buildValidClient())
                .barberService(buildValidBarberService())
                .employee(buildValidEmployee())
                .items(List.of(buildValidSaleItem()))
                .paymentMethodUsed(buildValidPaymentMethod())
                .build();
    }
}
