package com.launcher.animation;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import static com.launcher.animation.AnimationEngineConstant.*;

/**
 * Motor de animación encargado de controlar y ejecutar las animaciones en la aplicación BarberShop.
 * Ofrece métodos para iniciar, detener y gestionar diferentes tipos de animaciones.
 */

public final class AnimationEngine {

    private AnimationEngine() {
    }

    public static void fadeNodeIn(Node node) {

        FadeTransition fadeTransition = generateFadeTransitionInstance(node);

        configureTransitionOpacity(fadeTransition, NO_OPACITY, FULL_OPACITY);

        setDefaultInterpolatorOnTransition(fadeTransition);

        fadeTransition.play();
    }

    public static void fadeNodeIn(Node node, Double delayInMiliseconds) {

        FadeTransition fadeTransition = generateFadeTransitionInstance(node);

        configureTransitionOpacity(fadeTransition, NO_OPACITY, FULL_OPACITY);

        setDefaultInterpolatorOnTransition(fadeTransition);

        setTransitionDelayInMiliseconds(fadeTransition, delayInMiliseconds);

        fadeTransition.play();
    }

    public static void fadeNodeOut(Node node) {

        FadeTransition fadeTransition = generateFadeTransitionInstance(node);

        configureTransitionOpacity(fadeTransition, FULL_OPACITY, NO_OPACITY);

        setDefaultInterpolatorOnTransition(fadeTransition);

        configureTransitionBehaviorWhenClosing(fadeTransition, node);

        fadeTransition.play();
    }

    public static void slideAndFadeNodeOut(Node node, Double distanceX) {

        FadeTransition fadeTransition = generateFadeTransitionInstance(node);
        TranslateTransition translateTransition = generateRelativeTranslateTransition(node, distanceX);

        configureTransitionOpacity(fadeTransition, FULL_OPACITY, NO_OPACITY);

        ParallelTransition parallelTransition = generateParallelTransition(fadeTransition, translateTransition);

        setDefaultInterpolatorOnTransition(parallelTransition);

        configureTransitionBehaviorWhenClosing(parallelTransition, node);

        parallelTransition.play();
    }

    public static void slideAndFadeNodeIn(Node node, Double distanceX) {

        FadeTransition fadeTransition = generateFadeTransitionInstance(node);
        TranslateTransition translateTransition = generateAbsoluteTranslateTransition(node, distanceX);

        configureTransitionOpacity(fadeTransition, NO_OPACITY, FULL_OPACITY);

        ParallelTransition parallelTransition = generateParallelTransition(fadeTransition, translateTransition);

        setDefaultInterpolatorOnTransition(parallelTransition);

        parallelTransition.play();
    }

    private static FadeTransition generateFadeTransitionInstance(Node node) {

        return new FadeTransition(Duration.millis(DEFAULT_FADE_DURATION_IN_MS), node);
    }

    private static TranslateTransition generateRelativeTranslateTransition(Node node, Double distanceOverXAxis) {

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(DEFAULT_FADE_DURATION_IN_MS), node);
        translateTransition.setByX(distanceOverXAxis);

        return translateTransition;
    }

    private static TranslateTransition generateAbsoluteTranslateTransition(Node node, Double distanceOverXAxis) {

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(DEFAULT_FADE_DURATION_IN_MS), node);

        translateTransition.setFromX(distanceOverXAxis);
        translateTransition.setToX(STARTING_POSITION_OVER_X_AXIS);

        return translateTransition;
    }

    private static void setDefaultInterpolatorOnTransition(Transition transition) {

        transition.setInterpolator(DEFAULT_INTERPOLATOR);
    }

    private static void configureTransitionOpacity(FadeTransition transition, double startingOpacityValue, double endingOpacityValue) {

        transition.setFromValue(startingOpacityValue);
        transition.setToValue(endingOpacityValue);
    }

    private static ParallelTransition generateParallelTransition(FadeTransition fadeTransition, TranslateTransition translateTransition) {

        return new ParallelTransition(fadeTransition, translateTransition);
    }

    private static void configureTransitionBehaviorWhenClosing(Transition transition, Node node) {

        transition.setOnFinished(_ -> {

                    Parent nodeParent = node.getParent();

                    ((Pane) nodeParent).getChildren().remove(node);
                }
        );
    }

    private static void setTransitionDelayInMiliseconds(Transition transition, Double delayInMiliseconds) {

        transition.setDelay(Duration.millis(delayInMiliseconds));
    }
}