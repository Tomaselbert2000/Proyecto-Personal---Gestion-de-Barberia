package com.service.helper;

import com.dto.sale.SaleCreationDTO;
import com.mapper.interfaces.SaleMapper;
import com.model.*;
import com.repository.*;
import com.service.interfaces.SaleService;
import com.validation.sale.SaleValidator;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class SaleServiceTestHelper {

    public static void registerSale(SaleService saleService, SaleCreationDTO creationDTO) {

        saleService.registerNewSale(creationDTO);
    }

    public static void deleteSale(SaleService saleService, Sale sale) {

        saleService.deleteSale(sale.getSaleID());
    }

    public static void captureSale(ArgumentCaptor<Sale> saleCaptor, SaleRepository saleRepository) {

        verify(saleRepository).save(saleCaptor.capture());
    }

    public static void mockClient(ClientRepository clientRepository, Client client) {

        when(clientRepository.findById(client.getClientID())).thenReturn(Optional.of(client));
    }

    public static void mockEmployee(EmployeeRepository employeeRepository, Employee employee) {

        when(employeeRepository.findById(employee.getEmployeeID())).thenReturn(Optional.of(employee));
    }

    public static void mockPaymentMethod(PaymentMethodRepository paymentMethodRepository, PaymentMethod paymentMethod) {

        when(paymentMethodRepository.findById(paymentMethod.getPaymentMethodID())).thenReturn(Optional.of(paymentMethod));
    }

    public static void mockBarberService(BarberServiceRepository barberServiceRepository, BarberService barberService) {

        when(barberServiceRepository.findById(barberService.getBarbershopServiceID())).thenReturn(Optional.of(barberService));
    }

    public static void mockProductList(ProductRepository productRepository, List<Product> listToMock) {

        when(productRepository.findAllById(listToMock.stream().map(Product::getProductID).toList())).thenReturn(listToMock);
    }

    public static void mockSale(SaleRepository saleRepository, Sale saleOnDB) {

        when(saleRepository.findById(saleOnDB.getSaleID())).thenReturn(Optional.of(saleOnDB));
    }

    public static void mockBasicSuccesfulScenario(
            ClientRepository clientRepository,
            EmployeeRepository employeeRepository,
            BarberServiceRepository barberServiceRepository,
            PaymentMethodRepository paymentMethodRepository,
            Client client,
            BarberService barberService,
            Employee employee,
            PaymentMethod paymentMethod
    ) {
        mockClient(clientRepository, client);
        mockEmployee(employeeRepository, employee);
        mockBarberService(barberServiceRepository, barberService);
        mockPaymentMethod(paymentMethodRepository, paymentMethod);
    }

    public static void mockEmptyClient(ClientRepository clientRepository, Client client) {

        when(clientRepository.findById(client.getClientID())).thenReturn(Optional.empty());
    }

    public static void mockEmptySale(SaleRepository saleRepository, Sale sale) {

        when(saleRepository.findById(sale.getSaleID())).thenReturn(Optional.empty());
    }

    public static void mockEmptyEmployee(EmployeeRepository employeeRepository, Employee employee) {

        when(employeeRepository.findById(employee.getEmployeeID())).thenReturn(Optional.empty());
    }

    public static void mockEmptyPaymentMethod(PaymentMethodRepository paymentMethodRepository, PaymentMethod paymentMethod) {

        when(paymentMethodRepository.findById(paymentMethod.getPaymentMethodID())).thenReturn(Optional.empty());
    }

    public static void verifyValidatorCreationInteraction(SaleValidator validator, SaleCreationDTO creationDTO) {

        verify(validator).validateDTO(creationDTO);
    }

    public static void verifyMapperCreationInteraction(
            SaleMapper mapper,
            SaleCreationDTO creationDTO,
            Client client,
            Employee employee,
            BarberService barberService,
            PaymentMethod paymentMethod
    ) {

        verify(mapper).mapSaleCreationDtoToSale(eq(creationDTO), eq(client), eq(employee), eq(barberService), eq(paymentMethod), anyList());
    }
}
