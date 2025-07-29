package io.github.llfesteal.PlaytimeTracker.domain.service;

import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerDataService;
import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerService;
import io.github.llfesteal.PlaytimeTracker.domain.driven.SessionService;
import io.github.llfesteal.PlaytimeTracker.domain.model.Player;

public class PlayerServiceImp implements PlayerService {

    private final SessionService sessionService;
    private final PlayerDataService playerDataService;

    public PlayerServiceImp(SessionService sessionService, PlayerDataService playerDataService) {
        this.sessionService = sessionService;
        this.playerDataService = playerDataService;
    }

    @Override
    public void onPlayerJoin(Player player) {
        this.playerDataService.loadPlayerData(player.playerId());
        this.sessionService.createNewSession(player.playerId());
    }

    @Override
    public void onPlayerQuit(Player player) {
        this.playerDataService.unloadPlayerData(player.playerId());
        this.sessionService.endSession(player.playerId());
    }
}
