package com.service.interfaces;

import com.dto.product.ProductCreationDTO;
import com.dto.product.ProductInfoDTO;
import com.dto.product.ProductUpdateDTO;
import com.dto.stats.*;
import com.enums.ProductCategory;
import com.enums.StockStatus;

import java.util.List;

public interface ProductService {

    void registerNewProduct(ProductCreationDTO newProduct);

    void deleteProduct(Long productID);

    List<ProductInfoDTO> getProductsList();

    void updateProduct(Long productID, ProductUpdateDTO product);

    List<ProductInfoDTO> liveSearch(String productName, ProductCategory selectedCategory, StockStatus selectedStatus);

    ProductUpdateDTO getProductForUpdate(Long productID);

    ProductTotalStockStatsDTO getProductCountAndStockStats();

    ProductMostSoldStatsDTO getProductMostSoldStats();

    ProductHighestRevenueStatsDTO getProductHighestRevenueStats();

    ProductStockValueStatsDTO getProductStockValueStat();

    InventoryAlertStatsDTO getInventoryAlertStat();
}
