package io.github.llfesteal.PlaytimeTracker.application.api.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record Session(UUID playerId, LocalDateTime sessionStart, LocalDateTime sessionEnd) {
}
