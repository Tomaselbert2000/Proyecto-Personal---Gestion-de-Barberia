package com.barbershop;

import com.barbershop.enums.AppointmentStatus;
import com.barbershop.enums.BarberServiceCategory;
import com.barbershop.enums.ProductCategory;
import com.barbershop.model.*;
import com.barbershop.repository.*;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final BarberServiceRepository barberServiceRepository;
    private final AppointmentRepository appointmentRepository;
    private final ProductRepository productRepository;

    private Client client;
    private Employee employee;
    private BarberService barberService;
    private Appointment appointment;
    private Product product;

    private static final String NATIONAL_ID_CARD_NUMBER = "1234567";
    private static final String NAME = "Tomas Gabriel";
    private static final String SURNAME = "Elbert";
    private static final LocalDate REGISTRATION_DATE = LocalDate.of(2026, 1, 1);
    private static final String EMAIL = "tomas@gmail.com";
    private static final List<String> PHONE_LIST = List.of("1122334455");

    private static final String EMPLOYEE_NAME = "Ramiro";
    private static final String EMPLOYEE_SURNAME = "Ardiles";
    private static final LocalDate EMPLOYEE_HIRE_DATE = LocalDate.of(2025, 1, 1);
    private static final Boolean IS_ACTIVE_VALUE = true;
    private static final Double COMMISSION_PERECENTAGE = 0.70;

    private static final String SERVICE_NAME = "Corte de pelo + barba";
    private static final Double PRICE = 15000.0;
    private static final BarberServiceCategory BARBER_SERVICE_CATEGORY = BarberServiceCategory.CORTE_Y_BARBA;

    private static final LocalDateTime REGISTRATION_TIMESTAMP = LocalDateTime.of(2026, 1, 1, 10, 0);
    private static final LocalDateTime START_DATETIME = LocalDateTime.of(2026, 1, 1, 12, 30);
    private static final LocalDateTime END_DATETIME = LocalDateTime.of(2026, 1, 1, 13, 0);
    private static final AppointmentStatus STATUS = AppointmentStatus.PROGRAMADO;

    private static final String PRODUCT_NAME = "Shampoo matizador";
    private static final Double PRODUCT_COST = 6500.0;
    private static final ProductCategory PRODUCT_CATEGORY = ProductCategory.SHAMPOO;
    private static final Double PRODUCT_PRICE = 7650.0;
    private static final ProductPresentationUnit PRESENTATION_UNIT = ProductPresentationUnit.GRAMOS;
    private static final Integer PRESENTATION_SIZE = 120;
    private static final Double MIN_PRICE = 7000.0;
    private static final Double WHOLE_SALE_PRICE = 7300.0;
    private static final Double MAX_DISCOUNT_PERCENTAGE = 0.3;
    private static final String BRAND_NAME = "American Crew";
    private static final String OPTIONAL_DESCRIPTION = "Sin datos adicionales";
    private static final Integer CURRENT_STOCK_LEVEL = 150;
    private static final Integer SAFETY_STOCK_LEVEL = 90;
    private static final LocalDateTime PRODUCT_CREATION_DATE = LocalDateTime.of(2026, 1, 2, 15, 30);


    private static final LocalDateTime NOW = LocalDateTime.now();


    @Override
    public void run(String @NonNull ... args) {

        setupClient();
        saveClient();

        setupEmployee();
        saveEmployee();

        setupBarberService();
        saveBarberService();

        setupAppointment();
        saveAppointment();

        setupProduct();
        saveProduct();
    }

    private void setupClient() {

        client = Client.builder()
                .nationalIdentityCardNumber(NATIONAL_ID_CARD_NUMBER)
                .firstName(NAME)
                .lastName(SURNAME)
                .registrationDate(REGISTRATION_DATE)
                .email(EMAIL)
                .phoneNumbersList(PHONE_LIST).
                build();
    }

    private void setupEmployee() {

        employee = Employee.builder()
                .firstName(EMPLOYEE_NAME)
                .lastName(EMPLOYEE_SURNAME)
                .hireDate(EMPLOYEE_HIRE_DATE)
                .isActive(IS_ACTIVE_VALUE)
                .commissionPercentage(COMMISSION_PERECENTAGE)
                .build();
    }

    private void setupBarberService() {

        barberService = BarberService.builder()
                .name(SERVICE_NAME)
                .price(PRICE)
                .serviceCategory(BARBER_SERVICE_CATEGORY)
                .build();
    }

    private void setupAppointment() {

        appointment = Appointment.builder()
                .client(client)
                .barberservice(barberService)
                .employee(employee)
                .registrationTimestamp(REGISTRATION_TIMESTAMP)
                .startDateTime(START_DATETIME)
                .endDateTime(END_DATETIME)
                .modifiedDate(NOW)
                .currentStatus(STATUS)
                .build();
    }

    private void setupProduct() {

        product = Product.builder()
                .name(PRODUCT_NAME)
                .optionalDescription(OPTIONAL_DESCRIPTION)
                .brandName(BRAND_NAME)
                .presentationUnit(PRESENTATION_UNIT)
                .presentationSize(PRESENTATION_SIZE)
                .productCost(PRODUCT_COST)
                .currentPrice(PRODUCT_PRICE)
                .category(PRODUCT_CATEGORY)
                .currentStockLevel(CURRENT_STOCK_LEVEL)
                .safetyStockLevel(SAFETY_STOCK_LEVEL)
                .creationDate(PRODUCT_CREATION_DATE)
                .imageFilePath("")
                .build();
    }

    private void saveClient() {

        clientRepository.save(client);
    }

    private void saveEmployee() {

        employeeRepository.save(employee);
    }

    private void saveBarberService() {

        barberServiceRepository.save(barberService);
    }

    private void saveAppointment() {

        appointmentRepository.save(appointment);
    }

    private void saveProduct() {

        productRepository.save(product);
    }
}
