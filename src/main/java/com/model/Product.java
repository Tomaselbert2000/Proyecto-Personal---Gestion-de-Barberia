package com.model;

import com.enums.ProductCategory;
import com.enums.ProductPresentationUnit;
import com.enums.StockStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static com.model.Product.ProductStockLevelConstants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID;

    private String name;
    private String optionalDescription;
    private String brandName;

    private ProductPresentationUnit presentationUnit;
    private Integer presentationSize;

    private Double productCost;
    private Double minPrice;
    private Double currentPrice;
    private Double productWholeSalePrice;
    private Double maxDiscountPercentage;

    private Integer currentStockLevel;

    @Version
    private Integer version;

    private Integer safetyStockLevel;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private LocalDateTime creationDate;

    private String imageFilePath;

    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;

    @PrePersist
    @PreUpdate
    public void retrieveCurrentStockStatus() {

        if (currentStockLevel == null || safetyStockLevel == null) {
            setStockStatus(StockStatus.SUFICIENTE);
            return;
        }

        if (currentStockLevel <= (safetyStockLevel * CRITICAL_STOCK_LEVEL)) {
            setStockStatus(StockStatus.CRITICO);
            return;
        }

        if (currentStockLevel <= safetyStockLevel) {
            setStockStatus(StockStatus.BAJO);
            return;
        }

        if (currentStockLevel <= (safetyStockLevel * ATTENTION_STOCK_LEVEL)) {
            setStockStatus(StockStatus.ATENCION);
            return;
        }

        if (currentStockLevel >= (safetyStockLevel * OVERSTOCKED_LEVEL)) {
            setStockStatus(StockStatus.EXCEDIDO);
            return;
        }

        setStockStatus(StockStatus.SUFICIENTE);
    }

    public Double calculateCurrentProfit() {

        Double cost = getProductCost();
        Double price = getCurrentPrice();

        if (cost != null && cost > 0.0) {

            return profitResult(cost, price);
        }

        return 0.0;
    }

    private Double profitResult(Double cost, Double price) {

        return ((price - cost) / cost) * 100;
    }

    protected static final class ProductStockLevelConstants {

        protected static final Double CRITICAL_STOCK_LEVEL = 0.25;
        protected static final Double ATTENTION_STOCK_LEVEL = 1.5;
        protected static final Double OVERSTOCKED_LEVEL = 2.0;
    }
}
