package com.barbershop.mapper.implementation;

import com.barbershop.mapper.interfaces.AppointmentMapper;
import com.barbershop.dto.appointment.AppointmentCreationDTO;
import com.barbershop.dto.appointment.AppointmentInfoDTO;
import com.barbershop.dto.appointment.AppointmentUpdateDTO;
import com.barbershop.enums.AppointmentStatus;
import com.barbershop.exceptions.common.NullMapperInputException;
import com.barbershop.model.Appointment;
import com.barbershop.model.BarberService;
import com.barbershop.model.Client;
import com.barbershop.model.Employee;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppointmentMapperImpl implements AppointmentMapper {

    private final LocalDateTime TIMESTAMP = LocalDateTime.now();

    @Override
    public Appointment mapAppointmentCreationDtoToAppointmentEntity(
            AppointmentCreationDTO dto,
            Client client,
            Employee employee,
            BarberService service
    ) {

        if (dto == null || client == null || service == null || employee == null) throw new NullMapperInputException();

        AppointmentStatus defaultStatus = AppointmentStatus.PROGRAMADO;

        return Appointment.builder()
                .client(client)
                .barberservice(service)
                .employee(employee)
                .registrationTimestamp(TIMESTAMP)
                .startDateTime(dto.getStartDateTime().withSecond(0))
                .endDateTime(dto.getEndDateTime().withSecond(0))
                .modifiedDate(TIMESTAMP)
                .currentStatus(defaultStatus)
                .build();
    }

    @Override
    public Appointment mapAppointmentUpdateDtoToAppointmentEntity(
            AppointmentUpdateDTO updateDTO,
            Employee employee,
            BarberService service,
            Appointment appointmentOnDB
    ) {

        if (updateDTO == null || appointmentOnDB == null) throw new NullMapperInputException();

        setUpdatedDataOnEntity(updateDTO, employee, service, appointmentOnDB);

        return appointmentOnDB;
    }

    @Override
    public AppointmentInfoDTO mapAppointmentToInfoDto(Appointment appointment) {

        if (appointment == null) throw new NullMapperInputException();

        return AppointmentInfoDTO.builder()
                .id(appointment.getAppointmentID())
                .employeeID(appointment.getEmployee().getEmployeeID())
                .barberServiceID(appointment.getBarberservice().getBarbershopServiceID())
                .clientFirstName(appointment.getClient().getFirstName())
                .clientLastName(appointment.getClient().getLastName())
                .serviceName(appointment.getBarberservice().getName())
                .servicePrice(appointment.getBarberservice().getPrice())
                .employeeFirstName(appointment.getEmployee().getFirstName())
                .employeeLastName(appointment.getEmployee().getLastName())
                .registrationTimestamp(appointment.getRegistrationTimestamp())
                .startDateTime(appointment.getStartDateTime())
                .endDateTime(appointment.getEndDateTime())
                .currentStatus(appointment.getCurrentStatus())
                .optionalNotes(appointment.getOptionalNotes())
                .build();
    }

    @Override
    public List<AppointmentInfoDTO> mapAppointmentToInfoDto(List<Appointment> appointmentList) {

        if (appointmentList == null) throw new NullMapperInputException();

        return appointmentList.stream().map(this::mapAppointmentToInfoDto).collect(Collectors.toList());
    }

    @Override
    public AppointmentUpdateDTO mapAppointmentToUpdateDTO(Appointment appointment) {

        if (appointment == null) throw new NullMapperInputException();

        return AppointmentUpdateDTO.builder()
                .newEmployeeID(appointment.getEmployee().getEmployeeID())
                .newBarberserviceID(appointment.getBarberservice().getBarbershopServiceID())
                .newStartDateTime(appointment.getStartDateTime())
                .newEndDateTime(appointment.getEndDateTime())
                .newStatus(appointment.getCurrentStatus())
                .optionalNotes(appointment.getOptionalNotes())
                .build();
    }

    private void setUpdatedDataOnEntity(AppointmentUpdateDTO updateDTO, Employee employee, BarberService service, Appointment appointmentOnDB) {

        if (service != null) appointmentOnDB.setBarberservice(service);

        if (employee != null) appointmentOnDB.setEmployee(employee);

        if (updateDTO.getNewStartDateTime() != null)
            appointmentOnDB.setStartDateTime(updateDTO.getNewStartDateTime().withSecond(0));

        if (updateDTO.getNewEndDateTime() != null)
            appointmentOnDB.setEndDateTime(updateDTO.getNewEndDateTime().withSecond(0));

        if (updateDTO.getNewStatus() != null) appointmentOnDB.setCurrentStatus(updateDTO.getNewStatus());

        appointmentOnDB.setModifiedDate(TIMESTAMP);
    }
}
