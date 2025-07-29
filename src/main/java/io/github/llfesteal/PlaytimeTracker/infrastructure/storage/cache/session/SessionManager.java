package io.github.llfesteal.PlaytimeTracker.infrastructure.storage.cache.session;

import io.github.llfesteal.PlaytimeTracker.domain.model.Session;

import java.util.UUID;

public interface SessionManager {
    void add(Session session);

    Session getByPlayerId(UUID playerId);
}
