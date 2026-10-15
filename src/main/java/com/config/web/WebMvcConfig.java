package com.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

/**
 * Expone los archivos subidos por los usuarios (por ejemplo, las imágenes de
 * producto) como recursos estáticos bajo {@code /uploads/**}.
 *
 * <p>El directorio físico se configura con la propiedad
 * {@code app.uploads.directory} y por defecto es {@code uploads} relativo al
 * directorio de ejecución de la aplicación.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public static final String UPLOADS_URL_PATTERN = "/uploads/**";

    private final String uploadsDirectory;

    public WebMvcConfig(@Value("${app.uploads.directory:uploads}") String uploadsDirectory) {

        this.uploadsDirectory = uploadsDirectory;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String location = Path.of(uploadsDirectory).toAbsolutePath().normalize().toUri().toString();

        if (!location.endsWith("/")) {

            location = location + "/";
        }

        registry.addResourceHandler(UPLOADS_URL_PATTERN).addResourceLocations(location);
    }
}
