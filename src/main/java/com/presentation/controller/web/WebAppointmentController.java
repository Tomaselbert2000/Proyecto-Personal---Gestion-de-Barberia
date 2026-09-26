package com.presentation.controller.web;

import com.dto.appointment.AppointmentCreationDTO;
import com.dto.appointment.AppointmentInfoDTO;
import com.dto.appointment.AppointmentUpdateDTO;
import com.dto.stats.AppointmentCanceledStatsDTO;
import com.dto.stats.AppointmentMonthlyComparisonDTO;
import com.dto.stats.AppointmentTodayStatsDTO;
import com.dto.stats.AppointmentTomorrowStatsDTO;
import com.enums.AppointmentStatus;
import com.exceptions.BusinessException;
import com.service.interfaces.AppointmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.presentation.constants.HtmlConstants.Paths.APPOINTMENTS;
import static com.presentation.constants.HtmlConstants.Paths.APPOINTMENT_CREATION;
import static com.presentation.constants.HtmlConstants.Paths.APPOINTMENT_UPDATE;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_APPOINTMENTS;
import static com.presentation.constants.HtmlConstants.Redirects.redirectToUpdate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/appointments")
public class WebAppointmentController implements WebController<AppointmentCreationDTO> {

    private final AppointmentService service;

    @GetMapping()
    public String showAppointments(
            Model model,
            Principal principal,
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) String employeeName) {

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

        return APPOINTMENTS;
    }

    @PostMapping("/{appointmentID}/complete")
    public String markAppointmentAsComplete(@PathVariable Long appointmentID) {

        service.markAppointmentAsComplete(AppointmentInfoDTO.builder().id(appointmentID).build());
        return REDIRECT_APPOINTMENTS;
    }

    @PostMapping("/{appointmentID}/cancel")
    public String markAppointmentAsCanceled(@PathVariable Long appointmentID) {

        service.markAppointmentAsCanceled(AppointmentInfoDTO.builder().id(appointmentID).build());
        return REDIRECT_APPOINTMENTS;
    }

    @PostMapping("/{appointmentID}/delete")
    public String deleteAppointment(@PathVariable Long appointmentID) {

        service.deleteAppointment(appointmentID);
        return REDIRECT_APPOINTMENTS;
    }

    @GetMapping("/{appointmentID}/update")
    public String updateAppointment(@PathVariable Long appointmentID, Model model) {

        AppointmentInfoDTO dto = service.getAppointmentInfo(appointmentID);

        model.addAttribute("dto", dto);
        model.addAttribute("services", service.getBarberServicesFromServiceInstance());
        model.addAttribute("employees", service.getEmployeesFromServiceInstance());
        model.addAttribute("statuses", AppointmentStatus.values());

        return APPOINTMENT_UPDATE;
    }

    @GetMapping("/new")
    public String showAppointmentCreationForm(
            Model model,
            Principal principal) {

        return renderCreationForm(model, principal, new AppointmentCreationDTO());
    }

    @PostMapping("/new")
    public String createAppointment(
            @Valid @ModelAttribute(name = "dto") AppointmentCreationDTO dto,
            BindingResult bindingResult,
            Model model,
            Principal principal) {

        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "validationErrors", bindingResult.getFieldErrors()
                            .stream()
                            .map(FieldError::getDefaultMessage)
                            .filter(Objects::nonNull)
                            .toList());

            return renderCreationForm(model, principal, dto);
        }

        try {

            service.registerNewAppointment(dto);

            return REDIRECT_APPOINTMENTS;

        } catch (BusinessException exception) {

            model.addAttribute("validationErrors", List.of(exception.getMessage()));

            return renderCreationForm(model, principal, dto);
        }
    }

    @PostMapping("/{appointmentID}/update")
    public String updateAppointment(@PathVariable Long appointmentID, @ModelAttribute AppointmentUpdateDTO dto) {

        service.updateAppointment(appointmentID, dto);

        return redirectToUpdate(REDIRECT_APPOINTMENTS, appointmentID);
    }

    @Override
    public String renderCreationForm(Model model, Principal principal, AppointmentCreationDTO dto) {

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("dto", dto);

        populateCreationCatalog(model);

        return APPOINTMENT_CREATION;
    }

    @Override
    public void populateCreationCatalog(Model model) {

        model.addAttribute("services", service.getBarberServicesFromServiceInstance());
        model.addAttribute("employees", service.getEmployeesFromServiceInstance());
        model.addAttribute("statuses", AppointmentStatus.values());
    }
}
