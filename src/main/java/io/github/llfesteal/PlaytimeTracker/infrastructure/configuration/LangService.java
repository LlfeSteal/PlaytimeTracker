package io.github.llfesteal.PlaytimeTracker.infrastructure.configuration;

import java.util.List;
import java.util.UUID;

public interface LangService {

    String getErrorOnlyPlayersAllowedMessage();

    String getErrorNoSessionAlive();

    List<String> getPlaytimeMessages(String formattedCurrentPlaytime, String formattedTotalPlaytime);

    String getPlaytimeLookupMessage(String playerName, String duration, String startDate, String endDate);

    String getErrorPlayerNotFound(String playerIdentifier);
}
