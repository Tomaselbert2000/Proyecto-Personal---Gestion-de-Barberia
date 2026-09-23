package com.presentation.constants;

import static com.presentation.constants.HtmlConstants.Paths.*;

public final class HtmlConstants {

    private HtmlConstants() {
    }

    public static final class Paths {

        public static final String LOGIN = "login";
        public static final String REGISTER = "register";
        public static final String DASHBOARD = "dashboard";
        public static final String APPOINTMENTS = "appointments";
        public static final String BARBERSERVICES = "barberservices";
        public static final String UPDATE = "update";
    }

    public static final class Redirects {
        public static final String REDIRECT = "redirect:/";
        public static final String REDIRECT_LOGIN = REDIRECT + LOGIN;
        public static final String REDIRECT_APPOINTMENTS = REDIRECT + APPOINTMENTS;
        public static final String REDIRECT_BARBERSERVICES = REDIRECT + BARBERSERVICES;
        public static final String REDIRECT_BARBERSERVICE_UPDATE = REDIRECT + UPDATE;
    }
}
