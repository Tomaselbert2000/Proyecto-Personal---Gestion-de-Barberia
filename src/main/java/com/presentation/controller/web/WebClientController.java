package com.presentation.controller.web;

import com.dto.client.ClientInfoDTO;
import com.dto.client.ClientUpdateDTO;
import com.dto.stats.ClientAcquisitionStatsDTO;
import com.dto.stats.ClientNotesStatsDTO;
import com.dto.stats.ClientPhoneNumberStatsDTO;
import com.dto.stats.ClientRegistrationTrendStatDTO;
import com.enums.ClientNotesFilter;
import com.enums.RegisteredPhoneFilter;
import com.enums.RegistrationDateRange;
import com.service.interfaces.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

        ClientAcquisitionStatsDTO acquisitionStats = service.getClientStatsVsLastMonth();
        ClientPhoneNumberStatsDTO phoneNumbersStats = service.getPhoneNumberRegistrationStats();
        ClientNotesStatsDTO notesStats = service.getClientNotesStats();
        ClientRegistrationTrendStatDTO registrationTrendStats = service.getClientRegistrationTrendStats();

        List<ClientInfoDTO> liveSearch = service.liveSearch(name, registrationDateRange, phoneFilter, notesFilter);

        model.addAttribute("currentUser", principal.getName());

        model.addAttribute("acquisitionStats", acquisitionStats);
        model.addAttribute("phoneNumbersStats", phoneNumbersStats);
        model.addAttribute("notesStats", notesStats);
        model.addAttribute("registrationTrendStats", registrationTrendStats);

        model.addAttribute("phoneFilter", RegisteredPhoneFilter.values());
        model.addAttribute("notesFilter", ClientNotesFilter.values());
        model.addAttribute("registrationTrendFilter", RegistrationDateRange.values());

        model.addAttribute("liveSearch", liveSearch);

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
