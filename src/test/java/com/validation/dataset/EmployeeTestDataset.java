package com.validation.dataset;

import java.time.LocalDate;

public class EmployeeTestDataset {

    public static final String NAME = "Ramiro Hernan";
    public static final String SURNAME = "Ardiles";
    public static final LocalDate HIRE_DATE = LocalDate.of(2026, 1, 1);
    public static final Double COMMISSION_PERCENTAGE = 0.70;

    public static final String INVALID_NAME = "T0m@s";
    public static final String NAME_TOO_SHORT = "xyz";
    public static final String NAME_TOO_LONG = "Gh59iK7pzSCNGrrmCmrHE1LrpqtASecZ01PG0GHMb8a1R33cPCx9fWyC8n3afRENyAjHp3p2y2G4qZr3tbgUerdWuHmEqGLG6Up1ZXt93SHrApeeSDiaJFDe";
    public static final Double COMMISSION_PERCENTAGE_LOWER_THAN_ZERO = -0.50;
    public static final Double COMMISSION_PERCENTAGE_HIGHER_THAN_ONE = 1.25;

    public static final Boolean IS_ACTIVE = false;
    public static final LocalDate TERMINATION_DATE = LocalDate.of(2026, 12, 31);
    public static final Double NEW_COMMISSION_PERCENTAGE = 0.75;
}
