package com.service.interfaces;

import com.dto.product.ProductCreationDTO;
import com.dto.product.ProductInfoDTO;
import com.dto.product.ProductUpdateDTO;
import com.enums.ProductCategory;
import com.enums.StockStatus;

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

    ProductUpdateDTO getProductForUpdate(Long productID);
}
