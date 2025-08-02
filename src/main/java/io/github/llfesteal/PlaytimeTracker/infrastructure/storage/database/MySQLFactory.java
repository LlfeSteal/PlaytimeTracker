package io.github.llfesteal.PlaytimeTracker.infrastructure.storage.database;

import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.ConfigurationService;
import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.model.MySQLConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySQLFactory implements ConnectionFactory {
    public final ConfigurationService configurationService;
    public final Logger logger;

    public MySQLFactory(Logger logger, ConfigurationService configurationService) {
        this.configurationService = configurationService;
        this.logger = logger;
    }

    private static String getConnectionString(MySQLConfig mySQLConfig) {
        return "jdbc:mysql://" + mySQLConfig.getDatabaseHost()
                + ":" + mySQLConfig.getDatabasePort()
                + "/" + mySQLConfig.getDatabaseName();
    }

    @Override
    public Connection getConnection() {
        var databaseConfig = configurationService.getDatabaseConfig();
        try {
            return DriverManager.getConnection(getConnectionString(databaseConfig), databaseConfig.getDatabaseUser(), databaseConfig.getDatabasePassword());
        } catch (SQLException e) {
            this.logger.log(Level.SEVERE, "MySQL connection failed", e);
            return null;
        }
    }
}