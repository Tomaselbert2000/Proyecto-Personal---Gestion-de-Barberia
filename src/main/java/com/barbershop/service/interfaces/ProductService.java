package com.barbershop.service.interfaces;

import com.barbershop.dto.product.ProductCreationDTO;
import com.barbershop.dto.product.ProductInfoDTO;
import com.barbershop.dto.product.ProductUpdateDTO;
import com.barbershop.enums.ProductCategory;
import com.barbershop.enums.StockStatus;

import java.util.List;

public interface ProductService {

    void registerNewProduct(ProductCreationDTO newProduct);

    void deleteProduct(Long productID);

    List<ProductInfoDTO> getProductsList();

    ProductInfoDTO getProduct(Long productID);

    void updateProduct(Long productID, ProductUpdateDTO product);

    Long getProductsRegisteredCount();

    Long getProductsCreatedThisMonth();

    Long getProductsOnCriticalStockCount();

    Long getProductsOnLowStock();

    Double calculateTotalStockValue();

    void generateMonthlyStockValueHistory();

    Double calculateTotalStockValuePercentageVariationVsLastMonth();

    List<ProductInfoDTO> liveSearch(String productName, ProductCategory selectedCategory, StockStatus selectedStatus);
}
