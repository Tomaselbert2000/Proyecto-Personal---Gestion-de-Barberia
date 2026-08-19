package com;

import com.presentation.bootstrap.BarberiaJavaFxApplication;
import javafx.application.Application;
import javafx.application.HostServices;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BarberiaNuevoApplication {

    static void main(String[] args) {

        Application.launch(BarberiaJavaFxApplication.class, args);
    }

    @Bean
    HostServices hostServices(){

        return BarberiaJavaFxApplication.getGlobalHostServices();
    }
}
