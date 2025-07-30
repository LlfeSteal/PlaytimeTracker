package io.github.llfesteal.PlaytimeTracker.domain.driven;

import io.github.llfesteal.PlaytimeTracker.domain.model.Session;

import java.time.Duration;
import java.util.UUID;

public interface SessionService {
    void createNewSession(UUID playerId);

    Session getSessionByPlayerId(UUID playerId);

    void endSession(UUID playerId);

    void forceSaveSessions();

    Duration getPlayerCurrentSessionDuration(UUID playerId);
}
