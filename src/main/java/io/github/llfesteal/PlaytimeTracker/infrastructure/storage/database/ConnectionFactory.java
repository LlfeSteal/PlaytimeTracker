package io.github.llfesteal.PlaytimeTracker.infrastructure.storage.database;

import java.sql.Connection;

public interface ConnectionFactory {
    Connection getConnection();
}
