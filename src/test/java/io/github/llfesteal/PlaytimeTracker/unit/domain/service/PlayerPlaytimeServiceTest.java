package io.github.llfesteal.PlaytimeTracker.unit.domain.service;

import io.github.llfesteal.PlaytimeTracker.domain.driving.PlayerPlaytimeStorage;
import io.github.llfesteal.PlaytimeTracker.domain.driving.SessionStorage;
import io.github.llfesteal.PlaytimeTracker.domain.model.PlayerPlaytime;
import io.github.llfesteal.PlaytimeTracker.domain.model.Session;
import io.github.llfesteal.PlaytimeTracker.domain.service.PlayerPlaytimeServiceImp;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public final class PlayerPlaytimeServiceTest {

    @Test
    public void loadPlayerShouldGetAndStorePlayerPlaytime() {
        // Arrange
        var playerId = UUID.randomUUID();

        var playerSessions = new ArrayList<Session>() {{
            add(new Session(playerId, LocalDateTime.now(), LocalDateTime.now()));
            add(new Session(playerId, LocalDateTime.now(), LocalDateTime.now()));
        }};
        var sessionStorage = Mockito.mock(SessionStorage.class);
        when(sessionStorage.getAllPlayerSessions(playerId)).thenReturn(playerSessions);

        var playerPlaytimeStorage = Mockito.mock(PlayerPlaytimeStorage.class);

        var service = new PlayerPlaytimeServiceImp(sessionStorage, playerPlaytimeStorage);

        // Act
        service.loadPlayerData(playerId);

        // Assert
        verify(sessionStorage, times(1)).getAllPlayerSessions(playerId);
        verifyNoMoreInteractions(sessionStorage);

        var expectedPlayerPlaytime = new PlayerPlaytime(playerId, playerSessions);
        verify(playerPlaytimeStorage, times(1)).store(refEq(expectedPlayerPlaytime));
        verifyNoMoreInteractions(playerPlaytimeStorage);
    }

    @Test
    public void unloadPlayerShouldUnstorePlayerPlaytime() {
        // Arrange
        var playerId = UUID.randomUUID();

        var sessionStorage = Mockito.mock(SessionStorage.class);
        var playerPlaytimeStorage = Mockito.mock(PlayerPlaytimeStorage.class);

        var service = new PlayerPlaytimeServiceImp(sessionStorage, playerPlaytimeStorage);

        // Act
        service.unloadPlayerData(playerId);

        // Assert
        verify(playerPlaytimeStorage, times(1)).unstore(playerId);
        verifyNoInteractions(sessionStorage);
    }

    @Test
    public void getTotalPlayerPlaytimeShouldReturnTotalPlaytimeIncludingCurrentSession() {
        // Arrange
        var playerId = UUID.randomUUID();

        var sessions = new ArrayList<Session>() {{
            add(new Session(playerId, LocalDateTime.parse("2025-08-01T23:30:00"), LocalDateTime.parse("2025-08-02T00:30:00"))); // 1 hour
            add(new Session(playerId, LocalDateTime.parse("2025-08-02T06:00:00"), LocalDateTime.parse("2025-08-02T08:30:00"))); // 2.5 hours
            add(new Session(playerId, LocalDateTime.parse("2025-08-02T23:30:00"), LocalDateTime.parse("2025-08-03T00:30:00"))); // 1 hour
        }};

        var currentSession = new Session(playerId, LocalDateTime.parse("2025-08-02T13:00:00"), LocalDateTime.parse("2025-08-02T13:00:00"), true); // 1.5 hours

        var playerPlaytime = new PlayerPlaytime(playerId, sessions);

        var currentTime = LocalDateTime.parse("2025-08-02T14:30:00");

        try (var mockedStatic = mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(currentTime);

            var sessionStorage = mock(SessionStorage.class);
            when(sessionStorage.getActiveSessionByPlayerId(playerId)).thenReturn(currentSession);

            var playerPlaytimeStorage = mock(PlayerPlaytimeStorage.class);
            when(playerPlaytimeStorage.getPlaytime(playerId)).thenReturn(playerPlaytime);

            var service = new PlayerPlaytimeServiceImp(sessionStorage, playerPlaytimeStorage);

            // Act
            var result = service.getTotalPlayerPlaytime(playerId);

            // Assert
            assertThat(result).isEqualTo(Duration.ofHours(6L));

            verify(playerPlaytimeStorage, times(1)).getPlaytime(playerId);
            verifyNoMoreInteractions(playerPlaytimeStorage);

            verify(sessionStorage, times(1)).getActiveSessionByPlayerId(playerId);
            verifyNoMoreInteractions(sessionStorage);
        }
    }

    @Test
    public void getPlayerPlaytimeWithOverlappingSessionsShouldReturnOnlyScopedPlaytime() {
        // Arrange
        var playerId = UUID.randomUUID();
        var startDate = LocalDateTime.parse("2025-08-02T00:00:00");
        var endDate = LocalDateTime.parse("2025-08-03T00:00:00");

        var sessions = new ArrayList<Session>() {{
            add(new Session(playerId, LocalDateTime.parse("2025-08-01T23:30:00"), LocalDateTime.parse("2025-08-02T00:30:00"))); // 1 hour but only 30 minutes in scope
            add(new Session(playerId, LocalDateTime.parse("2025-08-02T06:00:00"), LocalDateTime.parse("2025-08-02T08:30:00"))); // 2.5 hours
            add(new Session(playerId, LocalDateTime.parse("2025-08-02T23:30:00"), LocalDateTime.parse("2025-08-03T00:30:00"))); // 1 hour but only 30 minutes in scope
        }};

        var currentSession = new Session(playerId, LocalDateTime.parse("2025-08-02T13:00:00"), null, true); // 1.5 hours

        var playerPlaytime = new PlayerPlaytime(playerId, sessions);

        var currentTime = LocalDateTime.parse("2025-08-02T14:30:00");

        try (var mockedStatic = mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(currentTime);

            var sessionStorage = mock(SessionStorage.class);
            when(sessionStorage.getActiveSessionByPlayerId(playerId)).thenReturn(currentSession);

            var playerPlaytimeStorage = mock(PlayerPlaytimeStorage.class);
            when(playerPlaytimeStorage.getPlaytime(playerId)).thenReturn(playerPlaytime);

            var service = new PlayerPlaytimeServiceImp(sessionStorage, playerPlaytimeStorage);

            // Act
            var result = service.getPlayerPlaytime(playerId, startDate, endDate);

            // Assert
            assertThat(result).isEqualTo(Duration.ofHours(5L));

            verify(playerPlaytimeStorage, times(1)).getPlaytime(playerId);
            verifyNoMoreInteractions(playerPlaytimeStorage);

            verify(sessionStorage, times(1)).getActiveSessionByPlayerId(playerId);
            verifyNoMoreInteractions(sessionStorage);
        }
    }

    @Test
    public void getPlayerPlaytimeWithOverlappingCurrentSessionsShouldReturnOnlyScopedPlaytime() {
        // Arrange
        var playerId = UUID.randomUUID();
        var startDate = LocalDateTime.parse("2025-08-02T00:00:00");
        var endDate = LocalDateTime.parse("2025-08-03T00:00:00");

        var sessions = new ArrayList<Session>() {{
            add(new Session(playerId, LocalDateTime.parse("2025-08-01T23:30:00"), LocalDateTime.parse("2025-08-02T00:30:00"))); // 1 hour but only 30 minutes in scope
            add(new Session(playerId, LocalDateTime.parse("2025-08-02T06:00:00"), LocalDateTime.parse("2025-08-02T08:30:00"))); // 2.5 hours
            add(new Session(playerId, LocalDateTime.parse("2025-08-02T15:00:00"), LocalDateTime.parse("2025-08-02T15:30:00"))); // 30 minutes
        }};

        var currentSession = new Session(playerId, LocalDateTime.parse("2025-08-02T23:30:00"), null, true); // 13.5 hours but only 30 minutes in scope

        var playerPlaytime = new PlayerPlaytime(playerId, sessions);

        var currentTime = LocalDateTime.parse("2025-08-03T13:00:00");

        try (var mockedStatic = mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(currentTime);

            var sessionStorage = mock(SessionStorage.class);
            when(sessionStorage.getActiveSessionByPlayerId(playerId)).thenReturn(currentSession);

            var playerPlaytimeStorage = mock(PlayerPlaytimeStorage.class);
            when(playerPlaytimeStorage.getPlaytime(playerId)).thenReturn(playerPlaytime);

            var service = new PlayerPlaytimeServiceImp(sessionStorage, playerPlaytimeStorage);

            // Act
            var result = service.getPlayerPlaytime(playerId, startDate, endDate);

            // Assert
            assertThat(result).isEqualTo(Duration.ofHours(4L));

            verify(playerPlaytimeStorage, times(1)).getPlaytime(playerId);
            verifyNoMoreInteractions(playerPlaytimeStorage);

            verify(sessionStorage, times(1)).getActiveSessionByPlayerId(playerId);
            verifyNoMoreInteractions(sessionStorage);
        }
    }

    @Test
    public void getTotalPlayerPlaytimeWithNoPlaytimeStoredAndNoCurrentSessionShouldReturnZero() {
        // Arrange
        var playerId = UUID.randomUUID();

        var sessionStorage = Mockito.mock(SessionStorage.class);
        when(sessionStorage.getActiveSessionByPlayerId(playerId)).thenReturn(null);
        when(sessionStorage.getAllPlayerSessions(playerId)).thenReturn(Collections.emptyList());

        var playerPlaytimeStorage = Mockito.mock(PlayerPlaytimeStorage.class);
        when(playerPlaytimeStorage.getPlaytime(playerId)).thenReturn(null);

        var service = new PlayerPlaytimeServiceImp(sessionStorage, playerPlaytimeStorage);

        // Act
        var result = service.getTotalPlayerPlaytime(playerId);

        // Assert
        assertThat(result).isEqualTo(Duration.ZERO);

        verify(playerPlaytimeStorage, times(1)).getPlaytime(playerId);
        verifyNoMoreInteractions(playerPlaytimeStorage);

        verify(sessionStorage, times(1)).getActiveSessionByPlayerId(playerId);
        verify(sessionStorage, times(1)).getAllPlayerSessions(playerId);
        verifyNoMoreInteractions(sessionStorage);
    }

    @Test
    public void getPlayerPlaytimeWithNoCurrentSessionShouldReturnPlayerPlaytime() {
        // Arrange
        var playerId = UUID.randomUUID();
        var startDate = LocalDateTime.parse("2025-08-02T00:00:00");
        var endDate = LocalDateTime.parse("2025-08-03T00:00:00");

        var sessions = new ArrayList<Session>() {{
            add(new Session(playerId, LocalDateTime.parse("2025-07-30T15:00:00"), LocalDateTime.parse("2025-07-30T15:30:00"))); // 30 minutes out of the scope
            add(new Session(playerId, LocalDateTime.parse("2025-08-01T23:30:00"), LocalDateTime.parse("2025-08-02T00:30:00"))); // 1 hour but only 30 minutes in scope
            add(new Session(playerId, LocalDateTime.parse("2025-08-02T06:00:00"), LocalDateTime.parse("2025-08-02T08:00:00"))); // 2 hours
            add(new Session(playerId, LocalDateTime.parse("2025-08-02T15:00:00"), LocalDateTime.parse("2025-08-02T15:30:00"))); // 30 minutes
        }};

        var playerPlaytime = new PlayerPlaytime(playerId, sessions);

        var sessionStorage = mock(SessionStorage.class);
        when(sessionStorage.getActiveSessionByPlayerId(playerId)).thenReturn(null);

        var playerPlaytimeStorage = mock(PlayerPlaytimeStorage.class);
        when(playerPlaytimeStorage.getPlaytime(playerId)).thenReturn(playerPlaytime);

        var service = new PlayerPlaytimeServiceImp(sessionStorage, playerPlaytimeStorage);

        // Act
        var result = service.getPlayerPlaytime(playerId, startDate, endDate);

        // Assert
        assertThat(result).isEqualTo(Duration.ofHours(3L));

        verify(playerPlaytimeStorage, times(1)).getPlaytime(playerId);
        verifyNoMoreInteractions();

        verify(sessionStorage, times(1)).getActiveSessionByPlayerId(playerId);
        verifyNoMoreInteractions(sessionStorage);
    }
}
