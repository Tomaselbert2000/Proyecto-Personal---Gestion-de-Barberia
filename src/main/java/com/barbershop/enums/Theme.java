package com.barbershop.enums;

import lombok.Getter;

import static com.barbershop.launcher.constants.ui.themes.ThemeFilePath.MD3_DARK_DARK_THEME_FILE_PATH;
import static com.barbershop.launcher.constants.ui.themes.ThemeFilePath.MD3_LIGHT_THEME_FILE_PATH;

@Getter
public enum Theme implements DescribableEnum {

    MD3_LIGHT("MD3 Claro", MD3_LIGHT_THEME_FILE_PATH, new String[]{"#6750A4", "#EADDFF", "#1D192B"}),
    MD3_DARK("MD3 Oscuro", MD3_DARK_DARK_THEME_FILE_PATH, new String[]{"#D0BCFF", "#4F378B", "#E8DEF8"}),
    LA_TERCERA("La Tercera", "la-tercera.css", new String[]{"#D4AF37", "#2C2C2C", "#FFFFFF"}),
    CUSTOM("Personalizado", "custom.css", new String[]{"#9E9E9E", "#E0E0E0", "#F5F5F5"});

    private final String displayName;
    private final String themeFilePath;
    private final String colors;

    Theme(String displayName, String themeFilePath, String[] colors) {

        this.displayName = displayName;
        this.themeFilePath = themeFilePath;
        this.colors = String.join(", ", colors);
    }
}
