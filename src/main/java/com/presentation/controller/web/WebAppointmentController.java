package com.presentation.controller.web;

import com.dto.appointment.AppointmentInfoDTO;
import com.dto.stats.AppointmentCanceledStatsDTO;
import com.dto.stats.AppointmentMonthlyComparisonDTO;
import com.dto.stats.AppointmentTodayStatsDTO;
import com.dto.stats.AppointmentTomorrowStatsDTO;
import com.enums.AppointmentStatus;
import com.service.interfaces.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import static com.presentation.constants.HtmlTemplatePath.APPOINTMENTS_HTML_PATH;

@Controller
@RequiredArgsConstructor
public class WebAppointmentController {

    private final AppointmentService service;

    @GetMapping("/appointments")
    public String showAppointments(
            Model model,
            Principal principal,
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) String employeeName
    ) {

        AppointmentTodayStatsDTO appointmentTodayStatsDTO = service.getAppointmentsTodayStats();
        AppointmentTomorrowStatsDTO appointmentTomorrowStatsDTO = service.getPendingAppointmentsStats();
        AppointmentMonthlyComparisonDTO appointmentMonthlyComparisonDTO = service.getMonthlyComparisonStats();
        AppointmentCanceledStatsDTO canceledStatsDTO = service.getCanceledStats();

        List<AppointmentInfoDTO> appointmentsList = service.liveSearch(clientName, date, status, employeeName);

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("appointmentsTodayStats", appointmentTodayStatsDTO);
        model.addAttribute("appointmentsTomorrowStats", appointmentTomorrowStatsDTO);
        model.addAttribute("monthlyComparisonStats", appointmentMonthlyComparisonDTO);
        model.addAttribute("canceledStats", canceledStatsDTO);
        model.addAttribute("appointmentsLiveSearch", appointmentsList);

        model.addAttribute("statuses", AppointmentStatus.values());
        model.addAttribute("employees", service.getEmployeesFromServiceInstance());

        model.addAttribute("selectedClientName", clientName);
        model.addAttribute("selectedDate", date);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedEmployeeName", employeeName);

        return APPOINTMENTS_HTML_PATH;
    }

    @PostMapping("/appointments/{appointmentID}/complete")
    public String markAppointmentAsComplete(@PathVariable Long appointmentID) {

        service.markAppointmentAsComplete(AppointmentInfoDTO.builder().id(appointmentID).build());
        return "redirect:/appointments";
    }

    @PostMapping("/appointments/{appointmentID}/cancel")
    public String markAppointmentAsCanceled(@PathVariable Long appointmentID) {

        service.markAppointmentAsCanceled(AppointmentInfoDTO.builder().id(appointmentID).build());
        return "redirect:/appointments";
    }

    @PostMapping("/appointments/{appointmentID}/delete")
    public String deleteAppointment(@PathVariable Long appointmentID) {

        service.deleteAppointment(appointmentID);
        return "redirect:/appointments";
    }
}
