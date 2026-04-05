package com.barbershop.service.implementation;

import com.barbershop.dto.product.ProductItemDTO;
import com.barbershop.dto.sale.SaleCreationDTO;
import com.barbershop.dto.sale.SaleInfoDTO;
import com.barbershop.exceptions.barberservice.BarberServiceNotFoundException;
import com.barbershop.exceptions.client.ClientNotFoundException;
import com.barbershop.exceptions.employee.EmployeeNotFoundException;
import com.barbershop.exceptions.paymentmethod.PaymentMethodNotFoundException;
import com.barbershop.exceptions.product.ProductNotFoundException;
import com.barbershop.exceptions.sale.InactiveEmployeeException;
import com.barbershop.exceptions.sale.InactivePaymentMethodException;
import com.barbershop.exceptions.sale.InsufficientProductStockException;
import com.barbershop.exceptions.sale.SaleNotFoundException;
import com.barbershop.mapper.interfaces.SaleMapper;
import com.barbershop.model.*;
import com.barbershop.repository.*;
import com.barbershop.service.interfaces.SaleService;
import com.barbershop.validation.sale.SaleValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final BarberServiceRepository barberServiceRepository;
    private final ProductRepository productRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final SaleValidator validator;
    private final SaleMapper mapper;

    @Override
    @Transactional
    public void registerNewSale(SaleCreationDTO saleDto) {

        validator.validateDTO(saleDto);

        Client client = loadClient(saleDto.getClientID());

        Employee employee = loadEmployee(saleDto.getEmployeeID());

        BarberService barberService = loadBarberService(saleDto.getBarberServiceID());

        PaymentMethod paymentMethod = loadPaymentMethod(saleDto.getPaymentMethodID());

        checkIfEmployeeIsNullAndBarberServiceNot(employee, barberService);

        checkIfEmployeeIsActive(employee);

        checkIfPaymentMethodIsActive(paymentMethod);

        List<Long> productIDs = generateProductIDsList(saleDto);

        List<Product> productList = loadProductListFromIdList(productIDs);

        validateProductListSizeAgainstIDsList(productIDs, productList);

        Map<Long, Product> productMap = generateProductMapFromList(productList);

        List<SaleItem> saleItemList = new ArrayList<>();

        loadSaleItemListFromDTO(saleItemList, saleDto, productMap);

        Sale newSale = generateSaleEntity(saleDto, client, employee, barberService, paymentMethod, saleItemList);

        saleRepository.save(newSale);
    }

    @Override
    @Transactional
    public void deleteSale(Long saleID) {

        Sale saleOnDB = saleRepository.findById(saleID).orElseThrow(SaleNotFoundException::new);

        restoreStockFromSaleItemList(saleOnDB);

        saleRepository.delete(saleOnDB);
    }

    @Override
    public SaleInfoDTO getSale(Long saleID) {

        Sale saleOnDB = saleRepository.findById(saleID).orElseThrow(SaleNotFoundException::new);

        return mapper.mapSaleToInfoDTO(saleOnDB);
    }

    @Override
    public List<SaleInfoDTO> getSaleList() {

        return mapper.mapSaleToInfoDTO(saleRepository.findAll());
    }

    private Client loadClient(Long clientID) {

        return clientRepository.findById(clientID).orElseThrow(ClientNotFoundException::new);
    }

    private Employee loadEmployee(Long employeeID) {

        if (employeeID == null) return null;

        return employeeRepository.findById(employeeID).orElseThrow(EmployeeNotFoundException::new);
    }

    private BarberService loadBarberService(Long barberServiceID) {

        if (barberServiceID == null) return null;

        return barberServiceRepository.findById(barberServiceID).orElseThrow(BarberServiceNotFoundException::new);
    }

    private PaymentMethod loadPaymentMethod(Long paymentMethodID) {

        return paymentMethodRepository.findById(paymentMethodID).orElseThrow(PaymentMethodNotFoundException::new);
    }

    private void checkIfEmployeeIsNullAndBarberServiceNot(Employee employee, BarberService barberService) {

        if (employee == null && barberService != null) throw new EmployeeNotFoundException();
    }

    private void checkIfEmployeeIsActive(Employee employee) {

        if (employee != null) {

            if (!employee.isActive()) throw new InactiveEmployeeException();

        }
    }

    private void checkIfPaymentMethodIsActive(PaymentMethod paymentMethod) {

        if (!paymentMethod.getIsActive()) throw new InactivePaymentMethodException();
    }

    private List<Long> generateProductIDsList(SaleCreationDTO saleDto) {

        return saleDto.getProductsDetail().stream().map(ProductItemDTO::getProductID).distinct().toList();
    }

    private List<Product> loadProductListFromIdList(List<Long> productIDs) {

        return productRepository.findAllById(productIDs);
    }

    private void validateProductListSizeAgainstIDsList(List<Long> productIDs, List<Product> productList) {

        if (productIDs.size() != productList.size()) throw new ProductNotFoundException();
    }

    private Map<Long, Product> generateProductMapFromList(List<Product> productList) {

        return productList.stream().collect(Collectors.toMap(Product::getProductID, p -> p));
    }

    private void loadSaleItemListFromDTO(List<SaleItem> saleItemList, SaleCreationDTO saleDto, Map<Long, Product> productMap) {

        for (ProductItemDTO itemDTO : saleDto.getProductsDetail()) {

            Product product = productMap.get(itemDTO.getProductID());

            checkProductStock(product, itemDTO);

            saleItemList.add(
                    SaleItem.builder()
                            .product(product)
                            .quantity(itemDTO.getQuantity())
                            .unitPrice(product.getCurrentPrice())
                            .build()
            );

            updateStock(product, itemDTO.getQuantity(), false);
        }
    }

    private Sale generateSaleEntity(
            SaleCreationDTO saleDto,
            Client client,
            Employee employee,
            BarberService barberService,
            PaymentMethod paymentMethod,
            List<SaleItem> saleItemList
    ) {

        return mapper.mapSaleCreationDtoToSale(saleDto, client, employee, barberService, paymentMethod, saleItemList);

    }

    private void checkProductStock(Product product, ProductItemDTO itemDTO) {

        if (product.getCurrentStockLevel() < itemDTO.getQuantity()) throw new InsufficientProductStockException();
    }

    private void restoreStockFromSaleItemList(Sale saleOnDB) {

        if (saleOnDB.getItems() == null) return;

        for (SaleItem item : saleOnDB.getItems()) {

            updateStock(item.getProduct(), item.getQuantity(), true);
        }
    }

    private void updateStock(Product product, Integer quantity, boolean isRestoration) {

        Integer currentStock = product.getCurrentStockLevel();

        if (isRestoration) {

            product.setCurrentStockLevel(currentStock + quantity);

        } else {

            product.setCurrentStockLevel(currentStock - quantity);

        }
    }
}
