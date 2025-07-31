package io.github.llfesteal.PlaytimeTracker.domain.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PlayerPlaytime {
    private final UUID playerId;
    private final List<Session> sessions;
    private final Duration totalDuration;

    public PlayerPlaytime(UUID playerId, List<Session> sessions) {
        this.playerId = playerId;
        this.sessions = sessions;
        this.totalDuration = sessions.stream().map(session -> session.getDuration(false)).reduce(Duration.ZERO, Duration::plus);
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Duration getDuration(LocalDateTime startDate, LocalDateTime endDate) {
        return sessions.stream()
                .filter(session -> !session.getSessionEnd().isBefore(startDate) && !session.getSessionStart().isAfter(endDate))
                .map(session -> new Session(
                        session.getPlayerId(),
                        session.getSessionStart().isAfter(startDate) ? session.getSessionStart() : startDate,
                        session.getSessionEnd().isBefore(endDate) ? session.getSessionEnd() : endDate
                ).getDuration(false))
                .reduce(Duration.ZERO, Duration::plus);
    }

    public Duration getTotalDuration() {
        return this.totalDuration;
    }
}
