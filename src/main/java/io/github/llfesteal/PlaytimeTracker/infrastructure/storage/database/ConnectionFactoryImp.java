package io.github.llfesteal.PlaytimeTracker.infrastructure.storage.database;

import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.ConfigurationService;
import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.model.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionFactoryImp implements ConnectionFactory {
    public final ConfigurationService configurationService;
    public final Logger logger;

    public ConnectionFactoryImp(Logger logger, ConfigurationService configurationService) {
        this.configurationService = configurationService;
        this.logger = logger;
    }

    @Override
    public Connection getConnection() {
        var databaseConfig = configurationService.getDatabaseConfig();
        try {
            return DriverManager.getConnection(getConnectionString(databaseConfig), databaseConfig.getDatabaseUser(), databaseConfig.getDatabasePassword());
        } catch (SQLException e) {
            this.logger.log(Level.SEVERE, "Database connection failed", e);
            return null;
        }
    }

    private static String getConnectionString(DatabaseConfig databaseConfig) {
        return "jdbc:mysql://" + databaseConfig.getDatabaseHost()
                + ":" + databaseConfig.getDatabasePort()
                + "/" + databaseConfig.getDatabaseName();
    }
}