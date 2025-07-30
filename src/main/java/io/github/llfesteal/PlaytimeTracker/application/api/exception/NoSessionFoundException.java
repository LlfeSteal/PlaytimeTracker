package io.github.llfesteal.PlaytimeTracker.application.api.exception;

import java.util.UUID;

public class NoSessionFoundException extends Exception {

    private final UUID playerId;

    public NoSessionFoundException(UUID playerId) {
        this.playerId = playerId;
    }

    @Override
    public String getMessage() {
        return "No session found for player " + playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }
}
