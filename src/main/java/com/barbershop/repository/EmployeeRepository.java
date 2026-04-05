package com.barbershop.repository;

import com.barbershop.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT COUNT(E.employeeID) FROM Employee AS E WHERE E.hireDate <=:endDateTime AND (E.terminationDate >=:startDateTime OR E.terminationDate IS NULL)")
    Long countEmployeesInRange(@Param("startDateTime") LocalDate startDateTime, @Param("endDateTime") LocalDate endDateTime);

    List<Employee> findTop5ByOrderByHireDateDesc();

    List<Employee> findTop5ByTerminationDateIsNotNullOrderByTerminationDateDesc();
}
