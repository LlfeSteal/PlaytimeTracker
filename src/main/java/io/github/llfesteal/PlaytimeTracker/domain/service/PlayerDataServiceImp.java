package io.github.llfesteal.PlaytimeTracker.domain.service;

import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerDataService;
import io.github.llfesteal.PlaytimeTracker.domain.driving.SessionStorage;
import io.github.llfesteal.PlaytimeTracker.domain.driving.PlayerDataStorage;
import io.github.llfesteal.PlaytimeTracker.domain.model.PlayerData;
import io.github.llfesteal.PlaytimeTracker.domain.model.Session;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PlayerDataServiceImp implements PlayerDataService {

    private final SessionStorage sessionStorage;
    private final PlayerDataStorage playerDataStorage;

    public PlayerDataServiceImp(SessionStorage sessionStorage, PlayerDataStorage playerDataStorage) {
        this.sessionStorage = sessionStorage;
        this.playerDataStorage = playerDataStorage;
    }

    @Override
    public void loadPlayerData(UUID playerId) {
        var sessions = this.sessionStorage.getAllPlayerSessions(playerId);
        var playerData = this.getPlayerData(playerId, sessions);
        this.playerDataStorage.store(playerData);
    }

    @Override
    public void unloadPlayerData(UUID playerId) {
        this.playerDataStorage.unstore(playerId);
    }

    @Override
    public Duration getTotalSavedSessionsDuration(UUID playerId) {
        return this.playerDataStorage.getData(playerId).getSavedPlaytime();
    }

    @Override
    public PlayerData getPlayerData(UUID playerId, LocalDateTime startDate, LocalDateTime endDate) {
        var sessions = this.sessionStorage.getPlayerSessions(playerId, startDate, endDate);
        return getPlayerData(playerId, sessions);
    }

    private PlayerData getPlayerData(UUID playerId, List<Session> sessions) {
        var totalDuration = Duration.ZERO;

        for (Session session : sessions) {
            totalDuration = totalDuration.plus(session.getDuration(false));
        }

        // TODO : Implement playerName
        return new PlayerData(playerId, "NOT_YET_IMPLEMENTED", totalDuration);
    }
}
