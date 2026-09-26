package com.presentation.controller.web;

import com.dto.barberservice.BarberServiceCreationDTO;
import com.dto.barberservice.BarberServiceInfoDTO;
import com.dto.barberservice.BarberServiceUpdateDTO;
import com.dto.stats.BarberServiceActiveOnCatalogStatsDTO;
import com.dto.stats.BarberServiceRevenueStatsDTO;
import com.dto.stats.BarberServiceSalesStatsDTO;
import com.dto.stats.BarberServiceUsageStatsDTO;
import com.enums.BarberServiceCategory;
import com.enums.PriceRanges;
import com.exceptions.BusinessException;
import com.service.interfaces.BarberserviceService;
import com.service.interfaces.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

import static com.presentation.constants.HtmlConstants.Paths.*;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_BARBERSERVICES;
import static com.presentation.constants.HtmlConstants.Redirects.redirectToUpdate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/barberservices")
public class WebBarberserviceController implements WebController<BarberServiceCreationDTO> {

    private final BarberserviceService barberserviceService;
    private final SaleService saleService;

    @GetMapping()
    public String showBarberserviceCatalog(
            Model model,
            @RequestParam(required = false) String serviceName,
            @RequestParam(required = false) BarberServiceCategory category,
            @RequestParam(required = false) PriceRanges priceRange,
            Principal principal) {

        Double minPrice = null, maxPrice = null;

        if (priceRange != null) {

            minPrice = priceRange.getMinPrice();
            maxPrice = priceRange.getMaxPrice();
        }

        BarberServiceActiveOnCatalogStatsDTO activeOnCatalogStatsDTO = barberserviceService.getActiveOnCatalogStats();
        BarberServiceSalesStatsDTO salesStatsDTO = saleService.getBarberServiceWithMostSales();
        BarberServiceRevenueStatsDTO revenueStatsDTO = saleService.getBarberServiceWithHighestRevenue();
        BarberServiceUsageStatsDTO usageStatsDTO = saleService.getBarberServiceWithLowestUsage();

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("activeOnCatalogStats", activeOnCatalogStatsDTO);
        model.addAttribute("saleStats", salesStatsDTO);
        model.addAttribute("revenueStats", revenueStatsDTO);
        model.addAttribute("usageStats", usageStatsDTO);

        model.addAttribute("liveSearch", barberserviceService.liveSearch(
                        serviceName,
                        category,
                        minPrice,
                        maxPrice
                )
        );

        model.addAttribute("priceRange", PriceRanges.values());
        model.addAttribute("category", BarberServiceCategory.values());

        model.addAttribute("serviceName", serviceName);
        model.addAttribute("category", category);
        model.addAttribute("priceRange", priceRange);

        return BARBERSERVICES;
    }

    @GetMapping("/new")
    public String showBarberServiceCreationForm(Model model, Principal principal) {

        populateCreationCatalog(model);
        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("dto", new BarberServiceCreationDTO());

        return BARBERSERVICE_CREATION;
    }

    @PostMapping("/new")
    public String createBarberService(
            @Valid @ModelAttribute(name = "dto") BarberServiceCreationDTO dto,
            BindingResult bindingResult,
            Model model,
            Principal principal
    ) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("validationErrors", bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .filter(Objects::nonNull)
                    .toList());

            return renderCreationForm(model, principal, dto);
        }

        try {

            barberserviceService.registerNewBarberService(dto);

            return REDIRECT_BARBERSERVICES;

        } catch (BusinessException exception) {

            model.addAttribute("validationErrors", List.of(exception.getMessage()));

            return renderCreationForm(model, principal, dto);
        }
    }

    @PostMapping("/{barberserviceID}/delete")
    public String deleteBarberService(@PathVariable Long barberserviceID) {

        barberserviceService.deleteBarberservice(barberserviceID);
        return REDIRECT_BARBERSERVICES;
    }

    @GetMapping("/{barberserviceID}/update")
    public String updateBarberService(@PathVariable Long barberserviceID, Model model) {

        BarberServiceInfoDTO dto = barberserviceService.getBarberServiceInfo(barberserviceID);

        model.addAttribute("dto", dto);
        model.addAttribute("categories", BarberServiceCategory.values());

        return BARBERSERVICE_UPDATE;
    }

    @PostMapping("/{barberserviceID}/update")
    public String updateBarberService(@PathVariable Long barberserviceID, @ModelAttribute BarberServiceUpdateDTO dto) {

        barberserviceService.updateService(barberserviceID, dto);

        return redirectToUpdate(REDIRECT_BARBERSERVICES, barberserviceID);
    }

    @Override
    public String renderCreationForm(Model model, Principal principal, BarberServiceCreationDTO dto) {

        populateCreationCatalog(model);

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("dto", dto);

        return BARBERSERVICE_CREATION;
    }

    @Override
    public void populateCreationCatalog(Model model) {

        model.addAttribute("categories", BarberServiceCategory.values());
    }
}
