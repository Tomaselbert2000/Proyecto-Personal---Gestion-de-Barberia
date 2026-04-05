package com.impl;

import com.barbershop.dto.product.ProductItemDTO;
import com.barbershop.dto.sale.SaleCreationDTO;
import com.barbershop.dto.sale.SaleInfoDTO;
import com.barbershop.enums.BarberServiceCategory;
import com.barbershop.enums.PaymentMethodModifierType;
import com.barbershop.enums.ProductCategory;
import com.barbershop.exceptions.client.ClientNotFoundException;
import com.barbershop.exceptions.employee.EmployeeNotFoundException;
import com.barbershop.exceptions.paymentmethod.PaymentMethodNotFoundException;
import com.barbershop.exceptions.sale.InactiveEmployeeException;
import com.barbershop.exceptions.sale.InactivePaymentMethodException;
import com.barbershop.exceptions.sale.InsufficientProductStockException;
import com.barbershop.exceptions.sale.SaleNotFoundException;
import com.barbershop.mapper.implementation.SaleMapperImpl;
import com.barbershop.mapper.interfaces.SaleMapper;
import com.barbershop.model.*;
import com.barbershop.repository.*;
import com.barbershop.service.implementation.SaleServiceImpl;
import com.barbershop.validation.sale.SaleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaleServiceImplTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private BarberServiceRepository barberServiceRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private SaleValidator validator;

    @Spy
    private SaleMapper mapper = new SaleMapperImpl();

    private Product product_A;
    private Product product_B;
    private Client client;
    private Employee employee;
    private BarberService barberService;
    private PaymentMethod paymentMethod;
    private Sale saleOnDB;
    private ServiceRecord record;

    @InjectMocks
    private SaleServiceImpl saleService;

    @Captor
    ArgumentCaptor<Sale> saleCaptor;

    private SaleCreationDTO creationDTO;

    private static final Long PRODUCT_ID_A = 1L;
    private static final Long PRODUCT_ID_B = 2L;
    private static final Long CLIENT_ID = 10L;
    private static final Long EMPLOYEE_ID = 20L;
    private static final Long BARBER_SERVICE_ID = 100L;
    private static final Long PAYMENT_METHOD_ID = 1000L;
    private static final Long SALE_ID = 123L;
    private static final Long RECORD_ID = 999L;

    @BeforeEach
    void init() {

        product_A = Product.builder()
                .productID(PRODUCT_ID_A)
                .name("Cera para modelar")
                .productCost(6000.0)
                .currentPrice(6850.0)
                .category(ProductCategory.CERA)
                .currentStockLevel(150)
                .safetyStockLevel(25)
                .build();

        product_B = Product.builder()
                .productID(PRODUCT_ID_B)
                .name("Shampoo matizador")
                .productCost(2500.0)
                .currentPrice(2900.0)
                .category(ProductCategory.SHAMPOO)
                .currentStockLevel(5)
                .safetyStockLevel(3)
                .build();

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
                .name("Corte de pelo básico")
                .price(15000.0)
                .serviceCategory(BarberServiceCategory.CORTE)
                .build();

        paymentMethod = PaymentMethod.builder().paymentMethodID(PAYMENT_METHOD_ID)
                .name("Mercado Pago")
                .description("")
                .isActive(true)
                .createdAt(LocalDate.of(2025, 1, 1))
                .modifierType(PaymentMethodModifierType.NINGUNO)
                .priceModifier(0.5)
                .build();

        creationDTO = SaleCreationDTO.builder().dateAndTime(LocalDateTime.of(2026, 1, 1, 13, 30))
                .clientID(CLIENT_ID)
                .employeeID(EMPLOYEE_ID)
                .paymentMethodID(PAYMENT_METHOD_ID)
                .barberServiceID(BARBER_SERVICE_ID)
                .productsDetail(List.of(new ProductItemDTO(PRODUCT_ID_A, 3)))
                .build();

        saleOnDB = Sale.builder()
                .saleID(SALE_ID)
                .dateAndTime(LocalDateTime.of(2026, 1, 1, 12, 15))
                .client(client)
                .barberService(barberService)
                .items(null)
                .paymentMethodUsed(paymentMethod)
                .total(16000.0)
                .build();

        record = ServiceRecord.builder()
                .serviceRecordID(RECORD_ID)
                .employee(employee)
                .client(client)
                .sale(saleOnDB)
                .timestamp(saleOnDB.getDateAndTime())
                .serviceName(barberService.getName())
                .priceAtMoment(saleOnDB.getTotal())
                .build();
    }

    @Test
    void givenNewSaleWithProductList_WhenCreating_ThenIsPersisted() {

        List<Long> productIDs = List.of(PRODUCT_ID_A);
        List<Product> listToMock = List.of(product_A);

        mockBasicSuccesfulScenario();
        mockProducts(productIDs, listToMock);

        registerSale();

        verify(validator).validateDTO(creationDTO);

        captureSale();

        Sale capturedSale = saleCaptor.getValue();

        assertNotNull(capturedSale);
        assertEquals(CLIENT_ID, capturedSale.getClient().getClientID());
        assertNotEquals(0.0, capturedSale.getTotal());
    }

    @Test
    void givenNewSaleWithBarberService_WhenCreating_ThenIsPersisted() {

        mockBasicSuccesfulScenario();

        List<ProductItemDTO> emptyList = List.of();

        paymentMethod.setModifierType(PaymentMethodModifierType.RECARGO);
        creationDTO.setProductsDetail(emptyList);

        Double expectedTotal = barberService.getPrice() + (barberService.getPrice() * paymentMethod.getPriceModifier());

        registerSale();

        verify(validator).validateDTO(creationDTO);
        verify(saleRepository, times(1)).save(any());

        captureSale();

        Sale capturedSale = saleCaptor.getValue();

        assertEquals(expectedTotal, capturedSale.getTotal());
    }

    @Test
    void givenRegisteredSale_ThenStockIsUpdated() {

        List<Long> productIDs = List.of(PRODUCT_ID_A);
        List<Product> listToMock = List.of(product_A);

        mockBasicSuccesfulScenario();
        mockProducts(productIDs, listToMock);

        registerSale();

        Integer expectedStock = 147;
        Integer returnedStock = product_A.getCurrentStockLevel();

        assertEquals(expectedStock, returnedStock);
    }

    @Test
    void givenNonExistingClient_WhenSaleIsRegistered_ThenThrows_ClientNotFoundException() {

        mockEmptyClient();

        assertThrows(ClientNotFoundException.class, this::registerSale);

        verifyThatSaleWasNotRegistered();
    }

    @Test
    void givenNonExistingPaymentMethod_WhenSaleIsRegistered_ThenThrows_PaymentMethodNotFoundException() {

        mockClient();
        mockEmployee();
        mockBarberService();
        mockEmptyPaymentMethod();

        assertThrows(PaymentMethodNotFoundException.class, this::registerSale);

        verifyThatSaleWasNotRegistered();
    }

    @Test
    void givenProductWith_CurrentStock10_WhenSaleIsRegisteredWith_Quantity15_ThenThrows_InsufficientProductStockException() {

        List<Long> productIDs = List.of(PRODUCT_ID_A);
        List<Product> listToMock = List.of(product_A);
        List<ProductItemDTO> itemDTOList = List.of(new ProductItemDTO(PRODUCT_ID_A, 15));

        mockBasicSuccesfulScenario();
        mockProducts(productIDs, listToMock);

        product_A.setCurrentStockLevel(10);
        creationDTO.setProductsDetail(itemDTOList);

        assertThrows(InsufficientProductStockException.class, this::registerSale);

        verifyThatSaleWasNotRegistered();
    }

    @Test
    void givenNonExistingEmployee_WhenCreating_ThenThrows_EmployeeNotFoundException() {

        mockClient();
        mockEmptyEmployee();

        creationDTO.setProductsDetail(List.of());

        assertThrows(EmployeeNotFoundException.class, this::registerSale);

        verifyThatSaleWasNotRegistered();
    }

    @Test
    void givenProductWith_CurrentStock10_WhenSaleIsRegisteredWith_Quantity10_ThenSaleIsRegisteredSuccesfully() {

        List<Long> productIDs = List.of(PRODUCT_ID_A);
        List<Product> listToMock = List.of(product_A);
        List<ProductItemDTO> itemDTOList = List.of(new ProductItemDTO(PRODUCT_ID_A, 10));

        mockBasicSuccesfulScenario();
        mockProducts(productIDs, listToMock);

        product_A.setCurrentStockLevel(10);
        creationDTO.setProductsDetail(itemDTOList);

        assertDoesNotThrow(this::registerSale);
    }

    @Test
    void givenExistingSale_WhenSearching_ThenReturnsItsInformation() {

        mockSale();

        SaleInfoDTO infoDTO = saleService.getSale(SALE_ID);

        assertNotNull(infoDTO);
        assertEquals(infoDTO.getClientFirstName(), client.getFirstName());
        assertEquals(infoDTO.getClientLastName(), client.getLastName());
        assertEquals(infoDTO.getBarberServiceName(), barberService.getName());
        assertEquals(infoDTO.getPaymentMethodName(), paymentMethod.getName());
        assertEquals(infoDTO.getDateAndTime(), saleOnDB.getDateAndTime());
        assertEquals(infoDTO.getTotal(), saleOnDB.getTotal());
    }

    @Test
    void givenNonExistingSale_WhenSearching_ThenReturns_SaleNotFoundException() {

        mockEmptySale();

        assertThrows(SaleNotFoundException.class, () -> saleService.getSale(SALE_ID));
    }

    @Test
    void givenExistingSale_WhenDeleting_ThenIsErased() {

        mockSale();

        deleteSale();

        verify(saleRepository, times(1)).delete(saleOnDB);
    }

    @Test
    void givenExistingSale_WhenDeleting_ThenStockIsRestored() {

        SaleItem item = SaleItem.builder()
                .product(product_A)
                .quantity(2)
                .build();

        saleOnDB.setItems(List.of(item));

        mockSale();

        deleteSale();

        verify(saleRepository).delete(saleOnDB);

        assertEquals(152, product_A.getCurrentStockLevel());
    }

    @Test
    void givenExistingSale_WithMultipleProducts_WhenDeleting_ThenStockIsRestoredForEachProduct() {

        SaleItem item1 = SaleItem.builder()
                .product(product_A)
                .quantity(2)
                .build();

        SaleItem item2 = SaleItem.builder()
                .product(product_B)
                .quantity(3)
                .build();

        List<SaleItem> saleItemList = List.of(item1, item2);

        saleOnDB.setItems(saleItemList);

        mockSale();

        deleteSale();

        assertEquals(152, product_A.getCurrentStockLevel());
        assertEquals(8, product_B.getCurrentStockLevel());
    }

    @Test
    void givenExistingSale_WithOnlyBarberService_AndNullSaleItemsList_WhenDeleting_ThenDoesNotThrowAnything() {

        saleOnDB.setItems(null);

        mockSale();

        assertDoesNotThrow(this::deleteSale);
    }

    @Test
    void givenNonExistingSale_WhenDeleting_ThenThrows_SaleNotFoundException() {

        mockEmptySale();

        assertThrows(SaleNotFoundException.class, this::deleteSale);

        verifyThatSaleWasNotDeleted();
    }

    @Test
    void given_N_ExistingSales_WhenGettingAll_ThenAListIsReturned() {

        List<Sale> saleList = List.of(saleOnDB);

        when(saleRepository.findAll()).thenReturn(saleList);

        List<SaleInfoDTO> infoDTOList = saleService.getSaleList();

        assertNotNull(infoDTOList);
        assertEquals(1, infoDTOList.size());
    }

    @Test
    void givenInsufficientStockOf_ProductA_Then_ProductBStock_IsNotModified() {

        product_A.setCurrentStockLevel(5);

        List<Long> productIDs = List.of(PRODUCT_ID_A, PRODUCT_ID_B);
        List<Product> listToMock = List.of(product_A, product_B);

        List<ProductItemDTO> itemDTOList = List.of(new ProductItemDTO(PRODUCT_ID_A, 7), new ProductItemDTO(PRODUCT_ID_B, 1));

        mockClient();
        mockEmployee();
        mockPaymentMethod();
        mockProducts(productIDs, listToMock);

        creationDTO.setBarberServiceID(null);
        creationDTO.setProductsDetail(itemDTOList);

        assertThrows(InsufficientProductStockException.class, this::registerSale);

        assertEquals(5, product_B.getCurrentStockLevel());
    }

    @Test
    void givenSaleWithBarberService_WhenCreating_ThenServiceRecordIsGenerated() {

        mockBasicSuccesfulScenario();

        creationDTO.setProductsDetail(List.of());

        registerSale();

        captureSale();

        Sale capturedSale = saleCaptor.getValue();

        ServiceRecord record = capturedSale.getServiceRecord();

        assertNotNull(record);
        assertEquals(client, record.getClient());
        assertEquals(employee, record.getEmployee());
        assertEquals(barberService.getName(), record.getServiceName());
        assertEquals(barberService.getPrice(), record.getPriceAtMoment());
        assertNotNull(record.getTimestamp());
    }

    @Test
    void givenExistingServiceRecord_WhenBarberServiceIsUpdated_ThenServiceRecordSnapshot_IsNotModified() {

        mockBasicSuccesfulScenario();

        creationDTO.setProductsDetail(List.of());

        registerSale();

        captureSale();

        Sale capturedSale = saleCaptor.getValue();

        ServiceRecord record = capturedSale.getServiceRecord();

        barberService.setPrice(9999.0);
        barberService.setName("Nuevo corte de pelo");

        assertEquals(15000.0, record.getPriceAtMoment());
        assertEquals("Corte de pelo básico", record.getServiceName());
    }

    @Test
    void givenNewSale_WithOnlyProductList_ThenServiceRecordIsNotGenerated() {

        List<Long> productIDs = List.of(PRODUCT_ID_A);
        List<Product> listToMock = List.of(product_A);

        mockProducts(productIDs, listToMock);
        mockClient();
        mockEmployee();
        mockPaymentMethod();

        creationDTO.setBarberServiceID(null);

        registerSale();

        captureSale();

        Sale capturedSale = saleCaptor.getValue();

        assertNull(capturedSale.getServiceRecord());
    }

    @Test
    void givenInactiveEmployee_WhenCreatingSale_ThenThrows_InactiveEmployeeException() {

        employee.setActive(false);

        mockBasicSuccesfulScenario();

        assertThrows(InactiveEmployeeException.class, this::registerSale);

        verifyThatSaleWasNotRegistered();
    }

    @Test
    void givenInactivePaymentMethod_WhenCreatingSale_ThenThrows_InactivePaymentMethodException() {

        paymentMethod.setIsActive(false);

        mockBasicSuccesfulScenario();

        assertThrows(InactivePaymentMethodException.class, this::registerSale);

        verifyThatSaleWasNotRegistered();
    }

    @Test
    void givenNewSale_WhenCreating_ThenServiceRecordAndSaleTimestamps_AreSynchronized() {

        mockBasicSuccesfulScenario();

        creationDTO.setProductsDetail(List.of());

        registerSale();

        captureSale();

        Sale capturedSale = saleCaptor.getValue();

        ServiceRecord record = capturedSale.getServiceRecord();

        assertEquals(record.getTimestamp(), capturedSale.getDateAndTime());
    }

    @Test
    void givenNewSale_WithDuplicateProductIDsInDTO_ThenProcessedSuccessfully() {

        List<ProductItemDTO> duplicatedItems = List.of(
                new ProductItemDTO(PRODUCT_ID_A, 1),
                new ProductItemDTO(PRODUCT_ID_A, 2)
        );

        creationDTO.setProductsDetail(duplicatedItems);

        when(productRepository.findAllById(anyList())).thenReturn(List.of(product_A));
        mockBasicSuccesfulScenario();

        assertDoesNotThrow(this::registerSale);

        captureSale();

        Sale capturedSale = saleCaptor.getValue();

        assertEquals(2, capturedSale.getItems().size(), "Debería haber 2 items aunque el ID sea el mismo");

        assertEquals(147, product_A.getCurrentStockLevel());
    }

    @Test
    void givenExistingSale_WithServiceRecord_WhenDeleting_ThenServiceRecordIsDeleted() {

        saleOnDB.setServiceRecord(record);

        mockSale();

        deleteSale();

        captureDeletedSale();

        Sale deletedSale = saleCaptor.getValue();

        assertNotNull(deletedSale.getServiceRecord());
        assertEquals(record, deletedSale.getServiceRecord());
        assertEquals(RECORD_ID, deletedSale.getServiceRecord().getServiceRecordID());
    }

    private void registerSale() {

        saleService.registerNewSale(creationDTO);
    }

    private void captureSale() {

        verify(saleRepository).save(saleCaptor.capture());
    }

    private void deleteSale() {

        saleService.deleteSale(SALE_ID);
    }

    private void captureDeletedSale() {

        verify(saleRepository).delete(saleCaptor.capture());
    }

    private void mockClient() {

        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.of(client));
    }

    private void mockEmployee() {

        when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee));
    }

    private void mockPaymentMethod() {

        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.of(paymentMethod));
    }

    private void mockBarberService() {

        when(barberServiceRepository.findById(BARBER_SERVICE_ID)).thenReturn(Optional.of(barberService));
    }

    private void mockProducts(List<Long> productIDs, List<Product> listToMock) {

        when(productRepository.findAllById(productIDs)).thenReturn(listToMock);
    }

    private void mockSale() {

        when(saleRepository.findById(SALE_ID)).thenReturn(Optional.of(saleOnDB));
    }

    private void mockBasicSuccesfulScenario() {

        mockClient();
        mockEmployee();
        mockBarberService();
        mockPaymentMethod();
    }

    private void mockEmptyClient() {

        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.empty());
    }

    private void mockEmptySale() {

        when(saleRepository.findById(SALE_ID)).thenReturn(Optional.empty());
    }

    private void mockEmptyEmployee() {

        when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.empty());
    }

    private void mockEmptyPaymentMethod() {

        when(paymentMethodRepository.findById(PAYMENT_METHOD_ID)).thenReturn(Optional.empty());
    }

    private void verifyThatSaleWasNotRegistered() {

        verify(saleRepository, never()).save(any());
    }

    private void verifyThatSaleWasNotDeleted() {

        verify(saleRepository, never()).delete(any());
    }
}
