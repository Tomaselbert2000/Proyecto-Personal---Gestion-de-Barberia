package com.dto.stats;

public final class EmptyStatDTOFactory {

    private EmptyStatDTOFactory() {
    }

    public static PaymentMethodUsageStatsDTO emptyPaymentMethodUsageStatsDTO() {

        return PaymentMethodUsageStatsDTO.builder().build();
    }

    public static PaymentMethodRevenueStatsDTO emptyPaymentMethodRevenueStatsDTO() {

        return PaymentMethodRevenueStatsDTO.builder().build();
    }

    public static EmployeeRevenueStatsDTO emptyEmployeeRevenueStatsDTO() {

        return EmployeeRevenueStatsDTO.builder().build();
    }

    public static EmployeeServicesCompletedStatsDTO emptyEmployeeServicesCompletedStatsDTO() {

        return EmployeeServicesCompletedStatsDTO.builder().build();
    }

    public static BarberServiceSalesStatsDTO emptyBarberServiceSaleStatsDTO() {

        return BarberServiceSalesStatsDTO.builder().build();
    }

    public static BarberServiceRevenueStatsDTO emptyBarberServiceRevenueStatsDTO() {

        return BarberServiceRevenueStatsDTO.builder().build();
    }

    public static BarberServiceUsageStatsDTO emptyBarberServiceUsageStatsDTO() {

        return BarberServiceUsageStatsDTO.builder().build();
    }

    public static AppointmentTodayStatsDTO emptyAppointmentTodayStatsDTO() {

        return AppointmentTodayStatsDTO.builder().build();
    }

    public static AppointmentTomorrowStatsDTO emptyAppointmentTomorrowStatsDTO() {

        return AppointmentTomorrowStatsDTO.builder().build();
    }

    public static AppointmentMonthlyComparisonDTO emptyAppointmentMonthlyComparisonDTO() {

        return AppointmentMonthlyComparisonDTO.builder().build();
    }

    public static AppointmentCanceledStatsDTO emptyAppointmentCanceledStatsDTO() {

        return AppointmentCanceledStatsDTO.builder().build();
    }

    public static ExpectedIncomeStatDTO emptyExpectedIncomeStatDTO() {

        return ExpectedIncomeStatDTO.builder().build();
    }

    public static BarberServiceActiveOnCatalogStatsDTO emptyActiveOnCatalogStatsDTO() {

        return BarberServiceActiveOnCatalogStatsDTO.builder().build();
    }

    public static ProductTotalStockStatsDTO emptyProductTotalStockStatsDTO() {

        return ProductTotalStockStatsDTO.builder().build();
    }

    public static ProductMostSoldStatsDTO emptyProductMostSoldStatsDTO() {

        return ProductMostSoldStatsDTO.builder().build();
    }

    public static ProductHighestRevenueStatsDTO emptyProductHighestRevenueStatsDTO() {

        return ProductHighestRevenueStatsDTO.builder().build();
    }

    public static ProductStockValueStatsDTO emptyProductTotalStockValueStatDTO() {

        return ProductStockValueStatsDTO.builder().build();
    }

    public static ClientAcquisitionStatsDTO emptyClientAcquisitionStatsDTO() {

        return ClientAcquisitionStatsDTO.builder().build();
    }

    public static InventoryAlertStatsDTO emptyInventoryAlertStatsDTO() {

        return InventoryAlertStatsDTO.builder().build();
    }

    public static TotalClientsStatsDTO emptyTotalClientsStatsDTO() {

        return TotalClientsStatsDTO.builder().build();
    }

    public static ClientPhoneNumberStatsDTO emptyClientPhoneNumberStatsDTO() {

        return ClientPhoneNumberStatsDTO.builder().build();
    }

    public static ClientRegistrationTrendStatDTO emptyClientRegistrationTrendStatDTO() {

        return ClientRegistrationTrendStatDTO.builder().build();
    }

    public static ClientNotesStatsDTO emptyClientNotesStatsDTO() {

        return ClientNotesStatsDTO.builder().build();
    }
}
