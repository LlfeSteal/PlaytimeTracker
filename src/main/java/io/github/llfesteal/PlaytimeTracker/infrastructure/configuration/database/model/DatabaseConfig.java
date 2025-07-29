package io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.database.model;

import fr.lifesteal.pluginframework.core.config.ConfigModel;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.Map;

@SerializableAs("DatabaseConfig")
public class DatabaseConfig extends ConfigModel {

    private String databaseHost;
    private int databasePort;
    private String databaseName;
    private String databaseUser;
    private String databasePassword;
    private String databasePrefix;

    public DatabaseConfig(String databaseHost, int databasePort, String databaseName, String databaseUser, String databasePassword, String databasePrefix) {
        this.databaseHost = databaseHost;
        this.databasePort = databasePort;
        this.databaseName = databaseName;
        this.databaseUser = databaseUser;
        this.databasePassword = databasePassword;
        this.databasePrefix = databasePrefix;
    }

    public DatabaseConfig(Map<String, Object> args) {
        super(args);
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public int getDatabasePort() {
        return databasePort;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public String getDatabasePrefix() {
        return databasePrefix;
    }

    @Override
    public ConfigModel clone() {
        return new DatabaseConfig(this.databaseHost, this.databasePort, this.databaseName, this.databaseUser, this.databasePassword, this.databasePrefix);
    }
}
