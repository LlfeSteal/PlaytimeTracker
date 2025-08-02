package io.github.llfesteal.PlaytimeTracker.unit.application.api;

import io.github.llfesteal.PlaytimeTracker.application.api.PlaytimeTrackerAPIImp;
import io.github.llfesteal.PlaytimeTracker.application.api.exception.NoSessionFoundException;
import io.github.llfesteal.PlaytimeTracker.application.api.exception.SessionAlreadyActiveException;
import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerPlaytimeService;
import io.github.llfesteal.PlaytimeTracker.domain.driven.SessionService;
import io.github.llfesteal.PlaytimeTracker.domain.model.Session;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

public final class PlaytimeTrackerAPITest {

    @Test
    public void startNewSessionShouldThrowSessionAlreadyActiveException() {
        // Arrange
        var playerId = UUID.randomUUID();

        var session = new Session(playerId, LocalDateTime.now().minusHours(1), LocalDateTime.now());
        var sessionService = Mockito.mock(SessionService.class);
        when(sessionService.getSessionByPlayerId(playerId)).thenReturn(session);

        var playerPlaytimeService = Mockito.mock(PlayerPlaytimeService.class);
        var playtimeTrackerAPI = new PlaytimeTrackerAPIImp(sessionService, playerPlaytimeService);

        // Act
        var thrown = catchThrowable(() -> playtimeTrackerAPI.startNewSession(playerId));

        // Assert
        assertThat(thrown)
                .isInstanceOf(SessionAlreadyActiveException.class)
                .hasMessage("Session of player " + playerId + " is already active.");

        verify(sessionService, times(1)).getSessionByPlayerId(playerId);
        verifyNoMoreInteractions(sessionService);

        verifyNoInteractions(playerPlaytimeService);
    }

    @Test
    public void startNewSessionShouldStartNewSession() {
        // Arrange
        var playerId = UUID.randomUUID();

        var sessionService = Mockito.mock(SessionService.class);
        when(sessionService.getSessionByPlayerId(playerId)).thenReturn(null);

        var playerPlaytimeService = Mockito.mock(PlayerPlaytimeService.class);
        var playtimeTrackerAPI = new PlaytimeTrackerAPIImp(sessionService, playerPlaytimeService);

        // Act
        var thrown = catchThrowable(() -> playtimeTrackerAPI.startNewSession(playerId));

        // Assert
        assertThat(thrown).isNull();
        verify(sessionService, times(1)).getSessionByPlayerId(playerId);
        verify(sessionService, times(1)).createNewSession(playerId);
        verifyNoMoreInteractions(sessionService);

        verifyNoInteractions(playerPlaytimeService);
    }

    @Test
    public void endSessionShouldThrowSessionNotFoundException() {
        // Arrange
        var playerId = UUID.randomUUID();

        var sessionService = Mockito.mock(SessionService.class);
        when(sessionService.getSessionByPlayerId(playerId)).thenReturn(null);

        var playerPlaytimeService = Mockito.mock(PlayerPlaytimeService.class);
        var playtimeTrackerAPI = new PlaytimeTrackerAPIImp(sessionService, playerPlaytimeService);

        // Act
        var thrown = catchThrowable(() -> playtimeTrackerAPI.endSession(playerId));

        // Assert
        assertThat(thrown)
                .isInstanceOf(NoSessionFoundException.class)
                .hasMessage("No session found for player " + playerId);

        verify(sessionService, times(1)).getSessionByPlayerId(playerId);
        verifyNoMoreInteractions(sessionService);

        verifyNoInteractions(playerPlaytimeService);
    }

    @Test
    public void endSessionShouldEndSession() {
        // Arrange
        var playerId = UUID.randomUUID();

        var session = new Session(playerId, LocalDateTime.now().minusHours(1), LocalDateTime.now());
        var sessionService = Mockito.mock(SessionService.class);
        when(sessionService.getSessionByPlayerId(playerId)).thenReturn(session);

        var playerPlaytimeService = Mockito.mock(PlayerPlaytimeService.class);
        var playtimeTrackerAPI = new PlaytimeTrackerAPIImp(sessionService, playerPlaytimeService);

        // Act
        var thrown = catchThrowable(() -> playtimeTrackerAPI.endSession(playerId));

        // Assert
        assertThat(thrown).isNull();
        verify(sessionService, times(1)).getSessionByPlayerId(playerId);
        verify(sessionService, times(1)).endSession(playerId);
        verifyNoMoreInteractions(sessionService);

        verifyNoInteractions(playerPlaytimeService);
    }

    @Test
    public void getPlayerPlaytimeShouldReturnPlayerPlaytime() {
        // Arrange
        var playerId = UUID.randomUUID();
        var startDate = LocalDateTime.now().minusHours(1);
        var endDate = LocalDateTime.now();

        var sessionService = Mockito.mock(SessionService.class);

        var playerPlaytimeService = Mockito.mock(PlayerPlaytimeService.class);
        when(playerPlaytimeService.getPlayerPlaytime(playerId, startDate, endDate)).thenReturn(Duration.ZERO.plusDays(1));

        var playtimeTrackerAPI = new PlaytimeTrackerAPIImp(sessionService, playerPlaytimeService);

        // Act
        var playtime = playtimeTrackerAPI.getPlayerPlaytime(playerId, startDate, endDate);

        // Assert
        assertThat(playtime).isEqualTo(Duration.ofDays(1));

        verify(playerPlaytimeService, times(1)).getPlayerPlaytime(playerId, startDate, endDate);
        verifyNoMoreInteractions(playerPlaytimeService);

        verifyNoInteractions(sessionService);
    }
}
