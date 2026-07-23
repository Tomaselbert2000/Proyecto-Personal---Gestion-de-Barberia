package com.config.time;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

/**
 * Configuración centralizada de gestión temporal para la aplicación.
 *
 * <p>Esta clase define los beans necesarios para establecer una fuente única
 * de tiempo sincronizado con la zona horaria de Argentina (Buenos Aires).
 * El componente se integra automáticamente con Spring para garantizar que
 * todas las operaciones temporales en la aplicación utilicen la misma referencia
 * temporal, evitando inconsistencias causadas por múltiples fuentes de tiempo.
 *
 * <p>El uso de {@link Clock} en lugar de {@link System#currentTimeMillis()} o
 * {@link java.time.LocalDate} proporciona:
 * <ul>
 *   <li>Control centralizado de la fuente temporal</li>
 *   <li>Facilidad para cambiar zonas horarias sin modificar el código</li>
 *   <li>Consistencia en operaciones asincrónicas que requieren tiempo</li>
 * </ul>
 *
 * <p>El bean se registra automáticamente en el contexto Spring y está disponible
 * para inyección en servicios, controladores, repositorios y componentes que
 * requieren acceso al tiempo actual.
 */
@Configuration
public class TimeConfig {

    /**
     * Proporciona una instancia de Clock configurada con la zona horaria de Buenos Aires.
     *
     * <p>El metodo crea y registra un bean de {@link Clock} que utilizará el reloj del sistema
     * con la zona horaria específica de Argentina (America/Argentina/Buenos_Aires). Esta configuración
     * garantiza que todas las operaciones temporales en la aplicación utilicen la misma referencia
     * temporal, evitando inconsistencias entre diferentes componentes.
     *
     * <p>El uso de {@link Clock()} en lugar de {@link java.time.Instant()} o
     * {@link System#currentTimeMillis()} permite:
     * <ul>
     *   <li>Control centralizado de la fuente temporal</li>
     *   <li>Cambio fácil de zona horaria sin modificar el código</li>
     *   <li>Consistencia en operaciones asincrónicas que requieren tiempo</li>
     * </ul>
     *
     * <p>La instancia es inyectada automáticamente en componentes que requieren
     * acceso al tiempo actual mediante constructor injection o @Autowired.
     *
     * @return una instancia configurada de Clock con zona horaria de Buenos Aires lista para
     * ser utilizada en operaciones temporales a nivel de aplicación.
     */
    @Bean
    public Clock clock() {

        return Clock.system(ZoneId.of("America/Argentina/Buenos_Aires"));
    }
}