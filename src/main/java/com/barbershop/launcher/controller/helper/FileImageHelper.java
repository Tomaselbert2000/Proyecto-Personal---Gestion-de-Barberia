package com.barbershop.launcher.controller.helper;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URI;

import static com.barbershop.launcher.controller.helper.HelperConstants.ALLOWED_IMAGE_EXTENSIONS;
import static com.barbershop.launcher.controller.helper.HelperConstants.FILE_CHOOSER_IMAGE_DESCRIPTION;

public class FileImageHelper {

    public static File getFileFromFileChooser(AnchorPane currentView) {

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extensionFilter = generateExtensionFilter();

        fileChooser.getExtensionFilters().add(extensionFilter);

        return new FileChooser().showOpenDialog(currentView.getScene().getWindow());
    }

    private static FileChooser.ExtensionFilter generateExtensionFilter() {

        return new FileChooser.ExtensionFilter(FILE_CHOOSER_IMAGE_DESCRIPTION, ALLOWED_IMAGE_EXTENSIONS);
    }

    public static void loadFileOnImageView(File file, ImageView imageView) {

        URI uriLocation = URI.create(file.toURI().toString());

        imageView.setImage(new Image(String.valueOf(uriLocation)));
    }

    public static void cleanImageView(ImageView imageView) {

        imageView.setImage(null);
    }
}
