package com.barbershop;

import com.barbershop.launcher.ui.JavafxApplication;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BarberiaNuevoApplication {

    static void main(String[] args) {

        Application.launch(JavafxApplication.class, args);
    }
}
