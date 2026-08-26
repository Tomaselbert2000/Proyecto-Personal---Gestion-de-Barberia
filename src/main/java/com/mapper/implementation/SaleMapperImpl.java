package com.mapper.implementation;

import com.dto.sale.SaleCreationDTO;
import com.dto.sale.SaleInfoDTO;
import com.mapper.interfaces.SaleMapper;
import com.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mapper.helper.MapperHelper.checkIfMapperInputIsNull;

@Component
public class SaleMapperImpl implements SaleMapper {

    private static final String EMPTY_SERVICE_DEFAULT_STRING = "Sin servicio / Venta de productos";

    @Override
    public Sale mapSaleCreationDtoToSale(
            SaleCreationDTO creationDTO,
            Client client,
            Employee employee,
            BarberService barberService,
            PaymentMethod paymentMethod,
            List<SaleItem> saleItemList
    ) {
        checkIfMapperInputIsNull(creationDTO, client, paymentMethod);

        double saleTotal = calculateSaleTotal(barberService, saleItemList);
        double saleTotalModifierValue = 0.0;

        switch (paymentMethod.getModifierType()) {

            case DESCUENTO -> saleTotalModifierValue = -(saleTotal * paymentMethod.getPriceModifier());
            case RECARGO -> saleTotalModifierValue = (saleTotal * paymentMethod.getPriceModifier());
            case NINGUNO -> saleTotalModifierValue = 0.0;
        }

        Sale newSale = Sale.builder()
                .dateAndTime(creationDTO.getDateAndTime())
                .client(client)
                .employee(employee)
                .barberService(barberService)
                .items(saleItemList)
                .paymentMethodUsed(paymentMethod)
                .total(saleTotal + saleTotalModifierValue)
                .modifierValue(saleTotalModifierValue)
                .build();

        if (saleItemList != null) {

            saleItemList.forEach(item -> item.setSale(newSale));
        }

        if(barberService != null){

            ServiceRecord record = generateServiceRecord(client, employee, barberService, newSale);

            newSale.setServiceRecord(record);
        }

        return newSale;
    }

    @Override
    public SaleInfoDTO mapSaleToInfoDTO(Sale sale) {

        checkIfMapperInputIsNull(sale);

        String barberServiceName;

        if (sale.getBarberService() == null) {

            barberServiceName = EMPTY_SERVICE_DEFAULT_STRING;

        } else {

            barberServiceName = sale.getBarberService().getName();
        }

        List<String> productNames = createListWithProductNames(sale);

        return SaleInfoDTO.builder()
                .saleID(sale.getSaleID())
                .dateAndTime(sale.getDateAndTime())
                .clientFirstName(sale.getClient().getFirstName())
                .clientLastName(sale.getClient().getLastName())
                .barberServiceName(barberServiceName)
                .employeeFirstName(sale.getEmployee().getFirstName())
                .employeeLastName(sale.getEmployee().getLastName())
                .productNames(productNames)
                .total(sale.getTotal())
                .paymentMethodName(sale.getPaymentMethodUsed().getName())
                .build();
    }

    @Override
    public List<SaleInfoDTO> mapSaleToInfoDTO(List<Sale> saleList) {

        checkIfMapperInputIsNull(saleList);

        return saleList.stream().map(this::mapSaleToInfoDTO).collect(Collectors.toList());
    }

    private Double calculateSaleTotal(BarberService barberService, List<SaleItem> saleItemList) {

        checkIfMapperInputIsNull(saleItemList);

        double serviceTotal = (barberService != null) ? barberService.getPrice() : 0.0;
        double itemListTotal = 0.0;

        if (barberService != null) serviceTotal = barberService.getPrice();

        for (SaleItem item : saleItemList) {

            if (item != null) {

                itemListTotal += item.getUnitPrice() * item.getQuantity();
            }
        }

        return serviceTotal + itemListTotal;
    }

    private ServiceRecord generateServiceRecord(
            Client client,
            Employee employee,
            BarberService barberService,
            Sale newSale
    ) {

        checkIfMapperInputIsNull(employee, barberService);

        return ServiceRecord.builder()
                .employee(employee)
                .client(client)
                .sale(newSale)
                .timestamp(newSale.getDateAndTime())
                .serviceName(barberService.getName() == null ? EMPTY_SERVICE_DEFAULT_STRING : barberService.getName())
                .priceAtMoment(barberService.getPrice())
                .build();
    }

    private List<String> createListWithProductNames(Sale sale) {

        checkIfMapperInputIsNull(sale);

        List<String> productNames = new ArrayList<>();

        for (SaleItem saleItem : sale.getItems()) {

            String productName = saleItem.getProduct().getName();

            productNames.add(productName);
        }

        return productNames;
    }
}
