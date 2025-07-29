package io.github.llfesteal.PlaytimeTracker.infrastructure.storage.cache.player;

import io.github.llfesteal.PlaytimeTracker.domain.model.PlayerData;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManagerImp implements PlayerDataManager {

    private final Map<UUID, PlayerData> playersData = new ConcurrentHashMap<>();

    @Override
    public void add(PlayerData playerData) {
        this.playersData.put(playerData.getPlayerId(), playerData);
    }

    @Override
    public void remove(UUID playerId) {
        this.playersData.remove(playerId);
    }

    @Override
    public PlayerData get(UUID playerId) {
        return this.playersData.get(playerId);
    }
}
