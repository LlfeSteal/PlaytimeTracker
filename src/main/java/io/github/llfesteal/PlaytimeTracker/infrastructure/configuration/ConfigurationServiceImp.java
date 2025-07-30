package io.github.llfesteal.PlaytimeTracker.infrastructure.configuration;

import fr.lifesteal.pluginframework.api.config.ConfigRepository;
import fr.lifesteal.pluginframework.core.config.ConfigParam;
import fr.lifesteal.pluginframework.core.config.ConfigServiceBase;
import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.model.DatabaseConfig;

import java.util.logging.Logger;

public class ConfigurationServiceImp extends ConfigServiceBase implements ConfigurationService {

    @ConfigParam(paramKey = "mysql")
    private DatabaseConfig databaseConfig = new DatabaseConfig("localhost", 3306, "test", "user", "", "PlaytimeTracker_");

    @ConfigParam(paramKey = "autosave-delay")
    private long autosaveDelay = 5000L;

    @ConfigParam(paramKey = "duration-format")
    private String durationFormat = "%d days, %02d hours, %02d minutes, %02d seconds";

    public ConfigurationServiceImp(Logger logger, ConfigRepository configRepository) {
        super(logger, configRepository);
    }

    @Override
    public DatabaseConfig getDatabaseConfig() {
        return databaseConfig;
    }

    @Override
    public long getAutosaveDelay() {
        return autosaveDelay;
    }

    @Override
    public String getDurationFormat() {
        return durationFormat;
    }
}
