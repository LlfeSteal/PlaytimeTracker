package io.github.llfesteal.PlaytimeTracker.infrastructure.storage.database.repository;

import io.github.llfesteal.PlaytimeTracker.domain.model.Session;

public interface SessionRepository {
    void init();
    void add(Session session);
}
