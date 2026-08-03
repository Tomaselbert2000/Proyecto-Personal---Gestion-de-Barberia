package com.presentation.support.control;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.presentation.constants.ControllerConstants.AppointmentControllerConstants.DATETIME_SUMMARY_FORMAT;
import static com.presentation.support.control.UIBasicComponents.setTextOnLabel;

public class AppointmentDateTimeSummaryHelper {

    public static void updateDatetimeSummary(
            Label summaryLabel,
            DatePicker datePicker,
            ComboBox<LocalTime> hourSelector,
            ComboBox<LocalTime> minuteSelector) {

        LocalDate date = datePicker.getValue();
        LocalTime hour = hourSelector.getValue();
        LocalTime minute = minuteSelector.getValue();

        if (date != null && hour != null && minute != null) {

            String dateTimeSummary = String.format(DATETIME_SUMMARY_FORMAT, date, hour.getHour(), minute.getMinute());
            setTextOnLabel(summaryLabel, dateTimeSummary);
        }
    }
}
