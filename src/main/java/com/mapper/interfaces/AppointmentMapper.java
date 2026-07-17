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

    Appointment mapAppointmentCreationDtoToAppointmentEntity(AppointmentCreationDTO dto, Client client, Employee employee, BarberService service);

    Appointment mapAppointmentUpdateDtoToAppointmentEntity(AppointmentUpdateDTO updateDTO, Employee employee, BarberService service, Appointment appointmentOnDB);

    AppointmentInfoDTO mapAppointmentToInfoDto(Appointment appointment);

    List<AppointmentInfoDTO> mapAppointmentToInfoDto(List<Appointment> appointmentList);

    AppointmentUpdateDTO mapAppointmentToUpdateDTO(Appointment appointment);
}
