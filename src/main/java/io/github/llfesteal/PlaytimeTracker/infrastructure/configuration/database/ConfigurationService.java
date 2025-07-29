package io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.database;

import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.database.model.DatabaseConfig;

public interface ConfigurationService {
    DatabaseConfig getDatabaseConfig();
}
