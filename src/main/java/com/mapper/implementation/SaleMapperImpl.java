package com.mapper.implementation;

import com.dto.sale.ReceiptItemDTO;
import com.dto.sale.SaleCreationDTO;
import com.dto.sale.SaleInfoDTO;
import com.mapper.helper.MapperHelper;
import com.mapper.interfaces.SaleMapper;
import com.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mapper.helper.MapperHelper.checkIfMapperInputIsNull;
import static com.mapper.helper.SaleCalculator.computeModifierValue;
import static com.mapper.helper.SaleCalculator.computeTotal;

@Component
public class SaleMapperImpl implements SaleMapper {

    private static final String EMPTY_SERVICE_DEFAULT_STRING = "Sin servicio / Venta de productos";

    @Override
    public Sale mapSaleCreationDtoToEntity(
            SaleCreationDTO dto,
            Client client,
            Employee employee,
            BarberService barberService,
            PaymentMethod paymentMethod,
            List<SaleItem> saleItemList
    ) {
        checkIfMapperInputIsNull(dto, client, paymentMethod);

        double saleTotal = computeTotal(barberService, saleItemList);
        double saleTotalModifierValue = computeModifierValue(saleTotal, paymentMethod);

        Sale newSale = Sale.builder()
                .dateAndTime(dto.getDateAndTime())
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

        if (barberService != null) {

            ServiceRecord record = generateServiceRecord(client, employee, barberService, newSale);

            newSale.setServiceRecord(record);
        }

        return newSale;
    }

    @Override
    public SaleInfoDTO mapSaleToInfoDTO(Sale entity) {

        checkIfMapperInputIsNull(entity);

        String barberServiceName = Optional.ofNullable(entity.getBarberService())
                .map(BarberService::getName)
                .orElse(EMPTY_SERVICE_DEFAULT_STRING);

        List<ReceiptItemDTO> receiptItems = createReceiptList(entity);

        return SaleInfoDTO.builder()
                .saleID(entity.getSaleID())
                .dateAndTime(entity.getDateAndTime())
                .clientFirstName(entity.getClient().getFirstName())
                .clientLastName(entity.getClient().getLastName())
                .barberServiceName(barberServiceName)
                .employeeFirstName(entity.getEmployee().getFirstName())
                .employeeLastName(entity.getEmployee().getLastName())
                .receiptItems(receiptItems)
                .total(entity.getTotal())
                .paymentMethodName(entity.getPaymentMethodUsed().getName())
                .build();
    }

    @Override
    public List<SaleInfoDTO> mapSaleToInfoDTO(List<Sale> entityList) {

        return MapperHelper.mapList(entityList, this::mapSaleToInfoDTO);
    }

    private ServiceRecord generateServiceRecord(
            Client client,
            Employee employee,
            BarberService barberService,
            Sale newSale
    ) {

        checkIfMapperInputIsNull(employee, barberService);

        String serviceName = MapperHelper.orDefault(barberService.getName(), EMPTY_SERVICE_DEFAULT_STRING);

        return ServiceRecord.builder()
                .employee(employee)
                .client(client)
                .sale(newSale)
                .timestamp(newSale.getDateAndTime())
                .serviceName(serviceName)
                .priceAtMoment(barberService.getPrice())
                .build();
    }

    private List<ReceiptItemDTO> createReceiptList(Sale sale) {

        List<ReceiptItemDTO> receiptList = new ArrayList<>();

        for (SaleItem saleItem : sale.getItems()) {

            receiptList.add(ReceiptItemDTO.builder()
                    .productName(saleItem.getProduct().getName())
                    .quantity(saleItem.getQuantity())
                    .unitPrice(BigDecimal.valueOf(saleItem.getUnitPrice()))
                    .build());
        }

        return receiptList;
    }
}
