package com.launcher.ui;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

/**
 * Evento personalizado que notifica cuando el Stage principal ha finalizado su carga.
 *
 * <p>Extiende {@link ApplicationEvent} de Spring para habilitar la comunicación
 * asíncrona entre componentes del sistema JavaFX y Spring. Este evento se dispara
 * automáticamente cuando el Stage alcanza el estado "ready", permitiendo a los
 * listeners como {@link com.launcher.ui.StageInitializer} coordinar la inicialización
 * de escenas y vistas después de que JavaFX haya completado la carga de recursos.
 *
 * <p>La arquitectura utiliza este evento para garantizar que la lógica de inicialización
 * de vistas (Login o Dashboard) se ejecute únicamente después de que el Stage esté
 * completamente configurado, evitando errores de acceso a recursos no inicializados.
 */
public class StageReadyEvent extends ApplicationEvent {

    /**
     * Crea una instancia del evento asociada al Stage principal.
     *
     * <p>El constructor delega en {@link ApplicationEvent#ApplicationEvent(Object)}
     * para establecer la fuente del evento, permitiendo que los listeners accedan
     * al Stage a través de {@link #getStage()}.
     *
     * @param source objeto fuente del evento, debe ser una instancia de {@link Stage}
     */
    public StageReadyEvent(Object source) {
        super(source);
    }

    /**
     * Obtiene el Stage principal asociado al evento.
     *
     * <p>Realiza un cast explícito a {@link Stage} para garantizar que el método
     * retorne siempre una instancia válida del tipo esperado. Este enfoque centraliza
     * la conversión de tipos y evita errores en los puntos de uso del evento.
     *
     * @return instancia del Stage principal que ha finalizado su carga
     */
    public Stage getStage() {

        return (Stage) getSource();
    }
}