package com.utils.info;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public final class AppInformation {

    private static final String DEVELOPER_NAME = "Tomas Gabriel Elbert";
    private final BuildProperties buildProperties;
    private final DataSource dataSource;

    public String getVersionNumber() {

        return buildProperties.getVersion();
    }

    public String getJavaVersion() {

        return System.getProperty("java.version");
    }

    public String getFrameworkVersion() {

        return SpringBootVersion.getVersion();
    }

    public Instant getBuildTimestamp() {

        return buildProperties.getTime();
    }

    public String getDatabaseName() {

        try (Connection connection = dataSource.getConnection()) {

            DatabaseMetaData metaData = connection.getMetaData();

            return metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion();

        } catch (SQLException exception) {

            return "Desconocida";
        }
    }

    public String getDeveloperName() {

        return DEVELOPER_NAME;
    }
}
