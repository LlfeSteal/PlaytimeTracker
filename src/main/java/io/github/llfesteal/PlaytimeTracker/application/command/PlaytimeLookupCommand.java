package io.github.llfesteal.PlaytimeTracker.application.command;

import fr.lifesteal.pluginframework.core.command.CommandExecutor;
import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerDataService;
import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.ConfigurationService;
import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.LangService;
import io.github.llfesteal.PlaytimeTracker.utils.StringUtils;
import io.github.llfesteal.PlaytimeTracker.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlaytimeLookupCommand extends CommandExecutor {

    private final PlayerDataService playerDataService;
    private final ConfigurationService configurationService;
    private final LangService langService;

    private UUID playerUUID;
    private String playerName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public PlaytimeLookupCommand(CommandSender issuer, Map<String, String> namedArgs, PlayerDataService playerDataService, ConfigurationService configurationService, LangService langService) {
        super(issuer, namedArgs);
        this.playerDataService = playerDataService;
        this.configurationService = configurationService;
        this.langService = langService;
    }

    @Override
    public boolean prepare() {
        var playerIdentifier = this.getArg("player");

        OfflinePlayer player = StringUtils.isUUID(playerIdentifier)
                ? Bukkit.getOfflinePlayer(UUID.fromString(playerIdentifier))
                : Bukkit.getPlayer(playerIdentifier);

        if (player == null || !player.hasPlayedBefore()) {
            this.getIssuer().sendMessage(this.langService.getErrorPlayerNotFound(playerIdentifier));
            return false;
        }

        this.playerUUID = player.getUniqueId();
        this.playerName = player.getName();

        var startDateArg = this.getArg("start_date");
        this.startDate = startDateArg != null ? LocalDateTime.parse(startDateArg) : null;

        var endDateArg = this.getArg("end_date");
        this.endDate = endDateArg != null ? LocalDateTime.parse(endDateArg) : null;

        return true;
    }

    @Override
    public boolean execute() {
        var playerData = this.playerDataService.getPlayerData(this.playerUUID,  this.startDate, this.endDate);
        var formattedDuration = TimeUtils.format(playerData.getSavedPlaytime(), this.configurationService.getDurationFormat());
        this.getIssuer().sendMessage(this.langService.getPlaytimeLookupMessage(this.playerName, formattedDuration, this.startDate.toString(), this.endDate.toString()));

        return true;
    }

    @Override
    public List<String> getTabSuggestion(String paramName) {
        return switch (paramName) {
            case "player" -> Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            case "start_date" -> new ArrayList<>() {{
                add(LocalDate.now().atStartOfDay().toString());
            }};
            case "end_date" -> new ArrayList<>() {{
                add(LocalDate.now().atTime(LocalTime.MAX).toString());
            }};
            default -> super.getTabSuggestion(paramName);
        };

    }
}
