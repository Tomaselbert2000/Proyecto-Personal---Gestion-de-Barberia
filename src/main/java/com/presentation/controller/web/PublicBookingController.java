package com.presentation.controller.web;

import com.dto.appointment.PublicAppointmentDTO;
import com.service.interfaces.AppointmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.presentation.constants.HtmlConstants.Paths.*;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_BOOKING_SUCCESS;

@Controller
@RequiredArgsConstructor
@RequestMapping("/public-booking")
public class PublicBookingController {

    private final AppointmentService appointmentService;

    @GetMapping
    public String showStepOne(Model model) {

        model.addAttribute("employees", appointmentService.getEmployeesFromServiceInstance().stream()
                .filter(employee -> Boolean.TRUE.equals(employee.getIsActive()))
                .toList());
        model.addAttribute("services", appointmentService.getBarberServicesFromServiceInstance().stream()
                .filter(service -> Boolean.TRUE.equals(service.getIsCurrentlyActive()))
                .toList());

        return PUBLIC_BOOKING_STEP_ONE;
    }

    @GetMapping("/calendar")
    public String showStepTwo(
            @RequestParam Long serviceId,
            @RequestParam Long employeeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model
    ) {

        if (date == null) {

            date = LocalDate.now();
        }

        model.addAttribute("serviceId", serviceId);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("date", date);

        List<LocalTime> availableSlots = appointmentService.getAvailableSlots(date, employeeId);

        model.addAttribute("availableSlots", availableSlots);
        loadSelection(model, serviceId, employeeId);

        return PUBLIC_BOOKING_STEP_TWO;
    }

    @GetMapping("/client-info")
    public String showStepThree(
            @RequestParam Long serviceId,
            @RequestParam Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
            HttpServletRequest request,
            Model model
    ) {

        request.getSession();

        model.addAttribute("dto", PublicAppointmentDTO.builder()
                .barberServiceID(serviceId)
                .employeeID(employeeId)
                .date(date)
                .startTime(time)
                .build());

        loadSelection(model, serviceId, employeeId);

        return PUBLIC_BOOKING_STEP_THREE;
    }

    @GetMapping("/booking-success")
    public String showStepFour() {

        return PUBLIC_BOOKING_SUCCESS;
    }

    @PostMapping("/confirm")
    public String confirm(
            @Valid @ModelAttribute(name = "dto") PublicAppointmentDTO dto,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList());
            loadSelection(model, dto.getBarberServiceID(), dto.getEmployeeID());

            return PUBLIC_BOOKING_STEP_THREE;
        }

        appointmentService.registerNewAppointment(dto);

        return REDIRECT_BOOKING_SUCCESS;
    }

    private void loadSelection(Model model, Long serviceId, Long employeeId) {

        model.addAttribute("barberService", appointmentService.getBarberServicesFromServiceInstance().stream()
                .filter(service -> service.getBarberServiceId().equals(serviceId))
                .findFirst()
                .orElse(null));
        model.addAttribute("employee", appointmentService.getEmployeesFromServiceInstance().stream()
                .filter(employee -> employee.getId().equals(employeeId))
                .findFirst()
                .orElse(null));
    }
}
