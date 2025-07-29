package io.github.llfesteal.PlaytimeTracker.domain.driving;

import io.github.llfesteal.PlaytimeTracker.domain.model.Session;

import java.util.UUID;

public interface ActiveSessionStorage {
    void add(Session session);

    Session findByPlayerId(UUID playerId);
}
