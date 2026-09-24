package com.presentation.controller.web;

import com.dto.paymentmethod.PaymentMethodInfoDTO;
import com.dto.paymentmethod.PaymentMethodUpdateDTO;
import com.enums.PaymentMethodModifierType;
import com.enums.PaymentMethodStatus;
import com.service.interfaces.PaymentMethodService;
import com.service.interfaces.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.presentation.constants.HtmlConstants.Paths.PAYMENTS;
import static com.presentation.constants.HtmlConstants.Paths.PAYMENT_UPDATE;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_PAYMENTS;
import static com.presentation.constants.HtmlConstants.Redirects.redirectToUpdate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payments")
public class WebPaymentMethodController {

    private final SaleService saleService;
    private final PaymentMethodService paymentMethodService;

    @GetMapping
    public String showPaymentCatalog(
            Principal principal,
            Model model,
            @RequestParam(required = false) String paymentName,
            @RequestParam(required = false) PaymentMethodStatus status,
            @RequestParam(required = false) PaymentMethodModifierType modifierType
    ) {

        model.addAttribute("currentUser", principal.getName());

        model.addAttribute("statuses", PaymentMethodStatus.values());
        model.addAttribute("modifiers", PaymentMethodModifierType.values());

        model.addAttribute("liveSearch", paymentMethodService.liveSearch(paymentName, status, modifierType));

        model.addAttribute("paymentUsageStats", saleService.getMostUsedPaymentMethod());
        model.addAttribute("revenueStats", saleService.getHighestRevenuePaymentMethod());
        model.addAttribute("activePaymentsStats", paymentMethodService.getPaymentMethodCountMarkedAsActive());
        model.addAttribute("modifierValueSumStats", saleService.getModifierValueSumAcrossAllSales());

        model.addAttribute("paymentName", paymentName);
        model.addAttribute("status", status);
        model.addAttribute("modifier", modifierType);

        return PAYMENTS;
    }

    @PostMapping("/{paymentID}/toggleStatus")
    public String togglePaymentMethodStatus(@PathVariable Long paymentID) {

        paymentMethodService.togglePaymentMethodStatus(paymentID);

        return REDIRECT_PAYMENTS;
    }

    @PostMapping("/{paymentID}/delete")
    public String deletePaymentMethod(@PathVariable Long paymentID) {

        paymentMethodService.deletePaymentMethod(paymentID);

        return REDIRECT_PAYMENTS;
    }

    @GetMapping("/{paymentID}/update")
    public String updatePaymentMethod(@PathVariable Long paymentID, Model model) {

        PaymentMethodInfoDTO dto = paymentMethodService.getPaymentMethod(paymentID);

        model.addAttribute("dto", dto);
        model.addAttribute("modifierType", PaymentMethodModifierType.values());

        return PAYMENT_UPDATE;
    }

    @PostMapping("/{paymentID}/update")
    public String updatePaymentMethod(@PathVariable Long paymentID, @ModelAttribute PaymentMethodUpdateDTO dto) {

        paymentMethodService.updatePaymentMethod(paymentID, dto);

        return redirectToUpdate(REDIRECT_PAYMENTS, paymentID);
    }
}
