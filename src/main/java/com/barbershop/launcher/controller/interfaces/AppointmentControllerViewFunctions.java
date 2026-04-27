package com.barbershop.launcher.controller.interfaces;

import com.barbershop.dto.barbershopservice.BarberServiceInfoDTO;
import com.barbershop.dto.employee.EmployeeInfoDTO;

public interface AppointmentControllerViewFunctions {

    void configureButtonActions();

    void configureBarberServiceSelection();

    void configureEmployeeSelection();

    void configureTimeSelectors();

    void onBarberServiceSelected(BarberServiceInfoDTO barberServiceSelected);

    void onEmployeeSelected(EmployeeInfoDTO employeeSelected);

    void updateDateTimeSummary();

    void setReferenceObjectsAsNull();

    void resetForm();
}
