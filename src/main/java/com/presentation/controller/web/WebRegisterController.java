package com.presentation.controller.web;

import com.dto.appuser.AppUserCreationDTO;
import com.service.interfaces.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

import static com.presentation.constants.HtmlConstants.Paths.REGISTER;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_LOGIN;

@Controller
@RequiredArgsConstructor
public class WebRegisterController {

    private final AppUserService service;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {

        model.addAttribute("dto", new AppUserCreationDTO());

        return REGISTER;
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute(name = "dto") AppUserCreationDTO dto,
            @RequestParam String confirmPassword,
            Model model) {

        if (Objects.equals(dto.getPassword(), confirmPassword)) {

            dto.setHasAdminRights(false);

            service.createAppUser(dto);

            return REDIRECT_LOGIN;

        } else {

            model.addAttribute("error", "Las credenciales ingresadas no coinciden");

            return REGISTER;
        }
    }
}
