package io.github.llfesteal.PlaytimeTracker.domain.service;

import io.github.llfesteal.PlaytimeTracker.domain.driven.SessionService;
import io.github.llfesteal.PlaytimeTracker.domain.driving.SessionStorage;
import io.github.llfesteal.PlaytimeTracker.domain.model.Session;

import java.time.LocalDateTime;
import java.util.UUID;

public class SessionServiceImp implements SessionService {

    private final SessionStorage sessionStorage;

    public SessionServiceImp(SessionStorage sessionStorage) {
        this.sessionStorage = sessionStorage;
    }

    @Override
    public void createNewSession(UUID playerId) {
        var newSession = new Session(playerId, LocalDateTime.now(), LocalDateTime.now());
        sessionStorage.add(newSession);
    }

    @Override
    public Session getSessionByPlayerId(UUID playerId) {
        return this.sessionStorage.findByPlayerId(playerId);
    }

    @Override
    public void endSession(UUID playerId) {
        this.sessionStorage.endSession(playerId);
    }
}
