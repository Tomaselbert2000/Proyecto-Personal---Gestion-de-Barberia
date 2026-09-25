package com.presentation.controller.web;

import com.dto.barberservice.BarberServiceInfoDTO;
import com.dto.barberservice.BarberServiceUpdateDTO;
import com.dto.stats.BarberServiceActiveOnCatalogStatsDTO;
import com.dto.stats.BarberServiceRevenueStatsDTO;
import com.dto.stats.BarberServiceSalesStatsDTO;
import com.dto.stats.BarberServiceUsageStatsDTO;
import com.enums.BarberServiceCategory;
import com.enums.PriceRanges;
import com.service.interfaces.BarberserviceService;
import com.service.interfaces.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.presentation.constants.HtmlConstants.Paths.BARBERSERVICES;
import static com.presentation.constants.HtmlConstants.Paths.BARBERSERVICE_UPDATE;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_BARBERSERVICES;
import static com.presentation.constants.HtmlConstants.Redirects.redirectToUpdate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/barberservices")
public class WebBarberserviceController {

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
}
