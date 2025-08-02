package io.github.llfesteal.PlaytimeTracker.infrastructure.configuration;

import fr.lifesteal.pluginframework.api.config.ConfigRepository;
import fr.lifesteal.pluginframework.core.config.ConfigParam;
import fr.lifesteal.pluginframework.core.config.ConfigServiceBase;
import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.model.MySQLConfig;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.database.DatabaseType;

import java.util.logging.Logger;

public class ConfigurationServiceImp extends ConfigServiceBase implements ConfigurationService {

    @ConfigParam(paramKey = "database.type")
    private String databaseType = "sqlite";

    @ConfigParam(paramKey = "database.prefix")
    private String databasePrefix = "PlaytimeTracker_";

    @ConfigParam(paramKey = "database.mysql")
    private MySQLConfig mySQLConfig = new MySQLConfig("localhost", 3306, "test", "user", "");

    @ConfigParam(paramKey = "database.sqlite.fileName")
    private String sqliteFileName = "playtime.db";

    @ConfigParam(paramKey = "autosave-delay")
    private long autosaveDelay = 5000L;

    @ConfigParam(paramKey = "duration-format")
    private String durationFormat = "%d days, %02d hours, %02d minutes, %02d seconds";

    public ConfigurationServiceImp(Logger logger, ConfigRepository configRepository) {
        super(logger, configRepository);
    }

    @Override
    public MySQLConfig getDatabaseConfig() {
        return mySQLConfig;
    }

    @Override
    public long getAutosaveDelay() {
        return autosaveDelay;
    }

    @Override
    public String getDurationFormat() {
        return durationFormat;
    }

    @Override
    public String getSqliteFileName() {
        return sqliteFileName;
    }

    @Override
    public String getDatabasePrefix() {
        return databasePrefix;
    }

    @Override
    public DatabaseType getDatabaseType() {
        if (databaseType.equalsIgnoreCase("mysql")) {
            return DatabaseType.MYSQL;
        }

        return DatabaseType.SQLITE;
    }
}
