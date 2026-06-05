package com.barbershop.enums;

import lombok.Getter;

import static com.barbershop.launcher.constants.ui.themes.ThemeFilePath.*;

@Getter
public enum Theme implements DescribableEnum {

    MD3_LIGHT("MD3 Claro", MD3_LIGHT_THEME_FILE_PATH),
    MD3_DARK("MD3 Oscuro", MD3_DARK_DARK_THEME_FILE_PATH),
    LA_TERCERA_LIGHT("La Tercera - Claro", LA_TERCERA_LIGHT_THEME_FILE_PATH),
    LA_TERCERA_DARK("La Tercera - Oscuro", LA_TERCERA_DARK_THEME_FILE_PATH),
    CUSTOM("Personalizado", "custom.css");

    private final String displayName;
    private final String themeFilePath;

    Theme(String displayName, String themeFilePath) {

        this.displayName = displayName;
        this.themeFilePath = themeFilePath;
    }
}
