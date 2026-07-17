package com.service.helper;

import com.dto.product.ProductCreationDTO;
import com.dto.product.ProductDTOCommonMethods;
import com.dto.product.ProductUpdateDTO;
import com.mapper.interfaces.ProductMapper;
import com.model.Product;
import com.repository.ProductRepository;
import com.service.interfaces.ProductService;
import com.validation.product.ProductValidator;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ProductServiceTestHelper {

    public static void captureProduct(ProductRepository productRepository, ArgumentCaptor<Product> productCaptor) {

        verify(productRepository).save(productCaptor.capture());
    }

    public static void registerNewProduct(ProductService productService, ProductCreationDTO creationDTO) {

        productService.registerNewProduct(creationDTO);
    }

    public static void updateProduct(ProductService productService, Product product, ProductUpdateDTO updateDTO) {

        productService.updateProduct(product.getProductID(), updateDTO);
    }

    public static void deleteProduct(ProductService productService, Product product) {

        productService.deleteProduct(product.getProductID());
    }

    public static void verifyValidatorCreationInteraction(ProductValidator validator, ProductCreationDTO creationDTO) {

        verify(validator).validateDTO(creationDTO);
    }

    public static void verifyValidatorUpdateInteraction(ProductValidator validator, ProductUpdateDTO updateDTO) {

        verify(validator).validateDTO(updateDTO);
    }

    public static void verifyMapperCreationInteraction(ProductMapper mapper, ProductCreationDTO creationDTO) {

        verify(mapper).mapProductCreationDTOtoEntity(creationDTO);
    }

    public static void verifyMapperCreationNoInteraction(ProductMapper mapper, ProductCreationDTO creationDTO) {

        verify(mapper, never()).mapProductCreationDTOtoEntity(creationDTO);
    }

    public static void verifyMapperUpdateInteraction(ProductMapper mapper, Product product, ProductUpdateDTO updateDTO) {

        verify(mapper).mapProductUpdateDTOtoEntity(product, updateDTO);
    }

    public static void verifyMapperUpdateNoInteraction(ProductMapper mapper, Product product, ProductUpdateDTO updateDTO) {

        verify(mapper, never()).mapProductUpdateDTOtoEntity(product, updateDTO);
    }

    public static void mockExistingProduct(ProductRepository productRepository, Product product) {

        when(productRepository.findById(product.getProductID())).thenReturn(Optional.of(product));
    }

    public static void mockThatProductDoesNotExist(ProductRepository productRepository, Product product) {

        when(productRepository.findById(product.getProductID())).thenReturn(Optional.empty());
    }

    public static void mockThatProductNameWillCauseConflict(ProductRepository productRepository, Product product) {

        when(productRepository.existsByName(product.getName())).thenReturn(true);
    }

    public static void mockThatProductNameWillCauseConflictOnUpdate(ProductRepository productRepository, ProductUpdateDTO updateDTO, Product product) {

        when(productRepository.existsByNameAndProductIDNot(updateDTO.getName(), product.getProductID())).thenReturn(true);
    }

    public static void mockRepoToReturnList(ProductRepository productRepository, List<Product> products) {

        when(productRepository.findAll()).thenReturn(products);
    }

    public static <T extends ProductDTOCommonMethods> void mockValidatorToThrowException(ProductValidator validator, Exception exception, T dto) {

        doThrow(exception).when(validator).validateDTO(dto);
    }
}