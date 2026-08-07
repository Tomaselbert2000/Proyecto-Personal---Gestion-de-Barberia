package com.dto.stats;

public final class EmptyStatDTOFactory {

    private static final String EMPTY_STRING_DATA = "Sin datos";
    private static final Long ZERO_L = 0L;
    private static final Double ZERO_DOT_ZERO = 0.0;

    public static PaymentMethodUsageStatsDTO emptyPaymentMethodUsageStatsDTO() {

        return PaymentMethodUsageStatsDTO.builder()
                .paymentMethodName(EMPTY_STRING_DATA)
                .amountOfSalesWhereIsUsed(ZERO_L)
                .build();
    }

    public static PaymentMethodRevenueStatsDTO emptyPaymentMethodRevenueStatsDTO() {

        return PaymentMethodRevenueStatsDTO.builder()
                .paymentMethod(EMPTY_STRING_DATA)
                .revenueAmount(ZERO_DOT_ZERO)
                .build();
    }

    public static EmployeeRevenueStatsDTO emptyEmployeeRevenueStatsDTO() {

        return EmployeeRevenueStatsDTO.builder()
                .employeeFirstname(EMPTY_STRING_DATA)
                .employeeLastname("")
                .totalRevenue(ZERO_DOT_ZERO)
                .build();
    }

    public static EmployeeServicesCompletedStatsDTO emptyEmployeeServicesCompletedStatsDTO() {

        return EmployeeServicesCompletedStatsDTO.builder()
                .employeFirstName(EMPTY_STRING_DATA)
                .employeLastName("")
                .totalServices(ZERO_L)
                .build();
    }

    public static BarberServiceSalesStatsDTO emptyBarberServiceSaleStatsDTO() {

        return BarberServiceSalesStatsDTO.builder()
                .barberServiceName(EMPTY_STRING_DATA)
                .amountOfSales(ZERO_L)
                .build();
    }

    public static BarberServiceRevenueStatsDTO emptyBarberServiceRevenueStatsDTO() {

        return BarberServiceRevenueStatsDTO.builder()
                .barberServiceName(EMPTY_STRING_DATA)
                .totalRevenue(ZERO_DOT_ZERO)
                .build();
    }

    public static BarberServiceUsageStatsDTO emptyBarberServiceUsageStatsDTO() {

        return BarberServiceUsageStatsDTO.builder()
                .barberServiceName(EMPTY_STRING_DATA)
                .totalUsage(ZERO_L)
                .build();
    }

    public static AppointmentTodayStatsDTO emptyAppointmentTodayStatsDTO() {

        return AppointmentTodayStatsDTO.builder()
                .appointmentCount(ZERO_L)
                .totalAmountAsFinished(ZERO_L)
                .build();
    }

    public static AppointmentTomorrowStatsDTO emptyAppointmentTomorrowStatsDTO() {

        return AppointmentTomorrowStatsDTO.builder()
                .totalPendingAppointments(ZERO_L)
                .scheduledAppointmentsTomorrow(ZERO_L)
                .build();
    }

    public static AppointmentMonthlyComparisonDTO emptyAppointmentMonthlyComparisonDTO() {

        return AppointmentMonthlyComparisonDTO.builder()
                .currentMonthAppointments(ZERO_L)
                .previousMonthAppointments(ZERO_L)
                .build();
    }

    public static AppointmentCanceledStatsDTO emptyAppointmentCanceledStatsDTO() {

        return AppointmentCanceledStatsDTO.builder()
                .canceledAppointmentThisMonth(ZERO_L)
                .totalAppointmentsThisMonth(ZERO_L)
                .build();
    }

    public static ExpectedIncomeStatDTO emptyExpectedIncomeStatDTO() {

        return ExpectedIncomeStatDTO.builder()
                .appointmentsToday(ZERO_L)
                .expectedIncomeSumForToday(ZERO_DOT_ZERO)
                .build();
    }

    public static BarberServiceActiveOnCatalogStatsDTO emptyActiveOnCatalogStatsDTO() {

        return BarberServiceActiveOnCatalogStatsDTO.builder()
                .amountOfActiveServices(ZERO_L)
                .amountOfDifferentCategories(ZERO_L)
                .build();
    }

    public static ProductTotalStockStatsDTO emptyProductTotalStockStatsDTO() {

        return ProductTotalStockStatsDTO.builder()
                .productCount(ZERO_L)
                .onLowOrCriticalStockCount(ZERO_L)
                .build();
    }

    public static ProductMostSoldStatsDTO emptyProductMostSoldStatsDTO() {

        return ProductMostSoldStatsDTO.builder()
                .productName(EMPTY_STRING_DATA)
                .unitsSold(ZERO_L)
                .build();
    }

    public static ProductHighestRevenueStatsDTO emptyProductHighestRevenueStatsDTO() {

        return ProductHighestRevenueStatsDTO.builder()
                .productName(EMPTY_STRING_DATA)
                .revenue(ZERO_DOT_ZERO)
                .build();
    }

    public static ProductStockValueStatsDTO emptyProductTotalStockValueStatDTO() {

        return ProductStockValueStatsDTO.builder()
                .totalStockValue(ZERO_DOT_ZERO)
                .totalUnits(ZERO_L)
                .build();
    }

    public static ClientAcquisitionStatsDTO emptyClientAcquisitionStatsDTO() {

        return ClientAcquisitionStatsDTO.builder()
                .newClientsThisMonth(ZERO_L)
                .newClientsLastMonth(ZERO_L)
                .percentageVsLastMonth(ZERO_DOT_ZERO)
                .build();
    }

    public static InventoryAlertStatsDTO emptyInventoryAlertStatsDTO() {

        return InventoryAlertStatsDTO.builder()
                .lowStockProductsCount(ZERO_L)
                .outOfStockProductsCount(ZERO_L)
                .build();
    }
}
