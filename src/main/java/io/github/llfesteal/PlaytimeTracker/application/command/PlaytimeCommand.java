package io.github.llfesteal.PlaytimeTracker.application.command;

import fr.lifesteal.pluginframework.core.command.CommandExecutor;
import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerPlaytimeService;
import io.github.llfesteal.PlaytimeTracker.domain.driven.SessionService;
import io.github.llfesteal.PlaytimeTracker.domain.model.Session;
import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.ConfigurationService;
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
    private final PlayerPlaytimeService playerPlaytimeService;
    private final ConfigurationService configurationService;

    private Session playerSession;

    public PlaytimeCommand(CommandSender issuer, Map<String, String> namedArgs, SessionService sessionService, LangService langService, Logger logger, PlayerPlaytimeService playerPlaytimeService, ConfigurationService configurationService) {
        super(issuer, namedArgs);
        this.sessionService = sessionService;
        this.langService = langService;
        this.logger = logger;
        this.playerPlaytimeService = playerPlaytimeService;
        this.configurationService = configurationService;
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
            return false;
        }

        return true;
    }

    @Override
    public boolean execute() {
        var currentPlaytime = this.sessionService.getPlayerCurrentSessionDuration(this.playerSession.getPlayerId());
        var formattedCurrentPlaytime = TimeUtils.format(currentPlaytime, this.configurationService.getDurationFormat());

        var totalPlaytime = this.playerPlaytimeService.getTotalPlayerPlaytime(this.playerSession.getPlayerId());
        var formattedTotalPlaytime = TimeUtils.format(totalPlaytime, this.configurationService.getDurationFormat());

        this.getIssuer().sendMessage(this.langService.getPlaytimeMessages(formattedCurrentPlaytime, formattedTotalPlaytime).toArray(new String[0]));

        return true;
    }
}
