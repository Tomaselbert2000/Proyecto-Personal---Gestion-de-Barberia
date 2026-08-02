package com;

import com.presentation.bootstrap.BarberiaJavaFxApplication;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BarberiaNuevoApplication {

    static void main(String[] args) {

        Application.launch(BarberiaJavaFxApplication.class, args);
    }
}
