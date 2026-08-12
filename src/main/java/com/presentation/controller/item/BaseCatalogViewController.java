package com.presentation.controller.item;

import javafx.beans.value.ObservableValue;
import javafx.scene.layout.VBox;

import java.util.List;

import static com.presentation.support.view.ContainerManager.cleanContainer;

public abstract class BaseCatalogViewController<T> {

    protected abstract List<T> searchCatalog();

    protected abstract VBox getItemListContainer();

    protected abstract void loadItemsOnView(List<T> items);

    protected void afterSearch(List<T> results) {
    }

    protected abstract void clearFilterNodes();

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
