package com.service.implementation;

import com.abstract_test_class.BaseServiceTest;
import com.dto.employee.EmployeeCreationDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.dto.employee.EmployeeUpdateDTO;
import com.exceptions.employee.*;
import com.mapper.implementation.EmployeeMapperImpl;
import com.mapper.interfaces.EmployeeMapper;
import com.model.Employee;
import com.repository.AppointmentRepository;
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
import static org.mockito.Mockito.*;

public class EmployeeServiceImplTest extends BaseServiceTest<Employee, EmployeeRepository> {

    private static final LocalDate HIRE_DATE = LocalDate.of(2026, 1, 1);
    private static final LocalDate INVALID_TERMINATION_DATE = LocalDate.of(2025, 1, 1);
    private static final LocalDate TERMINATION_DATE = LocalDate.of(2026, 12, 31);
    private static final LocalDate RETROACTIVE_TERMINATION_DATE = LocalDate.of(2026, 5, 5);
    private static final LocalDate FUTURE_TERMINATION_DATE = LocalDate.of(2031, 5, 5);
    private final Employee employeeOnDB = buildValidEmployee();
    private final EmployeeCreationDTO creationDTO = buildValidEmployeeCreationDTO();
    private final EmployeeUpdateDTO updateDTO = buildValidEmployeeUpdateDTO();

    @Spy
    private final EmployeeMapper mapper = new EmployeeMapperImpl();

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private EmployeeValidator validator;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

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
        when(employeeRepository.countMonthlyAppointmentsBatch(anyList(), any(), any())).thenReturn(List.of(employeeOnDB.getEmployeeID()));

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

    @Test
    @DisplayName("Dado un empleado en actividad y no desvinculado, podrá ser configurado como inactivo exitosamente")
    void givenCurrentlyWorkingEmployeeWhenSetAsInactiveDoesNotThrowsAnything() {

        employeeOnDB.setTerminationDate(null); // by default, factory employee has termination date, so we set it as null here before persist

        mockEmployee(employeeRepository, employeeOnDB);

        employeeService.changeEmployeeIsActiveValue(employeeOnDB.getEmployeeID());

        verify(employeeRepository, times(1)).save(employeeOnDB);

        assertFalse(employeeOnDB.isActive());
    }

    @Test
    @DisplayName("Dado un empleado inactivo y no desvinculado, podrá ser configurado nuevamente como activo")
    void givenCurrentlyWorkingEmployeeWhenSetAsActiveDoesNotThrowsAnything() {

        employeeOnDB.setTerminationDate(null); // by default, factory employee has termination date, so we set it as null here before persist
        employeeOnDB.setActive(false); // and we set the employee as inactive too

        mockEmployee(employeeRepository, employeeOnDB);

        employeeService.changeEmployeeIsActiveValue(employeeOnDB.getEmployeeID());

        verify(employeeRepository, times(1)).save(employeeOnDB);

        assertTrue(employeeOnDB.isActive());
    }

    @Test
    @DisplayName("Dado un empleado desvinculado, su estado será inactivo y no modificable")
    void givenEmployeeLaidOffThenStatusIsPermanentlyInactive() {

        employeeOnDB.setActive(true); // I force set the employee as active before saving

        mockEmployee(employeeRepository, employeeOnDB);

        employeeService.changeEmployeeIsActiveValue(employeeOnDB.getEmployeeID());

        assertFalse(employeeOnDB.isActive());
    }

    @Test
    @DisplayName("Dado un empleado que se envía como activo junto a una fecha de fin de relación laboral, la regla de negocio prevailsce y el empleado queda inactivo")
    void givenActiveFlagSentAlongsideTerminationDateWhenUpdatingThenEmployeeEndsUpInactive() {

        employeeOnDB.setActive(false);

        mockEmployee(employeeRepository, employeeOnDB);

        updateDTO.setIsActive(true);
        updateDTO.setTerminationDate(TERMINATION_DATE);

        updateEmployee(employeeService, employeeOnDB, updateDTO);

        assertAll(
                "El empleado desvinculado debe quedar inactivo",
                () -> assertFalse(employeeOnDB.isActive()),
                () -> assertEquals(TERMINATION_DATE, employeeOnDB.getTerminationDate())
        );
    }

    @Test
    @DisplayName("Dada una fecha de fin de relación laboral retroactiva pero posterior a la fecha de contratación, el empleado queda desvinculado e inactivo")
    void givenRetroactiveTerminationDateAfterHireDateWhenUpdatingThenEmployeeIsLaidOffAndInactive() {

        employeeOnDB.setActive(true);

        mockEmployee(employeeRepository, employeeOnDB);

        updateDTO.setIsActive(true);
        updateDTO.setTerminationDate(RETROACTIVE_TERMINATION_DATE);

        updateEmployee(employeeService, employeeOnDB, updateDTO);

        assertAll(
                "La baja retroactiva debe aceptarse y desactivar al empleado",
                () -> assertEquals(RETROACTIVE_TERMINATION_DATE, employeeOnDB.getTerminationDate()),
                () -> assertFalse(employeeOnDB.isActive())
        );
    }

    @Test
    @DisplayName("Dada una fecha de fin de relación laboral futura, el empleado queda desvinculado e inactivo")
    void givenFutureTerminationDateWhenUpdatingThenEmployeeIsLaidOffAndInactive() {

        employeeOnDB.setActive(true);

        mockEmployee(employeeRepository, employeeOnDB);

        updateDTO.setIsActive(true);
        updateDTO.setTerminationDate(FUTURE_TERMINATION_DATE);

        updateEmployee(employeeService, employeeOnDB, updateDTO);

        assertAll(
                "La baja futura debe aceptarse y desactivar al empleado",
                () -> assertEquals(FUTURE_TERMINATION_DATE, employeeOnDB.getTerminationDate()),
                () -> assertFalse(employeeOnDB.isActive())
        );
    }

    @Test
    @DisplayName("Dado un empleado desvinculado, al enviar el formulario de edición sin fecha de fin de relación laboral, el empleado vuelve a estar en relación laboral")
    void givenEmptyTerminationDateWhenUpdatingThenEmployeeIsNotLaidOffAnymore() {

        employeeOnDB.setActive(false);

        mockEmployee(employeeRepository, employeeOnDB);

        updateDTO.setIsActive(true);
        updateDTO.setTerminationDate(null);

        updateEmployee(employeeService, employeeOnDB, updateDTO);

        assertAll(
                "La fecha de cese debe quedar vaciada y el empleado vuelve a estar activo",
                () -> assertNull(employeeOnDB.getTerminationDate()),
                () -> assertTrue(employeeOnDB.isActive())
        );
    }

    @Test
    @DisplayName("Dado un empleado en relación laboral enviado como inactivo, su estado se persiste como inactivo")
    void givenInactiveFlagWithoutTerminationDateWhenUpdatingThenEmployeeEndsUpInactive() {

        employeeOnDB.setActive(true);

        mockEmployee(employeeRepository, employeeOnDB);

        updateDTO.setIsActive(false);
        updateDTO.setTerminationDate(null);

        updateEmployee(employeeService, employeeOnDB, updateDTO);

        assertAll(
                "El estado inactive enviado por el formulario debe respetarse",
                () -> assertNull(employeeOnDB.getTerminationDate()),
                () -> assertFalse(employeeOnDB.isActive())
        );
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