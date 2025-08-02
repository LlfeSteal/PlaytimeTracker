package io.github.llfesteal.PlaytimeTracker.domain.driven;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public interface PlayerPlaytimeService {
    void loadPlayerData(UUID playerId);

    void unloadPlayerData(UUID playerId);

    Duration getTotalPlayerPlaytime(UUID playerId);

    Duration getPlayerPlaytime(UUID playerId, LocalDateTime startDate, LocalDateTime endDate);
}
