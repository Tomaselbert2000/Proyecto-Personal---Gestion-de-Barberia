package com.service.interfaces;

import com.dto.appointment.AppointmentCreationDTO;
import com.dto.appointment.AppointmentInfoDTO;
import com.dto.appointment.AppointmentUpdateDTO;
import com.dto.appointment.PublicAppointmentDTO;
import com.dto.barberservice.BarberServiceInfoDTO;
import com.dto.client.ClientInfoDTO;
import com.dto.employee.EmployeeInfoDTO;
import com.dto.stats.*;
import com.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {

    void registerNewAppointment(AppointmentCreationDTO dto);

    void registerNewAppointment(PublicAppointmentDTO dto);

    void deleteAppointment(Long id);

    AppointmentInfoDTO getAppointmentInfo(Long id);

    List<AppointmentInfoDTO> getAppointmentsList();

    void updateAppointment(Long id, AppointmentUpdateDTO dto);

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

    ExpectedIncomeStatDTO getExpectedIncomeToday();

    Long getCount();

    List<LocalTime> getAvailableSlots(LocalDate date, Long employeeId);
}
