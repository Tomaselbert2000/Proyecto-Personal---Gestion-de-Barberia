package com.mapper.interfaces;

import com.dto.sale.SaleCreationDTO;
import com.dto.sale.SaleInfoDTO;
import com.model.*;

import java.util.List;

public interface SaleMapper {

    Sale mapSaleCreationDtoToEntity(
            SaleCreationDTO dto,
            Client client, Employee employee,
            BarberService barberService,
            PaymentMethod paymentMethod,
            List<SaleItem> saleItemList
    );

    SaleInfoDTO mapSaleToInfoDTO(Sale entity);

    List<SaleInfoDTO> mapSaleToInfoDTO(List<Sale> entityList);
}
