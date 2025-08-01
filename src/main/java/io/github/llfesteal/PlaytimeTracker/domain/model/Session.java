package io.github.llfesteal.PlaytimeTracker.domain.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class Session {
    private final UUID playerId;
    private final LocalDateTime sessionStart;
    private final boolean isActive;
    private LocalDateTime sessionEnd;

    public Session(UUID playerId, LocalDateTime sessionStart, LocalDateTime sessionEnd) {
        this.playerId = playerId;
        this.sessionStart = sessionStart;
        this.sessionEnd = sessionEnd;
        this.isActive = false;
    }

    public Session(UUID playerId, LocalDateTime sessionStart, LocalDateTime sessionEnd, boolean isActive) {
        this.playerId = playerId;
        this.sessionStart = sessionStart;
        this.sessionEnd = sessionEnd;
        this.isActive = isActive;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public LocalDateTime getSessionStart() {
        return sessionStart;
    }

    public LocalDateTime getSessionEnd() {
        return this.isActive ? LocalDateTime.now() : sessionEnd;
    }

    public void setSessionEnd(LocalDateTime sessionEnd) {
        this.sessionEnd = sessionEnd;
    }

    public Duration getDuration() {
        return Duration.between(sessionStart, this.getSessionEnd());
    }
}
