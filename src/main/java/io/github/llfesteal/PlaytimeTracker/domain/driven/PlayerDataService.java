package io.github.llfesteal.PlaytimeTracker.domain.driven;

import java.time.Duration;
import java.util.UUID;

public interface PlayerDataService {
    void loadPlayerData(UUID playerId);

    void unloadPlayerData(UUID playerId);

    Duration getTotalSavedSessionsDuration(UUID playerId);
}
