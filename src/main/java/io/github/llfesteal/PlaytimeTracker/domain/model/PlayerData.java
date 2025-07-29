package io.github.llfesteal.PlaytimeTracker.domain.model;

import java.time.Duration;
import java.util.UUID;

public class PlayerData {
    private final UUID playerId;
    private final String playerName;
    private final Duration savedPlaytime;

    public PlayerData(UUID playerId, String playerName, Duration savedPlaytime) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.savedPlaytime = savedPlaytime;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Duration getSavedPlaytime() {
        return savedPlaytime;
    }
}
