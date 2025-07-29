package io.github.llfesteal.PlaytimeTracker.domain.service;

import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerService;
import io.github.llfesteal.PlaytimeTracker.domain.driven.SessionService;
import io.github.llfesteal.PlaytimeTracker.domain.model.Player;

public class PlayerServiceImp implements PlayerService {

    private final SessionService sessionService;

    public PlayerServiceImp(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void onPlayerJoin(Player player) {
        this.sessionService.createNewSession(player.playerId());
    }

    @Override
    public void onPlayerQuit(Player player) {
        this.sessionService.endSession(player.playerId());
    }
}
