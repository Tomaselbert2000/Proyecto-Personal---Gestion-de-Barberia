package com.barbershop.launcher.ui;

import com.barbershop.BarberiaNuevoApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JavafxApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {

        this.context = loadSpringApplicationContext();
    }

    @Override
    public void start(Stage stage) {

        triggerEvent(stage);
    }

    @Override
    public void stop() {

        close();
    }

    private ConfigurableApplicationContext loadSpringApplicationContext() {

        return new SpringApplicationBuilder().sources(BarberiaNuevoApplication.class).run(getParameters().getRaw().toArray(new String[0]));
    }

    private void triggerEvent(Stage stage) {

        context.publishEvent(new StageReadyEvent(stage));
    }

    private void close() {

        context.close();
        Platform.exit();
    }
}
