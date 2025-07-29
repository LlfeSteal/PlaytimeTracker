package io.github.llfesteal.PlaytimeTracker.domain.driving;

import io.github.llfesteal.PlaytimeTracker.domain.model.Session;

import java.util.List;
import java.util.UUID;

public interface SessionStorage {
    void add(Session session);

    Session findByPlayerId(UUID playerId);

    void endSession(UUID playerId);

    List<Session> getAllPlayerSessions(UUID playerId);
}
