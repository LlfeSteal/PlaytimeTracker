package io.github.llfesteal.PlaytimeTracker.application.placeholder;

import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerDataService;
import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.ConfigurationService;
import io.github.llfesteal.PlaytimeTracker.utils.TimeUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class TodayPlaytimePlaceholder extends PlaceholderExpansion {

    private final Plugin plugin;
    private final PlayerDataService playerDataService;
    private final ConfigurationService configurationService;

    public TodayPlaytimePlaceholder(Plugin plugin, PlayerDataService playerDataService, ConfigurationService configurationService) {
        this.plugin = plugin;
        this.playerDataService = playerDataService;
        this.configurationService = configurationService;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "TodayPlaytime";
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

    private @Nullable String getPlaceholderValue(UUID playerId, @NotNull String params) {
        var playerPlaytime = this.playerDataService.getPlayerPlaytime(playerId, LocalDate.now().atStartOfDay(), LocalDate.now().atTime(LocalTime.MAX));

        return (params.equalsIgnoreCase("formatted"))
                ? TimeUtils.format(playerPlaytime, this.configurationService.getDurationFormat())
                : String.valueOf(playerPlaytime.toSeconds());
    }
}
