package com.barbershop.mapper.interfaces;

import com.barbershop.dto.sale.SaleCreationDTO;
import com.barbershop.dto.sale.SaleInfoDTO;
import com.barbershop.model.*;

import java.util.List;

public interface SaleMapper {

    Sale mapSaleCreationDtoToSale(
            SaleCreationDTO creationDTO,
            Client client, Employee employee,
            BarberService barberService,
            PaymentMethod paymentMethod,
            List<SaleItem> saleItemList
    );

    SaleInfoDTO mapSaleToInfoDTO(Sale sale);

    List<SaleInfoDTO> mapSaleToInfoDTO(List<Sale> saleList);
}
