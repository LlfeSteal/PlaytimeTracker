package io.github.llfesteal.PlaytimeTracker.domain.driven;

import io.github.llfesteal.PlaytimeTracker.domain.model.Session;

import java.util.UUID;

public interface SessionService {
    void createNewSession(UUID playerId);

    Session getSessionByPlayerId(UUID playerId);

    void endSession(UUID playerId);
}
