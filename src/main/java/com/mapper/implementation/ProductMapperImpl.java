package com.mapper.implementation;

import com.dto.product.ProductCreationDTO;
import com.dto.product.ProductInfoDTO;
import com.dto.product.ProductUpdateDTO;
import com.mapper.helper.MapperHelper;
import com.mapper.interfaces.ProductMapper;
import com.model.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

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
    public Product mapProductUpdateDTOtoEntity(Product entity, ProductUpdateDTO dto) {

        checkIfMapperInputIsNull(entity, dto);

        setUpdatedDataOnEntity(entity, dto);

        return entity;
    }

    @Override
    public ProductInfoDTO mapProductToInfoDTO(Product entity) {

        checkIfMapperInputIsNull(entity);

        entity.retrieveCurrentStockStatus();

        return ProductInfoDTO.builder()
                .id(entity.getProductID())
                .name(entity.getName())
                .productCost(entity.getProductCost())
                .currentPrice(entity.getCurrentPrice())
                .calculatedProfit(entity.calculateCurrentProfit())
                .currentStockLevel(entity.getCurrentStockLevel())
                .safetyStockLevel(entity.getSafetyStockLevel())
                .currentStockStatus(entity.getStockStatus())
                .imageFilePath(entity.getImageFilePath())
                .build();
    }

    @Override
    public List<ProductInfoDTO> mapProductToInfoDTO(List<Product> entityList) {

        return MapperHelper.mapList(entityList, this::mapProductToInfoDTO);
    }

    @Override
    public ProductUpdateDTO mapProductToUpdateDTO(Product entity) {

        checkIfMapperInputIsNull(entity);

        return ProductUpdateDTO.builder()
                .name(entity.getName())
                .optionalDescription(entity.getOptionalDescription())
                .brandName(entity.getBrandName())
                .presentationUnit(entity.getPresentationUnit())
                .presentationSize(entity.getPresentationSize())
                .productCost(entity.getProductCost())
                .minPrice(entity.getMinPrice())
                .currentPrice(entity.getCurrentPrice())
                .productWholeSalePrice(entity.getProductWholeSalePrice())
                .maxDiscountPercentage(entity.getMaxDiscountPercentage())
                .category(entity.getCategory())
                .currentStockLevel(entity.getCurrentStockLevel())
                .safetyStockLevel(entity.getSafetyStockLevel())
                .imageFilePath(entity.getImageFilePath())
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
