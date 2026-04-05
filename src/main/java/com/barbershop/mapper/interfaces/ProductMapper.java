package com.barbershop.mapper.interfaces;

import com.barbershop.dto.product.ProductCreationDTO;
import com.barbershop.dto.product.ProductInfoDTO;
import com.barbershop.dto.product.ProductUpdateDTO;
import com.barbershop.model.Product;

import java.util.List;

public interface ProductMapper {

    Product mapProductCreationDTOtoEntity(ProductCreationDTO dto);

    Product mapProductUpdateDTOtoEntity(Product product, ProductUpdateDTO updateDTO);

    ProductInfoDTO mapProductToInfoDTO(Product product);

    List<ProductInfoDTO> mapProductToInfoDTO(List<Product> productList);
}
