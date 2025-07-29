package io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.database;

import fr.lifesteal.pluginframework.api.config.ConfigRepository;
import fr.lifesteal.pluginframework.core.config.ConfigParam;
import fr.lifesteal.pluginframework.core.config.ConfigServiceBase;
import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.database.model.DatabaseConfig;

import java.util.logging.Logger;

public class ConfigurationServiceImp extends ConfigServiceBase implements ConfigurationService {

    @ConfigParam(paramKey = "mysql")
    private DatabaseConfig databaseConfig = new DatabaseConfig("localhost", 3306, "test", "user", "", "PlaytimeTracker_");

    public ConfigurationServiceImp(Logger logger, ConfigRepository configRepository) {
        super(logger, configRepository);
    }

    @Override
    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }
}
