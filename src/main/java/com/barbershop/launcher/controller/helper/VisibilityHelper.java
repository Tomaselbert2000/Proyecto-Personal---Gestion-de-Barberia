package com.barbershop.launcher.controller.helper;

import javafx.scene.Node;

public class VisibilityHelper {

    public static void setNodeAsVisible(Node... nodes) {

        for (Node node : nodes) {

            node.setVisible(true);
            node.setManaged(true);
        }
    }

    public static void setNodeAsNotVisible(Node... nodes) {

        for (Node node : nodes) {

            node.setVisible(false);
            node.setManaged(false);
        }
    }
}
