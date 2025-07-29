package io.github.llfesteal.PlaytimeTracker.infrastructure.storage.database.repository;

import io.github.llfesteal.PlaytimeTracker.domain.model.Session;
import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.database.ConfigurationService;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.database.ConnectionFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Logger;

public class SessionRepositoryImp implements SessionRepository {

    private static final String SESSION_TABLE = "sessions";
    private final Logger logger;
    private final ConnectionFactory connectionFactory;
    private final ConfigurationService configurationService;

    public SessionRepositoryImp(Logger logger, ConnectionFactory connectionFactory, ConfigurationService configurationService) {
        this.logger = logger;
        this.connectionFactory = connectionFactory;
        this.configurationService = configurationService;
    }

    @Override
    public void init() {
        this.createTableIfNotExist();
    }

    @Override
    public void add(Session session) {
        try(var connection = this.connectionFactory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `" + this.getTableFullName() + "`(`player_uuid`, `start_date`, `end_date`) VALUES (?,?,?)");
            statement.setString(1, session.getPlayerId().toString());
            statement.setTimestamp(2, Timestamp.valueOf(session.getSessionStart()));
            statement.setTimestamp(3, Timestamp.valueOf(session.getSessionEnd()));
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            this.logger.severe("Error while creating entry for session of player " + session.getPlayerId() + " : " + e.getMessage());
        }
    }

    @Override
    public void updateSessionEndDate(Session session) {
        try(var connection = this.connectionFactory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE `" + this.getTableFullName() + "` SET `end_date`= ? WHERE player_uuid = ? AND start_date = ?;");
            statement.setTimestamp(1, Timestamp.valueOf(session.getSessionEnd()));
            statement.setString(2, session.getPlayerId().toString());
            statement.setTimestamp(3, Timestamp.valueOf(session.getSessionStart()));
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            this.logger.severe("Error while updating end_date entry for session of player " + session.getPlayerId() + " : " + e.getMessage());
        }
    }

    private void createTableIfNotExist() {
        try(var connection = this.connectionFactory.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(getInitRequest());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            this.logger.severe("Error while creating table " + SESSION_TABLE + " : " + e.getMessage());
        }
    }

    private String getInitRequest() {
        return "CREATE TABLE IF NOT EXISTS `" + this.getTableFullName() + "` " +
                "(`player_uuid` VARCHAR(36) NOT NULL , " +
                "`start_date` DATETIME NOT NULL , " +
                "`end_date` DATETIME NOT NULL ," +
                "PRIMARY KEY (`player_uuid`,`start_date`)) " +
                "ENGINE = InnoDB;";
    }

    private String getTableFullName() {
        return this.configurationService.getDatabaseConfig().getDatabasePrefix() + SESSION_TABLE;
    }
}
