package com.service.implementation;

import com.abstract_test_class.BaseServiceTest;
import com.dto.product.ProductItemDTO;
import com.dto.sale.SaleCreationDTO;
import com.dto.sale.SaleInfoDTO;
import com.enums.PaymentMethodModifierType;
import com.exceptions.client.ClientNotFoundException;
import com.exceptions.employee.EmployeeNotFoundException;
import com.exceptions.paymentmethod.PaymentMethodNotFoundException;
import com.exceptions.sale.InactiveEmployeeException;
import com.exceptions.sale.InactivePaymentMethodException;
import com.exceptions.sale.InsufficientProductStockException;
import com.exceptions.sale.SaleNotFoundException;
import com.mapper.implementation.SaleMapperImpl;
import com.mapper.interfaces.SaleMapper;
import com.model.*;
import com.repository.*;
import com.service.helper.SaleServiceTestHelper;
import com.validation.sale.SaleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static com.factory.BarberServiceTestDataFactory.buildValidBarberService;
import static com.factory.ClientTestDataFactory.buildValidClient;
import static com.factory.EmployeeTestDataFactory.buildValidEmployee;
import static com.factory.PaymentMethodTestDataFactory.buildValidPaymentMethod;
import static com.factory.ProductTestDataFactory.buildValidProduct;
import static com.factory.SaleTestDataFactory.*;
import static com.service.helper.SaleServiceTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SaleServiceImplTest extends BaseServiceTest<Sale, SaleRepository> {

    private final Product product = buildValidProduct();
    private final Client client = buildValidClient();
    private final Employee employee = buildValidEmployee();
    private final BarberService barberService = buildValidBarberService();
    private final PaymentMethod paymentMethod = buildValidPaymentMethod();
    private final List<SaleItem> saleItemList = buildValidListSaleItems();
    private final Sale saleOnDB = buildValidSale();
    private final SaleCreationDTO creationDTO = buildValidSaleCreationDTO();
    private final List<ProductItemDTO> emptyList = List.of();
    @Captor
    ArgumentCaptor<Sale> saleCaptor;
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
    @InjectMocks
    private SaleServiceImpl saleService;

    @Override
    @BeforeEach
    protected void init() {

        syncSaleCreationDTO_IDsWithFactoryObjectsIDs();
    }

    @Override
    protected SaleRepository getPrimaryRepository() {

        return saleRepository;
    }

    @Test
    @DisplayName("Crea una nueva venta con lista de productos y la persiste")
    void givenNewSaleWithProductList_WhenCreating_ThenIsPersisted() {

        mockAllReposAndEntities();

        mockProductList(productRepository, List.of(product));

        registerSale(saleService, creationDTO);

        verifyCreationProcessSuccess();

        captureSale(saleCaptor, saleRepository);

        Sale capturedSale = saleCaptor.getValue();

        assertNotNull(capturedSale);
        assertEquals(client.getClientID(), capturedSale.getClient().getClientID());
        assertNotEquals(0.0, capturedSale.getTotal());
    }

    @Test
    @DisplayName("Crea una nueva venta con servicio de barbero y la persiste")
    void givenNewSaleWithBarberService_WhenCreating_ThenIsPersisted() {

        mockAllReposAndEntities();

        paymentMethod.setModifierType(PaymentMethodModifierType.RECARGO);
        creationDTO.setProductsDetail(emptyList);

        Double expectedTotal = barberService.getPrice() + (barberService.getPrice() * paymentMethod.getPriceModifier());

        registerSale(saleService, creationDTO);

        verify(validator).validateDTO(creationDTO);

        verifyThatEntityWasSaved();

        captureSale(saleCaptor, saleRepository);

        Sale capturedSale = saleCaptor.getValue();

        assertEquals(expectedTotal, capturedSale.getTotal());
    }

    @Test
    @DisplayName("Actualiza el stock cuando se registra una venta")
    void givenRegisteredSale_ThenStockIsUpdated() {

        mockProductList(productRepository, List.of(product));

        mockAllReposAndEntities();

        Integer expectedStock = product.getCurrentStockLevel() - creationDTO.getProductsDetail().getFirst().getQuantity();

        registerSale(saleService, creationDTO);

        Integer returnedStock = product.getCurrentStockLevel();

        assertEquals(expectedStock, returnedStock);
    }

    @Test
    @DisplayName("Lanza ClientNotFoundException cuando se registra una venta con cliente inexistente")
    void givenNonExistingClient_WhenSaleIsRegistered_ThenThrows_ClientNotFoundException() {

        mockEmptyClient(clientRepository, client);

        assertThrows(ClientNotFoundException.class, () -> registerSale(saleService, creationDTO));

        verifyThatEntityWasNotSaved();
    }

    @Test
    @DisplayName("Lanza PaymentMethodNotFoundException cuando se registra una venta con método de pago inexistente")
    void givenNonExistingPaymentMethod_WhenSaleIsRegistered_ThenThrows_PaymentMethodNotFoundException() {

        mockClient(clientRepository, client);
        mockEmployee(employeeRepository, employee);
        mockBarberService(barberServiceRepository, barberService);
        mockEmptyPaymentMethod(paymentMethodRepository, paymentMethod);

        assertThrows(PaymentMethodNotFoundException.class, () -> registerSale(saleService, creationDTO));

        verifyThatEntityWasNotSaved();
    }

    @Test
    @DisplayName("Lanza InsufficientProductStockException cuando se registra una venta con stock insuficiente de un producto")
    void givenProductWith_CurrentStock10_WhenSaleIsRegisteredWith_Quantity15_ThenThrows_InsufficientProductStockException() {

        List<Product> listToMock = List.of(product);
        List<ProductItemDTO> itemDTOList = List.of(new ProductItemDTO(product.getProductID(), 15));

        mockAllReposAndEntities();
        SaleServiceTestHelper.mockProductList(productRepository, listToMock);

        product.setCurrentStockLevel(10);
        creationDTO.setProductsDetail(itemDTOList);

        assertThrows(InsufficientProductStockException.class, () -> registerSale(saleService, creationDTO));

        verifyThatEntityWasNotSaved();
    }

    @Test
    @DisplayName("Lanza EmployeeNotFoundException cuando se registra una venta con empleado inexistente")
    void givenNonExistingEmployee_WhenCreating_ThenThrows_EmployeeNotFoundException() {

        mockClient(clientRepository, client);
        mockEmptyEmployee(employeeRepository, employee);

        creationDTO.setProductsDetail(List.of());

        assertThrows(EmployeeNotFoundException.class, () -> registerSale(saleService, creationDTO));

        verifyThatEntityWasNotSaved();
    }

    @Test
    @DisplayName("Registra una venta exitosamente cuando se registra con stock suficiente de un producto")
    void givenProductWith_CurrentStock10_WhenSaleIsRegisteredWith_Quantity10_ThenSaleIsRegisteredSuccesfully() {

        List<Product> listToMock = List.of(product);
        List<ProductItemDTO> itemDTOList = List.of(new ProductItemDTO(product.getProductID(), 10));

        mockAllReposAndEntities();
        SaleServiceTestHelper.mockProductList(productRepository, listToMock);

        product.setCurrentStockLevel(10);
        creationDTO.setProductsDetail(itemDTOList);

        assertDoesNotThrow(() -> registerSale(saleService, creationDTO));
    }

    @Test
    @DisplayName("Devuelve la información de una venta existente")
    void givenExistingSale_WhenSearching_ThenReturnsItsInformation() {

        mockSale(saleRepository, saleOnDB);

        SaleInfoDTO infoDTO = saleService.getSale(saleOnDB.getSaleID());

        checkInfoDTOAssertions(infoDTO);
    }

    @Test
    @DisplayName("Lanza SaleNotFoundException cuando se busca una venta inexistente")
    void givenNonExistingSale_WhenSearching_ThenReturns_SaleNotFoundException() {

        mockEmptySale(saleRepository, saleOnDB);

        assertThrows(SaleNotFoundException.class, () -> saleService.getSale(saleOnDB.getSaleID()));
    }

    @Test
    @DisplayName("Elimina una venta existente")
    void givenExistingSale_WhenDeleting_ThenIsErased() {

        mockSale(saleRepository, saleOnDB);

        deleteSale(saleService, saleOnDB);

        verifyThatEntityWasDeleted(saleOnDB);
    }

    @Test
    @DisplayName("Restaura el stock cuando se elimina una venta")
    void givenExistingSale_WhenDeleting_ThenStockIsRestored() {

        setProductInstanceAsFirstItemOnItemList();

        Integer expectedStockLevel = product.getCurrentStockLevel() + saleItemList.getFirst().getQuantity();

        mockSale(saleRepository, saleOnDB);

        deleteSale(saleService, saleOnDB);

        verify(saleRepository).delete(saleOnDB);

        assertEquals(expectedStockLevel, product.getCurrentStockLevel());
    }

    @Test
    @DisplayName("Restaura el stock para cada producto cuando se elimina una venta con múltiples productos")
    void givenExistingSale_WithMultipleProducts_WhenDeleting_ThenStockIsRestoredForEachProduct() {

        setProductInstanceAsFirstItemOnItemList();

        Integer expectedStockLevel = product.getCurrentStockLevel() + saleItemList.getFirst().getQuantity();

        mockSale(saleRepository, saleOnDB);

        deleteSale(saleService, saleOnDB);

        assertEquals(expectedStockLevel, product.getCurrentStockLevel());
    }

    @Test
    @DisplayName("No lanza ninguna excepción cuando se elimina una venta con solo servicio de barbero y lista de productos nula")
    void givenExistingSale_WithOnlyBarberService_AndNullSaleItemsList_WhenDeleting_ThenDoesNotThrowAnything() {

        saleOnDB.setItems(null);

        mockSale(saleRepository, saleOnDB);

        assertDoesNotThrow(() -> deleteSale(saleService, saleOnDB));
    }

    @Test
    @DisplayName("Lanza SaleNotFoundException cuando se intenta eliminar una venta inexistente")
    void givenNonExistingSale_WhenDeleting_ThenThrows_SaleNotFoundException() {

        mockEmptySale(saleRepository, saleOnDB);

        assertThrows(SaleNotFoundException.class, () -> deleteSale(saleService, saleOnDB));

        verifyThatEntityWasNotDeleted(saleOnDB);
    }

    @Test
    @DisplayName("Devuelve una lista de ventas existentes")
    void given_N_ExistingSales_WhenGettingAll_ThenAListIsReturned() {

        List<Sale> saleList = List.of(saleOnDB);

        when(saleRepository.findAll()).thenReturn(saleList);

        List<SaleInfoDTO> infoDTOList = saleService.getSaleList();

        assertNotNull(infoDTOList);
        assertEquals(1, infoDTOList.size());
    }

    @Test
    @DisplayName("No modifica el stock de ProductB cuando hay insuficiente stock de ProductA")
    void givenInsufficientStockOf_ProductA_Then_ProductBStock_IsNotModified() {

        product.setCurrentStockLevel(5);

        List<Product> listToMock = List.of(product);

        List<ProductItemDTO> itemDTOList = List.of(new ProductItemDTO(product.getProductID(), 7));

        mockClient(clientRepository, client);
        mockEmployee(employeeRepository, employee);
        mockPaymentMethod(paymentMethodRepository, paymentMethod);
        SaleServiceTestHelper.mockProductList(productRepository, listToMock);

        creationDTO.setBarberServiceID(null);
        creationDTO.setProductsDetail(itemDTOList);

        assertThrows(InsufficientProductStockException.class, () -> registerSale(saleService, creationDTO));
    }

    @Test
    @DisplayName("Genera un registro de servicio cuando se crea una venta con solo servicio de barbero")
    void givenSaleWithBarberService_WhenCreating_ThenServiceRecordIsGenerated() {

        mockAllReposAndEntities();

        creationDTO.setProductsDetail(List.of());

        registerSale(saleService, creationDTO);

        captureSale(saleCaptor, saleRepository);

        Sale capturedSale = saleCaptor.getValue();

        ServiceRecord record = capturedSale.getServiceRecord();

        checkServiceRecordAssertions(record);
    }

    @Test
    @DisplayName("No modifica el registro de servicio cuando se actualiza el servicio de barbero")
    void givenExistingServiceRecord_WhenBarberServiceIsUpdated_ThenServiceRecordSnapshot_IsNotModified() {

        String expectedOriginalName = barberService.getName();
        Double expectedOriginalPrice = barberService.getPrice();

        mockAllReposAndEntities();

        creationDTO.setProductsDetail(List.of());

        registerSale(saleService, creationDTO);

        captureSale(saleCaptor, saleRepository);

        Sale capturedSale = saleCaptor.getValue();

        ServiceRecord record = capturedSale.getServiceRecord();

        barberService.setPrice(9999.0);
        barberService.setName("Nuevo corte de pelo");

        assertEquals(expectedOriginalName, record.getServiceName());
        assertEquals(expectedOriginalPrice, record.getPriceAtMoment());
    }

    @Test
    @DisplayName("No genera un registro de servicio cuando se crea una venta con solo lista de productos")
    void givenNewSale_WithOnlyProductList_ThenServiceRecordIsNotGenerated() {

        List<Product> listToMock = List.of(product);

        mockProductList(productRepository, listToMock);
        mockClient(clientRepository, client);
        mockEmployee(employeeRepository, employee);
        mockPaymentMethod(paymentMethodRepository, paymentMethod);

        creationDTO.setBarberServiceID(null);

        registerSale(saleService, creationDTO);

        captureSale(saleCaptor, saleRepository);

        Sale capturedSale = saleCaptor.getValue();

        assertNull(capturedSale.getServiceRecord());
    }

    @Test
    @DisplayName("Lanza InactiveEmployeeException cuando se crea una venta con empleado inactivo")
    void givenInactiveEmployee_WhenCreatingSale_ThenThrows_InactiveEmployeeException() {

        employee.setActive(false);

        mockAllReposAndEntities();

        assertThrows(InactiveEmployeeException.class, () -> registerSale(saleService, creationDTO));

        verifyThatEntityWasNotSaved();
    }

    @Test
    @DisplayName("Lanza InactivePaymentMethodException cuando se crea una venta con método de pago inactivo")
    void givenInactivePaymentMethod_WhenCreatingSale_ThenThrows_InactivePaymentMethodException() {

        paymentMethod.setIsActive(false);

        mockAllReposAndEntities();

        assertThrows(InactivePaymentMethodException.class, () -> registerSale(saleService, creationDTO));

        verifyThatEntityWasNotSaved();
    }

    @Test
    @DisplayName("Sincroniza los tiempos de registro del servicio y la venta")
    void givenNewSale_WhenCreating_ThenServiceRecordAndSaleTimestamps_AreSynchronized() {

        mockAllReposAndEntities();

        creationDTO.setProductsDetail(List.of());

        registerSale(saleService, creationDTO);

        captureSale(saleCaptor, saleRepository);

        Sale capturedSale = saleCaptor.getValue();

        ServiceRecord record = capturedSale.getServiceRecord();

        assertEquals(record.getTimestamp(), capturedSale.getDateAndTime());
    }

    private void syncSaleCreationDTO_IDsWithFactoryObjectsIDs() {

        creationDTO.setClientID(client.getClientID());
        creationDTO.setEmployeeID(employee.getEmployeeID());
        creationDTO.setBarberServiceID(barberService.getBarbershopServiceID());
        creationDTO.setPaymentMethodID(paymentMethod.getPaymentMethodID());
        creationDTO.getProductsDetail().getFirst().setProductID(product.getProductID());
    }

    private void setProductInstanceAsFirstItemOnItemList() {

        saleItemList.getFirst().setProduct(product);
        saleOnDB.setItems(saleItemList);
    }

    private void mockAllReposAndEntities() {

        mockBasicSuccesfulScenario(
                clientRepository,
                employeeRepository,
                barberServiceRepository,
                paymentMethodRepository,
                client,
                barberService,
                employee,
                paymentMethod
        );
    }

    private void verifyCreationProcessSuccess() {

        verifyValidatorCreationInteraction(validator, creationDTO);
        verifyMapperCreationInteraction(mapper, creationDTO, client, employee, barberService, paymentMethod);
        verifyThatEntityWasSaved();
    }

    private void checkInfoDTOAssertions(SaleInfoDTO infoDTO) {

        assertNotNull(infoDTO);

        assertEquals(infoDTO.getClientFirstName(), client.getFirstName());
        assertEquals(infoDTO.getClientLastName(), client.getLastName());
        assertEquals(infoDTO.getBarberServiceName(), barberService.getName());
        assertEquals(infoDTO.getPaymentMethodName(), paymentMethod.getName());
        assertEquals(infoDTO.getDateAndTime(), saleOnDB.getDateAndTime());
        assertEquals(infoDTO.getTotal(), saleOnDB.getTotal());
    }

    private void checkServiceRecordAssertions(ServiceRecord record) {

        assertNotNull(record);

        assertEquals(client, record.getClient());
        assertEquals(employee, record.getEmployee());
        assertEquals(barberService.getName(), record.getServiceName());
        assertEquals(barberService.getPrice(), record.getPriceAtMoment());
        assertNotNull(record.getTimestamp());
    }
}