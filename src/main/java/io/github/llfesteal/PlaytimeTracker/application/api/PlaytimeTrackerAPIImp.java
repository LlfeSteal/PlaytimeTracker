package io.github.llfesteal.PlaytimeTracker.application.api;

import io.github.llfesteal.PlaytimeTracker.application.api.exception.NoSessionFoundException;
import io.github.llfesteal.PlaytimeTracker.application.api.exception.SessionAlreadyActiveException;
import io.github.llfesteal.PlaytimeTracker.application.api.model.Session;
import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerDataService;
import io.github.llfesteal.PlaytimeTracker.domain.driven.SessionService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public final class PlaytimeTrackerAPIImp implements PlaytimeTrackerAPI {

    private final SessionService sessionService;
    private final PlayerDataService playerDataService;

    public PlaytimeTrackerAPIImp(SessionService sessionService, PlayerDataService playerDataService) {
        this.sessionService = sessionService;
        this.playerDataService = playerDataService;
    }

    @Override
    public void startNewSession(UUID playerId) throws SessionAlreadyActiveException {
        var session = this.sessionService.getSessionByPlayerId(playerId);
        if (session != null) {
            throw new SessionAlreadyActiveException(playerId, toApiModel(session));
        }

        this.sessionService.createNewSession(playerId);
    }

    @Override
    public void endSession(UUID playerId) throws NoSessionFoundException {
        var session = this.sessionService.getSessionByPlayerId(playerId);
        if (session == null) {
            throw new NoSessionFoundException(playerId);
        }

        this.sessionService.endSession(playerId);
    }

    @Override
    public Duration getPlayerPlaytime(UUID playerId, LocalDateTime startDate, LocalDateTime endDate) {
        return this.playerDataService.getPlayerData(playerId, startDate, endDate).getSavedPlaytime();
    }

    private Session toApiModel(io.github.llfesteal.PlaytimeTracker.domain.model.Session model) {
        return new Session(model.getPlayerId(), model.getSessionStart(), model.getSessionEnd());
    }
}
