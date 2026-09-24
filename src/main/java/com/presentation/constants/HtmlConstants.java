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
        public static final String APPOINTMENT_UPDATE = "appointment-update";
        public static final String BARBERSERVICES = "barberservices";
        public static final String BARBERSERVICE_UPDATE = "barberservice-update";
        public static final String CLIENTS = "clients";
        public static final String CLIENT_UPDATE = "client-update";
        public static final String EMPLOYEES = "employees";
        public static final String EMPLOYEE_UPDATE = "employee-update";
        public static final String PAYMENTS = "payments";
        public static final String PAYMENT_UPDATE = "payment-update";
        public static final String PRODUCTS = "products";
        public static final String PRODUCT_STOCK = "product-stock";
        public static final String PRODUCT_UPDATE = "product-update";
    }

    public static final class Redirects {
        public static final String REDIRECT = "redirect:/";
        public static final String REDIRECT_LOGIN = REDIRECT + LOGIN;
        public static final String REDIRECT_APPOINTMENTS = REDIRECT + APPOINTMENTS;
        public static final String REDIRECT_BARBERSERVICES = REDIRECT + BARBERSERVICES;
        public static final String REDIRECT_CLIENTS = REDIRECT + CLIENTS;
        public static final String REDIRECT_EMPLOYEES = REDIRECT + EMPLOYEES;
        public static final String REDIRECT_PAYMENTS = REDIRECT + PAYMENTS;
        public static final String REDIRECT_PRODUCTS = REDIRECT + PRODUCTS;

        public static String redirectToUpdate(String baseRedirect, Long id) {

            return String.format("%s/%d/update", baseRedirect, id);
        }
    }
}
