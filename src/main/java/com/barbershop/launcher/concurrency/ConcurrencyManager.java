package com.barbershop.launcher.concurrency;

import javafx.concurrent.Task;
import org.jetbrains.annotations.UnknownNullability;

import java.util.function.Consumer;
import java.util.function.Supplier;

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
