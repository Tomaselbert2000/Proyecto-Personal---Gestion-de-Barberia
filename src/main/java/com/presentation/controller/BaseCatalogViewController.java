package com.presentation.controller;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

import static com.presentation.support.control.UIBasicComponents.setTextOnLabel;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;
import static com.presentation.support.view.ContainerManager.cleanContainer;

public abstract class BaseCatalogViewController<T> {

    protected static final String RESULTS_FOUND_SUFFIX = " encontrados";

    /**
     * Devuelve null cuando el valor de filtro coincide con la opción "TODOS" del enumerado,
     * para que el servicio interprete la búsqueda como "sin filtro" en ese campo.
     *
     * @param value El valor seleccionado en el filtro.
     * @param todos La constante "TODOS" del enumerado correspondiente.
     * @param <E>   El tipo del valor de filtro.
     * @return null si value es igual a todos, o value en caso contrario.
     */
    protected static <E> E nullIfTodos(E value, E todos) {

        return value == todos ? null : value;
    }

    protected abstract void configureButtonActions();

    protected abstract void loadGlobalStats();

    protected abstract Label getResultsCountLabel();

    protected abstract List<T> searchCatalog();

    protected abstract VBox getItemListContainer();

    protected abstract void loadItemsOnView(List<T> items);

    protected abstract void clearFilterNodes();

    protected abstract void initializeListContent();

    protected void afterSearch(List<T> results) {

        setTextOnLabel(getResultsCountLabel(), parseNumberValueToText(results.size()) + RESULTS_FOUND_SUFFIX);
    }

    protected final void resetSearchFilter() {

        clearFilterNodes();
        executeLiveSearch();
    }

    protected final void executeLiveSearch() {

        List<T> results = searchCatalog();

        cleanContainer(getItemListContainer());

        loadItemsOnView(results);

        afterSearch(results);
    }

    protected final void attachLiveSearchListeners(ObservableValue<?>... properties) {

        for (ObservableValue<?> property : properties) {

            property.addListener((_, _, _) -> executeLiveSearch());
        }
    }
}