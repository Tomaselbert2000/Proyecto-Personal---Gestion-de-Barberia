package com.validation.employee;

import com.exceptions.sale.InactiveEmployeeException;
import com.model.Employee;

public final class EmployeeServiceValidationHelper {

    public static void validateEmployeeIsActive(Employee employee) {

        if (employee != null) {

            if (!employee.isActive()) throw new InactiveEmployeeException();
        }
    }
}
