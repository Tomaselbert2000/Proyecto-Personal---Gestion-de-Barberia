package com.presentation.controller.web;

import com.dto.paymentmethod.PaymentMethodCreationDTO;
import com.dto.paymentmethod.PaymentMethodUpdateDTO;
import com.enums.PaymentMethodModifierType;
import com.enums.PaymentMethodStatus;
import com.exceptions.BusinessException;
import com.mapper.interfaces.PaymentMethodMapper;
import com.presentation.controller.BaseWebController;
import com.service.interfaces.PaymentMethodService;
import com.service.interfaces.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static com.presentation.constants.HtmlConstants.Paths.*;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_PAYMENTS;
import static com.presentation.constants.StringResource.OperationMessages.PAYMENT_METHOD_OPERATION_FAILED;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payments")
public class WebPaymentMethodController extends BaseWebController<PaymentMethodCreationDTO, PaymentMethodUpdateDTO> {

    private final SaleService saleService;
    private final PaymentMethodService paymentMethodService;
    private final PaymentMethodMapper mapper;

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

        model.addAttribute(OPERATION_FAILED_TAG, PAYMENT_METHOD_OPERATION_FAILED);

        return PAYMENTS;
    }

    @GetMapping("/new")
    public String showPaymentMethodCreationForm(Model model, Principal principal) {

        return showCreationForm(model, principal, new PaymentMethodCreationDTO());
    }

    @PostMapping("/new")
    public String createPaymentMethod(
            @Valid @ModelAttribute(name = "dto") PaymentMethodCreationDTO dto,
            BindingResult bindingResult,
            Model model,
            Principal principal
    ) {

        return createEntity(dto, bindingResult, model, principal, REDIRECT_PAYMENTS);
    }

    @PostMapping("/{paymentID}/toggleStatus")
    public String togglePaymentMethodStatus(@PathVariable Long paymentID) {

        try {

            paymentMethodService.togglePaymentMethodStatus(paymentID);

            return REDIRECT_PAYMENTS;

        } catch (BusinessException e) {

            return REDIRECT_PAYMENTS + ERROR_SUFFIX;
        }
    }

    @PostMapping("/{paymentID}/delete")
    public String deletePaymentMethod(@PathVariable Long paymentID) {

        return deleteEntity(paymentID, REDIRECT_PAYMENTS);
    }

    @GetMapping("/{paymentID}/update")
    public String updatePaymentMethod(
            @PathVariable Long paymentID,
            Model model,
            Principal principal
    ) {

        try {

            return renderUpdateForm(model, paymentID, principal, invokeServiceAndReturnDTO(paymentID));

        } catch (BusinessException e) {

            model.addAttribute(VALIDATION_TAG, List.of(e.getMessage()));

            return renderCreationForm(model, principal, new PaymentMethodCreationDTO());
        }
    }

    @PostMapping("/{paymentID}/update")
    public String updatePaymentMethod(
            @PathVariable Long paymentID,
            @Valid @ModelAttribute PaymentMethodUpdateDTO dto,
            BindingResult bindingResult,
            Model model,
            Principal principal
    ) {

        return updateEntity(paymentID, dto, bindingResult, model, principal, REDIRECT_PAYMENTS);
    }

    @Override
    protected PaymentMethodUpdateDTO invokeServiceAndReturnDTO(Long id) {

        return mapper.mapInfoDTOtoUpdateDTO(paymentMethodService.getPaymentMethod(id));
    }

    @Override
    protected void executeCreation(PaymentMethodCreationDTO dto) {

        paymentMethodService.registerNewPaymentMethod(dto);
    }

    @Override
    protected void executeUpdate(Long entityID, PaymentMethodUpdateDTO dto) {

        paymentMethodService.updatePaymentMethod(entityID, dto);
    }

    @Override
    protected void executeDeletion(Long entityID) {

        paymentMethodService.deletePaymentMethod(entityID);
    }

    @Override
    protected String renderCreationForm(Model model, Principal principal, PaymentMethodCreationDTO dto) {

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("dto", dto);

        populateCreationForm(model);

        return PAYMENT_CREATION;
    }

    @Override
    protected String renderUpdateForm(Model model, Long entityID, Principal principal, PaymentMethodUpdateDTO dto) {

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("payment", paymentMethodService.getPaymentMethod(entityID));
        model.addAttribute("modifiers", PaymentMethodModifierType.values());
        model.addAttribute("dto", dto);

        return PAYMENT_UPDATE;
    }

    @Override
    protected void populateCreationForm(Model model) {

        model.addAttribute("modifiers", PaymentMethodModifierType.values());
    }
}
