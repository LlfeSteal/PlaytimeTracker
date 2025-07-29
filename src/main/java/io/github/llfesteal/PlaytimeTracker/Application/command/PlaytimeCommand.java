package io.github.llfesteal.PlaytimeTracker.Application.command;

import fr.lifesteal.pluginframework.core.command.CommandExecutor;
import io.github.llfesteal.PlaytimeTracker.domain.driven.SessionService;
import io.github.llfesteal.PlaytimeTracker.domain.model.Session;
import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.LangService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.logging.Logger;

public class PlaytimeCommand extends CommandExecutor {

    private final SessionService sessionService;
    private final LangService langService;
    private final Logger logger;

    private Session playerSession;

    public PlaytimeCommand(CommandSender issuer, Map<String, String> namedArgs, SessionService sessionService, LangService langService, Logger logger) {
        super(issuer, namedArgs);
        this.sessionService = sessionService;
        this.langService = langService;
        this.logger = logger;
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
        var playtime = this.playerSession.getDuration(true);

        var formattedCurrentPlaytime = playtime.format("%d days, %02d hours, %02d minutes, %02d seconds");

        // TODO : Get total playtime.
        this.getIssuer().sendMessage(this.langService.getPlaytimeMessages(formattedCurrentPlaytime, "NOT_YET_IMPLEMENTED").toArray(new String[0]));

        return true;
    }
}
