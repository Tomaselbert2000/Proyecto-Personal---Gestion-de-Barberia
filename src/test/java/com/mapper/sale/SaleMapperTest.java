package com.mapper.sale;

import com.barbershop.dto.sale.SaleCreationDTO;
import com.barbershop.dto.sale.SaleInfoDTO;
import com.barbershop.enums.BarberServiceCategory;
import com.barbershop.enums.PaymentMethodModifierType;
import com.barbershop.mapper.implementation.SaleMapperImpl;
import com.barbershop.mapper.interfaces.SaleMapper;
import com.barbershop.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SaleMapperTest {

    private final SaleMapper mapper = new SaleMapperImpl();

    private Client client;
    private BarberService barberService;
    private PaymentMethod paymentMethod;
    private Employee employee;

    private SaleCreationDTO creationDTO;

    private Sale sale;

    private static final Long CLIENT_ID = 1L;
    private static final Long BARBER_SERVICE_ID = 2L;
    private static final Long PAYMENT_METHOD_ID = 3L;
    private static final Long EMPLOYEE_ID = 4L;

    @BeforeEach
    void init() {

        client = Client.builder()
                .clientID(CLIENT_ID)
                .nationalIdentityCardNumber("1234567")
                .firstName("Tomas Gabriel")
                .lastName("Elbert")
                .registrationDate(LocalDate.of(2026, 1, 1))
                .email("tomas@gmail.com")
                .phoneNumbersList(List.of("1122334455"))
                .build();

        employee = Employee.builder()
                .employeeID(EMPLOYEE_ID)
                .firstName("Ramiro Hernan")
                .lastName("Ardiles")
                .hireDate(LocalDate.of(2026, 1, 1))
                .isActive(true)
                .commissionPercentage(0.70)
                .build();

        barberService = BarberService.builder()
                .barbershopServiceID(BARBER_SERVICE_ID)
                .name("Corte de pelo americano")
                .price(2000.0)
                .serviceCategory(BarberServiceCategory.CORTE)
                .build();

        paymentMethod = PaymentMethod.builder()
                .paymentMethodID(PAYMENT_METHOD_ID)
                .name("Pago en efectivo")
                .description("")
                .isActive(true)
                .createdAt(LocalDate.of(2026, 1, 1))
                .modifierType(PaymentMethodModifierType.NINGUNO)
                .priceModifier(0.05)
                .build();

        creationDTO = SaleCreationDTO.builder()
                .dateAndTime(LocalDateTime.of(2026, 1, 1, 12, 0))
                .build();
    }

    @Test
    void givenNewSaleWithBarberServiceAndProductList_ThenIsSuccesfullyMapped() {

        SaleItem item1 = SaleItem.builder()
                .quantity(2)
                .unitPrice(500.0)
                .build();

        List<SaleItem> saleItemList = List.of(item1);

        sale = mapper.mapSaleCreationDtoToSale(creationDTO, client, employee, barberService, paymentMethod, saleItemList);

        assertNotNull(sale);
        assertEquals(3000.0, sale.getTotal());
        assertEquals("Pago en efectivo", sale.getPaymentMethodUsed().getName());
        assertEquals("Tomas Gabriel", sale.getClient().getFirstName());
        assertEquals(sale, sale.getItems().getFirst().getSale());
        assertNotNull(sale.getItems().getFirst().getSale());
    }

    @Test
    void givenNewSaleWithOnlyProductList_ThenIsSuccesfullyMapped() {

        SaleItem item1 = SaleItem.builder()
                .quantity(2)
                .unitPrice(500.0)
                .build();

        List<SaleItem> saleItemList = List.of(item1);

        sale = mapper.mapSaleCreationDtoToSale(creationDTO, client, employee, null, paymentMethod, saleItemList);

        assertNotNull(sale);
        assertEquals(1000.0, sale.getTotal());
        assertEquals("Pago en efectivo", sale.getPaymentMethodUsed().getName());
        assertEquals("Tomas Gabriel", sale.getClient().getFirstName());
        assertEquals(sale, sale.getItems().getFirst().getSale());
        assertNotNull(sale.getItems().getFirst().getSale());
    }

    @Test
    void givenNewSaleWithOnlyBarberService_ThenIsSuccesfullyMapped() {

        List<SaleItem> emptyList = List.of();

        sale = mapper.mapSaleCreationDtoToSale(creationDTO, client, employee, barberService, paymentMethod, emptyList);

        assertNotNull(sale);
        assertEquals(2000.0, sale.getTotal());
        assertEquals("Pago en efectivo", sale.getPaymentMethodUsed().getName());
        assertEquals("Tomas Gabriel", sale.getClient().getFirstName());
    }

    @Test
    void givenNewSale_WithDiversePriceProductList_ThenIsSuccesfullyRounded() {

        SaleItem item1 = SaleItem.builder().quantity(3).unitPrice(255.60).build();
        SaleItem item2 = SaleItem.builder().quantity(5).unitPrice(362.33).build();
        SaleItem item3 = SaleItem.builder().quantity(7).unitPrice(405.10).build();

        List<SaleItem> itemList = List.of(item1, item2, item3);

        sale = mapper.mapSaleCreationDtoToSale(creationDTO, client, employee, null, paymentMethod, itemList);

        assertNotNull(sale);
        assertEquals(5414.15, sale.getTotal());
        assertEquals("Pago en efectivo", sale.getPaymentMethodUsed().getName());
        assertEquals("Tomas Gabriel", sale.getClient().getFirstName());
    }

    @Test
    void givenExistingSale_WhenGettingInfo_ThenIsMappedSuccesfully() {

        List<SaleItem> emptyList = List.of();

        sale = Sale.builder()
                .saleID(1L)
                .dateAndTime(LocalDateTime.of(2026, 1, 1, 12, 30))
                .client(client)
                .barberService(barberService)
                .items(emptyList)
                .paymentMethodUsed(paymentMethod)
                .total(2000.0)
                .build();

        SaleInfoDTO result = mapper.mapSaleToInfoDTO(sale);

        assertNotNull(result);
    }

    @Test
    void givenExistingSaleWithOutBarberService_ThenBarberServiceNamePlaceHolderIs_EMPTY_SERVICE_DEFAULT_STRING() {

        SaleItem item1 = SaleItem.builder().quantity(3).unitPrice(255.60).build();
        SaleItem item2 = SaleItem.builder().quantity(5).unitPrice(362.33).build();
        SaleItem item3 = SaleItem.builder().quantity(7).unitPrice(405.10).build();

        List<SaleItem> itemList = List.of(item1, item2, item3);

        sale = Sale.builder()
                .saleID(1L)
                .dateAndTime(LocalDateTime.of(2026, 1, 1, 12, 30))
                .client(client)
                .barberService(null)
                .items(itemList)
                .paymentMethodUsed(paymentMethod)
                .total(5414.15)
                .build();

        SaleInfoDTO infoDTO = mapper.mapSaleToInfoDTO(sale);

        assertEquals("Sin servicio / Venta de productos", infoDTO.getBarberServiceName());
    }

    @Test
    void givenSaleItemList_ThenEachElementIsSuccesfullyLinked_ToTheSameEntity() {

        SaleItem item1 = SaleItem.builder().quantity(3).unitPrice(255.60).build();
        SaleItem item2 = SaleItem.builder().quantity(5).unitPrice(362.33).build();
        SaleItem item3 = SaleItem.builder().quantity(7).unitPrice(405.10).build();

        List<SaleItem> itemList = List.of(item1, item2, item3);

        sale = mapper.mapSaleCreationDtoToSale(creationDTO, client, employee, null, paymentMethod, itemList);

        assertTrue(sale.getItems().stream().anyMatch((item -> item.equals(item1))));
        assertTrue(sale.getItems().stream().anyMatch((item -> item.equals(item2))));
        assertTrue(sale.getItems().stream().anyMatch((item -> item.equals(item3))));

        assertEquals(sale, item1.getSale());
        assertEquals(sale, item2.getSale());
    }

    @Test
    void givenSaleWithBarberService_AndPriceModifierType_DESCUENTO_ThenADiscountIsApplied() {

        Double originalTotal = barberService.getPrice();

        paymentMethod.setModifierType(PaymentMethodModifierType.DESCUENTO);

        sale = mapper.mapSaleCreationDtoToSale(creationDTO, client, employee, barberService, paymentMethod, List.of());

        assertNotNull(sale);
        assertNotEquals(originalTotal, sale.getTotal());
        assertEquals(1900.0, sale.getTotal());
    }

    @Test
    void givenSaleWithBarberService_AndPriceModifierType_RECARGO_ThenExtraChargeIsApplied() {

        Double originalTotal = barberService.getPrice();

        paymentMethod.setModifierType(PaymentMethodModifierType.RECARGO);

        sale = mapper.mapSaleCreationDtoToSale(creationDTO, client, employee, barberService, paymentMethod, List.of());

        assertNotNull(sale);
        assertNotEquals(originalTotal, sale.getTotal());
        assertEquals(2100.0, sale.getTotal());
    }

    @Test
    void givenSaleWithBarberService_AndPriceModifierType_NINGUNO_ThenSaleTotalIsNotModified() {

        Double originalPrice = barberService.getPrice();

        paymentMethod.setModifierType(PaymentMethodModifierType.NINGUNO);

        sale = mapper.mapSaleCreationDtoToSale(creationDTO, client, employee, barberService, paymentMethod, List.of());

        assertNotNull(sale);
        assertEquals(originalPrice, sale.getTotal());
    }

    @Test
    void givenSaleWithProducts_AndPriceModifierType_NINGUNO_ThenSaleTotalIsNotModified() {

        SaleItem item1 = SaleItem.builder().quantity(2).unitPrice(255.50).build();
        SaleItem item2 = SaleItem.builder().quantity(5).unitPrice(360.20).build();

        List<SaleItem> saleItemList = List.of(item1, item2);

        Double originalTotal = 2312.0;

        paymentMethod.setModifierType(PaymentMethodModifierType.NINGUNO);

        sale = mapper.mapSaleCreationDtoToSale(creationDTO, client, employee, null, paymentMethod, saleItemList);

        assertNotNull(sale);
        assertEquals(originalTotal, sale.getTotal());
    }

    @Test
    void givenSaleWithProducts_AndPriceModifierType_DESCUENTO_ThenADiscountIsApplied() {

        SaleItem item1 = SaleItem.builder().quantity(3).unitPrice(750.0).build();
        SaleItem item2 = SaleItem.builder().quantity(1).unitPrice(1200.0).build();

        List<SaleItem> saleItemList = List.of(item1, item2);

        paymentMethod.setModifierType(PaymentMethodModifierType.DESCUENTO);

        Double originalTotal = 3450.0;
        Double finalTotal = 3450.0 - (3450.0 * 0.05);

        sale = mapper.mapSaleCreationDtoToSale(creationDTO, client, employee, null, paymentMethod, saleItemList);

        assertNotNull(sale);
        assertNotEquals(originalTotal, sale.getTotal());
        assertEquals(finalTotal, sale.getTotal());
    }

    @Test
    void givenSaleWithProducts_AndPriceModifierType_RECARGO_ThenAnExtraChargeIsApplied() {

        SaleItem item1 = SaleItem.builder().quantity(5).unitPrice(120.0).build();
        SaleItem item2 = SaleItem.builder().quantity(2).unitPrice(300.0).build();

        List<SaleItem> saleItemList = List.of(item1, item2);

        paymentMethod.setModifierType(PaymentMethodModifierType.RECARGO);

        Double originalTotal = 1200.0;
        Double finalTotal = 1200 + (1200.0 * 0.05);

        sale = mapper.mapSaleCreationDtoToSale(creationDTO, client, employee, null, paymentMethod, saleItemList);

        assertNotNull(sale);
        assertNotEquals(originalTotal, sale.getTotal());
        assertEquals(finalTotal, sale.getTotal());
    }

    @Test
    void givenMixedSale_WithPriceModifierType_NINGUNO_ThenSaleTotalIsNotModified() {

        SaleItem item1 = SaleItem.builder().quantity(1).unitPrice(100.0).build();
        SaleItem item2 = SaleItem.builder().quantity(3).unitPrice(80.0).build();

        List<SaleItem> saleItemList = List.of(item1, item2);

        paymentMethod.setModifierType(PaymentMethodModifierType.NINGUNO);
        barberService.setPrice(15000.0);

        Double originalTotal = barberService.getPrice() + 100 + 3 * 80;

        sale = mapper.mapSaleCreationDtoToSale(creationDTO, client, employee, barberService, paymentMethod, saleItemList);

        assertNotNull(sale);
        assertEquals(originalTotal, sale.getTotal());
    }

    @Test
    void givenMixedSale_WithPriceModifierType_DESCUENTO_ThenDiscountIsApplied() {

        SaleItem item1 = SaleItem.builder().quantity(1).unitPrice(100.0).build();
        SaleItem item2 = SaleItem.builder().quantity(3).unitPrice(80.0).build();

        List<SaleItem> saleItemList = List.of(item1, item2);

        paymentMethod.setModifierType(PaymentMethodModifierType.DESCUENTO);
        barberService.setPrice(15000.0);

        Double originalTotal = barberService.getPrice() + 100 + 3 * 80;
        Double finalTotal = originalTotal - (originalTotal * 0.05);

        sale = mapper.mapSaleCreationDtoToSale(creationDTO, client, employee, barberService, paymentMethod, saleItemList);

        assertNotNull(sale);
        assertNotEquals(originalTotal, sale.getTotal());
        assertEquals(finalTotal, sale.getTotal());
    }

    @Test
    void givenMixedSale_WithPriceModifierType_RECARGO_ThenAnExtraChargeIsApplied() {

        SaleItem item1 = SaleItem.builder().quantity(1).unitPrice(100.0).build();
        SaleItem item2 = SaleItem.builder().quantity(3).unitPrice(80.0).build();

        List<SaleItem> saleItemList = List.of(item1, item2);

        paymentMethod.setModifierType(PaymentMethodModifierType.RECARGO);
        barberService.setPrice(15000.0);

        Double originalTotal = barberService.getPrice() + 100 + 3 * 80;
        Double finalTotal = originalTotal + (originalTotal * 0.05);

        sale = mapper.mapSaleCreationDtoToSale(creationDTO, client, employee, barberService, paymentMethod, saleItemList);

        assertNotNull(sale);
        assertNotEquals(originalTotal, sale.getTotal());
        assertEquals(finalTotal, sale.getTotal());
    }

    @Test
    void givenSale_WhenBarberServicePriceIsChanged_ThenSaleKeepsPriceUsedWhenRegistered() {

        Double oldPrice = 12000.0;
        Double newPrice = 15000.0;

        barberService.setPrice(oldPrice);

        sale = mapper.mapSaleCreationDtoToSale(creationDTO, client, employee, barberService, paymentMethod, List.of());

        barberService.setPrice(newPrice);

        assertEquals(oldPrice, sale.getTotal());
    }
}
