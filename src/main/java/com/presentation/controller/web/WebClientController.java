package com.presentation.controller.web;

import com.dto.client.ClientCreationDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.client.ClientUpdateDTO;
import com.enums.ClientNotesFilter;
import com.enums.RegisteredPhoneFilter;
import com.enums.RegistrationDateRange;
import com.exceptions.BusinessException;
import com.service.interfaces.ClientService;
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

import static com.presentation.constants.HtmlConstants.Paths.CLIENTS;
import static com.presentation.constants.HtmlConstants.Paths.CLIENT_UPDATE;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_CLIENTS;
import static com.presentation.constants.HtmlConstants.Redirects.redirectToUpdate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/clients")
public class WebClientController implements WebController<ClientCreationDTO> {

    private final ClientService service;

    @GetMapping()
    public String showClientCatalog(
            Model model,
            Principal principal,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ClientNotesFilter notesFilter,
            @RequestParam(required = false) RegistrationDateRange registrationDateRange,
            @RequestParam(required = false) RegisteredPhoneFilter phoneFilter) {

        model.addAttribute("currentUser", principal.getName());

        model.addAttribute("acquisitionStats", service.getClientStatsVsLastMonth());
        model.addAttribute("phoneNumbersStats", service.getPhoneNumberRegistrationStats());
        model.addAttribute("notesStats", service.getClientNotesStats());
        model.addAttribute("registrationTrendStats", service.getClientRegistrationTrendStats());

        model.addAttribute("phoneFilterValues", RegisteredPhoneFilter.values());
        model.addAttribute("notesFilterValues", ClientNotesFilter.values());
        model.addAttribute("registrationTrendFilterValues", RegistrationDateRange.values());

        model.addAttribute("liveSearch", service.liveSearch(name, registrationDateRange, phoneFilter, notesFilter));

        model.addAttribute("name", name);
        model.addAttribute("notesFilter", notesFilter);
        model.addAttribute("registrationDateRange", registrationDateRange);
        model.addAttribute("phoneFilter", phoneFilter);

        return CLIENTS;
    }

    @GetMapping("/new")
    public String showClientCreationForm(Model model, Principal principal) {

        return renderCreationForm(model, principal, new ClientCreationDTO());
    }

    @PostMapping("/new")
    public String createClient(
            @Valid @ModelAttribute(name = "dto") ClientCreationDTO dto,
            BindingResult bindingResult,
            Model model,
            Principal principal) {

        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "validationErrors", bindingResult.getFieldErrors()
                            .stream()
                            .map(FieldError::getDefaultMessage)
                            .filter(Objects::nonNull)
                            .toList());

            return renderCreationForm(model, principal, dto);
        }

        try {

            service.registerNewClient(dto);

            return REDIRECT_CLIENTS;

        } catch (BusinessException exception) {

            model.addAttribute("validationErrors", List.of(exception.getMessage()));

            return renderCreationForm(model, principal, dto);
        }
    }

    @Override
    public String renderCreationForm(Model model, Principal principal, ClientCreationDTO dto) {
        return "";
    }

    @Override
    public void populateCreationCatalog(Model model) {

    }

    @PostMapping("/{clientID}/delete")
    public String deleteClient(@PathVariable Long clientID) {

        service.deleteClient(clientID);

        return REDIRECT_CLIENTS;
    }

    @GetMapping("/{clientID}/update")
    public String updateClient(@PathVariable Long clientID, Model model) {

        ClientInfoDTO dto = service.getClientInfo(clientID);

        model.addAttribute("dto", dto);

        return CLIENT_UPDATE;
    }

    @PostMapping("/{clientID}/update")
    public String updateClient(@PathVariable Long clientID, @ModelAttribute ClientUpdateDTO dto) {

        service.updateClient(clientID, dto);

        return redirectToUpdate(REDIRECT_CLIENTS, clientID);
    }
}