package com.presentation.controller.web;

import com.dto.product.ProductInfoDTO;
import com.dto.product.ProductUpdateDTO;
import com.enums.ProductCategory;
import com.enums.StockStatus;
import com.service.implementation.ProductServiceImpl;
import com.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.presentation.constants.HtmlConstants.Paths.*;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_PRODUCTS;
import static com.presentation.constants.HtmlConstants.Redirects.redirectToUpdate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class WebProductController {

    private final ProductService service;

    @GetMapping
    public String showProductCatalog(
            Principal principal,
            Model model,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) StockStatus stockStatus
    ) {

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("stockStats", service.getProductCountAndStockStats());
        model.addAttribute("mostSoldStats", service.getProductMostSoldStats());
        model.addAttribute("highestRevenueStats", service.getProductHighestRevenueStats());
        model.addAttribute("stockValueStats", service.getProductStockValueStat());
        model.addAttribute("inventoryAlertStats", service.getInventoryAlertStat());

        model.addAttribute("categories", ProductCategory.values());
        model.addAttribute("stockStatuses", StockStatus.values());

        model.addAttribute("category", category);
        model.addAttribute("stockStatus", stockStatus);

        model.addAttribute("liveSearch", service.liveSearch(name, category, stockStatus));

        model.addAttribute("name", name);
        model.addAttribute("category", category);
        model.addAttribute("stockStatus", stockStatus);

        return PRODUCTS;
    }

    @PostMapping("/{productID}/delete")
    public String deleteProduct(@PathVariable Long productID) {

        service.deleteProduct(productID);

        return REDIRECT_PRODUCTS;
    }

    @GetMapping("/{productID}/update")
    public String updateProduct(@PathVariable Long productID, Model model) {

        ProductUpdateDTO dto = service.getProductForUpdate(productID);

        model.addAttribute("dto", dto);

        return PRODUCT_UPDATE;
    }

    @PostMapping("/{productID}/update")
    public String updateProduct(@PathVariable Long productID, @ModelAttribute ProductUpdateDTO dto) {

        service.updateProduct(productID, dto);

        return redirectToUpdate(REDIRECT_PRODUCTS, productID);
    }

    @GetMapping("/{productID}/update-stock")
    public String registerStock(@PathVariable Long productID, Model model, Principal principal) {

        ProductInfoDTO productDTO = service.getProductInfo(productID);

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("product", productDTO);

        return PRODUCT_STOCK;
    }

    @PostMapping("/{productID}/update-stock")
    public String registerStock(
            @PathVariable Long productID,
            @RequestParam Integer quantity,
            @RequestParam ProductServiceImpl.StockUpdateOperation operation
    ) {

        service.updateProductStock(productID, quantity, operation);

        return REDIRECT_PRODUCTS;
    }
}
