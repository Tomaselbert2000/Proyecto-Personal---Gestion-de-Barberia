package com.test_constant;

import java.time.LocalDate;

import static com.service.helper.ServiceTestVerificationHelper.addTabs;
import static com.service.helper.ServiceTestVerificationHelper.returnAsLowercase;
import static com.test_constant.EmployeeTestConstants.CreationValidData.EMPLOYEE_FIRST_NAME;
import static com.test_constant.EmployeeTestConstants.CreationValidData.EMPLOYEE_LAST_NAME;

public final class EmployeeTestConstants {

    private EmployeeTestConstants() {
    }

    public static final class CreationValidData {

        public static final String EMPLOYEE_FIRST_NAME = "Ramiro Hernan";
        public static final String EMPLOYEE_LAST_NAME = "Ardiles";
        public static final LocalDate EMPLOYEE_HIRE_DATE = LocalDate.of(2026, 1, 1);
        public static final Double EMPLOYEE_COMMISSION_PERCENTAGE = 0.70;
    }

    public static final class UpdateValidData {

        public static final String EMPLOYEE_NEW_NAME = "Juan";
        public static final String EMPLOYEE_NEW_SURNAME = "Garcia";
        public static final Boolean EMPLOYEE_NEW_IS_ACTIVE = false;
        public static final LocalDate EMPLOYEE_TERMINATION_DATE = LocalDate.of(2026, 12, 31);
        public static final Double EMPLOYEE_NEW_COMMISSION_PERCENTAGE = 0.75;
    }

    public static final class MapperData {

        public static final Long EMPLOYEE_ID = 1L;
        public static final Boolean EMPLOYEE_IS_ACTIVE_VALUE = true;
        public static final String EMPLOYEE_FIRST_NAME_WITH_SPACES = addTabs(EMPLOYEE_FIRST_NAME);
        public static final String EMPLOYEE_LAST_NAME_WITH_SPACES = addTabs(EMPLOYEE_LAST_NAME);
        public static final String EMPLOYEE_LOWERCASE_FIRST_NAME = returnAsLowercase(EMPLOYEE_FIRST_NAME);
        public static final String EMPLOYEE_LOWERCASE_LAST_NAME = returnAsLowercase(EMPLOYEE_LAST_NAME);
    }

    public static final class InvalidData {

        public static final String EMPLOYEE_INVALID_NAME = "T0m@s";
        public static final String EMPLOYEE_NAME_TOO_SHORT = "xyz";
        public static final String EMPLOYEE_NAME_TOO_LONG = "Gh59iK7pzSCNGrrmCmrHE1LrpqtASecZ01PG0GHMb8a1R33cPCx9fWyC8n3afRENyAjHp3p2y2G4qZr3tbgUerdWuHmEqGLG6Up1ZXt93SHrApeeSDiaJFDe";
        public static final Double EMPLOYEE_COMMISSION_PERCENTAGE_LOWER_THAN_ZERO = -0.50;
        public static final Double EMPLOYEE_COMMISSION_PERCENTAGE_HIGHER_THAN_ONE = 1.25;
    }
}
