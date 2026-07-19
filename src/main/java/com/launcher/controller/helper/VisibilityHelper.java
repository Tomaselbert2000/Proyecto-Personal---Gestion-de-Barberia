package com.launcher.controller.helper;

import javafx.scene.Node;

public class VisibilityHelper {

    /**
     * Establece la visibilidad de los nodos especificados como visible.
     *
     * @param nodes Los nodos a establecer como visibles.
     */
    public static void setNodeAsVisible(Node... nodes) {
        for (Node node : nodes) {
            node.setVisible(true);
            node.setManaged(true);
        }
    }

    /**
     * Establece la visibilidad de los nodos especificados como no visible.
     *
     * @param nodes Los nodos a establecer como no visibles.
     */
    public static void setNodeAsNotVisible(Node... nodes) {
        for (Node node : nodes) {
            node.setVisible(false);
            node.setManaged(false);
        }
    }
}