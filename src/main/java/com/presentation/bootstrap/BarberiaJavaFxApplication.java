package com.presentation.bootstrap;

import com.BarberiaNuevoApplication;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
public class BarberiaJavaFxApplication extends Application {

    private ConfigurableApplicationContext context;

    @Getter
    private static HostServices globalHostServices;

    @Override
    public void init() {

        globalHostServices = getHostServices();

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

        return new SpringApplicationBuilder()
                .sources(BarberiaNuevoApplication.class)
                .initializers(context -> context.getBeanFactory().registerSingleton("hostServices", getHostServices()))
                .run(getParameters().getRaw().toArray(new String[0]));
    }

    private void triggerEvent(Stage stage) {

        context.publishEvent(new StageReadyEvent(stage));
    }

    private void close() {

        context.close();
        Platform.exit();
    }
}
