package com.barbershop.service.interfaces;

import com.barbershop.dto.appointment.AppointmentCreationDTO;
import com.barbershop.dto.appointment.AppointmentInfoDTO;
import com.barbershop.dto.appointment.AppointmentUpdateDTO;
import com.barbershop.enums.AppointmentStatus;

import java.util.List;

public interface AppointmentService {

    void registerNewAppointment(AppointmentCreationDTO newAppointment);

    void deleteAppointment(Long appointmentID);

    AppointmentInfoDTO getAppointmentInfo(Long appointmentID);

    List<AppointmentInfoDTO> getAppointmentsList();

    Long appointmentsByStatus(AppointmentStatus status);

    Long appointmentsToday();

    Long completedAppointmentsToday();

    Long appointmentsCreatedToday();

    Long appointmentsDuringThisMonth();

    void updateAppointment(Long appointmentID, AppointmentUpdateDTO updateDTO);

    Long calculatePercentageOfAppointmentsVsPreviousMonth();

    Long canceledAppointments();

    Long canceledAppointmentsVsPastWeek();

    Long getTotalAppointmentsCount();
}
