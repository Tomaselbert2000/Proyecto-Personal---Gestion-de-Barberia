package com.presentation.controller.web;

import com.dto.barberservice.BarberServiceInfoDTO;
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
import java.util.List;

import static com.presentation.constants.HtmlConstants.Paths.BARBERSERVICES;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_BARBERSERVICES;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_BARBERSERVICE_UPDATE;

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

        List<BarberServiceInfoDTO> catalog = barberserviceService.getServicesList();

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("activeOnCatalogStats", activeOnCatalogStatsDTO);
        model.addAttribute("saleStats", salesStatsDTO);
        model.addAttribute("revenueStats", revenueStatsDTO);
        model.addAttribute("usageStats", usageStatsDTO);

        model.addAttribute("catalog", catalog);

        model.addAttribute("liveSearch", barberserviceService.liveSearch(
                        serviceName,
                        category,
                        minPrice,
                        maxPrice
                )
        );

        model.addAttribute("priceRange", PriceRanges.values());
        model.addAttribute("category", BarberServiceCategory.values());


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

        return REDIRECT_BARBERSERVICE_UPDATE;
    }
}
