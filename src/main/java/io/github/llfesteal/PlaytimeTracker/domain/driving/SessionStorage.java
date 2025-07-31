package io.github.llfesteal.PlaytimeTracker.domain.driving;

import io.github.llfesteal.PlaytimeTracker.domain.model.Session;

import java.util.List;
import java.util.UUID;

public interface SessionStorage {
    void add(Session session);

    Session getSessionByPlayerId(UUID playerId);

    void endSession(UUID playerId);

    void updateSessionEndDate(Session session);

    List<Session> getAllPlayerSessions(UUID playerId);

    List<Session> getAllActiveSessions();
}
