package io.github.llfesteal.PlaytimeTracker.infrastructure.storage;

import io.github.llfesteal.PlaytimeTracker.domain.driving.ActiveSessionStorage;
import io.github.llfesteal.PlaytimeTracker.domain.model.Session;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.cache.session.SessionManager;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.database.repository.SessionRepository;

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
        sessionManager.add(session);
        sessionRepository.add(session);
    }

    @Override
    public Session findByPlayerId(UUID playerId) {
        return this.sessionManager.getByPlayerId(playerId);
    }
}
