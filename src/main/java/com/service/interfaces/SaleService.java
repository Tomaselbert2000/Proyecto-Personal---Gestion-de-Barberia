package com.service.interfaces;

import com.dto.sale.SaleCreationDTO;
import com.dto.sale.SaleInfoDTO;

import java.util.List;

public interface SaleService {

    void registerNewSale(SaleCreationDTO saleDto);

    void deleteSale(Long saleID);

    SaleInfoDTO getSale(Long saleID);

    List<SaleInfoDTO> getSaleList();
}
