package io.github.llfesteal.PlaytimeTracker.application.listener;

import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerService;
import io.github.llfesteal.PlaytimeTracker.domain.model.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final PlayerService playerService;

    public PlayerListener(PlayerService playerService) {
        this.playerService = playerService;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.playerService.onPlayerJoin(new Player(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.playerService.onPlayerQuit(new Player(event.getPlayer().getUniqueId()));
    }
}
