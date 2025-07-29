package io.github.llfesteal.PlaytimeTracker.infrastructure.storage;

import io.github.llfesteal.PlaytimeTracker.domain.driving.PlayerDataStorage;
import io.github.llfesteal.PlaytimeTracker.domain.model.PlayerData;
import io.github.llfesteal.PlaytimeTracker.domain.model.Session;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.cache.player.PlayerDataManager;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.database.repository.SessionRepository;

import java.util.List;
import java.util.UUID;

public class PlayerDataStorageImp implements PlayerDataStorage {

    private final SessionRepository sessionRepository;
    private final PlayerDataManager playerDataManager;

    public PlayerDataStorageImp(SessionRepository sessionRepository, PlayerDataManager playerDataManager) {
        this.sessionRepository = sessionRepository;
        this.playerDataManager = playerDataManager;
    }

    @Override
    public void store(PlayerData playerData) {
        this.playerDataManager.add(playerData);
    }

    @Override
    public void unstore(UUID playerId) {
        this.playerDataManager.remove(playerId);
    }

    @Override
    public PlayerData getData(UUID playerId) {
        return this.playerDataManager.get(playerId);
    }
}
