package com.presentation.controller.web;

import com.dto.product.ProductCreationDTO;
import com.dto.product.ProductInfoDTO;
import com.dto.product.ProductUpdateDTO;
import com.enums.ProductCategory;
import com.enums.ProductPresentationUnit;
import com.enums.StockStatus;
import com.exceptions.BusinessException;
import com.presentation.controller.BaseWebController;
import com.service.implementation.ProductServiceImpl;
import com.service.interfaces.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static com.presentation.constants.HtmlConstants.Paths.*;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_PRODUCTS;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class WebProductController extends BaseWebController<ProductCreationDTO, ProductUpdateDTO> {

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

        model.addAttribute("name", name);
        model.addAttribute("category", category);
        model.addAttribute("stockStatus", stockStatus);

        model.addAttribute("liveSearch", service.liveSearch(name, category, stockStatus));

        return PRODUCTS;
    }

    @GetMapping("/new")
    public String showProductCreationForm(Model model, Principal principal) {

        return showCreationForm(model, principal, new ProductCreationDTO());
    }

    @PostMapping("/new")
    public String createProduct(
            @Valid @ModelAttribute(name = "dto") ProductCreationDTO dto,
            BindingResult bindingResult,
            Model model,
            Principal principal
    ) {

        return createEntity(dto, bindingResult, model, principal, REDIRECT_PRODUCTS);
    }

    @PostMapping("/{productID}/delete")
    public String deleteProduct(@PathVariable Long productID) {

        return deleteEntity(productID, REDIRECT_PRODUCTS);
    }

    @GetMapping("/{productID}/update")
    public String updateProduct(
            @PathVariable Long productID,
            Model model,
            Principal principal
    ) {

        try {

            return renderUpdateForm(model, productID, principal, invokeServiceAndReturnDTO(productID));

        } catch (BusinessException e) {

            model.addAttribute(VALIDATION_TAG, List.of(e.getMessage()));

            return renderCreationForm(model, principal, new ProductCreationDTO());
        }
    }

    @PostMapping("/{productID}/update")
    public String updateProduct(
            @PathVariable Long productID,
            @Valid @ModelAttribute(name = "dto") ProductUpdateDTO dto,
            BindingResult bindingResult,
            Model model,
            Principal principal
    ) {

        return updateEntity(productID, dto, bindingResult, model, principal, REDIRECT_PRODUCTS);
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

        try {

            service.updateProductStock(productID, quantity, operation);

            return REDIRECT_PRODUCTS;

        } catch (BusinessException exception) {

            return REDIRECT_PRODUCTS + ERROR_SUFFIX;
        }
    }

    @Override
    protected ProductUpdateDTO invokeServiceAndReturnDTO(Long id) {

        return service.getProductForUpdate(id);
    }

    @Override
    protected void executeCreation(ProductCreationDTO dto) {

        service.registerNewProduct(dto);
    }

    @Override
    protected void executeUpdate(Long entityID, ProductUpdateDTO updateDTO) {

        service.updateProduct(entityID, updateDTO);
    }

    @Override
    protected void executeDeletion(Long entityID) {

        service.deleteProduct(entityID);
    }

    @Override
    protected String renderCreationForm(Model model, Principal principal, ProductCreationDTO dto) {

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("dto", dto);

        populateCreationForm(model);

        return PRODUCT_CREATION;
    }

    @Override
    protected String renderUpdateForm(Model model, Long entityID, Principal principal, ProductUpdateDTO updateDTO) {

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("product", service.getProductInfo(entityID));
        model.addAttribute("dto", updateDTO);
        model.addAttribute("categories", ProductCategory.values());
        model.addAttribute("productPresentationUnits", ProductPresentationUnit.values());

        return PRODUCT_UPDATE;
    }

    @Override
    protected void populateCreationForm(Model model) {

        model.addAttribute("categories", ProductCategory.values());
        model.addAttribute("productPresentationUnits", ProductPresentationUnit.values());
    }
}
