package io.github.llfesteal.PlaytimeTracker.application.api.exception;

import io.github.llfesteal.PlaytimeTracker.application.api.model.Session;

import java.util.UUID;

public class SessionAlreadyActiveException extends Exception {

    private final UUID playerId;
    private final Session session;

    public SessionAlreadyActiveException(UUID playerId, Session session) {
        this.playerId = playerId;
        this.session = session;
    }

    @Override
    public String getMessage() {
        return "Session of player " + playerId.toString() + " is already active.";
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Session getSession() {
        return session;
    }
}
