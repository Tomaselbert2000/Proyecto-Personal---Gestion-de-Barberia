package com.impl;

import com.barbershop.dto.product.ProductCreationDTO;
import com.barbershop.dto.product.ProductInfoDTO;
import com.barbershop.dto.product.ProductUpdateDTO;
import com.barbershop.enums.ProductCategory;
import com.barbershop.enums.StockStatus;
import com.barbershop.exceptions.product.*;
import com.barbershop.mapper.implementation.ProductMapperImpl;
import com.barbershop.mapper.interfaces.ProductMapper;
import com.barbershop.model.Product;
import com.barbershop.repository.ProductRepository;
import com.barbershop.service.implementation.ProductServiceImpl;
import com.barbershop.validation.product.ProductValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductValidator validator;

    @Spy
    private ProductMapper mapper = new ProductMapperImpl();

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    private ProductCreationDTO creationDTO;
    private ProductUpdateDTO updateDTO;
    private Product product;

    @InjectMocks
    private ProductServiceImpl productService;

    private static final Long PRODUCT_ID = 1L;
    private static final String NAME = "Cera para modelar";
    private static final Double COST = 2500.0;
    private static final Double CURRENT_PRICE = 2950.0;
    private static final ProductCategory CATEGORY = ProductCategory.CERA;
    private static final Integer CURRENT_STOCK_LEVEL = 150;
    private static final Integer SAFETY_STOCK_LEVEL = 40;

    private static final StockStatus EXPECTED_STOCK_STATUS = StockStatus.EXCEDIDO;
    private static final Double EXPECTED_PROFIT_PERCENTAGE = 18.0;

    private static final String UPDATE_NAME = "Cera para modelado de pelo";
    private static final Double UPDATE_COST = 2975.50;
    private static final Double UPDATE_PRICE = 3600.0;
    private static final Integer UPDATE_CURRENT_STOCK_LEVEL = 80;
    private static final Integer UPDATE_SAFETY_STOCK_LEVEL = 25;
    private static final Double UPDATE_PRICE_LOWER_THAN_PERSISTED_COST = 1000.0;

    @BeforeEach
    void init() {

        setupCreationDTO();
        setupUpdateDTO();
        setupProduct();
    }

    @Test
    @DisplayName("Dado un DTO de creación con datos correctos, deberá ser persistido exitosamente")
    void givenDTO_WithValidInformation_WhenCreating_ThenIsSuccesfullyPersisted() {

        registerNewProduct();

        captureProduct();

        verifyValidatorCreationInteraction();
        verifyMapperCreationInteraction();
        verifyThatProductWasRegistered();

        Product capturedProduct = productCaptor.getValue();

        assertAll(
                ("Verificación de campos"),
                () -> assertNotNull(capturedProduct),
                () -> assertEquals(NAME, capturedProduct.getName()),
                () -> assertEquals(COST, capturedProduct.getProductCost()),
                () -> assertEquals(CURRENT_PRICE, capturedProduct.getCurrentPrice()),
                () -> assertEquals(CATEGORY, capturedProduct.getCategory()),
                () -> assertEquals(CURRENT_STOCK_LEVEL, capturedProduct.getCurrentStockLevel()),
                () -> assertEquals(SAFETY_STOCK_LEVEL, capturedProduct.getSafetyStockLevel())
        );
    }

    @Test
    @DisplayName("Dado un DTO de creación con cualquiera de sus valores en NULL, el producto no será persistido")
    void givenAnyNullValue_WhenCreating_ThenProductIsNotPersisted() {

        doThrow(NullProductInputDataException.class).when(validator).validateDTO(creationDTO);

        assertThrows(NullProductInputDataException.class, this::registerNewProduct);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteraction();
        verifyThatProductWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre en blanco, el producto no será persistido")
    void givenBlankName_WhenCreating_ThenProductIsNotPersisted() {

        doThrow(BlankProductNameException.class).when(validator).validateDTO(creationDTO);

        assertThrows(BlankProductNameException.class, this::registerNewProduct);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteraction();
        verifyThatProductWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre inválido, el producto no será persistido")
    void givenInvalidName_WhenCreating_ThenProductIsNotPersisted() {

        doThrow(InvalidProductNameException.class).when(validator).validateDTO(creationDTO);

        assertThrows(InvalidProductNameException.class, this::registerNewProduct);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteraction();
        verifyThatProductWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre válido que ya fue registrado por otro producto," +
            " la validación deberá fallar y arrojará DuplicatedProductNameException")
    void givenDuplicatedName_WhenCreating_ThenThrows_DuplicatedProductNameException() {

        mockThatProductNameWillCauseConflict();

        assertThrows(DuplicatedProductNameException.class, this::registerNewProduct);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteraction();
        verifyThatProductWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio de costo negativo o cero, el producto no será persistido")
    void givenProductCostLowerOrEqualsThanZero_WhenCreating_ThenProductIsNotPersisted() {

        doThrow(InvalidProductCostException.class).when(validator).validateDTO(creationDTO);

        assertThrows(InvalidProductCostException.class, this::registerNewProduct);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteraction();
        verifyThatProductWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio de venta al público negativo o cero, el producto no será persistido")
    void givenProductPriceLowerOrEqualsThanZero_WhenCreating_ThenProductIsNotPersisted() {

        doThrow(InvalidProductCurrentPriceException.class).when(validator).validateDTO(creationDTO);

        assertThrows(InvalidProductCurrentPriceException.class, this::registerNewProduct);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteraction();
        verifyThatProductWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio de venta al público menor al valor de costo ingresado, el producto no será persistido")
    void givenProductCurrentPriceLowerThanCost_WhenCreating_ThenProductIsNotPersisted() {

        doThrow(InvalidProductCurrentPriceException.class).when(validator).validateDTO(creationDTO);

        assertThrows(InvalidProductCurrentPriceException.class, this::registerNewProduct);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteraction();
        verifyThatProductWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de stock actual menor que cero, el producto no será persistido")
    void givenCurrentStockLowerThanZero_WhenCreating_ThenProductIsNotPersisted() {

        doThrow(NegativeCurrentStockLevelException.class).when(validator).validateDTO(creationDTO);

        assertThrows(NegativeCurrentStockLevelException.class, this::registerNewProduct);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteraction();
        verifyThatProductWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de stock mínimo menor que cero, el producto no será persistido")
    void givenSafetyStockLevelLowerThanZero_WhenCreating_ThenProductIsNotPersisted() {

        doThrow(NegativeSafetyStockLevelException.class).when(validator).validateDTO(creationDTO);

        assertThrows(NegativeSafetyStockLevelException.class, this::registerNewProduct);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteraction();
        verifyThatProductWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un producto existente, deberá poder retornar su información mediante mapeo por DTO informativo")
    void givenExistingProduct_WhenSearching_ThenReturnsItsInformation() {

        mockExistingProduct();

        ProductInfoDTO returnedDTO = productService.getProduct(PRODUCT_ID);

        assertAll(
                ("Verificación de campos"),
                () -> assertEquals(NAME, returnedDTO.getName()),
                () -> assertEquals(COST, returnedDTO.getProductCost()),
                () -> assertEquals(CURRENT_PRICE, returnedDTO.getCurrentPrice()),
                () -> assertEquals(EXPECTED_STOCK_STATUS, returnedDTO.getCurrentStockStatus()),
                () -> assertEquals(EXPECTED_PROFIT_PERCENTAGE, returnedDTO.getCurrentProfitPercentage())
        );
    }

    @Test
    @DisplayName("Dado un ID de producto inexistente, arrojará ProductNotFoundException")
    void givenNonExistingProduct_WhenSearching_ThenThrows_ProductNotFoundException() {

        mockThatProductDoesNotExist();

        assertThrows(ProductNotFoundException.class, () -> productService.getProduct(PRODUCT_ID));
    }

    @Test
    @DisplayName("Dados N productos existentes, al obtener todos, se deberá retornar una lista mediante mapeo por DTO informativo")
    void given_N_ExistingProducts_WhenGettingAll_ThenListIsReturned() {

        mockRepoToReturnList();

        List<ProductInfoDTO> infoDTOList = productService.getProductsList();

        assertAll(
                "Verificación de campos",
                () -> assertNotNull(infoDTOList),
                () -> assertEquals(1, infoDTOList.size()),
                () -> assertEquals(NAME, infoDTOList.getFirst().getName()),
                () -> assertEquals(COST, infoDTOList.getFirst().getProductCost()),
                () -> assertEquals(CURRENT_PRICE, infoDTOList.getFirst().getCurrentPrice()),
                () -> assertEquals(EXPECTED_STOCK_STATUS, infoDTOList.getFirst().getCurrentStockStatus()),
                () -> assertEquals(EXPECTED_PROFIT_PERCENTAGE, infoDTOList.getFirst().getCurrentProfitPercentage())
        );
    }

    @Test
    @DisplayName("Dado un producto existente, deberá poder ser actualizado exitosamente")
    void givenExistingProduct_WhenUpdating_ThenIsSuccesfullyPersisted() {

        mockExistingProduct();

        updateProduct();

        captureProduct();

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateInteraction();
        verifyThatProductWasUpdated();

        Product capturedProduct = productCaptor.getValue();

        assertAll(
                "Verificación de campos",
                () -> assertNotNull(capturedProduct),
                () -> assertEquals(UPDATE_NAME, capturedProduct.getName()),
                () -> assertEquals(UPDATE_COST, capturedProduct.getProductCost()),
                () -> assertEquals(UPDATE_PRICE, capturedProduct.getCurrentPrice()),
                () -> assertEquals(UPDATE_CURRENT_STOCK_LEVEL, capturedProduct.getCurrentStockLevel()),
                () -> assertEquals(UPDATE_SAFETY_STOCK_LEVEL, capturedProduct.getSafetyStockLevel()),
                () -> assertEquals(CATEGORY, capturedProduct.getCategory())
        );
    }

    @Test
    @DisplayName("Dado un producto inexistente, al intentar actualizar sus datos, deberá arrojar ProductNotFoundException")
    void givenNonExistingProduct_WhenUpdating_ThenThrows_ProductNotFoundException() {

        mockThatProductDoesNotExist();

        assertThrows(ProductNotFoundException.class, this::updateProduct);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con 1 o más valores NULL, aquellos campos no serán persistidos y la entidad deberá mantener su valor anterior")
    void givenAnyNullValue_WhenUpdating_ThenProductIsNotPersisted() {

        setAllFieldsOnNullForUpdate();

        mockExistingProduct();

        updateProduct();

        captureProduct();

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateInteraction();
        verifyThatProductWasUpdated();

        Product capturedProduct = productCaptor.getValue();

        assertAll(
                "Verificación de campos",
                () -> assertNotNull(capturedProduct),
                () -> assertEquals(NAME, capturedProduct.getName()),
                () -> assertEquals(COST, capturedProduct.getProductCost()),
                () -> assertEquals(CURRENT_PRICE, capturedProduct.getCurrentPrice()),
                () -> assertEquals(CATEGORY, capturedProduct.getCategory()),
                () -> assertEquals(CURRENT_STOCK_LEVEL, capturedProduct.getCurrentStockLevel()),
                () -> assertEquals(SAFETY_STOCK_LEVEL, capturedProduct.getSafetyStockLevel())
        );
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre en blanco, la validación deberá fallar y no se persistirán cambios")
    void givenBlankName_WhenUpdating_ThenProductIsNotPersisted() {

        mockExistingProduct();

        doThrow(BlankProductNameException.class).when(validator).validateDTO(updateDTO);

        assertThrows(BlankProductNameException.class, this::updateProduct);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteraction();
        verifyThatProductWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre con caractéres inválidos, la validación deberá fallar y no se persistirán cambios")
    void givenInvalidName_WhenUpdating_ThenProductIsNotPersisted() {

        mockExistingProduct();

        doThrow(InvalidProductNameException.class).when(validator).validateDTO(updateDTO);

        assertThrows(InvalidProductNameException.class, this::updateProduct);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteraction();
        verifyThatProductWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre válido que ya fue registrado por otro producto," +
            " la validación deberá fallar y arrojará DuplicatedProductNameException")
    void givenDuplicatedName_WhenUpdating_ThenProductIsNotPersisted() {

        mockExistingProduct();
        mockThatProductNameWillCauseConflictOnUpdate();

        assertThrows(DuplicatedProductNameException.class, this::updateProduct);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteraction();
        verifyThatProductWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un precio de costo negativo o cero, la validación inicial deberá fallar y no se persistirán cambios")
    void givenCostLowerOrEqualsThanZero_WhenUpdating_ThenProductIsNotPersisted() {

        mockExistingProduct();

        doThrow(InvalidProductCostException.class).when(validator).validateDTO(updateDTO);

        assertThrows(InvalidProductCostException.class, this::updateProduct);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteraction();
        verifyThatProductWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un precio de venta al público negativo o cero, la validación inicial deberá fallar y no se persistirán cambios")
    void givenCurrentPriceLowerOrEqualsThanZero_WhenUpdating_ThenProductIsNotPersisted() {

        mockExistingProduct();

        doThrow(InvalidProductCurrentPriceException.class).when(validator).validateDTO(updateDTO);

        assertThrows(InvalidProductCurrentPriceException.class, this::updateProduct);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteraction();
        verifyThatProductWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un DTO de actualización que modifique el precio de venta pero no modifique el precio de costo," +
            " el nuevo precio de venta no podrá ser menor al precio de costo persistido anteriormente," +
            " por lo tanto no se persistirán cambios en el producto")
    void givenCurrentPriceLowerOrEqualsThanCost_WhenUpdatingOnlyPrice_ThenProductIsNotPersisted() {

        updateDTO.setProductCost(null);
        updateDTO.setCurrentPrice(UPDATE_PRICE_LOWER_THAN_PERSISTED_COST);

        mockExistingProduct();

        assertThrows(InvalidProductCurrentPriceException.class, this::updateProduct);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteraction();
        verifyThatProductWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un producto existente, deberá poder ser eliminado exitosamente")
    void givenExistingProduct_WhenDeleting_ThenIsSuccessfullyDeleted() {

        mockExistingProduct();

        deleteProduct();

        verifyThatProductWasDeleted();
    }

    @Test
    @DisplayName("Dado un producto inexistente, al intentar eliminarlo arrojará ProductNotFoundException")
    void givenNonExistingProduct_WhenDeleting_ThenThrows_ProductNotFoundException() {

        mockThatProductDoesNotExist();

        assertThrows(ProductNotFoundException.class, this::deleteProduct);
    }

    private void setupCreationDTO() {

        creationDTO = ProductCreationDTO.builder()
                .name(NAME)
                .productCost(COST)
                .currentPrice(CURRENT_PRICE)
                .category(CATEGORY)
                .currentStockLevel(CURRENT_STOCK_LEVEL)
                .safetyStockLevel(SAFETY_STOCK_LEVEL)
                .build();
    }

    private void setupUpdateDTO() {

        updateDTO = ProductUpdateDTO.builder()
                .name(UPDATE_NAME)
                .productCost(UPDATE_COST)
                .currentPrice(UPDATE_PRICE)
                .category(CATEGORY)
                .currentStockLevel(UPDATE_CURRENT_STOCK_LEVEL)
                .safetyStockLevel(UPDATE_SAFETY_STOCK_LEVEL)
                .build();
    }

    private void setupProduct() {

        product = Product.builder()
                .productID(PRODUCT_ID)
                .name(NAME)
                .productCost(COST)
                .currentPrice(CURRENT_PRICE)
                .category(CATEGORY)
                .currentStockLevel(CURRENT_STOCK_LEVEL)
                .safetyStockLevel(SAFETY_STOCK_LEVEL)
                .build();
    }

    private void captureProduct() {

        verify(productRepository).save(productCaptor.capture());
    }

    private void registerNewProduct() {

        productService.registerNewProduct(creationDTO);
    }

    private void updateProduct() {

        productService.updateProduct(PRODUCT_ID, updateDTO);
    }

    private void deleteProduct() {

        productService.deleteProduct(PRODUCT_ID);
    }

    private void setAllFieldsOnNullForUpdate() {

        updateDTO.setName(null);
        updateDTO.setProductCost(null);
        updateDTO.setCurrentPrice(null);
        updateDTO.setCategory(null);
        updateDTO.setCurrentStockLevel(null);
        updateDTO.setSafetyStockLevel(null);
    }

    private void verifyValidatorCreationInteraction() {

        verify(validator).validateDTO(creationDTO);
    }

    private void verifyValidatorUpdateInteraction() {

        verify(validator).validateDTO(updateDTO);
    }

    private void verifyMapperCreationInteraction() {

        verify(mapper).mapProductCreationDTOtoEntity(creationDTO);
    }

    private void verifyMapperCreationNoInteraction() {

        verify(mapper, never()).mapProductCreationDTOtoEntity(creationDTO);
    }

    private void verifyMapperUpdateInteraction() {

        verify(validator).validateDTO(updateDTO);
    }

    private void verifyMapperUpdateNoInteraction() {

        verify(mapper, never()).mapProductUpdateDTOtoEntity(product, updateDTO);
    }

    private void verifyThatProductWasRegistered() {

        verify(productRepository, times(1)).save(any());
    }

    private void verifyThatProductWasNotRegistered() {

        verify(productRepository, never()).save(any());
    }

    private void verifyThatProductWasUpdated() {

        verify(productRepository, times(1)).save(product);
    }

    private void verifyThatProductWasNotUpdated() {

        verify(productRepository, never()).save(product);
    }

    private void verifyThatProductWasDeleted() {

        verify(productRepository, times(1)).delete(any());
    }

    private void mockExistingProduct() {

        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
    }

    private void mockThatProductDoesNotExist() {

        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());
    }

    private void mockThatProductNameWillCauseConflict() {

        when(productRepository.existsByName(NAME)).thenReturn(true);
    }

    private void mockThatProductNameWillCauseConflictOnUpdate() {

        when(productRepository.existsByNameAndProductIDNot(UPDATE_NAME, PRODUCT_ID)).thenReturn(true);
    }

    private void mockRepoToReturnList() {

        when(productRepository.findAll()).thenReturn(List.of(product));
    }
}
