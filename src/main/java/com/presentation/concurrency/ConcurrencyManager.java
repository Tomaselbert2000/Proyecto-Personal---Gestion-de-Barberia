package com.presentation.concurrency;

import javafx.concurrent.Task;
import org.jetbrains.annotations.UnknownNullability;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Gestor centralizado para la ejecución de tareas concurrencias en el contexto de JavaFX.
 *
 * <p>Encapsula la lógica para ejecutar acciones de fondo en hilos separados del hilo principal (UI),
 * permitiendo operaciones asíncronas sin bloquear la interfaz de usuario. Proporciona un punto único
 * de configuración para callbacks que se ejecutan cuando las tareas completan con éxito, manteniendo
 * la separación entre lógica de negocio y presentación.</p>
 *
 * <p>Este gestor utiliza {@link Task} de JavaFX para manejar la ejecución asíncrona y garantiza
 * que los resultados se procesen en el hilo correcto, evitando problemas de concurrencia comunes
 * en aplicaciones con interfaz gráfica.</p>
 */
public final class ConcurrencyManager {

    /**
     * Ejecuta una tarea de fondo en un hilo separado y notifica el resultado en el hilo principal.
     *
     * <p>Este metodo encapsula la lógica para:
     * <ul>
     *   <li>Crea una {@link Task} con la acción de fondo proporcionada</li>
     *   <li>Configura un callback que se ejecuta cuando la tarea completa con éxito</li>
     *   <li>Inicia la ejecución en un nuevo hilo separado del hilo UI</li>
     * </ul>
     *
     * <p>La acción de fondo debe ser una operación que no bloquee el hilo principal,
     * permitiendo que la interfaz de usuario siga siendo responsiva durante la ejecución.</p>
     *
     * @param backgroundAction acción que se ejecutará en el hilo de fondo. Debe retornar el valor
     *                         del resultado de la operación asíncrona.
     * @param action           callback que se ejecuta cuando la tarea completa con éxito, recibiendo
     *                         el valor devuelto por la acción de fondo. Se ejecuta en el hilo principal (UI).
     * @throws java.lang.IllegalStateException si la tarea falla durante la ejecución (call() lanza excepción)
     */
    public static <T> void executeAsyncTask(Supplier<T> backgroundAction, Consumer<T> action) {

        Task<T> task = new Task<>() {

            @Override
            protected T call() {

                return backgroundAction.get();
            }
        };

        task.setOnSucceeded(_ -> action.accept(task.getValue())
        );

        startNewThreadWithTask(task);
    }

    /**
     * Ejecuta una tarea de fondo sin retorno (void) en un hilo separado y notifica el resultado en el hilo principal.
     *
     * <p>Este metodo encapsula la lógica para:
     * <ul>
     *   <li>Crea una {@link Task} con la acción de fondo proporcionada (Runnable)</li>
     *   <li>Configura un callback que se ejecuta cuando la tarea completa con éxito</li>
     *   <li>Inicia la ejecución en un nuevo hilo separado del hilo UI</li>
     * </ul>
     *
     * <p>La acción de fondo debe ser una operación que no bloquee el hilo principal,
     * permitiendo que la interfaz de usuario siga siendo responsiva durante la ejecución.</p>
     *
     * @param backgroundAction acción que se ejecutará en el hilo de fondo. No retorna valor (void).
     * @param uiAction         callback que se ejecuta cuando la tarea completa con éxito, recibiendo
     *                         null como valor devuelto por la acción de fondo. Se ejecuta en el hilo principal (UI).
     */
    public static void executeVoidAsyncTask(Runnable backgroundAction, Consumer<Void> uiAction) {

        Task<Void> task = new Task<>() {

            @Override
            protected Void call() {

                backgroundAction.run();
                return null;
            }
        };

        task.setOnSucceeded(_ -> uiAction.accept(task.getValue())
        );

        startNewThreadWithTask(task);
    }

    /**
     * Inicia la ejecución de una tarea en un nuevo hilo separado.
     *
     * <p>Este metodo encapsula la creación y arranque de un {@link Thread} con la tarea proporcionada,
     * permitiendo que la tarea se ejecute asíncronamente sin bloquear el hilo principal.
     *
     * <p>La tarea debe ser una instancia de {@link Task} o una clase que implemente {@link Runnable},
     * ya que JavaFX utiliza {@link Task} para manejar callbacks y estados de ejecución.</p>
     *
     * @param task tarea a ejecutar en un hilo separado. Debe ser una instancia válida de {@link Task}.
     * @throws java.lang.IllegalArgumentException si la tarea proporcionada es null o no es una instancia válida de Task
     */
    public static <T> void startNewThreadWithTask(@UnknownNullability Task<T> task) {

        Thread thread = new Thread(task);

        thread.start();
    }
}