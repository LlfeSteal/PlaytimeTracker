package io.github.llfesteal.PlaytimeTracker.infrastructure.storage;

import io.github.llfesteal.PlaytimeTracker.domain.driving.PlayerPlaytimeStorage;
import io.github.llfesteal.PlaytimeTracker.domain.model.PlayerPlaytime;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.cache.player.PlayerPlaytimeManager;

import java.util.UUID;

public class PlayerPlaytimeStorageImp implements PlayerPlaytimeStorage {

    private final PlayerPlaytimeManager playerPlaytimeManager;

    public PlayerPlaytimeStorageImp(PlayerPlaytimeManager playerPlaytimeManager) {
        this.playerPlaytimeManager = playerPlaytimeManager;
    }

    @Override
    public void store(PlayerPlaytime playerData) {
        this.playerPlaytimeManager.store(playerData);
    }

    @Override
    public void unstore(UUID playerId) {
        this.playerPlaytimeManager.unstore(playerId);
    }

    @Override
    public PlayerPlaytime getPlaytime(UUID playerId) {
        return this.playerPlaytimeManager.getPlaytime(playerId);
    }
}
