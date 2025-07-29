package io.github.llfesteal.PlaytimeTracker.domain.driving;

import io.github.llfesteal.PlaytimeTracker.domain.model.PlayerData;

import java.util.UUID;

public interface PlayerDataStorage {
    void store(PlayerData playerData);

    void unstore(UUID playerId);

    PlayerData getData(UUID playerId);
}
