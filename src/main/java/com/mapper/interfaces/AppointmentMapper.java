package com.mapper.interfaces;

import com.dto.appointment.AppointmentCreationDTO;
import com.dto.appointment.AppointmentInfoDTO;
import com.dto.appointment.AppointmentUpdateDTO;
import com.model.Appointment;
import com.model.BarberService;
import com.model.Client;
import com.model.Employee;

import java.util.List;

public interface AppointmentMapper {

    Appointment mapAppointmentCreationDtoToEntity(
            AppointmentCreationDTO dto,
            Client client,
            Employee employee,
            BarberService service
    );

    Appointment mapAppointmentUpdateDtoToEntity(
            AppointmentUpdateDTO dto,
            Employee employee,
            BarberService service,
            Appointment entity
    );

    AppointmentInfoDTO mapEntityToInfoDto(Appointment entity);

    List<AppointmentInfoDTO> mapEntityToInfoDto(List<Appointment> entityList);
}
