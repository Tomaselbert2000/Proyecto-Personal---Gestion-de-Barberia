package com.presentation.controller.web;

import com.enums.SaleCompositionFilter;
import com.service.interfaces.EmployeeService;
import com.service.interfaces.PaymentMethodService;
import com.service.interfaces.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

import static com.presentation.constants.HtmlConstants.Paths.SALES;
import static com.presentation.constants.HtmlConstants.Paths.SALE_DETAIL;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_SALES;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sales")
public class WebSaleController {

    private final SaleService saleService;
    private final PaymentMethodService paymentMethodService;
    private final EmployeeService employeeService;

    @GetMapping
    public String showSaleCatalog(
            Principal principal,
            Model model,
            @RequestParam(required = false) BigDecimal minTotal,
            @RequestParam(required = false) BigDecimal maxTotal,
            @RequestParam(required = false) String paymentName,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) SaleCompositionFilter compositionFilter
    ) {

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("compositionFilters", SaleCompositionFilter.values());
        model.addAttribute("payments", paymentMethodService.getNames());
        model.addAttribute("employees", employeeService.getNames());

        model.addAttribute("minTotal", minTotal);
        model.addAttribute("maxTotal", maxTotal);
        model.addAttribute("employeeName", employeeName);
        model.addAttribute("paymentName", paymentName);
        model.addAttribute("compositionFilter", compositionFilter);

        model.addAttribute("liveSearch", saleService.liveSearch(minTotal, maxTotal, paymentName, employeeName, compositionFilter));

        model.addAttribute("monthlyIncomeStats", saleService.getMonthlyIncomeStats());
        model.addAttribute("averageTicketStats", saleService.getAverageTicketStats());
        model.addAttribute("salesTodayStats", saleService.getSalesTodayStats());
        model.addAttribute("productIncomeStats", saleService.getProductIncomeStats());

        return SALES;
    }

    @GetMapping("/{saleID}/details")
    public String showSaleDetail(@PathVariable Long saleID, Model model, Principal principal) {

        model.addAttribute("dto", saleService.getSale(saleID));
        model.addAttribute("currentUser", principal.getName());

        return SALE_DETAIL;
    }

    @PostMapping("/{saleID}/cancel")
    public String cancelSale(@PathVariable Long saleID) {

        saleService.cancelSale(saleID);

        return REDIRECT_SALES;
    }
}
