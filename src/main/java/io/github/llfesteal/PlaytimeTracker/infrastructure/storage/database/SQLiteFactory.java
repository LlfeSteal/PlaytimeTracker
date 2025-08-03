package io.github.llfesteal.PlaytimeTracker.infrastructure.storage.database;

import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.ConfigurationService;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class SQLiteFactory implements ConnectionFactory {

    public final ConfigurationService configurationService;
    public final Logger logger;
    private final File folder;

    public SQLiteFactory(Logger logger, ConfigurationService configurationService, File folder) {
        this.configurationService = configurationService;
        this.logger = logger;
        this.folder = folder;
    }

    @Override
    public Connection getConnection() {
        var file = new File(this.folder, "database/" + this.configurationService.getSqliteFileName());
        try {
            if (!file.exists() && (!file.getParentFile().mkdirs() || !file.createNewFile())) {
                this.logger.severe("Unable to create SQLite file");
            }

            return DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
        } catch (SQLException e) {
            this.logger.severe("SQLite connection failed : " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            this.logger.severe("Failed to create SQLite file : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
