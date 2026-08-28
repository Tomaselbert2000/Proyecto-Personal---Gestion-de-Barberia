package com.mapper.implementation;

import com.dto.appointment.AppointmentCreationDTO;
import com.dto.appointment.AppointmentInfoDTO;
import com.dto.appointment.AppointmentUpdateDTO;
import com.enums.AppointmentStatus;
import com.mapper.helper.MapperHelper;
import com.mapper.interfaces.AppointmentMapper;
import com.model.Appointment;
import com.model.BarberService;
import com.model.Client;
import com.model.Employee;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.mapper.helper.MapperHelper.checkIfMapperInputIsNull;

@Component
public class AppointmentMapperImpl implements AppointmentMapper {

    @Override
    public Appointment mapAppointmentCreationDtoToEntity(
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
                .startDateTime(MapperHelper.truncateToMinute(dto.getStartDateTime()))
                .endDateTime(MapperHelper.truncateToMinute(dto.getEndDateTime()))
                .modifiedDate(LocalDateTime.now())
                .currentStatus(defaultStatus)
                .build();
    }

    @Override
    public Appointment mapAppointmentUpdateDtoToEntity(
            AppointmentUpdateDTO dto,
            Employee employee,
            BarberService service,
            Appointment entity
    ) {

        checkIfMapperInputIsNull(dto, entity);

        setUpdatedDataOnEntity(dto, employee, service, entity);

        return entity;
    }

    @Override
    public AppointmentInfoDTO mapEntityToInfoDto(Appointment entity) {

        checkIfMapperInputIsNull(entity);

        return AppointmentInfoDTO.builder()
                .id(entity.getAppointmentID())
                .employeeID(entity.getEmployee().getEmployeeID())
                .barberServiceID(entity.getBarberservice().getBarbershopServiceID())
                .clientFirstName(entity.getClient().getFirstName())
                .clientLastName(entity.getClient().getLastName())
                .serviceName(entity.getBarberservice().getName())
                .servicePrice(entity.getBarberservice().getPrice())
                .employeeFirstName(entity.getEmployee().getFirstName())
                .employeeLastName(entity.getEmployee().getLastName())
                .registrationTimestamp(entity.getRegistrationTimestamp())
                .startDateTime(entity.getStartDateTime())
                .endDateTime(entity.getEndDateTime())
                .currentStatus(entity.getCurrentStatus())
                .optionalNotes(entity.getOptionalNotes())
                .build();
    }

    @Override
    public List<AppointmentInfoDTO> mapEntityToInfoDto(List<Appointment> entityList) {

        return MapperHelper.mapList(entityList, this::mapEntityToInfoDto);
    }

    private void setUpdatedDataOnEntity(
            AppointmentUpdateDTO updateDTO,
            Employee employee,
            BarberService service,
            Appointment appointmentOnDB
    ) {

        if (service != null) appointmentOnDB.setBarberservice(service);

        if (employee != null) appointmentOnDB.setEmployee(employee);

        if (updateDTO.getNewStartDateTime() != null)
            appointmentOnDB.setStartDateTime(MapperHelper.truncateToMinute(updateDTO.getNewStartDateTime()));

        if (updateDTO.getNewEndDateTime() != null)
            appointmentOnDB.setEndDateTime(MapperHelper.truncateToMinute(updateDTO.getNewEndDateTime()));

        if (updateDTO.getNewStatus() != null) appointmentOnDB.setCurrentStatus(updateDTO.getNewStatus());

        appointmentOnDB.setModifiedDate(LocalDateTime.now());
    }
}
