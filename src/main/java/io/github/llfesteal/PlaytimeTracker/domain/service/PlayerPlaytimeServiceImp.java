package io.github.llfesteal.PlaytimeTracker.domain.service;

import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerPlaytimeService;
import io.github.llfesteal.PlaytimeTracker.domain.driving.PlayerPlaytimeStorage;
import io.github.llfesteal.PlaytimeTracker.domain.driving.SessionStorage;
import io.github.llfesteal.PlaytimeTracker.domain.model.PlayerPlaytime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerPlaytimeServiceImp implements PlayerPlaytimeService {

    private final SessionStorage sessionStorage;
    private final PlayerPlaytimeStorage playerPlaytimeStorage;

    public PlayerPlaytimeServiceImp(SessionStorage sessionStorage, PlayerPlaytimeStorage playerPlaytimeStorage) {
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
        var currentSession = this.sessionStorage.getActiveSessionByPlayerId(playerId);

        return currentSession == null
                ? playerPlaytime.getTotalDuration()
                : playerPlaytime.getTotalDuration().plus(currentSession.getDuration());
    }

    @Override
    public Duration getPlayerPlaytime(UUID playerId, LocalDateTime startDate, LocalDateTime endDate) {
        var playerPlaytime = getPlayerPlaytime(playerId);
        var currentSession = this.sessionStorage.getActiveSessionByPlayerId(playerId);

        return playerPlaytime.getDuration(startDate, endDate, currentSession);
    }

    private PlayerPlaytime getPlayerPlaytime(UUID playerId) {
        var playerPlaytime = this.playerPlaytimeStorage.getPlaytime(playerId);

        return playerPlaytime != null
                ? playerPlaytime
                : new PlayerPlaytime(playerId, this.sessionStorage.getAllPlayerSessions(playerId));
    }
}
