package io.github.llfesteal.PlaytimeTracker.Application.command;

import fr.lifesteal.pluginframework.core.command.CommandExecutor;
import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerDataService;
import io.github.llfesteal.PlaytimeTracker.domain.driven.SessionService;
import io.github.llfesteal.PlaytimeTracker.domain.model.Session;
import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.LangService;
import io.github.llfesteal.PlaytimeTracker.utils.TimeUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.logging.Logger;

public class PlaytimeCommand extends CommandExecutor {

    private final SessionService sessionService;
    private final LangService langService;
    private final Logger logger;
    private final PlayerDataService playerDataService;

    private Session playerSession;

    public PlaytimeCommand(CommandSender issuer, Map<String, String> namedArgs, SessionService sessionService, LangService langService, Logger logger, PlayerDataService playerDataService) {
        super(issuer, namedArgs);
        this.sessionService = sessionService;
        this.langService = langService;
        this.logger = logger;
        this.playerDataService = playerDataService;
    }

    @Override
    public boolean prepare() {

        if (!(this.getIssuer() instanceof Player player)) {
            this.getIssuer().sendMessage(this.langService.getErrorOnlyPlayersAllowedMessage());
            return false;
        }

        this.playerSession = this.sessionService.getSessionByPlayerId(player.getUniqueId());

        if (this.playerSession == null) {
            this.getIssuer().sendMessage(this.langService.getErrorNoSessionAlive());
            logger.severe("The player " + player.getUniqueId() + " has no session active !");
        }

        return true;
    }

    @Override
    public boolean execute() {
        var currentPlaytime = this.playerSession.getDuration(true);
        var formattedCurrentPlaytime = TimeUtils.format(currentPlaytime, "%d days, %02d hours, %02d minutes, %02d seconds");

        var totalPlaytime = this.playerDataService.getPlayerData(this.playerSession.getPlayerId()).getSavedPlaytime().plus(currentPlaytime);
        var formattedTotalPlaytime = TimeUtils.format(totalPlaytime, "%d days, %02d hours, %02d minutes, %02d seconds");

        this.getIssuer().sendMessage(this.langService.getPlaytimeMessages(formattedCurrentPlaytime, formattedTotalPlaytime).toArray(new String[0]));

        return true;
    }
}
