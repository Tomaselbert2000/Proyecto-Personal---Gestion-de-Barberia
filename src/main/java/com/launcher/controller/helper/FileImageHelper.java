package com.launcher.controller.helper;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URI;

import static com.launcher.constants.StringResource.DisplayString.ALLOWED_IMAGE_EXTENSIONS;
import static com.launcher.constants.StringResource.DisplayString.FILE_CHOOSER_IMAGE_DESCRIPTION;

public class FileImageHelper {

    /**
     * Abre un cuadro de diálogo para seleccionar un archivo de imagen.
     *
     * @param currentView El contenedor actual donde se mostrará el cuadro de diálogo.
     * @return El archivo seleccionado, o null si no se selecciona ningún archivo.
     */
    public static File getFileFromFileChooser(AnchorPane currentView) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(generateExtensionFilter());
        return fileChooser.showOpenDialog(currentView.getScene().getWindow());
    }

    /**
     * Genera un filtro de extensión para el cuadro de diálogo de archivos.
     *
     * @return El filtro de extensión configurado para permitir solo imágenes.
     */
    private static FileChooser.ExtensionFilter generateExtensionFilter() {
        return new FileChooser.ExtensionFilter(FILE_CHOOSER_IMAGE_DESCRIPTION, ALLOWED_IMAGE_EXTENSIONS);
    }

    /**
     * Carga un archivo de imagen en un ImageView.
     *
     * @param file      El archivo de imagen a cargar.
     * @param imageView El ImageView donde se cargará la imagen.
     */
    public static void loadFileOnImageView(File file, ImageView imageView) {
        URI uriLocation = URI.create(file.toURI().toString());
        imageView.setImage(new Image(String.valueOf(uriLocation)));
    }

    /**
     * Limpia un ImageView, eliminando cualquier imagen que contenga.
     *
     * @param imageView El ImageView a limpiar.
     */
    public static void cleanImageView(ImageView imageView) {
        imageView.setImage(null);
    }
}