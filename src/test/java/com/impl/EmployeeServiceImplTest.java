package com.impl;

import com.barbershop.dto.employee.EmployeeCreationDTO;
import com.barbershop.dto.employee.EmployeeInfoDTO;
import com.barbershop.dto.employee.EmployeeUpdateDTO;
import com.barbershop.exceptions.employee.*;
import com.barbershop.mapper.implementation.EmployeeMapperImpl;
import com.barbershop.mapper.interfaces.EmployeeMapper;
import com.barbershop.model.Employee;
import com.barbershop.repository.EmployeeRepository;
import com.barbershop.service.implementation.EmployeeServiceImpl;
import com.barbershop.validation.employee.EmployeeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeValidator validator;

    @Spy
    private EmployeeMapper mapper = new EmployeeMapperImpl();

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employeeOnDB;
    private EmployeeCreationDTO creationDTO;
    private EmployeeUpdateDTO updateDTO;
    private EmployeeInfoDTO infoDTO;

    private static final Long EMPLOYEE_ID = 1L;
    private static final Long NON_EXISTING_EMPLOYEE_ID = 999L;
    private static final LocalDate HIRE_DATE = LocalDate.of(2026, 1, 1);
    private static final LocalDate TERMINATION_DATE = LocalDate.of(2026, 1, 31);
    private static final LocalDate INVALID_TERMINATION_DATE = LocalDate.of(2025, 1, 1);
    private static final Double COMMISSION_PERCENTAGE = 0.70;

    @BeforeEach
    void init() {

        setupEmployee();
        setupCreationDTO();
        setupUpdateDTO();
        setupInfoDTO();
    }

    @Test
    @DisplayName("Dado un DTO de creación de empleado con datos válidos, deberá ser persistido correctamente.")
    void givenNewEmployeeWithValidData_WhenCreating_ThenIsPersisted() {

        registerNewEmployee();

        verifyValidatorCreationInteraction();
        verifyMapperCreationInteraction();
        verifyThatEmployeeWasRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación de empleado con cualquiera de sus datos en NULL, arrojará NullEmployeeInputDataException")
    void givenAnyNullValue_WhenCreating_ThenThrows_NullEmployeeInputDataException() {

        doThrow(NullEmployeeInputDataException.class).when(validator).validateDTO(creationDTO);

        assertThrows(NullEmployeeInputDataException.class, this::registerNewEmployee);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatEmployeeWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre y/o apellido compuestos de un String inválido, el registro fallará y el empleado no deberá ser persistido")
    void givenInvalidFirstOrLastName_WhenCreating_ThenRegisterShouldFail() {

        doThrow(InvalidEmployeeNameException.class).when(validator).validateDTO(creationDTO);

        assertThrows(InvalidEmployeeNameException.class, this::registerNewEmployee);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatEmployeeWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de comisión inválido, el registro deberá fallar y el empleado no será persistido")
    void givenInvalid_CommissionPercentageValue_WhenCreating_ThenEmployeeIsNotPersisted() {

        doThrow(InvalidCommissionPercentageException.class).when(validator).validateDTO(creationDTO);

        assertThrows(InvalidCommissionPercentageException.class, this::registerNewEmployee);

        verifyValidatorCreationInteraction();
        verifyMapperCreationNoInteractions();
        verifyThatEmployeeWasNotRegistered();
    }

    @Test
    @DisplayName("Dado un empleado previamente registrado de manera exitosa, deberá retornar su información mediante mapeo por DTO informativo")
    void givenExistingEmployee_WhenSearching_ThenReturnsItsInformationAsDTO() {

        mockEmployee();
        mockEmployeeInfoDTO();

        EmployeeInfoDTO returnedInfoDTO = employeeService.getEmployeeInfo(EMPLOYEE_ID);

        assertAll(
                "Verificacion de campos",
                () -> assertEquals(returnedInfoDTO.getFirstName(), employeeOnDB.getFirstName()),
                () -> assertEquals(returnedInfoDTO.getLastName(), employeeOnDB.getLastName()),
                () -> assertEquals(returnedInfoDTO.getHireDateAsString(), HIRE_DATE.toString())
        );
    }

    @Test
    @DisplayName("Dado un ID de empleado no registrado en el sistema previamente, al ejecutar la búsqueda arrojará EmployeeNotFoundException")
    void givenNonExistingEmployee_WhenSearching_ThenThrows_EmployeeNotFoundException() {

        mockNonExistingEmployee();

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeInfo(NON_EXISTING_EMPLOYEE_ID));
    }

    @Test
    @DisplayName("Dado un ID de empleado registrado previamente en el sistema, deberá poder ser eliminado exitosamente")
    void givenExistingEmployee_WhenDeleting_ThenIsSuccessfullyErased() {

        mockEmployee();

        deleteEmployee();

        verifyThatEmployeeWasDeleted();
    }

    @Test
    @DisplayName("Dado un ID de empleado no registrado previamente en el sistema, al intentar eliminarlo arrojará EmployeeNotFoundException")
    void givenNonExistingEmployee_WhenDeleting_ThenThrows_EmployeeNotFoundException() {

        mockNonExistingEmployee();

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee(NON_EXISTING_EMPLOYEE_ID));

        verifyThatEmployeeWasNotDeleted();
    }

    @Test
    @DisplayName("Dados N empleados registrados previamente en el sistema, retornarán su información dentro de una lista mediante mapeo por DTO informativo")
    void given_N_ExistingEmployees_WhenGettingAll_ThenReturns_InfoDTOList() {

        mockEmployeeList();

        List<EmployeeInfoDTO> returnedList = employeeService.getEmployeeList();

        assertAll(
                "Verificación de campos",
                () -> assertNotNull(returnedList),
                () -> assertEquals(1, returnedList.size()),
                () -> assertEquals(returnedList.getFirst().getFirstName(), employeeOnDB.getFirstName()),
                () -> assertEquals(returnedList.getFirst().getLastName(), employeeOnDB.getLastName()),
                () -> assertEquals(returnedList.getFirst().getHireDateAsString(), employeeOnDB.getHireDate().toString())
        );
    }

    @Test
    @DisplayName("Dado un DTO de actualización con información válida, deberán persistirse los cambios de manera exitosa")
    void givenExistingEmployee_WhenUpdating_ThenIsSuccessfullyPersisted() {

        mockEmployee();

        updateEmployee();

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateInteraction();
        verifyThatEmployeeWasRegistered();
    }

    @Test
    @DisplayName("Dado un empleado ya registrado en el sistema, al intentar actualizar su nombre y/o apellido con valores String inválidos, no se persistirán los cambios")
    void givenExistingEmployee_WhenUpdatingWithInvalidFirstOrLastName_ThenIsNotPersisted() {

        mockEmployee();

        doThrow(InvalidEmployeeNameException.class).when(validator).validateDTO(updateDTO);

        assertThrows(InvalidEmployeeNameException.class, this::updateEmployee);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatEmployeeWasNotUpdated();
    }

    @Test
    @DisplayName("Dado un empleado ya registrado en el sistema, al intentar actualizar su porcentaje de comisión con un valor inválido, no se persistirán los cambios")
    void givenInvalidCommissionPercentage_WhenUpdating_ThenEmployeeIsNotPersisted() {

        mockEmployee();

        doThrow(InvalidCommissionPercentageException.class).when(validator).validateDTO(updateDTO);

        assertThrows(InvalidCommissionPercentageException.class, this::updateEmployee);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatEmployeeWasNotUpdated();
    }

    @Test
    @DisplayName("Dada una fecha de fin de relación laboral anterior a la fecha de contratación del empleado, no se persistirán los cambios")
    void givenTerminationDateBeforeHireDate_WhenUpdating_ThenEmployeeIsNotPersisted() {

        mockEmployee();

        updateDTO.setTerminationDate(INVALID_TERMINATION_DATE);

        assertThrows(InvalidEmployeeTerminationDateException.class, this::updateEmployee);

        verifyValidatorUpdateInteraction();
        verifyMapperUpdateNoInteractions();
        verifyThatEmployeeWasNotUpdated();
    }

    private void setupEmployee() {

        employeeOnDB = Employee.builder()
                .employeeID(EMPLOYEE_ID)
                .firstName("Ramiro")
                .lastName("Ardiles")
                .hireDate(HIRE_DATE)
                .isActive(true)
                .commissionPercentage(COMMISSION_PERCENTAGE)
                .build();
    }

    private void setupCreationDTO() {

        creationDTO = EmployeeCreationDTO.builder()
                .firstName("Ramiro")
                .lastName("Ardiles")
                .hireDate(HIRE_DATE)
                .commissionPercentage(COMMISSION_PERCENTAGE)
                .build();
    }

    private void setupUpdateDTO() {

        updateDTO = EmployeeUpdateDTO.builder()
                .firstName("Tomas Gabriel")
                .lastName("Elbert")
                .terminationDate(TERMINATION_DATE)
                .isActive(true)
                .commissionPercentage(0.65)
                .build();
    }

    private void setupInfoDTO() {

        infoDTO = EmployeeInfoDTO.builder()
                .firstName(employeeOnDB.getFirstName())
                .lastName(employeeOnDB.getLastName())
                .hireDateAsString(employeeOnDB.getHireDate().toString())
                .build();
    }

    private void registerNewEmployee() {

        employeeService.registerNewEmployee(creationDTO);
    }

    private void deleteEmployee() {

        employeeService.deleteEmployee(EMPLOYEE_ID);
    }

    private void updateEmployee() {

        employeeService.updateEmployee(EMPLOYEE_ID, updateDTO);
    }

    private void verifyValidatorCreationInteraction() {

        verify(validator).validateDTO(creationDTO);
    }

    private void verifyValidatorUpdateInteraction() {

        verify(validator).validateDTO(updateDTO);
    }

    private void verifyMapperCreationInteraction() {

        verify(mapper).mapEmployeeCreationDtoToEntity(creationDTO);
    }

    private void verifyMapperUpdateInteraction() {

        verify(mapper).mapEmployeeUpdateDtoToEntity(employeeOnDB, updateDTO);
    }

    private void verifyMapperCreationNoInteractions() {

        verify(mapper, never()).mapEmployeeCreationDtoToEntity(creationDTO);
    }

    private void verifyMapperUpdateNoInteractions() {

        verify(mapper, never()).mapEmployeeUpdateDtoToEntity(employeeOnDB, updateDTO);
    }

    private void verifyThatEmployeeWasRegistered() {

        verify(employeeRepository, times(1)).save(any());
    }

    private void verifyThatEmployeeWasNotRegistered() {

        verify(employeeRepository, never()).save(any());
    }

    private void verifyThatEmployeeWasDeleted() {

        verify(employeeRepository, times(1)).delete(employeeOnDB);
    }

    private void verifyThatEmployeeWasNotDeleted() {

        verify(employeeRepository, never()).delete(any());
    }

    private void verifyThatEmployeeWasNotUpdated() {

        verifyThatEmployeeWasNotRegistered();
    }

    private void mockEmployee() {

        when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employeeOnDB));
    }

    private void mockNonExistingEmployee() {

        when(employeeRepository.findById(NON_EXISTING_EMPLOYEE_ID)).thenReturn(Optional.empty());
    }

    private void mockEmployeeInfoDTO() {

        when(mapper.mapEmployeeToInfoDTO(employeeOnDB)).thenReturn(infoDTO);
    }

    private void mockEmployeeList() {

        List<Employee> employeeList = List.of(employeeOnDB);

        when(employeeRepository.findAll()).thenReturn(employeeList);
    }
}
