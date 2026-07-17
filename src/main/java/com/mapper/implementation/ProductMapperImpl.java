package com.mapper.implementation;

import com.dto.product.ProductCreationDTO;
import com.dto.product.ProductInfoDTO;
import com.dto.product.ProductUpdateDTO;
import com.enums.StockStatus;
import com.exceptions.common.NullMapperInputException;
import com.mapper.interfaces.ProductMapper;
import com.model.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.utils.strings.StringCleaner.formatAsSentence;

@Component
public class ProductMapperImpl implements ProductMapper {

    private static final Double MIN_COST_NUMERICAL_VALUE = 0.0;
    private static final Double CRITICAL_STOCK_PERCENTAGE = 0.25;
    private static final Double ATTENTION_TO_STOCK_PERCENTAGE = 1.5;
    private static final Double OVERSTOCKED_PERCENTAGE = 2.0;

    @Override
    public Product mapProductCreationDTOtoEntity(ProductCreationDTO creationDTO) {

        if (creationDTO == null) throw new NullMapperInputException();

        LocalDateTime creationDate = LocalDateTime.now();

        return Product.builder()
                .name(formatAsSentence(creationDTO.getName()).trim())
                .optionalDescription(creationDTO.getOptionalDescription().trim())
                .brandName(formatAsSentence(creationDTO.getBrandName()).trim())
                .presentationUnit(creationDTO.getPresentationUnit())
                .presentationSize(creationDTO.getPresentationSize())
                .productCost(creationDTO.getProductCost())
                .minPrice(creationDTO.getMinPrice())
                .currentPrice(creationDTO.getCurrentPrice())
                .productWholeSalePrice(creationDTO.getProductWholeSalePrice())
                .maxDiscountPercentage(creationDTO.getMaxDiscountPercentage())
                .currentStockLevel(creationDTO.getCurrentStockLevel())
                .safetyStockLevel(creationDTO.getSafetyStockLevel())
                .category(creationDTO.getCategory())
                .creationDate(creationDate)
                .imageFilePath(creationDTO.getImageFilePath())
                .build();
    }

    @Override
    public Product mapProductUpdateDTOtoEntity(Product product, ProductUpdateDTO updateDTO) {

        if (product == null || updateDTO == null) throw new NullMapperInputException();

        setUpdatedDataOnEntity(product, updateDTO);

        return product;
    }

    @Override
    public ProductInfoDTO mapProductToInfoDTO(Product product) {

        if (product == null) throw new NullMapperInputException();

        Double profitPercentage = calculateCurrentProfit(product);

        StockStatus currentStatus = retrieveCurrentStatus(product);

        return ProductInfoDTO.builder()
                .id(product.getProductID())
                .name(product.getName())
                .productCost(product.getProductCost())
                .currentPrice(product.getCurrentPrice())
                .currentProfitPercentage(profitPercentage)
                .currentStockLevel(product.getCurrentStockLevel())
                .safetyStockLevel(product.getSafetyStockLevel())
                .currentStockStatus(currentStatus)
                .imageFilePath(product.getImageFilePath())
                .build();
    }

    @Override
    public List<ProductInfoDTO> mapProductToInfoDTO(List<Product> productList) {

        if (productList == null) throw new NullMapperInputException();

        return productList.stream().map(this::mapProductToInfoDTO).collect(Collectors.toList());
    }

    @Override
    public ProductUpdateDTO mapProductToUpdateDTO(Product product) {

        if (product == null) throw new NullMapperInputException();

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

    private Double calculateCurrentProfit(Product product) {

        Double cost = product.getProductCost();
        Double price = product.getCurrentPrice();

        if (cost > MIN_COST_NUMERICAL_VALUE) {

            return profitResult(cost, price);
        }

        return 0.0;
    }

    private Double profitResult(Double cost, Double price) {

        return ((price - cost) / cost) * 100;
    }

    private StockStatus retrieveCurrentStatus(Product product) {

        Integer currentLevel = product.getCurrentStockLevel();
        Integer safetyLevel = product.getSafetyStockLevel();

        if (currentLevel == null || safetyLevel == null) return StockStatus.SUFICIENTE;

        if (currentLevel <= (safetyLevel * CRITICAL_STOCK_PERCENTAGE)) return StockStatus.CRITICO;

        if (currentLevel <= safetyLevel) return StockStatus.BAJO;

        if (currentLevel <= (safetyLevel * ATTENTION_TO_STOCK_PERCENTAGE)) return StockStatus.ATENCION;

        if (currentLevel >= (safetyLevel * OVERSTOCKED_PERCENTAGE)) return StockStatus.EXCEDIDO;

        return StockStatus.SUFICIENTE;
    }
}
