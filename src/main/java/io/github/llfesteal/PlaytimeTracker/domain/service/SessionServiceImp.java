package io.github.llfesteal.PlaytimeTracker.domain.service;

import io.github.llfesteal.PlaytimeTracker.domain.driven.SessionService;
import io.github.llfesteal.PlaytimeTracker.domain.driving.ActiveSessionStorage;
import io.github.llfesteal.PlaytimeTracker.domain.model.Session;

import java.time.LocalDateTime;
import java.util.UUID;

public class SessionServiceImp implements SessionService {

    private final ActiveSessionStorage activeSessionStorage;

    public SessionServiceImp(ActiveSessionStorage activeSessionStorage) {
        this.activeSessionStorage = activeSessionStorage;
    }

    @Override
    public Session createNewSession(UUID playerId) {
        var newSession = new Session(playerId, LocalDateTime.now(), LocalDateTime.now());
        activeSessionStorage.add(newSession);
        return newSession;
    }

    @Override
    public Session getSessionByPlayerId(UUID playerId) {
        return this.activeSessionStorage.findByPlayerId(playerId);
    }

    @Override
    public void endSession(UUID playerId) {
        this.activeSessionStorage.endSession(playerId);
    }
}
