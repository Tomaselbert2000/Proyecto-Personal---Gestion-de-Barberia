package com.mapper.implementation;

import com.dto.product.ProductCreationDTO;
import com.dto.product.ProductInfoDTO;
import com.dto.product.ProductUpdateDTO;
import com.mapper.interfaces.ProductMapper;
import com.model.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mapper.helper.MapperHelper.checkIfMapperInputIsNull;
import static com.utils.strings.StringCleaner.formatAsSentence;

@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product mapProductCreationDTOtoEntity(ProductCreationDTO dto) {

        checkIfMapperInputIsNull(dto);

        LocalDateTime creationDate = LocalDateTime.now();

        return Product.builder()
                .name(formatAsSentence(dto.getName()).trim())
                .optionalDescription(dto.getOptionalDescription().trim())
                .brandName(formatAsSentence(dto.getBrandName()).trim())
                .presentationUnit(dto.getPresentationUnit())
                .presentationSize(dto.getPresentationSize())
                .productCost(dto.getProductCost())
                .minPrice(dto.getMinPrice())
                .currentPrice(dto.getCurrentPrice())
                .productWholeSalePrice(dto.getProductWholeSalePrice())
                .maxDiscountPercentage(dto.getMaxDiscountPercentage())
                .currentStockLevel(dto.getCurrentStockLevel())
                .safetyStockLevel(dto.getSafetyStockLevel())
                .category(dto.getCategory())
                .creationDate(creationDate)
                .imageFilePath(dto.getImageFilePath())
                .build();
    }

    @Override
    public Product mapProductUpdateDTOtoEntity(Product product, ProductUpdateDTO updateDTO) {

        checkIfMapperInputIsNull(product, updateDTO);

        setUpdatedDataOnEntity(product, updateDTO);

        return product;
    }

    @Override
    public ProductInfoDTO mapProductToInfoDTO(Product product) {

        checkIfMapperInputIsNull(product);

        product.retrieveCurrentStockStatus();

        return ProductInfoDTO.builder()
                .id(product.getProductID())
                .name(product.getName())
                .productCost(product.getProductCost())
                .currentPrice(product.getCurrentPrice())
                .calculatedProfit(product.calculateCurrentProfit())
                .currentStockLevel(product.getCurrentStockLevel())
                .safetyStockLevel(product.getSafetyStockLevel())
                .currentStockStatus(product.getStockStatus())
                .imageFilePath(product.getImageFilePath())
                .build();
    }

    @Override
    public List<ProductInfoDTO> mapProductToInfoDTO(List<Product> productList) {

        checkIfMapperInputIsNull(productList);

        return productList.stream().map(this::mapProductToInfoDTO).collect(Collectors.toList());
    }

    @Override
    public ProductUpdateDTO mapProductToUpdateDTO(Product product) {

        checkIfMapperInputIsNull(product);

        return ProductUpdateDTO.builder()
                .name(product.getName())
                .optionalDescription(product.getOptionalDescription())
                .brandName(product.getBrandName())
                .presentationUnit(product.getPresentationUnit())
                .presentationSize(product.getPresentationSize())
                .productCost(product.getProductCost())
                .minPrice(product.getMinPrice())
                .currentPrice(product.getCurrentPrice())
                .productWholeSalePrice(product.getProductWholeSalePrice())
                .maxDiscountPercentage(product.getMaxDiscountPercentage())
                .category(product.getCategory())
                .currentStockLevel(product.getCurrentStockLevel())
                .safetyStockLevel(product.getSafetyStockLevel())
                .imageFilePath(product.getImageFilePath())
                .build();
    }

    private void setUpdatedDataOnEntity(Product product, ProductUpdateDTO updateDTO) {

        if (updateDTO.getName() != null) product.setName(formatAsSentence(updateDTO.getName()));

        if (updateDTO.getBrandName() != null)
            product.setBrandName(formatAsSentence(updateDTO.getBrandName()));

        if (updateDTO.getOptionalDescription() != null)
            product.setOptionalDescription(updateDTO.getOptionalDescription().trim());

        if (updateDTO.getPresentationUnit() != null) product.setPresentationUnit(updateDTO.getPresentationUnit());

        if (updateDTO.getPresentationSize() != null) product.setPresentationSize(updateDTO.getPresentationSize());

        if (updateDTO.getProductCost() != null) product.setProductCost(updateDTO.getProductCost());

        if (updateDTO.getMinPrice() != null) product.setMinPrice(updateDTO.getMinPrice());

        if (updateDTO.getCurrentPrice() != null) product.setCurrentPrice(updateDTO.getCurrentPrice());

        if (updateDTO.getMaxDiscountPercentage() != null)
            product.setMaxDiscountPercentage(updateDTO.getMaxDiscountPercentage());

        if (updateDTO.getCategory() != null) product.setCategory(updateDTO.getCategory());

        if (updateDTO.getCurrentStockLevel() != null) product.setCurrentStockLevel(updateDTO.getCurrentStockLevel());

        if (updateDTO.getSafetyStockLevel() != null) product.setSafetyStockLevel(updateDTO.getSafetyStockLevel());

        if (updateDTO.getImageFilePath() != null) product.setImageFilePath(updateDTO.getImageFilePath());
    }
}
