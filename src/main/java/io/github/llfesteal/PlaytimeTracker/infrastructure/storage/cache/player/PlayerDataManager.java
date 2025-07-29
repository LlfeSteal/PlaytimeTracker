package io.github.llfesteal.PlaytimeTracker.infrastructure.storage.cache.player;

import io.github.llfesteal.PlaytimeTracker.domain.model.PlayerData;

import java.util.UUID;

public interface PlayerDataManager {
    void add(PlayerData playerData);

    void remove(UUID playerId);

    PlayerData get(UUID playerId);
}
