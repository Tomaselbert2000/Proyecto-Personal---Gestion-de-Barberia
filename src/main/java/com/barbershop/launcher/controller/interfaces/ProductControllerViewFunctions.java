package com.barbershop.launcher.controller.interfaces;

import com.barbershop.dto.product.ProductInfoDTO;

public interface ProductControllerViewFunctions {

    void configureButtonActions(ProductInfoDTO... infoDTO);

    void handleImageSelection();
}
