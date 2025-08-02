package io.github.llfesteal.PlaytimeTracker.infrastructure.configuration;

import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.model.MySQLConfig;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.database.DatabaseType;

public interface ConfigurationService {
    MySQLConfig getDatabaseConfig();

    long getAutosaveDelay();

    String getDurationFormat();

    String getSqliteFileName();

    String getDatabasePrefix();

    DatabaseType getDatabaseType();
}
