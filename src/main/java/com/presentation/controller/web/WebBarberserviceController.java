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
import com.presentation.controller.BaseWebController;
import com.service.interfaces.BarberserviceService;
import com.service.interfaces.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.presentation.constants.HtmlConstants.Paths.*;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_BARBERSERVICES;
import static com.presentation.constants.HtmlConstants.Redirects.redirectToUpdate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/barberservices")
public class WebBarberserviceController extends BaseWebController<BarberServiceCreationDTO> {

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

        model.addAttribute("priceRangeValues", PriceRanges.values());
        model.addAttribute("categories", BarberServiceCategory.values());

        model.addAttribute("serviceName", serviceName);
        model.addAttribute("category", category);
        model.addAttribute("priceRange", priceRange);

        return BARBERSERVICES;
    }

    @GetMapping("/new")
    public String showBarberServiceCreationForm(Model model, Principal principal) {

        return showCreationForm(model, principal, new BarberServiceCreationDTO());
    }

    @PostMapping("/new")
    public String createBarberService(
            @Valid @ModelAttribute(name = "dto") BarberServiceCreationDTO dto,
            BindingResult bindingResult,
            Model model,
            Principal principal
    ) {

        return createEntity(dto, bindingResult, model, principal, REDIRECT_BARBERSERVICES);
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
    protected void executeCreation(BarberServiceCreationDTO dto) {

        barberserviceService.registerNewBarberService(dto);
    }

    @Override
    public String renderCreationForm(Model model, Principal principal, BarberServiceCreationDTO dto) {

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("dto", dto);

        populateCreationForm(model);

        return BARBERSERVICE_CREATION;
    }

    @Override
    protected void populateCreationForm(Model model) {

        model.addAttribute("categories", BarberServiceCategory.values());
    }
}
