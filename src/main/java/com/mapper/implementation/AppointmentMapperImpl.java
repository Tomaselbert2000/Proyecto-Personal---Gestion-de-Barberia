package com.mapper.implementation;

import com.dto.appointment.AppointmentCreationDTO;
import com.dto.appointment.AppointmentInfoDTO;
import com.dto.appointment.AppointmentUpdateDTO;
import com.enums.AppointmentStatus;
import com.mapper.interfaces.AppointmentMapper;
import com.model.Appointment;
import com.model.BarberService;
import com.model.Client;
import com.model.Employee;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mapper.helper.MapperHelper.checkIfMapperInputIsNull;

@Component
public class AppointmentMapperImpl implements AppointmentMapper {

    @Override
    public Appointment mapAppointmentCreationDtoToAppointmentEntity(
            AppointmentCreationDTO dto,
            Client client,
            Employee employee,
            BarberService service
    ) {

        checkIfMapperInputIsNull(dto, client, service, employee);

        AppointmentStatus defaultStatus = AppointmentStatus.PROGRAMADO;

        return Appointment.builder()
                .client(client)
                .barberservice(service)
                .employee(employee)
                .registrationTimestamp(LocalDateTime.now())
                .startDateTime(dto.getStartDateTime().withSecond(0))
                .endDateTime(dto.getEndDateTime().withSecond(0))
                .modifiedDate(LocalDateTime.now())
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

        checkIfMapperInputIsNull(updateDTO, appointmentOnDB);

        setUpdatedDataOnEntity(updateDTO, employee, service, appointmentOnDB);

        return appointmentOnDB;
    }

    @Override
    public AppointmentInfoDTO mapAppointmentToInfoDto(Appointment appointment) {

        checkIfMapperInputIsNull(appointment);

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

        checkIfMapperInputIsNull(appointmentList);

        return appointmentList.stream().map(this::mapAppointmentToInfoDto).collect(Collectors.toList());
    }

    private void setUpdatedDataOnEntity(
            AppointmentUpdateDTO updateDTO,
            Employee employee,
            BarberService service,
            Appointment appointmentOnDB
    ) {

        checkIfMapperInputIsNull(updateDTO, employee, service, appointmentOnDB);

        if (service != null) appointmentOnDB.setBarberservice(service);

        if (employee != null) appointmentOnDB.setEmployee(employee);

        if (updateDTO.getNewStartDateTime() != null)
            appointmentOnDB.setStartDateTime(updateDTO.getNewStartDateTime().withSecond(0));

        if (updateDTO.getNewEndDateTime() != null)
            appointmentOnDB.setEndDateTime(updateDTO.getNewEndDateTime().withSecond(0));

        if (updateDTO.getNewStatus() != null) appointmentOnDB.setCurrentStatus(updateDTO.getNewStatus());

        appointmentOnDB.setModifiedDate(LocalDateTime.now());
    }
}
