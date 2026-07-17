package com.mapper.interfaces;

import com.dto.product.ProductCreationDTO;
import com.dto.product.ProductInfoDTO;
import com.dto.product.ProductUpdateDTO;
import com.model.Product;

import java.util.List;

public interface ProductMapper {

    Product mapProductCreationDTOtoEntity(ProductCreationDTO dto);

    Product mapProductUpdateDTOtoEntity(Product product, ProductUpdateDTO updateDTO);

    ProductInfoDTO mapProductToInfoDTO(Product product);

    List<ProductInfoDTO> mapProductToInfoDTO(List<Product> productList);

    ProductUpdateDTO mapProductToUpdateDTO(Product product);
}
