package io.github.llfesteal.PlaytimeTracker.domain.service;

import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerPlaytimeService;
import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerService;
import io.github.llfesteal.PlaytimeTracker.domain.driven.SessionService;
import io.github.llfesteal.PlaytimeTracker.domain.model.Player;

public class PlayerServiceImp implements PlayerService {

    private final SessionService sessionService;
    private final PlayerPlaytimeService playerPlaytimeService;

    public PlayerServiceImp(SessionService sessionService, PlayerPlaytimeService playerPlaytimeService) {
        this.sessionService = sessionService;
        this.playerPlaytimeService = playerPlaytimeService;
    }

    @Override
    public void onPlayerJoin(Player player) {
        this.playerPlaytimeService.loadPlayerData(player.playerId());
        this.sessionService.createNewSession(player.playerId());
    }

    @Override
    public void onPlayerQuit(Player player) {
        this.playerPlaytimeService.unloadPlayerData(player.playerId());
        this.sessionService.endSession(player.playerId());
    }
}
