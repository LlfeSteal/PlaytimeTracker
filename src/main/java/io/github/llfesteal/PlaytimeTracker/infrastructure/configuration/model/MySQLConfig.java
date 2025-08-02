package io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.model;

import fr.lifesteal.pluginframework.core.config.ConfigModel;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.Map;

@SerializableAs("MySQLConfig")
public class MySQLConfig extends ConfigModel {

    private String databaseHost;
    private int databasePort;
    private String databaseName;
    private String databaseUser;
    private String databasePassword;

    public MySQLConfig(String databaseHost, int databasePort, String databaseName, String databaseUser, String databasePassword) {
        this.databaseHost = databaseHost;
        this.databasePort = databasePort;
        this.databaseName = databaseName;
        this.databaseUser = databaseUser;
        this.databasePassword = databasePassword;
    }

    public MySQLConfig(Map<String, Object> args) {
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

    @Override
    public ConfigModel clone() {
        return new MySQLConfig(this.databaseHost, this.databasePort, this.databaseName, this.databaseUser, this.databasePassword);
    }
}
