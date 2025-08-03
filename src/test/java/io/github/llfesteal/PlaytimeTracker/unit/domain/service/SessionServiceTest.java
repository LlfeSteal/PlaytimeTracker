package io.github.llfesteal.PlaytimeTracker.unit.domain.service;

import io.github.llfesteal.PlaytimeTracker.domain.driving.SessionStorage;
import io.github.llfesteal.PlaytimeTracker.domain.model.Session;
import io.github.llfesteal.PlaytimeTracker.domain.service.SessionServiceImp;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public final class SessionServiceTest {

    @Test
    public void createNewSessionShouldAddNewSessionToStorage() {
        // Arrange
        var playerId = UUID.randomUUID();
        var currentTime = LocalDateTime.parse("2025-08-02T14:30:00");

        try (var mockedStatic = mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(currentTime);

            var sessionStorage = mock(SessionStorage.class);
            var service = new SessionServiceImp(sessionStorage);

            // Act
            service.createNewSession(playerId);

            // Assert
            var expectedSession = new Session(playerId, currentTime, currentTime, true);
            verify(sessionStorage, times(1)).add(refEq(expectedSession));
            verifyNoMoreInteractions(sessionStorage);
        }
    }

    @Test
    public void getSessionByPlayerIdShouldReturnActiveSession() {
        // Arrange
        var playerId = UUID.randomUUID();
        var expectedSession = new Session(playerId, LocalDateTime.now(), LocalDateTime.now(), true);

        var sessionStorage = mock(SessionStorage.class);
        when(sessionStorage.getActiveSessionByPlayerId(playerId)).thenReturn(expectedSession);

        var service = new SessionServiceImp(sessionStorage);

        // Act
        var result = service.getSessionByPlayerId(playerId);

        // Assert
        assertThat(result).isEqualTo(expectedSession);

        verify(sessionStorage, times(1)).getActiveSessionByPlayerId(playerId);
        verifyNoMoreInteractions(sessionStorage);
    }

    @Test
    public void endSessionShouldUpdateAndEndSession() {
        // Arrange
        var playerId = UUID.randomUUID();
        var startTime = LocalDateTime.parse("2025-08-02T13:00:00");
        var currentTime = LocalDateTime.parse("2025-08-02T14:30:00");
        var activeSession = new Session(playerId, startTime, startTime, true);

        try (var mockedStatic = mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(currentTime);

            var sessionStorage = mock(SessionStorage.class);
            when(sessionStorage.getActiveSessionByPlayerId(playerId)).thenReturn(activeSession);

            var service = new SessionServiceImp(sessionStorage);

            // Act
            service.endSession(playerId);

            // Assert
            assertThat(activeSession.getSessionEnd()).isEqualTo(currentTime);

            verify(sessionStorage, times(1)).getActiveSessionByPlayerId(playerId);
            verify(sessionStorage, times(1)).endSession(playerId);
            verifyNoMoreInteractions(sessionStorage);
        }
    }

    @Test
    public void forceSaveSessionsShouldUpdateAllActiveSessions() {
        // Arrange
        var currentTime = LocalDateTime.parse("2025-08-02T14:30:00");
        var activeSessions = new ArrayList<Session>() {{
            add(new Session(UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now(), true));
            add(new Session(UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now(), true));
        }};

        try (var mockedStatic = mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(currentTime);

            var sessionStorage = mock(SessionStorage.class);
            when(sessionStorage.getAllActiveSessions()).thenReturn(activeSessions);

            var service = new SessionServiceImp(sessionStorage);

            // Act
            service.forceSaveSessions();

            // Assert
            verify(sessionStorage, times(1)).getAllActiveSessions();
            for (var session : activeSessions) {
                verify(sessionStorage, times(1)).updateSessionEndDate(session);
                assertThat(session.getSessionEnd()).isEqualTo(currentTime);
            }
            verifyNoMoreInteractions(sessionStorage);
        }
    }

    @Test
    public void getPlayerCurrentSessionDurationShouldReturnNullWhenNoActiveSession() {
        // Arrange
        var playerId = UUID.randomUUID();
        var sessionStorage = mock(SessionStorage.class);
        when(sessionStorage.getActiveSessionByPlayerId(playerId)).thenReturn(null);

        var service = new SessionServiceImp(sessionStorage);

        // Act
        var result = service.getPlayerCurrentSessionDuration(playerId);

        // Assert
        assertThat(result).isNull();

        verify(sessionStorage, times(1)).getActiveSessionByPlayerId(playerId);
        verifyNoMoreInteractions(sessionStorage);
    }

    @Test
    public void getPlayerCurrentSessionDurationShouldReturnDurationWhenActiveSession() {
        // Arrange
        var playerId = UUID.randomUUID();
        var startTime = LocalDateTime.parse("2025-08-02T13:00:00");
        var currentTime = LocalDateTime.parse("2025-08-02T14:30:00");

        try (var mockedStatic = mockStatic(LocalDateTime.class, Mockito.CALLS_REAL_METHODS)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(currentTime);

            var activeSession = new Session(playerId, startTime, currentTime, true);
            var sessionStorage = mock(SessionStorage.class);
            when(sessionStorage.getActiveSessionByPlayerId(playerId)).thenReturn(activeSession);

            var service = new SessionServiceImp(sessionStorage);

            // Act
            var result = service.getPlayerCurrentSessionDuration(playerId);

            // Assert
            assertThat(result).isEqualTo(Duration.ofMinutes(90));

            verify(sessionStorage, times(1)).getActiveSessionByPlayerId(playerId);
            verifyNoMoreInteractions(sessionStorage);
        }
    }
}
