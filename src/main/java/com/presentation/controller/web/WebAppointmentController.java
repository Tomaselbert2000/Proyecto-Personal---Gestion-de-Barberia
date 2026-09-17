package com.presentation.controller.web;

import com.dto.stats.AppointmentCanceledStatsDTO;
import com.dto.stats.AppointmentMonthlyComparisonDTO;
import com.dto.stats.AppointmentTodayStatsDTO;
import com.dto.stats.AppointmentTomorrowStatsDTO;
import com.service.interfaces.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.presentation.constants.HtmlTemplatePath.APPOINTMENTS_HTML_PATH;

@Controller
@RequiredArgsConstructor
public class WebAppointmentController {

    private final AppointmentService service;

    @GetMapping("/appointments")
    public String showAppointments(Model model) {

        AppointmentTodayStatsDTO appointmentTodayStatsDTO = service.getAppointmentsTodayStats();
        AppointmentTomorrowStatsDTO appointmentTomorrowStatsDTO = service.getPendingAppointmentsStats();
        AppointmentMonthlyComparisonDTO appointmentMonthlyComparisonDTO = service.getMonthlyComparisonStats();
        AppointmentCanceledStatsDTO canceledStatsDTO = service.getCanceledStats();

        model.addAttribute("appointmentsTodayStats", appointmentTodayStatsDTO);
        model.addAttribute("appointmentsTomorrowStats", appointmentTomorrowStatsDTO);
        model.addAttribute("monthlyComparisonStats", appointmentMonthlyComparisonDTO);
        model.addAttribute("canceledStats", canceledStatsDTO);

        return APPOINTMENTS_HTML_PATH;
    }
}
