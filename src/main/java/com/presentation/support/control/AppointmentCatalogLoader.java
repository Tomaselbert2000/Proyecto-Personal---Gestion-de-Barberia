package com.presentation.support.control;

import com.dto.barberservice.BarberServiceInfoDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.service.interfaces.AppointmentService;
import javafx.scene.control.ComboBox;

import java.util.List;

import static com.presentation.support.control.ComboBoxHelper.loadGenericTypeListOnComboBox;

public class AppointmentCatalogLoader {

    public static void loadCatalog(
            ComboBox<BarberServiceInfoDTO> serviceCB,
            ComboBox<EmployeeInfoDTO> employeeCB,
            AppointmentService appointmentService
    ) {

        List<BarberServiceInfoDTO> catalog = appointmentService.getBarberServicesFromServiceInstance();
        List<EmployeeInfoDTO> employees = appointmentService.getEmployeesFromServiceInstance();
        loadGenericTypeListOnComboBox(serviceCB, catalog);
        loadGenericTypeListOnComboBox(employeeCB, employees);
    }
}
