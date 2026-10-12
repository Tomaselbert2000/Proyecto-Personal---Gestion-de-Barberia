package com.presentation.controller.web;

import com.dto.appointment.PublicAppointmentDTO;
import com.service.interfaces.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

        model.addAttribute("employees", appointmentService.getEmployeesFromServiceInstance());
        model.addAttribute("services", appointmentService.getBarberServicesFromServiceInstance());

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
        model.addAttribute("employeeid", employeeId);
        model.addAttribute("date", date);

        List<LocalTime> availableSlots = appointmentService.getAvailableSlots(date, employeeId);

        model.addAttribute("availableSlots", availableSlots);

        return PUBLIC_BOOKING_STEP_TWO;
    }

    @GetMapping("/client-info")
    public String showStepThree(
            @RequestParam Long serviceId,
            @RequestParam Long employeeId,
            @RequestParam LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalTime time,
            Model model
    ) {

        model.addAttribute("serviceId", serviceId);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("date", date);
        model.addAttribute("time", time);

        return PUBLIC_BOOKING_STEP_THREE;
    }

    @PostMapping("/confirm")
    public String confirm(@Valid @ModelAttribute(name = "dto") PublicAppointmentDTO dto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            return PUBLIC_BOOKING_STEP_THREE;
        }

        appointmentService.registerNewAppointment(dto);

        return REDIRECT_BOOKING_SUCCESS;
    }
}
