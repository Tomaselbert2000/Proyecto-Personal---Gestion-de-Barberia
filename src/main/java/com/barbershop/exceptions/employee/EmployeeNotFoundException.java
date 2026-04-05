package com.barbershop.exceptions.employee;

public class EmployeeNotFoundException extends RuntimeException{

    @Override
    public String getMessage(){

        return "No se encontraron coincidencias de empleados para el ID proporcionado.";
    }
}
