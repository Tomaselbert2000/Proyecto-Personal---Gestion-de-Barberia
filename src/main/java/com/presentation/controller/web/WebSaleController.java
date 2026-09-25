package com.presentation.controller.web;

import static com.presentation.constants.HtmlConstants.Paths.SALES;
import static com.presentation.constants.HtmlConstants.Paths.SALE_DETAIL;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_SALES;

import java.math.BigDecimal;
import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.enums.SaleCompositionFilter;
import com.service.interfaces.EmployeeService;
import com.service.interfaces.PaymentMethodService;
import com.service.interfaces.SaleService;

import lombok.RequiredArgsConstructor;

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
        @RequestParam (required = false) BigDecimal minTotal,
        @RequestParam (required = false) BigDecimal maxTotal,
        @RequestParam (required = false) String paymentName,
        @RequestParam (required = false) String employeeName,
        @RequestParam (required = false) SaleCompositionFilter compositionFilter
    ){
        
        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("compositionFilters", SaleCompositionFilter.values());

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
    public String showSaleDetail(@PathVariable Long saleID, Model model){

        model.addAttribute("dto", saleService.getSale(saleID));

        return SALE_DETAIL;
    }

    @PostMapping("/{saleID}/cancel")
    public String cancelSale(@PathVariable Long saleID){

        saleService.cancelSale(saleID);

        return REDIRECT_SALES;
    }
}
