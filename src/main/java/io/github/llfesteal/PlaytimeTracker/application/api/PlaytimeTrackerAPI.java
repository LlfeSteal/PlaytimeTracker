package io.github.llfesteal.PlaytimeTracker.application.api;

import io.github.llfesteal.PlaytimeTracker.application.api.exception.NoSessionFoundException;
import io.github.llfesteal.PlaytimeTracker.application.api.exception.SessionAlreadyActiveException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public interface PlaytimeTrackerAPI {
    void startNewSession(UUID playerId) throws SessionAlreadyActiveException;

    void endSession(UUID playerId) throws NoSessionFoundException;

    Duration getPlayerPlaytime(UUID playerId, LocalDateTime startDate, LocalDateTime endDate);
}
