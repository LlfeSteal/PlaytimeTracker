package io.github.llfesteal.PlaytimeTracker.infrastructure.storage.database.repository;

import io.github.llfesteal.PlaytimeTracker.domain.model.Session;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SessionRepository {
    void init();
    void add(Session session);

    void updateSessionEndDate(Session session);

    List<Session> getPlayerSessions(UUID playerId);
}
