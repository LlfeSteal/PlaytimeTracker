package io.github.llfesteal.PlaytimeTracker.domain.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class PlayerPlaytime {
    private final UUID playerId;
    private final List<Session> sessions;
    private final Duration totalDuration;

    public PlayerPlaytime(UUID playerId, List<Session> sessions) {
        this.playerId = playerId;
        this.sessions = sessions;
        this.totalDuration = sessions.stream().map(Session::getDuration).reduce(Duration.ZERO, Duration::plus);
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Duration getDuration(LocalDateTime startDate, LocalDateTime endDate, Session extraSession) {
        if (extraSession == null) {
            return getDuration(this.sessions.stream(), startDate, endDate);
        }

        return getDuration(Stream.concat(sessions.stream(), Stream.of(extraSession)), startDate, endDate);
    }

    private Duration getDuration(Stream<Session> sessions, LocalDateTime startDate, LocalDateTime endDate) {
        return sessions
                .filter(session -> !session.getSessionEnd().isBefore(startDate) && !session.getSessionStart().isAfter(endDate))
                .map(session -> new Session(
                        session.getPlayerId(),
                        session.getSessionStart().isAfter(startDate) ? session.getSessionStart() : startDate,
                        session.getSessionEnd().isBefore(endDate) ? session.getSessionEnd() : endDate
                ).getDuration())
                .reduce(Duration.ZERO, Duration::plus);
    }

    public Duration getTotalDuration() {
        return this.totalDuration;
    }
}
