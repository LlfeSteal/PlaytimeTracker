package io.github.llfesteal.PlaytimeTracker.infrastructure.configuration;

import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.model.DatabaseConfig;

public interface ConfigurationService {
    DatabaseConfig getDatabaseConfig();

    long getAutosaveDelay();

    String getDurationFormat();
}
