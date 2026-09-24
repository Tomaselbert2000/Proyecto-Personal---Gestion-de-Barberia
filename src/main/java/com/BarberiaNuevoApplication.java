package com;

import com.presentation.bootstrap.BarberiaJavaFxApplication;
import com.utils.time.TimeCalculation;
import javafx.application.Application;
import javafx.application.HostServices;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
@EnableScheduling
public class BarberiaNuevoApplication {

    static void main(String[] args) {

        Application.launch(BarberiaJavaFxApplication.class, args);

        TimeCalculation.setDateProvider(LocalDate::now);
        TimeCalculation.setDatetimeProvider(LocalDateTime::now);
    }

    @Bean
    HostServices hostServices() {

        return BarberiaJavaFxApplication.getGlobalHostServices();
    }
}
