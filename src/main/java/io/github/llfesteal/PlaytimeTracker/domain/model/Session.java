package io.github.llfesteal.PlaytimeTracker.domain.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class Session {
    private final UUID playerId;
    private final LocalDateTime sessionStart;
    private LocalDateTime sessionEnd;

    public Session(UUID playerId, LocalDateTime sessionStart, LocalDateTime sessionEnd) {
        this.playerId = playerId;
        this.sessionStart = sessionStart;
        this.sessionEnd = sessionEnd;
    }

    public void setSessionEnd(LocalDateTime sessionEnd) {
        this.sessionEnd = sessionEnd;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public LocalDateTime getSessionStart() {
        return sessionStart;
    }

    public LocalDateTime getSessionEnd() {
        return sessionEnd;
    }

    public SessionDuration getDuration(boolean isSessionActive) {
        return new SessionDuration(Duration.between(sessionStart, isSessionActive ? LocalDateTime.now() : sessionEnd));
    }
}
