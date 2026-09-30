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
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.presentation.constants.HtmlConstants.Paths.*;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_PRODUCTS;
import static com.presentation.constants.StringResource.OperationMessages.PRODUCT_OPERATION_FAILED;

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
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String stockStatus
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

        model.addAttribute("liveSearch", service.liveSearch(
                name,
                parseEnumValue(category, ProductCategory.class),
                parseEnumValue(stockStatus, StockStatus.class))
        );

        model.addAttribute(OPERATION_FAILED_TAG, PRODUCT_OPERATION_FAILED);

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

            return REDIRECT_PRODUCTS + ERROR_SUFFIX;
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

        try {

            ProductInfoDTO productDTO = service.getProductInfo(productID);

            model.addAttribute("currentUser", principal.getName());
            model.addAttribute("product", productDTO);

            return PRODUCT_STOCK;

        } catch (BusinessException e) {

            return REDIRECT_PRODUCTS + ERROR_SUFFIX;
        }
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

        } catch (BusinessException | IllegalArgumentException | TransactionSystemException exception) {

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
