package com.barbershop.launcher.controller.employee;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class EmployeeController {

    private static final String EMPLOYEE_ITEM_VIEW_PATH = "/view/EmployeeCardItem.fxml";

    private final ApplicationContext applicationContext;

    @FXML
    private VBox employeeListVBox;

    @FXML
    private Label pageTitle;

    @FXML
    private Label pageSubtitle;

    public EmployeeController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @FXML
    public void initialize() {
    }

    public void updateTitle(String title, String subtitle) {
        if (pageTitle != null) {
            pageTitle.setText(title);
        }
        if (pageSubtitle != null) {
            pageSubtitle.setText(subtitle);
        }
    }
}
