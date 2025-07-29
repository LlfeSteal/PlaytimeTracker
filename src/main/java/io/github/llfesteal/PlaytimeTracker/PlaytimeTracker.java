package io.github.llfesteal.PlaytimeTracker;

import fr.lifesteal.pluginframework.api.config.ConfigService;
import fr.lifesteal.pluginframework.core.plugin.PluginBase;
import io.github.llfesteal.PlaytimeTracker.Application.command.PlaytimeCommand;
import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerService;
import io.github.llfesteal.PlaytimeTracker.domain.driven.SessionService;
import io.github.llfesteal.PlaytimeTracker.domain.service.PlayerServiceImp;
import io.github.llfesteal.PlaytimeTracker.domain.service.SessionServiceImp;
import io.github.llfesteal.PlaytimeTracker.Application.listener.PlayerListener;
import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.ConfigurationServiceImp;
import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.model.DatabaseConfig;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.ActiveSessionStorageImp;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.cache.session.SessionManagerImp;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.database.ConnectionFactoryImp;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.database.repository.SessionRepositoryImp;
import org.bukkit.command.Command;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class PlaytimeTracker extends PluginBase {

    static {
        ConfigurationSerialization.registerClass(DatabaseConfig.class, "DatabaseConfig");
    }

    private SessionService sessionService;
    private PlayerService playerService;
    private ConfigurationServiceImp configurationService;

    @Override
    public void init() {
        var sessionManager = new SessionManagerImp();

        var configurationRepository = getConfigRepositoryFactory().getNewYamlRepository("", "config.yml");
        this.configurationService = new ConfigurationServiceImp(getLogger(), configurationRepository);
        var connectionFactory = new ConnectionFactoryImp(getLogger(), this.configurationService);
        var sessionRepository = new SessionRepositoryImp(getLogger(), connectionFactory, this.configurationService);
        sessionRepository.init();
        var sessionStorage = new ActiveSessionStorageImp(sessionManager, sessionRepository);

        this.sessionService = new SessionServiceImp(sessionStorage);
        this.playerService = new PlayerServiceImp(this.sessionService);
    }

    @Override
    protected List<Command> registerCommands() {
        return new ArrayList<>() {{
            add(getPluginCommandFactory()
                    .setName("playtime")
                    .setDescription("Playtime")
                    .addAlias("pt")
                    .setDefaultCommand(getCommandBaseBuilder()
                            .setPermission("playtime.playtime")
                            .setExecutorType(PlaytimeCommand.class)
                            .addExtraArgument(sessionService)
                            .build())
                    .build());
        }};
    }

    @Override
    protected List<ConfigService> registerConfigurationsServices() {
        return new ArrayList<>(){{
            add(configurationService);
        }};
    }

    @Override
    protected List<Listener> registerListeners() {
        return new ArrayList<>() {{
            add(new PlayerListener(playerService));
        }};
    }
}
