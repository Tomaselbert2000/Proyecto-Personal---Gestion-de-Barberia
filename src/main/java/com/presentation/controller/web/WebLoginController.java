package com.presentation.controller.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.presentation.constants.HtmlTemplatePath.LOGIN_HTML_PATH;

@Controller
@RequiredArgsConstructor
public class WebLoginController {

    @GetMapping("/login")
    public String showLoginForm() {

        return LOGIN_HTML_PATH;
    }
}