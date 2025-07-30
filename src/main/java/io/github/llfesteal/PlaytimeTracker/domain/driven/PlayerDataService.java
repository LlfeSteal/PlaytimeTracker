package io.github.llfesteal.PlaytimeTracker.domain.driven;

import io.github.llfesteal.PlaytimeTracker.domain.model.PlayerData;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public interface PlayerDataService {
    void loadPlayerData(UUID playerId);

    void unloadPlayerData(UUID playerId);

    Duration getTotalSavedSessionsDuration(UUID playerId);

    PlayerData getPlayerData(UUID playerId, LocalDateTime startDate, LocalDateTime endDate);
}
