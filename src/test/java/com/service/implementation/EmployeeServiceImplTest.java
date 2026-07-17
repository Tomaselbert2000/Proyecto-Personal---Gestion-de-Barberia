package com.service.implementation;

import com.abstract_test_class.BaseServiceTest;
import com.dto.employee.EmployeeCreationDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.dto.employee.EmployeeUpdateDTO;
import com.exceptions.employee.*;
import com.mapper.implementation.EmployeeMapperImpl;
import com.mapper.interfaces.EmployeeMapper;
import com.model.Employee;
import com.repository.EmployeeRepository;
import com.validation.employee.EmployeeValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.time.LocalDate;
import java.util.List;

import static com.factory.EmployeeTestDataFactory.*;
import static com.service.helper.EmployeeServiceTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeServiceImplTest extends BaseServiceTest<Employee, EmployeeRepository> {

    private static final LocalDate HIRE_DATE = LocalDate.of(2026, 1, 1);
    private static final LocalDate INVALID_TERMINATION_DATE = LocalDate.of(2025, 1, 1);

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeValidator validator;

    @Spy
    private EmployeeMapper mapper = new EmployeeMapperImpl();

    @InjectMocks
    private EmployeeServiceImpl employeeService;
    private final Employee employeeOnDB = buildValidEmployee();
    private final EmployeeCreationDTO creationDTO = buildValidEmployeeCreationDTO();
    private final EmployeeUpdateDTO updateDTO = buildValidEmployeeUpdateDTO();

    @Override
    protected EmployeeRepository getPrimaryRepository() {

        return employeeRepository;
    }

    @Test
    @DisplayName("Dado un DTO de creación de empleado con datos válidos, deberá ser persistido correctamente.")
    void givenNewEmployeeWithValidData_WhenCreating_ThenIsPersisted() {

        registerNewEmployee(employeeService, creationDTO);

        verifyCreationProcessSuccess();
    }

    @Test
    @DisplayName("Dado un DTO de creación de empleado con cualquiera de sus datos en NULL, arrojará NullEmployeeInputDataException")
    void givenAnyNullValue_WhenCreating_ThenThrows_NullEmployeeInputDataException() {

        mockValidatorToThrowException(validator, new NullEmployeeInputDataException(), creationDTO);

        assertThrows(NullEmployeeInputDataException.class, () -> registerNewEmployee(employeeService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un nombre y/o apellido compuestos de un String inválido, el registro fallará y el empleado no deberá ser persistido")
    void givenInvalidFirstOrLastName_WhenCreating_ThenRegisterShouldFail() {

        mockValidatorToThrowException(validator, new InvalidEmployeeNameException(), creationDTO);

        assertThrows(InvalidEmployeeNameException.class, () -> registerNewEmployee(employeeService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un DTO de creación con un valor de comisión inválido, el registro deberá fallar y el empleado no será persistido")
    void givenInvalid_CommissionPercentageValue_WhenCreating_ThenEmployeeIsNotPersisted() {

        mockValidatorToThrowException(validator, new InvalidCommissionPercentageException(), creationDTO);

        assertThrows(InvalidCommissionPercentageException.class, () -> registerNewEmployee(employeeService, creationDTO));

        verifyCreationProcessFailure();
    }

    @Test
    @DisplayName("Dado un empleado previamente registrado de manera exitosa, deberá retornar su información mediante mapeo por DTO informativo")
    void givenExistingEmployee_WhenSearching_ThenReturnsItsInformationAsDTO() {

        mockEmployee(employeeRepository, employeeOnDB);

        EmployeeInfoDTO returnedInfoDTO = employeeService.getEmployeeInfo(employeeOnDB.getEmployeeID());

        verifyInfoDTOAssertions(returnedInfoDTO);
    }

    @Test
    @DisplayName("Dado un ID de empleado no registrado en el sistema previamente, al ejecutar la búsqueda arrojará EmployeeNotFoundException")
    void givenNonExistingEmployee_WhenSearching_ThenThrows_EmployeeNotFoundException() {

        mockNonExistingEmployee(employeeRepository, employeeOnDB);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeInfo(employeeOnDB.getEmployeeID()));
    }

    @Test
    @DisplayName("Dado un ID de empleado registrado previamente en el sistema, deberá poder ser eliminado exitosamente")
    void givenExistingEmployee_WhenDeleting_ThenIsSuccessfullyErased() {

        mockEmployee(employeeRepository, employeeOnDB);

        deleteEmployee(employeeService, employeeOnDB);

        verifyThatEntityWasDeleted(employeeOnDB);
    }

    @Test
    @DisplayName("Dado un ID de empleado no registrado previamente en el sistema, al intentar eliminarlo arrojará EmployeeNotFoundException")
    void givenNonExistingEmployee_WhenDeleting_ThenThrows_EmployeeNotFoundException() {

        mockNonExistingEmployee(employeeRepository, employeeOnDB);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee(employeeOnDB.getEmployeeID()));

        verifyThatEntityWasNotDeleted(employeeOnDB);
    }

    @Test
    @DisplayName("Dados N empleados registrados previamente en el sistema, retornarán su información dentro de una lista mediante mapeo por DTO informativo")
    void given_N_ExistingEmployees_WhenGettingAll_ThenReturns_InfoDTOList() {

        mockEmployeeList(employeeRepository, List.of(employeeOnDB));

        List<EmployeeInfoDTO> returnedList = employeeService.getEmployeeList();

        verifyInfoDTOListAssertions(returnedList);
    }

    @Test
    @DisplayName("Dado un DTO de actualización con información válida, deberán persistirse los cambios de manera exitosa")
    void givenExistingEmployee_WhenUpdating_ThenIsSuccessfullyPersisted() {

        mockEmployee(employeeRepository, employeeOnDB);

        updateEmployee(employeeService, employeeOnDB, updateDTO);

        verifyUpdateProcessSuccess();
    }

    @Test
    @DisplayName("Dado un empleado ya registrado en el sistema, al intentar actualizar su nombre y/o apellido con valores String inválidos, no se persistirán los cambios")
    void givenExistingEmployee_WhenUpdatingWithInvalidFirstOrLastName_ThenIsNotPersisted() {

        mockEmployee(employeeRepository, employeeOnDB);

        mockValidatorToThrowException(validator, new InvalidEmployeeNameException(), updateDTO);

        assertThrows(InvalidEmployeeNameException.class, () -> updateEmployee(employeeService, employeeOnDB, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dado un empleado ya registrado en el sistema, al intentar actualizar su porcentaje de comisión con un valor inválido, no se persistirán los cambios")
    void givenInvalidCommissionPercentage_WhenUpdating_ThenEmployeeIsNotPersisted() {

        mockEmployee(employeeRepository, employeeOnDB);

        mockValidatorToThrowException(validator, new InvalidCommissionPercentageException(), updateDTO);

        assertThrows(InvalidCommissionPercentageException.class, () -> updateEmployee(employeeService, employeeOnDB, updateDTO));

        verifyUpdateProcessFailure();
    }

    @Test
    @DisplayName("Dada una fecha de fin de relación laboral anterior a la fecha de contratación del empleado, no se persistirán los cambios")
    void givenTerminationDateBeforeHireDate_WhenUpdating_ThenEmployeeIsNotPersisted() {

        mockEmployee(employeeRepository, employeeOnDB);

        updateDTO.setTerminationDate(INVALID_TERMINATION_DATE);

        assertThrows(InvalidEmployeeTerminationDateException.class, () -> updateEmployee(employeeService, employeeOnDB, updateDTO));

        verifyUpdateProcessFailure();
    }

    private void verifyCreationProcessSuccess() {
        verifyValidatorCreationInteraction(validator, creationDTO);
        verifyMapperCreationInteraction(mapper, creationDTO);
        verifyThatEntityWasSaved();
    }

    private void verifyCreationProcessFailure() {

        verifyValidatorCreationInteraction(validator, creationDTO);
        verifyMapperCreationNoInteractions(mapper, creationDTO);
        verifyThatEntityWasNotSaved();
    }

    private void verifyUpdateProcessSuccess() {
        verifyValidatorUpdateInteraction(validator, updateDTO);
        verifyMapperUpdateInteraction(mapper, employeeOnDB, updateDTO);
        verifyThatEntityWasSaved();
    }

    private void verifyUpdateProcessFailure() {

        verifyValidatorUpdateInteraction(validator, updateDTO);
        verifyMapperUpdateNoInteractions(mapper, employeeOnDB, updateDTO);
        verifyThatEntityWasNotSaved();
    }

    private void verifyInfoDTOAssertions(EmployeeInfoDTO returnedInfoDTO) {
        assertAll(
                "Verificacion de campos",
                () -> assertEquals(returnedInfoDTO.getFirstName(), employeeOnDB.getFirstName()),
                () -> assertEquals(returnedInfoDTO.getLastName(), employeeOnDB.getLastName()),
                () -> assertEquals(returnedInfoDTO.getHireDateAsString(), HIRE_DATE.toString())
        );
    }

    private void verifyInfoDTOListAssertions(List<EmployeeInfoDTO> returnedList) {
        assertNotNull(returnedList);

        assertAll(
                "Verificación de campos",
                () -> assertEquals(1, returnedList.size()),
                () -> assertEquals(returnedList.getFirst().getFirstName(), employeeOnDB.getFirstName()),
                () -> assertEquals(returnedList.getFirst().getLastName(), employeeOnDB.getLastName()),
                () -> assertEquals(returnedList.getFirst().getHireDateAsString(), employeeOnDB.getHireDate().toString())
        );
    }
}