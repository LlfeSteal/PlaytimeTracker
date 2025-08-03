package io.github.llfesteal.PlaytimeTracker.unit.application.command;

import io.github.llfesteal.PlaytimeTracker.application.command.PlaytimeCommand;
import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerPlaytimeService;
import io.github.llfesteal.PlaytimeTracker.domain.driven.SessionService;
import io.github.llfesteal.PlaytimeTracker.domain.model.Session;
import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.ConfigurationService;
import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.LangService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public final class PlaytimeCommandTest {

    @Test
    public void prepareWithoutPlayerIssuerShouldReturnFalseAndSendErrorMessage() {
        // Arrange
        var issuer = Mockito.mock(CommandSender.class);
        var args = new HashMap<String, String>();
        var sessionService = Mockito.mock(SessionService.class);
        var langService = Mockito.mock(LangService.class);
        when(langService.getErrorOnlyPlayersAllowedMessage()).thenReturn("ErrorOnlyPlayersAllowedMessage");

        var logger = Mockito.mock(Logger.class);
        var playerPlaytimeService = Mockito.mock(PlayerPlaytimeService.class);
        var configurationService = Mockito.mock(ConfigurationService.class);

        var command = new PlaytimeCommand(issuer, args, sessionService, langService, logger, playerPlaytimeService, configurationService);

        // Act
        boolean result = command.prepare();

        // Assert
        assertThat(result).isFalse();

        verify(issuer, times(1)).sendMessage("ErrorOnlyPlayersAllowedMessage");
        verifyNoMoreInteractions(issuer);

        verify(langService, times(1)).getErrorOnlyPlayersAllowedMessage();
        verifyNoMoreInteractions(langService);

        verifyNoInteractions(logger);
        verifyNoInteractions(sessionService);
        verifyNoInteractions(playerPlaytimeService);
        verifyNoInteractions(configurationService);
    }

    @Test
    public void prepareWithoutActiveSessionShouldReturnFalseSendErrorMessageAndLogError() {
        // Arrange
        var playerId = UUID.randomUUID();

        var issuer = Mockito.mock(Player.class);
        when(issuer.getUniqueId()).thenReturn(playerId);

        var args = new HashMap<String, String>();

        var sessionService = Mockito.mock(SessionService.class);
        when(sessionService.getSessionByPlayerId(playerId)).thenReturn(null);

        var langService = Mockito.mock(LangService.class);
        when(langService.getErrorNoSessionAlive()).thenReturn("No Active Session");

        var logger = Mockito.mock(Logger.class);
        var playerPlaytimeService = Mockito.mock(PlayerPlaytimeService.class);
        var configurationService = Mockito.mock(ConfigurationService.class);

        var command = new PlaytimeCommand(issuer, args, sessionService, langService, logger, playerPlaytimeService, configurationService);

        // Act
        boolean result = command.prepare();

        // Assert
        assertThat(result).isFalse();

        verify(issuer, times(1)).sendMessage("No Active Session");
        verify(issuer, times(2)).getUniqueId();
        verifyNoMoreInteractions(issuer);

        verify(langService, times(1)).getErrorNoSessionAlive();
        verifyNoMoreInteractions(langService);

        verify(logger, times(1)).severe("The player " + playerId + " has no session active !");
        verifyNoMoreInteractions(logger);

        verify(sessionService, times(1)).getSessionByPlayerId(playerId);
        verifyNoMoreInteractions(sessionService);


        verifyNoInteractions(playerPlaytimeService);
        verifyNoInteractions(configurationService);
    }

    @Test
    public void prepareShouldReturnTrue() {
        // Arrange
        var playerId = UUID.randomUUID();

        var issuer = Mockito.mock(Player.class);
        when(issuer.getUniqueId()).thenReturn(playerId);

        var args = new HashMap<String, String>();

        var session = new Session(playerId, LocalDateTime.now().minusHours(1), LocalDateTime.now());
        var sessionService = Mockito.mock(SessionService.class);
        when(sessionService.getSessionByPlayerId(playerId)).thenReturn(session);

        var langService = Mockito.mock(LangService.class);
        var logger = Mockito.mock(Logger.class);
        var playerPlaytimeService = Mockito.mock(PlayerPlaytimeService.class);
        var configurationService = Mockito.mock(ConfigurationService.class);

        var command = new PlaytimeCommand(issuer, args, sessionService, langService, logger, playerPlaytimeService, configurationService);

        // Act
        boolean result = command.prepare();

        // Assert
        assertThat(result).isTrue();

        verify(issuer, times(1)).getUniqueId();
        verifyNoMoreInteractions(issuer);

        verify(sessionService, times(1)).getSessionByPlayerId(playerId);
        verifyNoMoreInteractions(sessionService);

        verifyNoInteractions(langService);
        verifyNoInteractions(logger);
        verifyNoInteractions(playerPlaytimeService);
        verifyNoInteractions(configurationService);
    }

    @Test
    public void executeShouldReturnTrueAndSendInfoMessage() {
        // Arrange
        var playerId = UUID.randomUUID();

        var issuer = Mockito.mock(Player.class);
        when(issuer.getUniqueId()).thenReturn(playerId);

        var args = new HashMap<String, String>();

        var session = new Session(playerId, LocalDateTime.now().minusHours(1), LocalDateTime.now());
        var sessionService = Mockito.mock(SessionService.class);
        when(sessionService.getSessionByPlayerId(playerId)).thenReturn(session);
        when(sessionService.getPlayerCurrentSessionDuration(playerId)).thenReturn(Duration.ofSeconds(93784));

        var langService = Mockito.mock(LangService.class);
        when(langService
                .getPlaytimeMessages("1 days, 02 hours, 03 minutes, 04 seconds", "2 days, 04 hours, 06 minutes, 08 seconds"))
                .thenReturn(List.of("info message"));

        var logger = Mockito.mock(Logger.class);

        var playerPlaytimeService = Mockito.mock(PlayerPlaytimeService.class);
        when(playerPlaytimeService.getTotalPlayerPlaytime(playerId)).thenReturn(Duration.ofSeconds(187568));

        var configurationService = Mockito.mock(ConfigurationService.class);
        when(configurationService.getDurationFormat()).thenReturn("%d days, %02d hours, %02d minutes, %02d seconds");

        var command = new PlaytimeCommand(issuer, args, sessionService, langService, logger, playerPlaytimeService, configurationService);
        command.prepare();

        // Act
        boolean result = command.execute();

        // Assert
        assertThat(result).isTrue();

        verify(issuer, times(1)).getUniqueId();
        verify(issuer, times(1)).sendMessage(new String[]{"info message"});
        verifyNoMoreInteractions(issuer);

        verify(sessionService, times(1)).getSessionByPlayerId(playerId);
        verify(sessionService, times(1)).getPlayerCurrentSessionDuration(playerId);
        verifyNoMoreInteractions(sessionService);

        verify(langService, times(1)).getPlaytimeMessages("1 days, 02 hours, 03 minutes, 04 seconds", "2 days, 04 hours, 06 minutes, 08 seconds");
        verifyNoMoreInteractions(langService);

        verify(configurationService, times(2)).getDurationFormat();
        verifyNoMoreInteractions(configurationService);

        verify(playerPlaytimeService, times(1)).getTotalPlayerPlaytime(playerId);
        verifyNoMoreInteractions(playerPlaytimeService);

        verifyNoInteractions(logger);
    }
}
