package com.presentation.controller.web;

import com.dto.appointment.AppointmentCreationDTO;
import com.dto.appointment.AppointmentInfoDTO;
import com.dto.appointment.AppointmentUpdateDTO;
import com.dto.stats.AppointmentCanceledStatsDTO;
import com.dto.stats.AppointmentMonthlyComparisonDTO;
import com.dto.stats.AppointmentTodayStatsDTO;
import com.dto.stats.AppointmentTomorrowStatsDTO;
import com.enums.AppointmentStatus;
import com.presentation.controller.BaseWebController;
import com.service.interfaces.AppointmentService;
import com.service.interfaces.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import static com.presentation.constants.HtmlConstants.Paths.*;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_APPOINTMENTS;
import static com.presentation.constants.HtmlConstants.Redirects.redirectToUpdate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/appointments")
public class WebAppointmentController extends BaseWebController<AppointmentCreationDTO> {

    private final AppointmentService appointmentService;
    private final ClientService clientService;

    @GetMapping()
    public String showAppointments(
            Model model,
            Principal principal,
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) String employeeName
    ) {

        AppointmentTodayStatsDTO appointmentTodayStatsDTO = appointmentService.getAppointmentsTodayStats();
        AppointmentTomorrowStatsDTO appointmentTomorrowStatsDTO = appointmentService.getPendingAppointmentsStats();
        AppointmentMonthlyComparisonDTO appointmentMonthlyComparisonDTO = appointmentService.getMonthlyComparisonStats();
        AppointmentCanceledStatsDTO canceledStatsDTO = appointmentService.getCanceledStats();

        List<AppointmentInfoDTO> appointmentsList = appointmentService.liveSearch(clientName, date, status, employeeName);

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("appointmentsTodayStats", appointmentTodayStatsDTO);
        model.addAttribute("appointmentsTomorrowStats", appointmentTomorrowStatsDTO);
        model.addAttribute("monthlyComparisonStats", appointmentMonthlyComparisonDTO);
        model.addAttribute("canceledStats", canceledStatsDTO);
        model.addAttribute("appointmentsLiveSearch", appointmentsList);

        model.addAttribute("statuses", AppointmentStatus.values());
        model.addAttribute("employees", appointmentService.getEmployeesFromServiceInstance());

        model.addAttribute("selectedClientName", clientName);
        model.addAttribute("selectedDate", date);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedEmployeeName", employeeName);

        return APPOINTMENTS;
    }

    @GetMapping("/new")
    public String showAppointmentCreationForm(Model model, Principal principal) {

        return showCreationForm(model, principal, new AppointmentCreationDTO());
    }

    @PostMapping("/new")
    public String createAppointment(
            @Valid @ModelAttribute(name = "dto") AppointmentCreationDTO dto,
            BindingResult bindingResult,
            Model model,
            Principal principal
    ) {

        return createEntity(dto, bindingResult, model, principal, REDIRECT_APPOINTMENTS);
    }

    @PostMapping("/{appointmentID}/complete")
    public String markAppointmentAsComplete(@PathVariable Long appointmentID) {

        appointmentService.markAppointmentAsComplete(
                AppointmentInfoDTO.builder()
                        .id(appointmentID)
                        .build()
        );

        return REDIRECT_APPOINTMENTS;
    }

    @PostMapping("/{appointmentID}/cancel")
    public String markAppointmentAsCanceled(@PathVariable Long appointmentID) {

        appointmentService.markAppointmentAsCanceled(
                AppointmentInfoDTO.builder()
                        .id(appointmentID)
                        .build()
        );

        return REDIRECT_APPOINTMENTS;
    }

    @PostMapping("/{appointmentID}/delete")
    public String deleteAppointment(@PathVariable Long appointmentID) {

        appointmentService.deleteAppointment(appointmentID);

        return REDIRECT_APPOINTMENTS;
    }

    @GetMapping("/{appointmentID}/update")
    public String updateAppointment(@PathVariable Long appointmentID, Model model) {

        AppointmentInfoDTO dto = appointmentService.getAppointmentInfo(appointmentID);

        model.addAttribute("dto", dto);
        model.addAttribute("services", appointmentService.getBarberServicesFromServiceInstance());
        model.addAttribute("employees", appointmentService.getEmployeesFromServiceInstance());
        model.addAttribute("statuses", AppointmentStatus.values());

        return APPOINTMENT_UPDATE;
    }

    @PostMapping("/{appointmentID}/update")
    public String updateAppointment(@PathVariable Long appointmentID, @ModelAttribute AppointmentUpdateDTO dto) {

        appointmentService.updateAppointment(appointmentID, dto);

        return redirectToUpdate(REDIRECT_APPOINTMENTS, appointmentID);
    }

    @Override
    protected void executeCreation(AppointmentCreationDTO dto) {

        appointmentService.registerNewAppointment(dto);
    }

    @Override
    public String renderCreationForm(Model model, Principal principal, AppointmentCreationDTO dto) {

        populateCreationForm(model);
        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("dto", dto);

        return APPOINTMENT_CREATION;
    }

    @Override
    protected void populateCreationForm(Model model) {

        model.addAttribute("clients", clientService.getClientList());
        model.addAttribute("services", appointmentService.getBarberServicesFromServiceInstance());
        model.addAttribute("employees", appointmentService.getEmployeesFromServiceInstance());
    }
}
