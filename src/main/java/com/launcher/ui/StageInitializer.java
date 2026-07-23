package com.launcher.ui;

import com.enums.ViewRedirection;
import com.launcher.controller.helper.ViewRedirectionHelper;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import static com.launcher.constants.StringResource.DisplayString.APP_TITLE;
import static com.launcher.constants.ViewPath.DASHBOARD_VIEW_PATH;
import static com.launcher.constants.ViewPath.LOGIN_VIEW_PATH;

/**
 * Inicializador de escena principal para la aplicación JavaFX integrada con Spring.
 *
 * <p>Este componente orquesta el ciclo de vida inicial del Stage principal, estableciendo
 * la configuración básica (título, escena primaria) y determinando la vista inicial
 * basada en el estado del usuario (sesión activa o requerida). Se integra con Spring
 * mediante {@link ApplicationListener} para reaccionar al evento {@code StageReadyEvent},
 * garantizando que la inicialización ocurra después de que JavaFX haya cargado completamente
 * la escena.
 *
 * <p>El componente actúa como punto central de coordinación entre:
 * <ul>
 *   <li>La gestión de escenas ({@link SceneManager})</li>
 *   <li>La lógica de redirección de vistas ({@link ViewRedirectionHelper})</li>
 * </ul>
 *
 * <p>Esta arquitectura permite desacoplar la inicialización del Stage de la lógica
 * de negocio, manteniendo el control de flujo en un único punto de entrada.
 */
@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    private final SceneManager sceneManager;
    private final ViewRedirectionHelper viewRedirectionHelper;

    /**
     * Inyecta las dependencias necesarias para la inicialización del Stage.
     *
     * <p>La inyección de constructor garantiza que el objeto sea inmutable y
     * que las dependencias estén completamente resueltas antes de su uso.
     *
     * @param sceneManager          gestor de escenas encargado de cargar y cambiar vistas
     * @param viewRedirectionHelper helper que determina qué vista mostrar inicialmente
     */
    public StageInitializer(SceneManager sceneManager, ViewRedirectionHelper viewRedirectionHelper) {

        this.sceneManager = sceneManager;
        this.viewRedirectionHelper = viewRedirectionHelper;
    }

    /**
     * Maneja el evento de inicialización del Stage principal.
     *
     * <p>Este metodo se ejecuta automáticamente cuando JavaFX finaliza la carga
     * de la escena principal. Realiza las siguientes operaciones:
     * <ol>
     *   <li>Establece el título de la aplicación</li>
     *   <li>Registra el Stage como escenario primario para el SceneManager</li>
     *   <li>Determina la vista inicial basada en el estado del usuario</li>
     *   <li>Carga la escena correspondiente (Login o Dashboard)</li>
     * </ol>
     *
     * <p>El uso de {@link ViewRedirectionHelper#determineInitialView()} permite
     * centralizar la lógica de carga de la pantalla principal basado en
     * si el usuario prefiere mantener la sesión iniciada (omitiendo el Login),
     * facilitando cambios futuros sin modificar este componente. El switch expression
     * garantiza que solo se cargue una escena válida y evita código muerto.
     *
     * @param event evento que indica que el Stage ha finalizado su carga
     */
    @Override
    public void onApplicationEvent(StageReadyEvent event) {

        Stage stage = event.getStage();

        stage.setTitle(APP_TITLE);

        sceneManager.setPrimaryStage(stage);

        ViewRedirection viewToShow = viewRedirectionHelper.determineInitialView();

        switch (viewToShow) {

            case LOGIN -> sceneManager.switchScene(LOGIN_VIEW_PATH);

            case DASHBOARD -> sceneManager.switchScene(DASHBOARD_VIEW_PATH);
        }
    }

    /**
     * Indica si el listener soporta ejecución asíncrona.
     *
     * <p>Delega la respuesta al comportamiento por defecto de {@link ApplicationListener},
     * asegurando que el evento se procese en el hilo correcto del FXApplication.
     * Esta implementación garantiza compatibilidad con el modelo de hilos de JavaFX.
     *
     * @return {@code true} si soporta ejecución asíncrona, {@code false} en caso contrario
     */
    @Override
    public boolean supportsAsyncExecution() {

        return ApplicationListener.super.supportsAsyncExecution();
    }
}