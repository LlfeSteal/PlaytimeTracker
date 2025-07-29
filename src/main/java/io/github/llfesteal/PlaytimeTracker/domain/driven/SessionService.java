package io.github.llfesteal.PlaytimeTracker.domain.driven;

import io.github.llfesteal.PlaytimeTracker.domain.model.Session;

import java.util.Optional;
import java.util.UUID;

public interface SessionService {
    Session createNewSession(UUID playerId);

    Session getSessionByPlayerId(UUID playerId);
}
