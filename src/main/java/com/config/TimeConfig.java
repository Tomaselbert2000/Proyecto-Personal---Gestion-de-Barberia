package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

/**
 * Configuración relacionada con el manejo del tiempo en la aplicación BarberShop.
 * Define las configuraciones necesarias para el formato y el procesamiento de fechas y horas.
 */

@Configuration
public class TimeConfig {

    @Bean
    public Clock clock() {

        return Clock.system(ZoneId.of("America/Argentina/Buenos_Aires"));
    }
}
