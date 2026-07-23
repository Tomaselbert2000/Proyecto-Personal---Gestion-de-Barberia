package com.launcher.animation;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import static com.launcher.constants.AnimationEngineConstants.*;

/**
 * Motor de animación encargado de gestionar transiciones visuales en la aplicación.
 * Proporciona métodos estáticos para ejecutar animaciones de entrada, salida y combinadas
 * (deslizamiento + desvanecimiento) sobre nodos de JavaFX.
 *
 * <p>Este componente sigue el patrón Singleton implícito mediante constructor privado,
 * asegurando que todas las operaciones de animación compartan configuraciones globales
 * definidas en {@link com.launcher.constants.AnimationEngineConstants}. La arquitectura separa la lógica de
 * animación de la gestión del ciclo de vida de los nodos.</p>
 *
 * <p>Las animaciones se ejecutan asíncronamente y los nodos eliminados del contenedor
 * padre cuando finalizan transiciones de salida, garantizando una limpieza automática
 * de recursos visuales.</p>
 */
public final class AnimationEngine {

    private AnimationEngine() {
    }

    /**
     * Ejecuta una animación de desvanecimiento para mostrar un nodo desde la opacidad cero hasta completa.
     * Utiliza interpolador por defecto y duración configurada globalmente.
     *
     * @param node nodo JavaFX sobre el cual aplicar la animación de entrada
     */
    public static void fadeNodeIn(Node node) {

        FadeTransition fadeTransition = generateFadeTransitionInstance(node);

        configureTransitionOpacity(fadeTransition, NO_OPACITY, FULL_OPACITY);

        setDefaultInterpolatorOnTransition(fadeTransition);

        fadeTransition.play();
    }

    /**
     * Ejecuta una animación de desvanecimiento para mostrar un nodo con un retraso personalizado.
     * El retraso se aplica antes del inicio de la transición, permitiendo sincronizar
     * múltiples animaciones en secuencia.
     *
     * @param node               nodo JavaFX sobre el cual aplicar la animación de entrada
     * @param delayInMiliseconds retraso en milisegundos antes de iniciar la animación
     */
    public static void fadeNodeIn(Node node, Double delayInMiliseconds) {

        FadeTransition fadeTransition = generateFadeTransitionInstance(node);

        configureTransitionOpacity(fadeTransition, NO_OPACITY, FULL_OPACITY);

        setDefaultInterpolatorOnTransition(fadeTransition);

        setTransitionDelayInMiliseconds(fadeTransition, delayInMiliseconds);

        fadeTransition.play();
    }

    /**
     * Ejecuta una animación de desvanecimiento para ocultar un nodo desde opacidad completa hasta cero.
     * Al finalizar la animación, el nodo se elimina del contenedor padre, liberando memoria y
     * evitando acumulación de elementos visuales innecesarios.
     *
     * @param node nodo JavaFX sobre el cual aplicar la animación de salida
     */
    public static void fadeNodeOut(Node node) {

        FadeTransition fadeTransition = generateFadeTransitionInstance(node);

        configureTransitionOpacity(fadeTransition, FULL_OPACITY, NO_OPACITY);

        setDefaultInterpolatorOnTransition(fadeTransition);

        configureTransitionBehaviorWhenClosing(fadeTransition, node);

        fadeTransition.play();
    }

    /**
     * Ejecuta una animación combinada de deslizamiento horizontal y desvanecimiento para ocultar un nodo.
     * Utiliza {@link ParallelTransition} para ejecutar ambas transiciones simultáneamente:
     * primero el nodo se traslada en el eje X y luego desaparece mediante opacidad.
     * Al finalizar, el nodo se elimina del contenedor padre.
     *
     * @param node      nodo JavaFX sobre el cual aplicar la animación de salida combinada
     * @param distanceX distancia de desplazamiento en el eje X (puede ser negativo para mover a la izquierda)
     */
    public static void slideAndFadeNodeOut(Node node, Double distanceX) {

        FadeTransition fadeTransition = generateFadeTransitionInstance(node);
        TranslateTransition translateTransition = generateRelativeTranslateTransition(node, distanceX);

        configureTransitionOpacity(fadeTransition, FULL_OPACITY, NO_OPACITY);

        ParallelTransition parallelTransition = generateParallelTransition(fadeTransition, translateTransition);

        setDefaultInterpolatorOnTransition(parallelTransition);

        configureTransitionBehaviorWhenClosing(parallelTransition, node);

        parallelTransition.play();
    }

    /**
     * Ejecuta una animación combinada de deslizamiento horizontal y desvanecimiento para mostrar un nodo.
     * El nodo comienza desplazado en el eje X y se traslada a su posición inicial mientras aumenta su opacidad.
     * Utiliza {@link ParallelTransition} para sincronizar ambas transiciones.
     *
     * @param node      nodo JavaFX sobre el cual aplicar la animación de entrada combinada
     * @param distanceX distancia inicial de desplazamiento en el eje X (puede ser negativo para mostrar desde la izquierda)
     */
    public static void slideAndFadeNodeIn(Node node, Double distanceX) {

        FadeTransition fadeTransition = generateFadeTransitionInstance(node);
        TranslateTransition translateTransition = generateAbsoluteTranslateTransition(node, distanceX);

        configureTransitionOpacity(fadeTransition, NO_OPACITY, FULL_OPACITY);

        ParallelTransition parallelTransition = generateParallelTransition(fadeTransition, translateTransition);

        setDefaultInterpolatorOnTransition(parallelTransition);

        parallelTransition.play();
    }

    /**
     * Genera una instancia de {@link FadeTransition} para el nodo especificado.
     * Utiliza la duración de desvanecimiento definida globalmente en las constantes del sistema.
     *
     * @param node nodo JavaFX sobre el cual aplicar la transición de opacidad
     * @return instancia configurada de {@link FadeTransition} lista para ser usada
     */
    private static FadeTransition generateFadeTransitionInstance(Node node) {

        return new FadeTransition(Duration.millis(DEFAULT_FADE_DURATION_IN_MS), node);
    }

    /**
     * Genera una transición de traslación relativa que mueve el nodo en el eje X desde su posición actual.
     * La distancia especificada se aplica como desplazamiento relativo, permitiendo animaciones
     * fluidas sin depender de coordenadas absolutas.
     *
     * @param node              nodo JavaFX sobre el cual aplicar la traslación
     * @param distanceOverXAxis distancia de desplazamiento en el eje X
     * @return instancia configurada de {@link TranslateTransition} con desplazamiento relativo
     */
    private static TranslateTransition generateRelativeTranslateTransition(Node node, Double distanceOverXAxis) {

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(DEFAULT_FADE_DURATION_IN_MS), node);
        translateTransition.setByX(distanceOverXAxis);

        return translateTransition;
    }

    /**
     * Genera una transición de traslación absoluta que mueve el nodo desde una posición inicial específica hasta su posición original.
     * Útil para animaciones de entrada donde el nodo comienza desplazado y retorna a su ubicación base.
     *
     * @param node              nodo JavaFX sobre el cual aplicar la traslación
     * @param distanceOverXAxis distancia inicial de desplazamiento en el eje X (posición desde donde comenzar)
     * @return instancia configurada de {@link TranslateTransition} con coordenadas absolutas definidas
     */
    private static TranslateTransition generateAbsoluteTranslateTransition(Node node, Double distanceOverXAxis) {

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(DEFAULT_FADE_DURATION_IN_MS), node);

        translateTransition.setFromX(distanceOverXAxis);
        translateTransition.setToX(STARTING_POSITION_OVER_X_AXIS);

        return translateTransition;
    }

    /**
     * Configura el interpolador por defecto en una transición.
     * Asegura consistencia visual entre todas las animaciones ejecutadas por este motor,
     * aplicando la curva de interpolación definida globalmente.
     *
     * @param transition transición JavaFX sobre la cual aplicar el interpolador
     */
    private static void setDefaultInterpolatorOnTransition(Transition transition) {

        transition.setInterpolator(DEFAULT_INTERPOLATOR);
    }

    /**
     * Configura los valores de opacidad inicial y final en una transición de desvanecimiento.
     * Define el rango de animación para la propiedad opacidad del nodo.
     *
     * @param transition           transición {@link FadeTransition} sobre la cual configurar los valores de opacidad
     * @param startingOpacityValue valor de opacidad inicial (0.0 a 1.0)
     * @param endingOpacityValue   valor de opacidad final (0.0 a 1.0)
     */
    private static void configureTransitionOpacity(FadeTransition transition, double startingOpacityValue, double endingOpacityValue) {

        transition.setFromValue(startingOpacityValue);
        transition.setToValue(endingOpacityValue);
    }

    /**
     * Genera una transición paralela que ejecuta múltiples transiciones simultáneamente.
     * Permite combinar animaciones de opacidad y traslación para efectos visuales complejos.
     *
     * @param fadeTransition      transición de desvanecimiento a incluir en la ejecución paralela
     * @param translateTransition transición de traslación a incluir en la ejecución paralela
     * @return instancia configurada de {@link ParallelTransition} con ambas transiciones
     */
    private static ParallelTransition generateParallelTransition(FadeTransition fadeTransition, TranslateTransition translateTransition) {

        return new ParallelTransition(fadeTransition, translateTransition);
    }

    /**
     * Configura el comportamiento de limpieza al finalizar una transición de salida.
     * Elimina el nodo del contenedor padre para liberar recursos y evitar acumulación de elementos visuales.
     * La operación se ejecuta automáticamente cuando la transición completa su ciclo.
     *
     * @param transition transición JavaFX sobre la cual configurar el callback de finalización
     * @param node       nodo a eliminar del contenedor padre al finalizar la animación
     */
    private static void configureTransitionBehaviorWhenClosing(Transition transition, Node node) {

        transition.setOnFinished(_ -> {

                    Parent nodeParent = node.getParent();

                    ((Pane) nodeParent).getChildren().remove(node);
                }
        );
    }

    /**
     * Configura el retraso de inicio en una transición.
     * Permite sincronizar múltiples animaciones ejecutándose en secuencia temporal.
     *
     * @param transition         transición JavaFX sobre la cual configurar el retraso
     * @param delayInMiliseconds retraso en milisegundos antes de iniciar la animación
     */
    private static void setTransitionDelayInMiliseconds(Transition transition, Double delayInMiliseconds) {

        transition.setDelay(Duration.millis(delayInMiliseconds));
    }
}