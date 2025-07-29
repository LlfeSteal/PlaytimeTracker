package io.github.llfesteal.PlaytimeTracker.domain.driven;

import io.github.llfesteal.PlaytimeTracker.domain.model.PlayerData;

import java.util.UUID;

public interface PlayerDataService {
    void loadPlayerData(UUID playerId);

    void unloadPlayerData(UUID playerId);

    PlayerData getPlayerData(UUID playerId);
}
