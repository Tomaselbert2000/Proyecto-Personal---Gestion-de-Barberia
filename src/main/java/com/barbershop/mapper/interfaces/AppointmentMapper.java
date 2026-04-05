package com.barbershop.mapper.interfaces;

import com.barbershop.dto.appointment.AppointmentCreationDTO;
import com.barbershop.dto.appointment.AppointmentInfoDTO;
import com.barbershop.dto.appointment.AppointmentUpdateDTO;
import com.barbershop.model.Appointment;
import com.barbershop.model.BarberService;
import com.barbershop.model.Client;
import com.barbershop.model.Employee;

import java.util.List;

public interface AppointmentMapper {

    Appointment mapAppointmentCreationDtoToAppointmentEntity(AppointmentCreationDTO dto, Client client, Employee employee, BarberService service);

    Appointment mapAppointmentUpdateDtoToAppointmentEntity(AppointmentUpdateDTO updateDTO, Employee employee, BarberService service, Appointment appointmentOnDB);

    AppointmentInfoDTO mapAppointmentToInfoDto(Appointment appointment);

    List<AppointmentInfoDTO> mapAppointmentToInfoDto(List<Appointment> appointmentList);
}
