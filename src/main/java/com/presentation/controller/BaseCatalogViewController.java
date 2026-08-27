package com.presentation.controller;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

import static com.presentation.support.control.UIBasicComponents.setTextOnLabel;
import static com.presentation.support.control.ValidationFormatter.parseNumberValueToText;
import static com.presentation.support.view.ContainerManager.cleanContainer;

public abstract class BaseCatalogViewController<T> {

    @FXML
    public final void initialize(){

        loadGlobalStats();
        initializeListContent();
        attachLiveSearchListeners(getSearchProperties());
        configureButtonActions();
    }

    protected static final String RESULTS_FOUND_SUFFIX = " encontrados";

    protected static <E> E nullIfTodos(E value, E todos) {

        return java.util.Objects.equals(value, todos) ? null : value;
    }

    protected abstract ObservableValue<?>[] getSearchProperties();

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