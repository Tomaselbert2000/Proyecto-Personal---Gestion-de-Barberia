package com.presentation.controller.web;

import com.config.preferences.AppPreferences;
import com.dto.settings.SettingsUpdateDTO;
import com.enums.Theme;
import com.exceptions.settings.InvalidServiceHourException;
import com.utils.info.AppInformation;
import com.validation.settings.SettingsUpdateValidator;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.presentation.constants.ControllerConstants.SettingsControllerConstants.MAX_PASSWORD_LENGTH;
import static com.presentation.constants.ControllerConstants.SettingsControllerConstants.MIN_PASSWORD_LENGTH;
import static com.presentation.constants.HtmlConstants.Paths.SETTINGS;
import static com.presentation.constants.HtmlConstants.Redirects.REDIRECT_SETTINGS;
import static com.presentation.support.control.ValidationFormatter.getConstraintViolationsList;

@Controller
@RequiredArgsConstructor
@RequestMapping("/settings")
public class WebSettingsController {

    private static final String DEFAULT_OPENING_TIME = "08:00";
    private static final String DEFAULT_CLOSING_TIME = "20:00";
    private static final DateTimeFormatter BUILD_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());

    private final AppPreferences appPreferences;
    private final AppInformation appInformation;
    private final SettingsUpdateValidator settingsUpdateValidator;

    @GetMapping
    public String showSettings(
            Principal principal,
            Model model,
            @RequestParam(required = false, defaultValue = "false") Boolean saved
    ) {

        populateModel(model, principal, null, saved, null);

        return SETTINGS;
    }

    @PostMapping
    public String saveSettings(
            Principal principal,
            Model model,
            @RequestParam String themeSelected,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String openingHour,
            @RequestParam(required = false) String closingHour,
            @RequestParam(required = false) Boolean newAppointmentNotificationEnabled,
            @RequestParam(required = false) Boolean clientReminderNotificationEnabled,
            @RequestParam(required = false) Boolean lowStockNotificationEnabled,
            @RequestParam(required = false) Boolean workplaceChangesNotificationEnabled
    ) {

        SettingsUpdateDTO dto = SettingsUpdateDTO.builder()
                .themeSelected(resolveWebTheme(themeSelected))
                .name(blankToNull(name))
                .phone(blankToNull(phone))
                .email(blankToNull(email))
                .address(blankToNull(address))
                .openingHour(LocalTime.parse(openingHour == null || openingHour.isBlank() ? DEFAULT_OPENING_TIME : openingHour))
                .closingHour(LocalTime.parse(closingHour == null || closingHour.isBlank() ? DEFAULT_CLOSING_TIME : closingHour))
                .newAppointmentNotificationEnabled(Boolean.TRUE.equals(newAppointmentNotificationEnabled))
                .clientReminderNotificationEnabled(Boolean.TRUE.equals(clientReminderNotificationEnabled))
                .lowStockNotificationEnabled(Boolean.TRUE.equals(lowStockNotificationEnabled))
                .workplaceChangesNotificationEnabled(Boolean.TRUE.equals(workplaceChangesNotificationEnabled))
                .build();

        try {

            settingsUpdateValidator.validateDTO(dto);
            appPreferences.saveSettings(dto);

            return REDIRECT_SETTINGS;

        } catch (ConstraintViolationException exception) {

            populateModel(model, principal, dto, false, getConstraintViolationsList(exception));

            return SETTINGS;

        } catch (InvalidServiceHourException exception) {

            populateModel(model, principal, dto, false, exception.getMessage());

            return SETTINGS;
        }
    }

    private void populateModel(
            Model model,
            Principal principal,
            SettingsUpdateDTO dto,
            boolean saved,
            String errorMessage
    ) {

        loadUsernameAndThemeOnModel(model, principal, dto);

        loadBarbershopInformationOnModel(model, dto);

        loadServiceHoursOnModel(model, dto);

        loadNotificationsSettings(model, dto);

        loadAppInfoOnModel(model);

        loadPasswordRestrictionsOnModel(model);

        model.addAttribute("saved", saved);
        model.addAttribute("error", errorMessage != null);
        model.addAttribute("errorMessage", errorMessage);
    }

    private void loadUsernameAndThemeOnModel(Model model, Principal principal, SettingsUpdateDTO dto) {

        model.addAttribute("currentUser", principal.getName());
        model.addAttribute("themes", Arrays.stream(Theme.values())
                .filter(theme -> theme != Theme.CUSTOM)
                .toList());
        model.addAttribute("selectedTheme", dto != null ? dto.getThemeSelected() : appPreferences.getTheme());
    }

    private static void loadPasswordRestrictionsOnModel(Model model) {

        model.addAttribute("minPasswordLength", MIN_PASSWORD_LENGTH);
        model.addAttribute("maxPasswordLength", MAX_PASSWORD_LENGTH);
    }

    private void loadAppInfoOnModel(Model model) {

        model.addAttribute("appVersion", appInformation.getVersionNumber());
        model.addAttribute("frameworkInfo", "Java " + appInformation.getJavaVersion() + ", SpringBoot " + appInformation.getFrameworkVersion());
        model.addAttribute("buildTimestamp", appInformation.getBuildTimestamp() != null ? BUILD_TIME_FORMATTER.format(appInformation.getBuildTimestamp()) : "—");
        model.addAttribute("databaseName", appInformation.getDatabaseName());
        model.addAttribute("developerName", appInformation.getDeveloperName());
    }

    private void loadNotificationsSettings(Model model, SettingsUpdateDTO dto) {

        model.addAttribute("newAppointmentNotifications", dto != null ? dto.getNewAppointmentNotificationEnabled() : appPreferences.isNewAppointmentNotificationEnabled());
        model.addAttribute("clientReminderNotifications", dto != null ? dto.getClientReminderNotificationEnabled() : appPreferences.isClientReminderNotificationEnabled());
        model.addAttribute("lowStockNotifications", dto != null ? dto.getLowStockNotificationEnabled() : appPreferences.isLowStockNotificationEnabled());
        model.addAttribute("workplaceChangesNotifications", dto != null ? dto.getWorkplaceChangesNotificationEnabled() : appPreferences.isWorkplaceChangesNotificationEnabled());
    }

    private void loadServiceHoursOnModel(Model model, SettingsUpdateDTO dto) {

        model.addAttribute("openingTime", dto != null && dto.getOpeningHour() != null ? dto.getOpeningHour().toString() : appPreferences.getBarberShopOpeningTime());
        model.addAttribute("closingTime", dto != null && dto.getClosingHour() != null ? dto.getClosingHour().toString() : appPreferences.getBarberShopClosingTime());

        model.addAttribute("hours", buildHourOptions());
    }

    private void loadBarbershopInformationOnModel(Model model, SettingsUpdateDTO dto) {

        model.addAttribute("name", dto != null ? nullToEmpty(dto.getName()) : appPreferences.getBarberShopName());
        model.addAttribute("phone", dto != null ? nullToEmpty(dto.getPhone()) : appPreferences.getBarberShopPhoneNumber());
        model.addAttribute("email", dto != null ? nullToEmpty(dto.getEmail()) : appPreferences.getBarberShopEmail());
        model.addAttribute("address", dto != null ? nullToEmpty(dto.getAddress()) : appPreferences.getBarberShopAddress());
    }

    private static List<String> buildHourOptions() {

        return IntStream.range(0, 24)
                .mapToObj(hour -> String.format("%02d:00", hour))
                .toList();
    }

    private static String resolveWebTheme(String themeSelected) {

        return Arrays.stream(Theme.values())
                .filter(theme -> theme != Theme.CUSTOM)
                .filter(theme -> theme.name().equals(themeSelected))
                .findFirst()
                .map(Theme::name)
                .orElse(Theme.MD3_LIGHT.name());
    }

    private static String blankToNull(String value) {

        return value == null || value.isBlank() ? null : value.trim();
    }

    private static String nullToEmpty(String value) {

        return value == null ? "" : value;
    }
}