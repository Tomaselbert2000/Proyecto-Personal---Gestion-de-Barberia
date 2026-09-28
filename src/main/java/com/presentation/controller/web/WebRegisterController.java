package com.presentation.controller.web;

import com.dto.appuser.AppUserCreationDTO;
import com.dto.appuser.AppUserUpdateDTO;
import com.exceptions.BusinessException;
import com.presentation.controller.BaseWebController;
import com.service.interfaces.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

import static com.presentation.constants.HtmlConstants.Paths.REGISTER;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_LOGIN;

@Controller
@RequiredArgsConstructor
public class WebRegisterController extends BaseWebController<AppUserCreationDTO, AppUserUpdateDTO> {

    private final AppUserService service;

    @GetMapping("/register")
    public String showRegisterForm(Model model, Principal principal) {

        return renderCreationForm(model, principal, new AppUserCreationDTO());
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute(name = "dto") AppUserCreationDTO dto,
            BindingResult bindingResult,
            @RequestParam String confirmPassword,
            Model model
    ) {

        if (bindingResult.hasErrors()) {

            model.addAttribute(VALIDATION_TAG, collectValidationErrors(bindingResult));

            return REGISTER;
        }

        try {

            if (Objects.equals(dto.getPassword(), confirmPassword)) {

                executeCreation(dto);

                return REDIRECT_LOGIN;

            } else {

                model.addAttribute("error", "Las credenciales ingresadas no coinciden");

                return REGISTER;
            }

        } catch (BusinessException exception) {

            model.addAttribute(VALIDATION_TAG, List.of(exception.getMessage()));

            return REGISTER;
        }
    }

    @Override
    protected AppUserUpdateDTO invokeServiceAndReturnDTO(Long id) {
        return null;
    }

    @Override
    protected void executeCreation(AppUserCreationDTO dto) {

        dto.setHasAdminRights(false);

        service.createAppUser(dto);
    }

    @Override
    protected void executeUpdate(Long entityID, AppUserUpdateDTO appUserUpdateDTO) {
    }

    @Override
    protected void executeDeletion(Long entityID) {
    }

    @Override
    protected String renderCreationForm(Model model, Principal principal, AppUserCreationDTO dto) {

        model.addAttribute("dto", dto);

        return REGISTER;
    }

    @Override
    protected String renderUpdateForm(Model model, Long entityID, Principal principal, AppUserUpdateDTO appUserUpdateDTO) {
        return "";
    }

    @Override
    protected void populateCreationForm(Model model) {
    }
}
