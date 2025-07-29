package io.github.llfesteal.PlaytimeTracker.domain.driven;

import io.github.llfesteal.PlaytimeTracker.domain.model.Player;

public interface PlayerService {
    void onPlayerJoin(Player player);

    void onPlayerQuit(Player player);
}
