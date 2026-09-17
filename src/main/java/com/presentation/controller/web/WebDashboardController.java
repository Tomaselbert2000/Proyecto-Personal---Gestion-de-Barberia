package com.presentation.controller.web;

import com.dto.activity.RecentActivityDTO;
import com.dto.stats.AppointmentTodayStatsDTO;
import com.dto.stats.ClientAcquisitionStatsDTO;
import com.dto.stats.ExpectedIncomeStatDTO;
import com.dto.stats.InventoryAlertStatsDTO;
import com.service.interfaces.AppointmentService;
import com.service.interfaces.ClientService;
import com.service.interfaces.DashboardService;
import com.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

import static com.presentation.constants.HtmlTemplatePath.DASHBOARD_HTML_PATH;

@Controller
@RequiredArgsConstructor
public class WebDashboardController {

    private final DashboardService dashboardService;
    private final ClientService clientService;
    private final AppointmentService appointmentService;
    private final ProductService productService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {

        String currentUserName = principal.getName();

        model.addAttribute("currentUser", currentUserName);

        ClientAcquisitionStatsDTO clientAcquisitionStatsDTO = clientService.getClientStatsVsLastMonth();
        ExpectedIncomeStatDTO expectedIncomeStatDTO = appointmentService.getExpectedIncomeToday();
        AppointmentTodayStatsDTO appointmentTodayStatsDTO = appointmentService.getAppointmentsTodayStats();
        InventoryAlertStatsDTO inventoryAlertStatsDTO = productService.getInventoryAlertStat();

        List<RecentActivityDTO> recentActivity = dashboardService.getRecentActivityLog();

        model.addAttribute("clientStats", clientAcquisitionStatsDTO);
        model.addAttribute("expectedIncomeStats", expectedIncomeStatDTO);
        model.addAttribute("appointmentStats", appointmentTodayStatsDTO);
        model.addAttribute("inventoryStats", inventoryAlertStatsDTO);
        model.addAttribute("recentActivity", recentActivity);

        return DASHBOARD_HTML_PATH;
    }
}
