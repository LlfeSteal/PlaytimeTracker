package io.github.llfesteal.PlaytimeTracker.infrastructure.storage;

import io.github.llfesteal.PlaytimeTracker.domain.driving.ActiveSessionStorage;
import io.github.llfesteal.PlaytimeTracker.domain.model.Session;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.cache.session.SessionManager;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.database.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class ActiveSessionStorageImp implements ActiveSessionStorage {

    private final SessionManager sessionManager;
    private final SessionRepository sessionRepository;

    public ActiveSessionStorageImp(SessionManager sessionManager, SessionRepository sessionRepository) {
        this.sessionManager = sessionManager;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void add(Session session) {
        this.sessionManager.add(session);
        this.sessionRepository.add(session);
    }

    @Override
    public Session findByPlayerId(UUID playerId) {
        return this.sessionManager.getByPlayerId(playerId);
    }

    @Override
    public void endSession(UUID playerId) {
        var session = this.sessionManager.remove(playerId);
        session.setSessionEnd(LocalDateTime.now());
        this.sessionRepository.updateSessionEndDate(session);
    }
}
