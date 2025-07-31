package io.github.llfesteal.PlaytimeTracker.domain.service;

import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerDataService;
import io.github.llfesteal.PlaytimeTracker.domain.driving.SessionStorage;
import io.github.llfesteal.PlaytimeTracker.domain.driving.PlayerPlaytimeStorage;
import io.github.llfesteal.PlaytimeTracker.domain.model.PlayerPlaytime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerDataServiceImp implements PlayerDataService {

    private final SessionStorage sessionStorage;
    private final PlayerPlaytimeStorage playerPlaytimeStorage;

    public PlayerDataServiceImp(SessionStorage sessionStorage, PlayerPlaytimeStorage playerPlaytimeStorage) {
        this.sessionStorage = sessionStorage;
        this.playerPlaytimeStorage = playerPlaytimeStorage;
    }

    @Override
    public void loadPlayerData(UUID playerId) {
        var playerPlaytime = new PlayerPlaytime(playerId, this.sessionStorage.getAllPlayerSessions(playerId));
        this.playerPlaytimeStorage.store(playerPlaytime);
    }

    @Override
    public void unloadPlayerData(UUID playerId) {
        this.playerPlaytimeStorage.unstore(playerId);
    }

    @Override
    public Duration getTotalPlayerPlaytime(UUID playerId) {
        var playerPlaytime = getPlayerPlaytime(playerId);
        var currentPlaytime = this.sessionStorage.getSessionByPlayerId(playerId);

        return currentPlaytime == null
                ? playerPlaytime.getTotalDuration()
                : playerPlaytime.getTotalDuration().plus(currentPlaytime.getDuration(true));
    }

    @Override
    public Duration getPlayerPlaytime(UUID playerId, LocalDateTime startDate, LocalDateTime endDate) {
        var playerPlaytime = getPlayerPlaytime(playerId);

        return playerPlaytime.getDuration(startDate, endDate);
    }

    private PlayerPlaytime getPlayerPlaytime(UUID playerId) {
        var playerPlaytime = this.playerPlaytimeStorage.getPlaytime(playerId);

        return playerPlaytime != null
                ? playerPlaytime
                : new PlayerPlaytime(playerId, this.sessionStorage.getAllPlayerSessions(playerId));
    }
}
