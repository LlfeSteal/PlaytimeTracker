package io.github.llfesteal.PlaytimeTracker.application.placeholder;

import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerDataService;
import io.github.llfesteal.PlaytimeTracker.domain.driven.SessionService;
import io.github.llfesteal.PlaytimeTracker.utils.TimeUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TotalPlaytimePlaceholder extends PlaceholderExpansion {

    private final Plugin plugin;
    private final SessionService sessionService;
    private final PlayerDataService playerDataService;

    public TotalPlaytimePlaceholder(Plugin plugin, SessionService sessionService, PlayerDataService playerDataService) {
        this.plugin = plugin;
        this.sessionService = sessionService;
        this.playerDataService = playerDataService;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "TotalPlaytime";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", this.plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        return this.getPlaceholderValue(player.getUniqueId(), params);
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        return this.getPlaceholderValue(player.getUniqueId(), params);
    }

    private String getPlaceholderValue(UUID playerId, @NotNull String params) {
        var totalPlaytime = this.playerDataService.getTotalSavedSessionsDuration(playerId);

        var currentPlaytime = this.sessionService.getPlayerCurrentSessionDuration(playerId);
        if (currentPlaytime != null) {
            totalPlaytime = totalPlaytime.plus(currentPlaytime);
        }

        return (params.equalsIgnoreCase("formatted"))
                ? TimeUtils.format(totalPlaytime, "%d days, %02d hours, %02d minutes, %02d seconds")
                : String.valueOf(totalPlaytime.toSeconds());
    }
}
