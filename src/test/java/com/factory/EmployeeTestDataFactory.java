package com.factory;

import com.dto.employee.EmployeeCreationDTO;
import com.dto.employee.EmployeeUpdateDTO;
import com.model.Employee;

import static com.test_constant.AppointmentTestConstants.UpdateValidData.NEW_EMPLOYEE_ID;
import static com.test_constant.EmployeeTestConstants.CreationValidData.*;
import static com.test_constant.EmployeeTestConstants.MapperData.EMPLOYEE_ID;
import static com.test_constant.EmployeeTestConstants.MapperData.EMPLOYEE_IS_ACTIVE_VALUE;
import static com.test_constant.EmployeeTestConstants.UpdateValidData.*;

public class EmployeeTestDataFactory {

    private EmployeeTestDataFactory() {
    }

    public static EmployeeCreationDTO buildValidEmployeeCreationDTO() {

        return EmployeeCreationDTO.builder()
                .firstName(EMPLOYEE_FIRST_NAME)
                .lastName(EMPLOYEE_LAST_NAME)
                .hireDate(EMPLOYEE_HIRE_DATE)
                .commissionPercentage(EMPLOYEE_COMMISSION_PERCENTAGE)
                .build();
    }

    public static EmployeeUpdateDTO buildValidEmployeeUpdateDTO() {

        return EmployeeUpdateDTO.builder()
                .firstName(EMPLOYEE_NEW_NAME)
                .lastName(EMPLOYEE_NEW_SURNAME)
                .isActive(EMPLOYEE_NEW_IS_ACTIVE)
                .terminationDate(EMPLOYEE_TERMINATION_DATE)
                .commissionPercentage(EMPLOYEE_NEW_COMMISSION_PERCENTAGE)
                .build();
    }

    public static Employee buildValidEmployee() {

        return Employee.builder().
                employeeID(EMPLOYEE_ID)
                .firstName(EMPLOYEE_FIRST_NAME)
                .lastName(EMPLOYEE_LAST_NAME)
                .hireDate(EMPLOYEE_HIRE_DATE)
                .terminationDate(EMPLOYEE_TERMINATION_DATE)
                .isActive(EMPLOYEE_IS_ACTIVE_VALUE)
                .commissionPercentage(EMPLOYEE_COMMISSION_PERCENTAGE)
                .build();
    }

    public static Employee buildValidEmployeeForUpdateOperation(){

        return Employee.builder().
                employeeID(NEW_EMPLOYEE_ID)
                .firstName(EMPLOYEE_FIRST_NAME)
                .lastName(EMPLOYEE_LAST_NAME)
                .hireDate(EMPLOYEE_HIRE_DATE)
                .terminationDate(EMPLOYEE_TERMINATION_DATE)
                .isActive(EMPLOYEE_IS_ACTIVE_VALUE)
                .commissionPercentage(EMPLOYEE_COMMISSION_PERCENTAGE)
                .build();
    }
}
