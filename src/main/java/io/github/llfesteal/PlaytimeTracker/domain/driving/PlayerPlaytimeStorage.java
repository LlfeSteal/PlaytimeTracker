package io.github.llfesteal.PlaytimeTracker.domain.driving;

import io.github.llfesteal.PlaytimeTracker.domain.model.PlayerPlaytime;

import java.util.UUID;

public interface PlayerPlaytimeStorage {
    void store(PlayerPlaytime playerData);

    void unstore(UUID playerId);

    PlayerPlaytime getPlaytime(UUID playerId);
}
