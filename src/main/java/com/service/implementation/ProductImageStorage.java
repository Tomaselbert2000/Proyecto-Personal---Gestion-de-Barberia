package com.service.implementation;

import com.exceptions.product.InvalidProductImageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * Almacena en disco las imágenes subidas para los productos y devuelve la ruta
 * pública con la que pueden servirse desde la web.
 *
 * <p>Los archivos se guardan en el subdirectorio {@code products} dentro del
 * directorio configurado por {@code app.uploads.directory}, que se publica en
 * {@code /uploads/**} (ver {@code WebMvcConfig}).
 */
@Component
public class ProductImageStorage {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png");
    private static final String PRODUCTS_SUBFOLDER = "products";
    private static final String PUBLIC_PREFIX = "/uploads/";

    private final Path productsDirectory;

    public ProductImageStorage(@Value("${app.uploads.directory:uploads}") String uploadsDirectory) {

        this.productsDirectory = Path.of(uploadsDirectory).toAbsolutePath().normalize().resolve(PRODUCTS_SUBFOLDER);
    }

    public String store(MultipartFile imageFile) {

        String extension = resolveExtension(imageFile.getOriginalFilename());

        if (!ALLOWED_EXTENSIONS.contains(extension)) {

            throw new InvalidProductImageException();
        }

        String fileName = UUID.randomUUID() + "." + extension;

        try (InputStream inputStream = imageFile.getInputStream()) {

            Files.createDirectories(productsDirectory);

            Files.copy(inputStream, productsDirectory.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException exception) {

            throw new InvalidProductImageException();
        }

        return PUBLIC_PREFIX + PRODUCTS_SUBFOLDER + "/" + fileName;
    }

    private static String resolveExtension(String originalFilename) {

        if (originalFilename == null || originalFilename.isBlank()) {

            return "";
        }

        int dotIndex = originalFilename.lastIndexOf('.');

        return dotIndex < 0 ? "" : originalFilename.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }
}
