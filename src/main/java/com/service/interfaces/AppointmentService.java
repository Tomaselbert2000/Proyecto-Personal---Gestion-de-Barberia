package com.service.interfaces;

import com.dto.appointment.AppointmentCreationDTO;
import com.dto.appointment.AppointmentInfoDTO;
import com.dto.appointment.AppointmentUpdateDTO;
import com.dto.barberservice.BarberServiceInfoDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.dto.stats.AppointmentCanceledStatsDTO;
import com.dto.stats.AppointmentMonthlyComparisonDTO;
import com.dto.stats.AppointmentTodayStatsDTO;
import com.dto.stats.AppointmentTomorrowStatsDTO;
import com.enums.AppointmentStatus;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    void registerNewAppointment(AppointmentCreationDTO newAppointment);

    void deleteAppointment(Long appointmentID);

    AppointmentInfoDTO getAppointmentInfo(Long appointmentID);

    List<AppointmentInfoDTO> getAppointmentsList();

    Long appointmentsToday();

    Long completedAppointmentsToday();

    void updateAppointment(Long appointmentID, AppointmentUpdateDTO updateDTO);

    List<AppointmentInfoDTO> liveSearch(String clientName, LocalDate date, AppointmentStatus selectedAppointmentStatus, String employeeName);

    List<EmployeeInfoDTO> getEmployeesFromServiceInstance();

    void markAppointmentAsComplete(AppointmentInfoDTO dto);

    void markAppointmentAsCanceled(AppointmentInfoDTO dto);

    List<BarberServiceInfoDTO> getBarberServicesFromServiceInstance();

    List<ClientInfoDTO> clientLiveSearchByName(String searchName);

    AppointmentTodayStatsDTO getAppointmentsTodayStats();

    AppointmentTomorrowStatsDTO getPendingAppointmentsStats();

    AppointmentMonthlyComparisonDTO getMonthlyComparisonStats();

    AppointmentCanceledStatsDTO getCanceledStats();

    Long getCount();
}
