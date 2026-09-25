package com.presentation.controller.web;

import com.dto.client.ClientInfoDTO;
import com.dto.client.ClientUpdateDTO;
import com.enums.ClientNotesFilter;
import com.enums.RegisteredPhoneFilter;
import com.enums.RegistrationDateRange;
import com.service.interfaces.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.presentation.constants.HtmlConstants.Paths.CLIENTS;
import static com.presentation.constants.HtmlConstants.Paths.CLIENT_UPDATE;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_CLIENTS;
import static com.presentation.constants.HtmlConstants.Redirects.redirectToUpdate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/clients")
public class WebClientController {

    private final ClientService service;

    @GetMapping()
    public String showClientCatalog(
            Model model,
            Principal principal,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ClientNotesFilter notesFilter,
            @RequestParam(required = false) RegistrationDateRange registrationDateRange,
            @RequestParam(required = false) RegisteredPhoneFilter phoneFilter
    ) {

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
