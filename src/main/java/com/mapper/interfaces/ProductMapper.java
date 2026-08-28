package com.mapper.interfaces;

import com.dto.product.ProductCreationDTO;
import com.dto.product.ProductInfoDTO;
import com.dto.product.ProductUpdateDTO;
import com.model.Product;

import java.util.List;

public interface ProductMapper {

    Product mapProductCreationDTOtoEntity(ProductCreationDTO dto);

    Product mapProductUpdateDTOtoEntity(Product entity, ProductUpdateDTO dto);

    ProductInfoDTO mapProductToInfoDTO(Product entity);

    List<ProductInfoDTO> mapProductToInfoDTO(List<Product> entityList);

    ProductUpdateDTO mapProductToUpdateDTO(Product entity);
}
