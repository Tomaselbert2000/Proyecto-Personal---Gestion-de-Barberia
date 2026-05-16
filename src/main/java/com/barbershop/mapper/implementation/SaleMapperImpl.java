package com.barbershop.mapper.implementation;

import com.barbershop.dto.sale.SaleCreationDTO;
import com.barbershop.dto.sale.SaleInfoDTO;
import com.barbershop.exceptions.common.NullMapperInputException;
import com.barbershop.mapper.interfaces.SaleMapper;
import com.barbershop.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

        if (creationDTO == null || client == null || paymentMethod == null) throw new NullMapperInputException();

        Double saleTotal = calculateSaleTotal(barberService, saleItemList, paymentMethod);

        Sale newSale = Sale.builder()
                .dateAndTime(creationDTO.getDateAndTime())
                .client(client)
                .employee(employee)
                .barberService(barberService)
                .items(saleItemList)
                .paymentMethodUsed(paymentMethod)
                .total(saleTotal)
                .build();

        if (saleItemList != null) {

            saleItemList.forEach(item -> item.setSale(newSale));
        }

        ServiceRecord record = generateServiceRecord(client, employee, barberService, newSale);

        newSale.setServiceRecord(record);

        return newSale;
    }

    @Override
    public SaleInfoDTO mapSaleToInfoDTO(Sale sale) {

        if (sale == null) throw new NullMapperInputException();

        String barberServiceName;

        if (sale.getBarberService() == null) {

            barberServiceName = EMPTY_SERVICE_DEFAULT_STRING;

        } else {

            barberServiceName = sale.getBarberService().getName();
        }

        return SaleInfoDTO.builder()
                .dateAndTime(sale.getDateAndTime())
                .clientFirstName(sale.getClient().getFirstName())
                .clientLastName(sale.getClient().getLastName())
                .barberServiceName(barberServiceName)
                .total(sale.getTotal())
                .paymentMethodName(sale.getPaymentMethodUsed().getName())
                .build();
    }

    @Override
    public List<SaleInfoDTO> mapSaleToInfoDTO(List<Sale> saleList) {

        if (saleList == null) throw new NullMapperInputException();

        return saleList.stream().map(this::mapSaleToInfoDTO).collect(Collectors.toList());
    }

    private Double calculateSaleTotal(BarberService barberService, List<SaleItem> saleItemList, PaymentMethod paymentMethod) {

        double serviceTotal = (barberService != null) ? barberService.getPrice() : 0.0;
        double itemListTotal = 0.0;

        if (barberService != null) serviceTotal = barberService.getPrice();

        for (SaleItem item : saleItemList) {

            if (item != null) {

                itemListTotal += item.getUnitPrice() * item.getQuantity();
            }
        }

        Double totalAfterCalculations = serviceTotal + itemListTotal;

        switch (paymentMethod.getModifierType()) {

            case DESCUENTO -> {

                return totalAfterCalculations - (totalAfterCalculations * paymentMethod.getPriceModifier());
            }
            case RECARGO -> {

                return totalAfterCalculations + (totalAfterCalculations * paymentMethod.getPriceModifier());
            }
            case NINGUNO -> {

                return totalAfterCalculations;
            }
        }
        return null;
    }

    private ServiceRecord generateServiceRecord(Client client, Employee employee, BarberService barberService, Sale newSale) {

        if (employee == null || barberService == null) return null;

        return ServiceRecord.builder()
                .employee(employee)
                .client(client)
                .sale(newSale)
                .timestamp(newSale.getDateAndTime())
                .serviceName(barberService.getName())
                .priceAtMoment(barberService.getPrice())
                .build();
    }
}
