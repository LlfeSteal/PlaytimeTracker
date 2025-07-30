package io.github.llfesteal.PlaytimeTracker.application.placeholder;

import io.github.llfesteal.PlaytimeTracker.domain.driven.SessionService;
import io.github.llfesteal.PlaytimeTracker.utils.TimeUtils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CurrentPlaytimePlaceholder extends PlaceholderExpansion {

    private final Plugin plugin;
    private final SessionService sessionService;

    public CurrentPlaytimePlaceholder(Plugin plugin, SessionService sessionService) {
        this.plugin = plugin;
        this.sessionService = sessionService;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "CurrentPlaytime";
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
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        var sessionDuration = this.sessionService.getPlayerCurrentSessionDuration(player.getUniqueId());

        if (sessionDuration == null) {
            return null;
        }

        return (params.equalsIgnoreCase("formatted"))
                ? TimeUtils.format(sessionDuration, "%d days, %02d hours, %02d minutes, %02d seconds")
                : String.valueOf(sessionDuration.toSeconds());
    }
}
