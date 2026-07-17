package com.mapper.sale;

import com.dto.sale.SaleCreationDTO;
import com.enums.PaymentMethodModifierType;
import com.mapper.interfaces.SaleMapper;
import com.model.*;

import java.util.List;

public class SaleMapperCreationTestUtils {

    public static Sale mapEntity(SaleCreationDTO creationDTO, SaleMapper mapper, Client client, Employee employee, BarberService barberService, PaymentMethod paymentMethod, List<SaleItem> saleItemList) {

        return mapper.mapSaleCreationDtoToSale(
                creationDTO, client, employee, barberService, paymentMethod, saleItemList
        );
    }

    public static void setPaymentModifierTypeAsNINGUNO(PaymentMethod paymentMethod) {

        paymentMethod.setModifierType(PaymentMethodModifierType.NINGUNO);
    }

    public static void setPaymentModifierTypeAsDESCUENTO(PaymentMethod paymentMethod) {

        paymentMethod.setModifierType(PaymentMethodModifierType.DESCUENTO);
    }

    public static void setPaymentModifierTypeAsRECARGO(PaymentMethod paymentMethod) {

        paymentMethod.setModifierType(PaymentMethodModifierType.RECARGO);
    }

    public static Double calculateItemsTotal(List<SaleItem> saleItemList) {

        double total = 0.0;

        for (SaleItem item : saleItemList) {
            total += item.getQuantity() * item.getUnitPrice();
        }

        return total;
    }

    public static Double calculateBarberServicePriceAndSaleItemsListSum(PaymentMethod paymentMethod, List<SaleItem> saleItemList, BarberService barberService) {
        double sumTotal = calculateItemsTotal(saleItemList) + barberService.getPrice();

        switch (paymentMethod.getModifierType()) {
            case DESCUENTO -> {
                return sumTotal - (sumTotal * paymentMethod.getPriceModifier());
            }
            case RECARGO -> {
                return sumTotal + (sumTotal * paymentMethod.getPriceModifier());
            }
        }

        return sumTotal;
    }

    public static Double calculateBarberServicePriceBasedOnPaymentMethodModifier(PaymentMethod paymentMethod, BarberService barberService) {
        switch (paymentMethod.getModifierType()) {
            case DESCUENTO -> {
                return barberService.getPrice() - (barberService.getPrice() * paymentMethod.getPriceModifier());
            }
            case RECARGO -> {
                return barberService.getPrice() + (barberService.getPrice() * paymentMethod.getPriceModifier());
            }
        }

        return 0.0;
    }

    public static Double calculateSaleItemsSumBasedOnPaymentMethodModifier(List<SaleItem> saleItemList, PaymentMethod paymentMethod) {
        Double saleItemTotal = calculateItemsTotal(saleItemList);

        switch (paymentMethod.getModifierType()) {
            case DESCUENTO -> {
                return saleItemTotal - (saleItemTotal * paymentMethod.getPriceModifier());
            }
            case RECARGO -> {
                return saleItemTotal + (saleItemTotal * paymentMethod.getPriceModifier());
            }
        }

        return 0.0;
    }
}
