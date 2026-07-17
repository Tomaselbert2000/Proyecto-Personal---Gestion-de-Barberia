package com.launcher.concurrency;

import javafx.concurrent.Task;
import org.jetbrains.annotations.UnknownNullability;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Gestor de concurrencia encargado de manejar las operaciones concurrentes en la aplicación BarberShop.
 * Ofrece métodos para iniciar, detener y gestionar hilos y tareas asincrónicas.
 */

public final class ConcurrencyManager {

    public static <T> void executeUITask(Supplier<T> backgroundAction, Consumer<T> uiACtion) {

        Task<T> task = new Task<>() {

            @Override
            protected T call() {

                return backgroundAction.get();
            }
        };

        task.setOnSucceeded(_ -> uiACtion.accept(task.getValue())
        );

        startNewThreadWithTask(task);
    }

    public static <T> void startNewThreadWithTask(@UnknownNullability Task<T> task) {

        Thread thread = new Thread(task);

        thread.start();
    }
}
