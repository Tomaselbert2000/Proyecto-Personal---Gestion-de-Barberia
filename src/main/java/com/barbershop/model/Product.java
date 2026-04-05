package com.barbershop.model;

import com.barbershop.enums.ProductCategory;
import com.barbershop.enums.ProductPresentationUnit;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private Integer safetyStockLevel;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    private LocalDateTime creationDate;

    private String imageFilePath;
}
