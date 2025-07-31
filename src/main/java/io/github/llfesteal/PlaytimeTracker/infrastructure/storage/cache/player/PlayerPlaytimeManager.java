package io.github.llfesteal.PlaytimeTracker.infrastructure.storage.cache.player;

import io.github.llfesteal.PlaytimeTracker.domain.model.PlayerPlaytime;

import java.util.UUID;

public interface PlayerPlaytimeManager {
    void store(PlayerPlaytime playerData);

    void unstore(UUID playerId);

    PlayerPlaytime getPlaytime(UUID playerId);
}
