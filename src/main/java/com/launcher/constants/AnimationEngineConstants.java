package com.launcher.constants;

import javafx.animation.Interpolator;

/**
 * Clase que contiene constantes utilizadas para el manejo de animaciones en la aplicación BarberShop.
 * Define valores como duración, tipos de transición y otros parámetros necesarios para las animaciones.
 */

public final class AnimationEngineConstants {

    public static final double ANIMATION_DELAY_IN_MS = 100;
    public static final double STARTING_POSITION_OVER_X_AXIS = 0.0;
    public static final double NO_OPACITY = 0.0;
    public static final double FULL_OPACITY = 1.0;
    public static final int DEFAULT_FADE_DURATION_IN_MS = 400;
    public static final Interpolator DEFAULT_INTERPOLATOR = Interpolator.EASE_OUT;
    private AnimationEngineConstants() {
    }
}
