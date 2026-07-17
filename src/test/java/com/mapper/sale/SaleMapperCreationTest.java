package com.mapper.sale;

import com.dto.sale.SaleCreationDTO;
import com.mapper.implementation.SaleMapperImpl;
import com.mapper.interfaces.SaleMapper;
import com.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.factory.BarberServiceTestDataFactory.buildValidBarberService;
import static com.factory.ClientTestDataFactory.buildValidClient;
import static com.factory.EmployeeTestDataFactory.buildValidEmployee;
import static com.factory.PaymentMethodTestDataFactory.buildValidPaymentMethod;
import static com.factory.SaleTestDataFactory.buildValidListSaleItems;
import static com.factory.SaleTestDataFactory.buildValidSaleCreationDTO;
import static com.mapper.sale.SaleMapperCreationTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SaleMapperCreationTest {

    private final SaleMapper mapper = new SaleMapperImpl();
    private final Client client = buildValidClient();
    private final Employee employee = buildValidEmployee();
    private final SaleCreationDTO creationDTO = buildValidSaleCreationDTO();
    private final PaymentMethod paymentMethod = buildValidPaymentMethod();
    private final BarberService barberService = buildValidBarberService();
    private List<SaleItem> saleItemList = buildValidListSaleItems();

    @Test
    @DisplayName("Dado un DTO de creación de venta con solo un servicio prestado y sin modificador de precio, el total deberá ser el valor de dicho servicio")
    void givenSaleCreationDTOWithSingleServiceCharged_WhenCreating_ThenTotalIsEqualsToServicePriceAfterPriceModifierModification() {

        setPaymentModifierTypeAsNINGUNO(paymentMethod);

        saleItemList = List.of();

        Sale mappedSale = mapEntity(
                creationDTO,
                mapper,
                client,
                employee,
                barberService,
                paymentMethod,
                saleItemList
        );

        assertEquals(barberService.getPrice(), mappedSale.getTotal());
    }

    @Test
    @DisplayName("Dado un DTO de creación de venta con solo una lista de productos y sin modificador de precio, el total deberá ser igual a la suma de productos")
    void givenSaleCreationDTOWithSingleProductListCharged_WhenCreating_ThenTotalIsEqualsToProductPricesSum() {

        setPaymentModifierTypeAsNINGUNO(paymentMethod);

        Sale mappedSale = mapEntity(
                creationDTO,
                mapper,
                client,
                employee,
                null,
                paymentMethod,
                saleItemList
        );

        assertEquals(calculateItemsTotal(saleItemList), mappedSale.getTotal());
    }

    @Test
    @DisplayName("Dado un DTO de creación de venta con servicio y lista de productos, y sin modificador de precio, el total será igual al precio del servicio y la suma de lalista de productos")
    void givenSaleCreationDTOWithBarberServiceAndProductListCharged_WhenCreating_ThenTotalIsEqualsToSumOfProductPricesAndSaleItems() {

        setPaymentModifierTypeAsNINGUNO(paymentMethod);

        Sale mappedSale = mapEntity(
                creationDTO,
                mapper,
                client,
                employee,
                barberService,
                paymentMethod,
                saleItemList
        );

        assertEquals(calculateBarberServicePriceAndSaleItemsListSum(paymentMethod, saleItemList, barberService), mappedSale.getTotal());
    }

    @Test
    @DisplayName("Dado un DTO de creación con solo un servicio y modificador DESCUENTO, el total será igual al precio del servicio con dicho descuento aplicado")
    void givenSaleCreationDTO_WithOnlyBarberServiceCharged_AndPaymentMethodModifier_DESCUENTO_WhenCreating_ThenTotalIsEqualsToBarberServicePriceMinusPriceModifierValue() {

        setPaymentModifierTypeAsDESCUENTO(paymentMethod);

        saleItemList = List.of();

        Sale mappedSale = mapEntity(
                creationDTO,
                mapper,
                client,
                employee,
                barberService,
                paymentMethod,
                saleItemList
        );

        assertEquals(calculateBarberServicePriceBasedOnPaymentMethodModifier(paymentMethod, barberService), mappedSale.getTotal());
    }

    @Test
    @DisplayName("Dado un DTO de creación con solo productos y modificador DESCUENTO, el total será igual a la suma de productos con dicho descuento aplicado")
    void givenSaleCreationDTO_WithOnlyProducts_AndPaymentMethodModifier_DESCUENTO_WhenCreating_ThenTotalIsEqualsToSaleItemsSumMinusPriceModifierValue() {

        setPaymentModifierTypeAsDESCUENTO(paymentMethod);

        Sale mappedSale = mapEntity(
                creationDTO,
                mapper,
                client,
                employee,
                null,
                paymentMethod,
                saleItemList
        );

        assertEquals(calculateSaleItemsSumBasedOnPaymentMethodModifier(saleItemList, paymentMethod), mappedSale.getTotal());
    }

    @Test
    @DisplayName("Dado un DTO de creación con un servicio, productos y un modificador DESCUENTO, el total será igual a la suma del precio de servicio y productos con el descuento aplicado")
    void givenSaleCreationDTO_WithBarberService_Products_AndPaymentMethodModifier_DESCUENTO_WhenCreating_ThenTotalIsEqualsToSaleItemsAndBarberServicePriceSumMinusPriceModifierValue() {

        setPaymentModifierTypeAsDESCUENTO(paymentMethod);

        Sale mappedSale = mapEntity(
                creationDTO,
                mapper,
                client,
                employee,
                barberService,
                paymentMethod,
                saleItemList
        );

        assertEquals(calculateBarberServicePriceAndSaleItemsListSum(paymentMethod, saleItemList, barberService), mappedSale.getTotal());
    }

    @Test
    @DisplayName("Dado un DTO de creacioń con solo un servicio y un modificador RECARGO, el total será igual al precio del servicio con el recargo aplicado")
    void givenSaleCreationDTO_WithOnlyBarberServiceCharged_AndPaymentMethodModifier_RECARGO_When_Creating_ThenTotalIsEqualsToBarberServicePricePlusPaymentMethodModifierValue() {

        setPaymentModifierTypeAsRECARGO(paymentMethod);
    }
}
