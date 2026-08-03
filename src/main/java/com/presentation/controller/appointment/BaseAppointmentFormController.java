package com.presentation.controller.appointment;

import com.service.interfaces.AppointmentService;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

import static com.presentation.support.control.ComboBoxHelper.cleanComboBoxes;
import static com.presentation.support.control.UIBasicComponents.cleanDatePicker;

@RequiredArgsConstructor
public abstract class BaseAppointmentFormController {

    protected final AppointmentService appointmentService;
    protected final ApplicationContext applicationContext;

    protected final void resetForm() {

        resetReferenceObjects();
        cleanComboBoxes(getComboboxesToReset());
        cleanDatePicker(getDatePickerToReset());
        restoreNotes();
        toggleContainersVisibility();
        afterReset();
    }

    protected abstract void resetReferenceObjects();

    protected abstract ComboBox<?>[] getComboboxesToReset();

    protected abstract DatePicker getDatePickerToReset();

    protected abstract void restoreNotes();

    protected abstract void toggleContainersVisibility();

    protected void afterReset(){
    }
}
