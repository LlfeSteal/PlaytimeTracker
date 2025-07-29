package io.github.llfesteal.PlaytimeTracker.Application.command;

import fr.lifesteal.pluginframework.core.command.CommandExecutor;
import io.github.llfesteal.PlaytimeTracker.domain.driven.SessionService;
import io.github.llfesteal.PlaytimeTracker.domain.model.Session;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class PlaytimeCommand extends CommandExecutor {

    private final SessionService sessionService;
    private Session playerSession;

    public PlaytimeCommand(CommandSender issuer, Map<String, String> namedArgs, SessionService sessionService) {
        super(issuer, namedArgs);
        this.sessionService = sessionService;
    }

    @Override
    public boolean prepare() {

        if (!(this.getIssuer() instanceof Player player)) {
            // TODO : Send message that only players can use this command.
            return false;
        }

        this.playerSession = this.sessionService.getSessionByPlayerId(player.getUniqueId());

        if (this.playerSession == null) {
            // TODO: Send error that player has no session alive.
            // TODO : Log error.
        }

        return true;
    }

    @Override
    public boolean execute() {
        var playtime = this.playerSession.getDuration(true);

        // TODO : Send real message.
        this.getIssuer().sendMessage(playtime.format("Votre temps de jeu sur cette session : %d days, %02d hours, %02d minutes, %02d seconds"));
        // TODO : Ajouter le temps de jeu total.

        return true;
    }
}
