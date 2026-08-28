package com.mapper.helper;

import com.model.BarberService;
import com.model.PaymentMethod;
import com.model.SaleItem;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class SaleCalculator {

    private SaleCalculator() {
    }

    public static double computeTotal(BarberService barberService, List<SaleItem> items) {

        double serviceTotal = Optional.ofNullable(barberService).map(BarberService::getPrice).orElse(0.0);
        double itemsTotal = Optional.ofNullable(items).orElse(List.of()).stream()
                .filter(Objects::nonNull)
                .mapToDouble(saleItem -> saleItem.getUnitPrice() * saleItem.getQuantity())
                .sum();

        return serviceTotal + itemsTotal;
    }

    public static double computeModifierValue(double subtotal, PaymentMethod paymentMethod) {

        return switch (paymentMethod.getModifierType()) {

            case DESCUENTO -> -(subtotal * paymentMethod.getPriceModifier());

            case RECARGO -> subtotal * paymentMethod.getPriceModifier();

            case NINGUNO -> 0.0;

            case TODOS -> throw new IllegalStateException(
                    "El tipo de modificador TODOS no es válido para el medio de pago de una venta.");
        };
    }
}
