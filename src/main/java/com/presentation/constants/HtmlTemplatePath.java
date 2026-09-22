package com.presentation.constants;

public final class HtmlTemplatePath {

    private HtmlTemplatePath() {
    }

    public static final String LOGIN_HTML_PATH = "login";
    public static final String REGISTER_HTML_PATH = "register";
    public static final String REDIRECT = "redirect:/";
    public static final String REDIRECT_LOGIN = REDIRECT + LOGIN_HTML_PATH;
    public static final String DASHBOARD_HTML_PATH = "dashboard";
    public static final String APPOINTMENTS_HTML_PATH = "appointments";
    public static final String REDIRECT_APPOINTMENTS = REDIRECT + APPOINTMENTS_HTML_PATH;
}
