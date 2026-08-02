package com.service.implementation;

import com.dto.product.ProductCreationDTO;
import com.dto.product.ProductInfoDTO;
import com.dto.product.ProductUpdateDTO;
import com.dto.stats.ProductHighestRevenueStatsDTO;
import com.dto.stats.ProductMostSoldStatsDTO;
import com.dto.stats.ProductStockValueStatsDTO;
import com.dto.stats.ProductTotalStockStatsDTO;
import com.enums.ProductCategory;
import com.enums.StockStatus;
import com.exceptions.product.DuplicatedProductNameException;
import com.exceptions.product.InvalidProductCurrentPriceException;
import com.exceptions.product.ProductNotFoundException;
import com.mapper.interfaces.ProductMapper;
import com.model.Product;
import com.repository.ProductRepository;
import com.repository.SaleItemRepository;
import com.service.interfaces.ProductService;
import com.validation.product.ProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.launcher.constants.StringResource.DisplayString.EMPTY_DATA_STAT;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SaleItemRepository saleItemRepository;
    private final ProductValidator validator;
    private final ProductMapper mapper;

    @Override
    @Transactional
    public void registerNewProduct(ProductCreationDTO creationDTO) {

        validator.validateDTO(creationDTO);

        checkNameAvailability(creationDTO.getName());

        productRepository.save(mapper.mapProductCreationDTOtoEntity(creationDTO));
    }

    @Override
    @Transactional
    public void deleteProduct(Long productID) {

        Product productToDelete = loadProduct(productID);

        productRepository.delete(productToDelete);
    }

    @Override
    public List<ProductInfoDTO> getProductsList() {

        return mapper.mapProductToInfoDTO(productRepository.findAll());
    }

    public ProductInfoDTO getProduct(Long productID) {

        return mapper.mapProductToInfoDTO(loadProduct(productID));
    }

    @Override
    @Transactional
    public void updateProduct(Long productID, ProductUpdateDTO updateDTO) {

        Product productOnDB = loadProduct(productID);

        validator.validateDTO(updateDTO);

        checkNameAvailability(updateDTO.getName(), productID);

        checkIfNewCurrentPriceIsLowerThanCurrentCost(updateDTO.getCurrentPrice(), productOnDB.getProductCost());

        productRepository.save(mapper.mapProductUpdateDTOtoEntity(productOnDB, updateDTO));
    }

    @Override
    public Long getProductsRegisteredCount() {

        return productRepository.count();
    }

    @Override
    public Long getProductsOnLowStock() {

        List<ProductInfoDTO> productInfoDTOList = getProductsList();

        return getCountByStockStatus(productInfoDTOList);
    }

    @Override
    public List<ProductInfoDTO> liveSearch(String productName, ProductCategory selectedCategory, StockStatus selectedStatus) {

        List<Product> products = productRepository.liveSearchWithFilters(productName, selectedCategory);

        List<ProductInfoDTO> mappedProducts = mapper.mapProductToInfoDTO(products);

        if (selectedStatus == null) {

            return mappedProducts;
        }

        return mappedProducts.stream().filter(productInfoDTO -> productInfoDTO.getCurrentStockStatus().equals(selectedStatus)).toList();
    }

    @Override
    public ProductUpdateDTO getProductForUpdate(Long productID) {

        return mapper.mapProductToUpdateDTO(loadProduct(productID));
    }

    @Override
    public ProductTotalStockStatsDTO getProductCountAndStockStats() {

        ProductTotalStockStatsDTO productTotalStockStatsDTO = productRepository.getProductCountAndStockLevelStats();

        if (productTotalStockStatsDTO != null) {

            return productTotalStockStatsDTO;

        } else {

            return emptyProductTotalStockStatsDTO();
        }
    }

    @Override
    public ProductMostSoldStatsDTO getProductMostSoldStats() {

        List<ProductMostSoldStatsDTO> productMostSoldStatsDTOList = saleItemRepository.getProductsSaleStats();

        if (!productMostSoldStatsDTOList.isEmpty()) {

            return productMostSoldStatsDTOList.getFirst();

        } else {

            return emptyProductMostSoldStatsDTO();
        }
    }

    @Override
    public ProductHighestRevenueStatsDTO getProductHighestRevenueStats() {

        List<ProductHighestRevenueStatsDTO> productHighestRevenueStatsDTOS = saleItemRepository.getProductRevenueStats();

        if (!productHighestRevenueStatsDTOS.isEmpty()) {

            return productHighestRevenueStatsDTOS.getFirst();

        } else {

            return emptyProductHighestRevenueStatsDTO();
        }
    }

    @Override
    public ProductStockValueStatsDTO getProductStockValueStat() {

        ProductStockValueStatsDTO productStockValueStatDTO = productRepository.getTotalStockValue();

        if (productStockValueStatDTO != null) {

            return productStockValueStatDTO;

        } else {

            return emptyProductTotalStockValueStatDTO();
        }
    }

    private void checkNameAvailability(String name) {

        if (productRepository.existsByName(name)) throw new DuplicatedProductNameException();
    }

    private void checkNameAvailability(String name, Long productID) {

        if (productRepository.existsByNameAndProductIDNot(name, productID)) throw new DuplicatedProductNameException();
    }

    private void checkIfNewCurrentPriceIsLowerThanCurrentCost(Double newCurrentPrice, Double persistedProductCost) {

        if (newCurrentPrice != null) {

            if (newCurrentPrice < persistedProductCost) throw new InvalidProductCurrentPriceException();
        }
    }

    private Product loadProduct(Long productID) {

        return productRepository.findById(productID).orElseThrow(ProductNotFoundException::new);
    }

    private Long getCountByStockStatus(List<ProductInfoDTO> productInfoDTOList) {

        Long counter = 0L;

        for (ProductInfoDTO infoDTO : productInfoDTOList) {

            if (infoDTO.getCurrentStockStatus() == StockStatus.BAJO) counter++;
        }

        return counter;
    }

    private ProductTotalStockStatsDTO emptyProductTotalStockStatsDTO() {

        return ProductTotalStockStatsDTO.builder()
                .productCount(0L)
                .onLowOrCriticalStockCount(0L)
                .build();
    }

    private ProductMostSoldStatsDTO emptyProductMostSoldStatsDTO() {

        return ProductMostSoldStatsDTO.builder()
                .productName(EMPTY_DATA_STAT)
                .unitsSold(0L)
                .build();
    }

    private ProductHighestRevenueStatsDTO emptyProductHighestRevenueStatsDTO() {

        return ProductHighestRevenueStatsDTO.builder()
                .productName(EMPTY_DATA_STAT)
                .revenue(0.0)
                .build();
    }

    private ProductStockValueStatsDTO emptyProductTotalStockValueStatDTO() {

        return ProductStockValueStatsDTO.builder()
                .totalStockValue(0.0)
                .totalUnits(0L)
                .build();
    }
}
