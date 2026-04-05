package com.barbershop.service.interfaces;

import com.barbershop.dto.sale.SaleCreationDTO;
import com.barbershop.dto.sale.SaleInfoDTO;

import java.util.List;

public interface SaleService {

    void registerNewSale(SaleCreationDTO saleDto);

    void deleteSale(Long saleID);

    SaleInfoDTO getSale(Long saleID);

    List<SaleInfoDTO> getSaleList();
}
