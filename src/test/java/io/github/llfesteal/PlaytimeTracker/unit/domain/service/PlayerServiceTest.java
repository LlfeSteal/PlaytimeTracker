package io.github.llfesteal.PlaytimeTracker.unit.domain.service;

import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerPlaytimeService;
import io.github.llfesteal.PlaytimeTracker.domain.driven.SessionService;
import io.github.llfesteal.PlaytimeTracker.domain.model.Player;
import io.github.llfesteal.PlaytimeTracker.domain.service.PlayerServiceImp;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

public final class PlayerServiceTest {

    @Test
    void onPlayerJoin_ShouldLoadPlayerDataAndCreateNewSession() {
        // Arrange
        var playerId = UUID.randomUUID();
        var player = new Player(playerId);

        var sessionService = mock(SessionService.class);
        var playerPlaytimeService = mock(PlayerPlaytimeService.class);
        var playerService = new PlayerServiceImp(sessionService, playerPlaytimeService);

        // Act
        playerService.onPlayerJoin(player);

        // Assert
        verify(playerPlaytimeService, times(1)).loadPlayerData(playerId);
        verify(sessionService, times(1)).createNewSession(playerId);

        verifyNoMoreInteractions(playerPlaytimeService, sessionService);
    }

    @Test
    void onPlayerQuit_ShouldUnloadPlayerDataAndEndSession() {
        // Arrange
        var playerId = UUID.randomUUID();
        var player = new Player(playerId);

        var sessionService = mock(SessionService.class);
        var playerPlaytimeService = mock(PlayerPlaytimeService.class);
        var playerService = new PlayerServiceImp(sessionService, playerPlaytimeService);

        // Act
        playerService.onPlayerQuit(player);

        // Assert
        verify(playerPlaytimeService, times(1)).unloadPlayerData(playerId);
        verify(sessionService, times(1)).endSession(playerId);

        verifyNoMoreInteractions(playerPlaytimeService, sessionService);
    }
}
