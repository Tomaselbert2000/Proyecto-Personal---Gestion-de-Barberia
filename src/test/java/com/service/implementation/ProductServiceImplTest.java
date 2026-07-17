package com.service.implementation;

import com.abstract_test_class.BaseServiceTest;
import com.dto.product.ProductCreationDTO;
import com.dto.product.ProductInfoDTO;
import com.dto.product.ProductUpdateDTO;
import com.exceptions.product.*;
import com.mapper.implementation.ProductMapperImpl;
import com.mapper.interfaces.ProductMapper;
import com.model.Product;
import com.repository.ProductRepository;
import com.validation.product.ProductValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static com.factory.ProductTestDataFactory.*;
import static com.service.helper.ProductServiceTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceImplTest extends BaseServiceTest<Product, ProductRepository> {

    private final ProductCreationDTO creationDTO = buildValidProductCreationDTO();
    private final ProductUpdateDTO updateDTO = buildValidProductUpdateDTO();
    private final Product product = buildValidProduct();
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductValidator validator;
    @Spy
    private ProductMapper mapper = new ProductMapperImpl();
    @Captor
    private ArgumentCaptor<Product> productCaptor;
    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("Dado un DTO de creación con datos correctos, deberá ser persistido exitosamente")
    void givenDTO_WithValidInformation_WhenCreating_ThenIsSuccesfullyPersisted() {

        registerNewProduct(productService, creationDTO);

        captureProduct(productRepository, productCaptor);

        verifyCreationProcessSuccess();

        Product capturedProduct = productCaptor.getValue();

        checkCreationDTOAssertions(capturedProduct);
    }

    @Test
    @DisplayName("Dado un DTO de creación con cualquiera de sus valores en NULL, el producto no será persistido")
    void givenAnyNullValue_WhenCreating_ThenProductIsNotPersisted() {

        mockValidatorToThrowException(validator, new NullProductInputDataException(), creationDTO);

        assertThrows(NullProductInputDataException.class, () -> registerNewProduct(productService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre en blanco, el producto no será persistido")
    void givenBlankName_WhenCreating_ThenProductIsNotPersisted() {

        mockValidatorToThrowException(validator, new BlankProductNameException(), creationDTO);

        assertThrows(BlankProductNameException.class, () -> registerNewProduct(productService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre inválido, el producto no será persistido")
    void givenInvalidName_WhenCreating_ThenProductIsNotPersisted() {

        mockValidatorToThrowException(validator, new InvalidProductNameException(), creationDTO);

        assertThrows(InvalidProductNameException.class, () -> registerNewProduct(productService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre válido que ya fue registrado por otro producto," +
            " la validación deberá fallar y arrojará DuplicatedProductNameException")
    void givenDuplicatedName_WhenCreating_ThenThrows_DuplicatedProductNameException() {

        mockThatProductNameWillCauseConflict(productRepository, product);

        assertThrows(DuplicatedProductNameException.class, () -> registerNewProduct(productService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio de costo negativo o cero, el producto no será persistido")
    void givenProductCostLowerOrEqualsThanZero_WhenCreating_ThenProductIsNotPersisted() {

        mockValidatorToThrowException(validator, new InvalidProductCostException(), creationDTO);

        assertThrows(InvalidProductCostException.class, () -> registerNewProduct(productService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio de venta al público negativo o cero, el producto no será persistido")
    void givenProductPriceLowerOrEqualsThanZero_WhenCreating_ThenProductIsNotPersisted() {

        mockValidatorToThrowException(validator, new InvalidProductCurrentPriceException(), creationDTO);

        assertThrows(InvalidProductCurrentPriceException.class, () -> registerNewProduct(productService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un precio de venta al público menor al valor de costo ingresado, el producto no será persistido")
    void givenProductCurrentPriceLowerThanCost_WhenCreating_ThenProductIsNotPersisted() {

        mockValidatorToThrowException(validator, new InvalidProductCurrentPriceException(), creationDTO);

        assertThrows(InvalidProductCurrentPriceException.class, () -> registerNewProduct(productService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de stock actual menor que cero, el producto no será persistido")
    void givenCurrentStockLowerThanZero_WhenCreating_ThenProductIsNotPersisted() {

        mockValidatorToThrowException(validator, new NegativeCurrentStockLevelException(), creationDTO);

        assertThrows(NegativeCurrentStockLevelException.class, () -> registerNewProduct(productService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de stock mínimo menor que cero, el producto no será persistido")
    void givenSafetyStockLevelLowerThanZero_WhenCreating_ThenProductIsNotPersisted() {

        mockValidatorToThrowException(validator, new NegativeSafetyStockLevelException(), creationDTO);

        assertThrows(NegativeSafetyStockLevelException.class, () -> registerNewProduct(productService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un producto existente, deberá poder retornar su información mediante mapeo por DTO informativo")
    void givenExistingProduct_WhenSearching_ThenReturnsItsInformation() {

        mockExistingProduct(productRepository, product);

        ProductInfoDTO returnedDTO = productService.getProduct(product.getProductID());

        assertAll(
                ("Verificación de campos"),
                () -> assertEquals(product.getName(), returnedDTO.getName()),
                () -> assertEquals(product.getProductCost(), returnedDTO.getProductCost()),
                () -> assertEquals(product.getCurrentPrice(), returnedDTO.getCurrentPrice()),
                () -> assertNotNull(returnedDTO.getCurrentStockStatus()),
                () -> assertNotNull(returnedDTO.getCurrentProfitPercentage())
        );
    }

    @Test
    @DisplayName("Dado un ID de producto inexistente, arrojará ProductNotFoundException")
    void givenNonExistingProduct_WhenSearching_ThenThrows_ProductNotFoundException() {

        mockThatProductDoesNotExist(productRepository, product);

        assertThrows(ProductNotFoundException.class, () -> productService.getProduct(product.getProductID()));
    }

    @Test
    @DisplayName("Dados N productos existentes, al obtener todos, se deberá retornar una lista mediante mapeo por DTO informativo")
    void given_N_ExistingProducts_WhenGettingAll_ThenListIsReturned() {

        mockRepoToReturnList(productRepository, List.of(product));

        List<ProductInfoDTO> infoDTOList = productService.getProductsList();

        checkInfoDTOAssertions(infoDTOList);
    }

    @Test
    @DisplayName("Dado un producto existente, deberá poder ser actualizado exitosamente")
    void givenExistingProduct_WhenUpdating_ThenIsSuccesfullyPersisted() {

        mockExistingProduct(productRepository, product);

        updateProduct(productService, product, updateDTO);

        captureProduct(productRepository, productCaptor);

        verifyUpdateProcessSuccess();

        Product capturedProduct = productCaptor.getValue();

        checkUpdateDTOAssertions(capturedProduct);
    }

    @Test
    @DisplayName("Dado un producto inexistente, al intentar actualizar sus datos, deberá arrojar ProductNotFoundException")
    void givenNonExistingProduct_WhenUpdating_ThenThrows_ProductNotFoundException() {

        mockThatProductDoesNotExist(productRepository, product);

        assertThrows(ProductNotFoundException.class, () -> updateProduct(productService, product, updateDTO));
    }

    @Test
    @DisplayName("Dado un DTO de actualización con 1 o más valores NULL, aquellos campos no serán persistidos y la entidad deberá mantener su valor anterior")
    void givenAnyNullValue_WhenUpdating_ThenProductIsNotPersisted() {

        setAllFieldsOnNull(updateDTO);

        mockExistingProduct(productRepository, product);

        updateProduct(productService, product, updateDTO);

        captureProduct(productRepository, productCaptor);

        verifyUpdateProcessSuccess();

        Product capturedProduct = productCaptor.getValue();

        assertNotNull(capturedProduct);

        assertAll(
                "Verificación de campos",
                () -> assertEquals(product.getName(), capturedProduct.getName()),
                () -> assertEquals(product.getProductCost(), capturedProduct.getProductCost()),
                () -> assertEquals(product.getCurrentPrice(), capturedProduct.getCurrentPrice()),
                () -> assertEquals(product.getCategory(), capturedProduct.getCategory()),
                () -> assertEquals(product.getCurrentStockLevel(), capturedProduct.getCurrentStockLevel()),
                () -> assertEquals(product.getSafetyStockLevel(), capturedProduct.getSafetyStockLevel())
        );
    }

    private void setAllFieldsOnNull(ProductUpdateDTO updateDTO) {
        updateDTO.setName(null);
        updateDTO.setOptionalDescription(null);
        updateDTO.setBrandName(null);
        updateDTO.setPresentationUnit(null);
        updateDTO.setPresentationSize(null);
        updateDTO.setProductCost(null);
        updateDTO.setMinPrice(null);
        updateDTO.setCurrentPrice(null);
        updateDTO.setProductWholeSalePrice(null);
        updateDTO.setMaxDiscountPercentage(null);
        updateDTO.setCategory(null);
        updateDTO.setCurrentStockLevel(null);
        updateDTO.setSafetyStockLevel(null);
        updateDTO.setImageFilePath(null);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre en blanco, la validación deberá fallar y no se persistirán cambios")
    void givenBlankName_WhenUpdating_ThenProductIsNotPersisted() {

        mockExistingProduct(productRepository, product);

        mockValidatorToThrowException(validator, new BlankProductNameException(), updateDTO);

        assertThrows(BlankProductNameException.class, () -> updateProduct(productService, product, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un nombre con caractéres inválidos, la validación deberá fallar y no se persistirán cambios")
    void givenInvalidName_WhenUpdating_ThenProductIsNotPersisted() {

        mockExistingProduct(productRepository, product);

        mockValidatorToThrowException(validator, new InvalidProductNameException(), updateDTO);

        assertThrows(InvalidProductNameException.class, () -> updateProduct(productService, product, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre válido que ya fue registrado por otro producto," +
            " la validación deberá fallar y arrojará DuplicatedProductNameException")
    void givenDuplicatedName_WhenUpdating_ThenProductIsNotPersisted() {

        mockExistingProduct(productRepository, product);
        mockThatProductNameWillCauseConflictOnUpdate(productRepository, updateDTO, product);

        assertThrows(DuplicatedProductNameException.class, () -> updateProduct(productService, product, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un precio de costo negativo o cero, la validación inicial deberá fallar y no se persistirán cambios")
    void givenCostLowerOrEqualsThanZero_WhenUpdating_ThenProductIsNotPersisted() {

        mockExistingProduct(productRepository, product);

        mockValidatorToThrowException(validator, new InvalidProductCostException(), updateDTO);

        assertThrows(InvalidProductCostException.class, () -> updateProduct(productService, product, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de actualización con un precio de venta al público negativo o cero, la validación inicial deberá fallar y no se persistirán cambios")
    void givenCurrentPriceLowerOrEqualsThanZero_WhenUpdating_ThenProductIsNotPersisted() {

        mockExistingProduct(productRepository, product);

        mockValidatorToThrowException(validator, new InvalidProductCurrentPriceException(), updateDTO);

        assertThrows(InvalidProductCurrentPriceException.class, () -> updateProduct(productService, product, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de actualización que modifique el precio de venta pero no modifique el precio de costo," +
            " el nuevo precio de venta no podrá ser menor al precio de costo persistido anteriormente," +
            " por lo tanto no se persistirán cambios en el producto")
    void givenCurrentPriceLowerOrEqualsThanCost_WhenUpdatingOnlyPrice_ThenProductIsNotPersisted() {

        updateDTO.setProductCost(null);
        updateDTO.setCurrentPrice(product.getProductCost() - 1);

        mockExistingProduct(productRepository, product);

        assertThrows(InvalidProductCurrentPriceException.class, () -> updateProduct(productService, product, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un producto existente, deberá poder ser eliminado exitosamente")
    void givenExistingProduct_WhenDeleting_ThenIsSuccessfullyDeleted() {

        mockExistingProduct(productRepository, product);

        deleteProduct(productService, product);

        verifyThatEntityWasDeleted(product);
    }

    @Test
    @DisplayName("Dado un producto inexistente, al intentar eliminarlo arrojará ProductNotFoundException")
    void givenNonExistingProduct_WhenDeleting_ThenThrows_ProductNotFoundException() {

        mockThatProductDoesNotExist(productRepository, product);

        assertThrows(ProductNotFoundException.class, () -> deleteProduct(productService, product));
    }

    @Override
    protected ProductRepository getPrimaryRepository() {

        return productRepository;
    }

    private void checkCreationDTOAssertions(Product capturedProduct) {

        assertNotNull(capturedProduct);

        assertAll(
                ("Verificación de campos"),
                () -> assertEquals(creationDTO.getName(), capturedProduct.getName()),
                () -> assertEquals(creationDTO.getProductCost(), capturedProduct.getProductCost()),
                () -> assertEquals(creationDTO.getCurrentPrice(), capturedProduct.getCurrentPrice()),
                () -> assertEquals(creationDTO.getCategory(), capturedProduct.getCategory()),
                () -> assertEquals(creationDTO.getCurrentStockLevel(), capturedProduct.getCurrentStockLevel()),
                () -> assertEquals(creationDTO.getSafetyStockLevel(), capturedProduct.getSafetyStockLevel())
        );
    }

    private void checkInfoDTOAssertions(List<ProductInfoDTO> infoDTOList) {

        assertNotNull(infoDTOList);

        assertAll(
                "Verificación de campos",
                () -> assertEquals(1, infoDTOList.size()),
                () -> assertEquals(product.getName(), infoDTOList.getFirst().getName()),
                () -> assertEquals(product.getProductCost(), infoDTOList.getFirst().getProductCost()),
                () -> assertEquals(product.getCurrentPrice(), infoDTOList.getFirst().getCurrentPrice()),
                () -> assertNotNull(infoDTOList.getFirst().getCurrentStockStatus()),
                () -> assertNotNull(infoDTOList.getFirst().getCurrentProfitPercentage())
        );
    }

    private void checkUpdateDTOAssertions(Product capturedProduct) {

        assertNotNull(capturedProduct);

        assertAll(
                "Verificación de campos",
                () -> assertEquals(updateDTO.getName(), capturedProduct.getName()),
                () -> assertEquals(updateDTO.getProductCost(), capturedProduct.getProductCost()),
                () -> assertEquals(updateDTO.getCurrentPrice(), capturedProduct.getCurrentPrice()),
                () -> assertEquals(updateDTO.getCurrentStockLevel(), capturedProduct.getCurrentStockLevel()),
                () -> assertEquals(updateDTO.getSafetyStockLevel(), capturedProduct.getSafetyStockLevel()),
                () -> assertEquals(updateDTO.getCategory(), capturedProduct.getCategory())
        );
    }

    private void verifyCreationProcessSuccess() {

        verifyValidatorCreationInteraction(validator, creationDTO);
        verifyMapperCreationInteraction(mapper, creationDTO);
        verifyThatEntityWasSaved();
    }

    private void verifyCreationProcessFailure() {

        verifyValidatorCreationInteraction(validator, creationDTO);
        verifyMapperCreationNoInteraction(mapper, creationDTO);
        verifyThatEntityWasNotSaved();
    }

    private void verifyUpdateProcessSuccess() {

        verifyValidatorUpdateInteraction(validator, updateDTO);
        verifyMapperUpdateInteraction(mapper, product, updateDTO);
        verifyThatEntityWasSaved();
    }

    private void verifyUpdateProcessFailure() {

        verifyValidatorUpdateInteraction(validator, updateDTO);
        verifyMapperUpdateNoInteraction(mapper, product, updateDTO);
        verifyThatEntityWasNotSaved();
    }
}