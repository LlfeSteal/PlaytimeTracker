package io.github.llfesteal.PlaytimeTracker.infrastructure.storage.cache.player;

import io.github.llfesteal.PlaytimeTracker.domain.driving.PlayerPlaytimeStorage;
import io.github.llfesteal.PlaytimeTracker.domain.model.PlayerData;
import io.github.llfesteal.PlaytimeTracker.domain.model.PlayerPlaytime;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerPlaytimeManagerImp implements PlayerPlaytimeStorage {

    private final Map<UUID, PlayerPlaytime> playersData = new ConcurrentHashMap<>();

    @Override
    public void store(PlayerPlaytime playerData) {
        this.playersData.put(playerData.getPlayerId(), playerData);
    }

    @Override
    public void unstore(UUID playerId) {
        this.playersData.remove(playerId);
    }

    @Override
    public PlayerPlaytime getPlaytime(UUID playerId) {
        return this.playersData.get(playerId);
    }
}
