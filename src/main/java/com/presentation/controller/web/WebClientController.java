package com.presentation.controller.web;

import com.dto.client.ClientCreationDTO;
import com.dto.client.ClientUpdateDTO;
import com.enums.ClientNotesFilter;
import com.enums.RegisteredPhoneFilter;
import com.enums.RegistrationDateRange;
import com.mapper.interfaces.ClientMapper;
import com.presentation.controller.BaseWebController;
import com.service.interfaces.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.presentation.constants.HtmlConstants.Paths.*;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_CLIENTS;

@Controller
@RequiredArgsConstructor
@RequestMapping("/clients")
public class WebClientController extends BaseWebController<ClientCreationDTO, ClientUpdateDTO> {

    private final ClientService service;
    private final ClientMapper mapper;

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

        return showCreationForm(model, principal, new ClientCreationDTO());
    }

    @PostMapping("/new")
    public String createClient(
            @Valid @ModelAttribute(name = "dto") ClientCreationDTO dto,
            BindingResult bindingResult,
            Model model,
            Principal principal) {

        return createEntity(dto, bindingResult, model, principal, REDIRECT_CLIENTS);
    }

    @Override
    protected ClientUpdateDTO invokeServiceAndReturnDTO(Long id) {

        return mapper.mapInfoDTOtoUpdateDTO(service.getClientInfo(id));
    }

    @Override
    protected void executeCreation(ClientCreationDTO dto) {

        service.registerNewClient(dto);
    }

    @Override
    protected void executeUpdate(Long entityID, ClientUpdateDTO updateDTO) {

        service.updateClient(entityID, updateDTO);
    }

    @Override
    protected void executeDeletion(Long entityID) {

        service.deleteClient(entityID);
    }

    @Override
    public String renderCreationForm(Model model, Principal principal, ClientCreationDTO dto) {

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("dto", dto);

        populateCreationForm(model);

        return CLIENT_CREATION;
    }

    @Override
    protected String renderUpdateForm(Model model, Long entityID, Principal principal, ClientUpdateDTO updateDTO) {

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("client", service.getClientInfo(entityID));
        model.addAttribute("updateDTO", updateDTO);

        return CLIENT_UPDATE;
    }

    @Override
    protected void populateCreationForm(Model model) {
    }

    @PostMapping("/{clientID}/delete")
    public String deleteClient(@PathVariable Long clientID) {

        return deleteEntity(clientID, REDIRECT_CLIENTS);
    }

    @GetMapping("/{clientID}/update")
    public String updateClient(@PathVariable Long clientID, Model model, Principal principal) {

        return renderUpdateForm(model, clientID, principal, invokeServiceAndReturnDTO(clientID));
    }

    @PostMapping("/{clientID}/update")
    public String updateClient(
            @PathVariable Long clientID,
            @Valid @ModelAttribute ClientUpdateDTO dto,
            BindingResult bindingResult,
            Model model,
            Principal principal
    ) {

        return updateEntity(clientID, dto, bindingResult, model, principal, REDIRECT_CLIENTS);
    }
}