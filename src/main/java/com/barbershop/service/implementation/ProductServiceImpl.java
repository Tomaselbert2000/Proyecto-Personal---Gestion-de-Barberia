package com.barbershop.service.implementation;

import com.barbershop.dto.product.ProductCreationDTO;
import com.barbershop.dto.product.ProductInfoDTO;
import com.barbershop.dto.product.ProductUpdateDTO;
import com.barbershop.enums.ProductCategory;
import com.barbershop.enums.StockStatus;
import com.barbershop.exceptions.product.DuplicatedProductNameException;
import com.barbershop.exceptions.product.InvalidProductCurrentPriceException;
import com.barbershop.exceptions.product.ProductNotFoundException;
import com.barbershop.mapper.interfaces.ProductMapper;
import com.barbershop.model.MonthlyStockValueHistory;
import com.barbershop.model.Product;
import com.barbershop.repository.MonthlyStockValueHistoryRepository;
import com.barbershop.repository.ProductRepository;
import com.barbershop.service.interfaces.ProductService;
import com.barbershop.validation.product.ProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.barbershop.utils.time.TimeCalculation.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String CRON_SCHEDULE_VALUE = "0 59 23 L * ?";

    private final ProductRepository productRepository;
    private final MonthlyStockValueHistoryRepository monthlyStockValueHistoryRepository;
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

    @Override
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
    public Long getProductsCreatedThisMonth() {

        LocalDateTime startOfCurrentMonth = getStartOfCurrentMonth().atStartOfDay();
        LocalDateTime endOfCurrentMonth = getEndOfCurrentMonth().atTime(LAST_SECOND_OF_DAY);

        return productRepository.countByCreationDateBetween(startOfCurrentMonth, endOfCurrentMonth);
    }

    @Override
    public Long getProductsOnCriticalStockCount() {

        List<ProductInfoDTO> productInfoDTOList = getProductsList();

        return getCountByStockStatus(productInfoDTOList, StockStatus.CRITICO);
    }

    @Override
    public Long getProductsOnLowStock() {

        List<ProductInfoDTO> productInfoDTOList = getProductsList();

        return getCountByStockStatus(productInfoDTOList, StockStatus.BAJO);
    }

    @Override
    public Double calculateTotalStockValue() {

        Double totalStockValue = productRepository.getTotalStockValue();

        if (totalStockValue == null) return 0.0;

        return totalStockValue;
    }

    @Override
    @Transactional
    @Scheduled(cron = CRON_SCHEDULE_VALUE)
    public void generateMonthlyStockValueHistory() {

        LocalDate date = getCurrentDate();
        LocalDateTime calculationTimestamp = LocalDateTime.now();
        Double totalStockValue = calculateTotalStockValue();

        MonthlyStockValueHistory monthlyStockValueHistory = MonthlyStockValueHistory.builder()
                .localDate(date)
                .calculationTimestamp(calculationTimestamp)
                .totalStockValue(totalStockValue)
                .build();

        monthlyStockValueHistoryRepository.save(monthlyStockValueHistory);
    }

    @Override
    public Double calculateTotalStockValuePercentageVariationVsLastMonth() {

        MonthlyStockValueHistory latestRegister = monthlyStockValueHistoryRepository.findTop1ByOrderByLocalDateDesc();

        if (latestRegister != null) {

            Double lastMonthStockValue = latestRegister.getTotalStockValue();
            Double currentStockValue = calculateTotalStockValue();

            if (lastMonthStockValue == null || lastMonthStockValue == 0.0) return 0.0;

            return ((currentStockValue - lastMonthStockValue) / lastMonthStockValue) * 100;
        }

        return 0.0;
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

    private Long getCountByStockStatus(List<ProductInfoDTO> productInfoDTOList, StockStatus stockStatus) {

        Long counter = 0L;

        for (ProductInfoDTO infoDTO : productInfoDTOList) {

            if (infoDTO.getCurrentStockStatus() == stockStatus) counter++;
        }

        return counter;
    }
}
