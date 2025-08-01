package io.github.llfesteal.PlaytimeTracker;

import fr.lifesteal.pluginframework.api.config.ConfigService;
import fr.lifesteal.pluginframework.core.plugin.PluginBase;
import io.github.llfesteal.PlaytimeTracker.application.api.PlaytimeTrackerAPI;
import io.github.llfesteal.PlaytimeTracker.application.api.PlaytimeTrackerAPIImp;
import io.github.llfesteal.PlaytimeTracker.application.command.PlaytimeCommand;
import io.github.llfesteal.PlaytimeTracker.application.command.PlaytimeLookupCommand;
import io.github.llfesteal.PlaytimeTracker.application.listener.PlayerListener;
import io.github.llfesteal.PlaytimeTracker.application.placeholder.CurrentPlaytimePlaceholder;
import io.github.llfesteal.PlaytimeTracker.application.placeholder.TodayPlaytimePlaceholder;
import io.github.llfesteal.PlaytimeTracker.application.placeholder.TotalPlaytimePlaceholder;
import io.github.llfesteal.PlaytimeTracker.application.task.BackupSessionsTask;
import io.github.llfesteal.PlaytimeTracker.domain.driven.PlayerService;
import io.github.llfesteal.PlaytimeTracker.domain.driven.SessionService;
import io.github.llfesteal.PlaytimeTracker.domain.driving.PlayerPlaytimeStorage;
import io.github.llfesteal.PlaytimeTracker.domain.service.PlayerDataServiceImp;
import io.github.llfesteal.PlaytimeTracker.domain.service.PlayerServiceImp;
import io.github.llfesteal.PlaytimeTracker.domain.service.SessionServiceImp;
import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.ConfigurationServiceImp;
import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.LangServiceImp;
import io.github.llfesteal.PlaytimeTracker.infrastructure.configuration.model.DatabaseConfig;
import io.github.llfesteal.PlaytimeTracker.infrastructure.schedule.BukkitSchedulerWrapper;
import io.github.llfesteal.PlaytimeTracker.infrastructure.schedule.SchedulerService;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.SessionStorageImp;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.cache.player.PlayerPlaytimeManagerImp;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.cache.session.SessionManager;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.cache.session.SessionManagerImp;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.database.ConnectionFactoryImp;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.database.repository.SessionRepository;
import io.github.llfesteal.PlaytimeTracker.infrastructure.storage.database.repository.SessionRepositoryImp;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PlaytimeTracker extends PluginBase {

    static {
        ConfigurationSerialization.registerClass(DatabaseConfig.class, "DatabaseConfig");
    }

    private final SchedulerService schedulerService = new BukkitSchedulerWrapper(this);
    private final List<BukkitTask> tasks = new ArrayList<>();

    private final SessionManager sessionManager = new SessionManagerImp();
    private final PlayerPlaytimeStorage playerPlaytimeStorage = new PlayerPlaytimeManagerImp();
    private SessionRepository sessionRepository;
    private final Logger logger = getLogger();
    private SessionService sessionService;
    private PlayerService playerService;
    private LangServiceImp langService;
    private ConfigurationServiceImp configurationService;
    private PlayerDataServiceImp playerDataService;
    private PlaytimeTrackerAPI api;

    @Override
    public void init() {
        var langConfigurationRepository = getConfigRepositoryFactory().getNewYamlRepository("", "lang.yml");
        this.langService = new LangServiceImp(this.logger, langConfigurationRepository);
        var configurationRepository = getConfigRepositoryFactory().getNewYamlRepository("", "config.yml");
        this.configurationService = new ConfigurationServiceImp(this.logger, configurationRepository);
        var connectionFactory = new ConnectionFactoryImp(this.logger, this.configurationService);
        this.sessionRepository = new SessionRepositoryImp(this.logger, connectionFactory, this.configurationService);
        var sessionStorage = new SessionStorageImp(this.sessionManager, sessionRepository);
        this.sessionService = new SessionServiceImp(sessionStorage);
        this.playerDataService = new PlayerDataServiceImp(sessionStorage, playerPlaytimeStorage);
        this.playerService = new PlayerServiceImp(this.sessionService, playerDataService);
        this.api = new PlaytimeTrackerAPIImp(this.sessionService, this.playerDataService);
    }

    @Override
    public void postInit() {
        sessionRepository.init();
        initSchedulers();
        initPlaceholders();
    }

    public PlaytimeTrackerAPI getApi() {
        return this.api;
    }

    private void initSchedulers() {
        for (BukkitTask task : this.tasks) {
            task.cancel();
        }
        this.tasks.clear();

        tasks.add(this.schedulerService.runTaskTimerAsynchronously(new BackupSessionsTask(this.sessionService), this.configurationService.getAutosaveDelay(), this.configurationService.getAutosaveDelay()));
    }

    private void initPlaceholders() {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new CurrentPlaytimePlaceholder(this, this.sessionService, this.configurationService).register();
            new TotalPlaytimePlaceholder(this, this.sessionService, this.playerDataService, this.configurationService).register();
            new TodayPlaytimePlaceholder(this, this.playerDataService, this.configurationService).register();
        }
    }

    @Override
    public void onDisable() {
        this.sessionService.forceSaveSessions();
        super.onDisable();
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
                            .addExtraArgument(langService)
                            .addExtraArgument(logger)
                            .addExtraArgument(playerDataService)
                            .addExtraArgument(configurationService)
                            .build())
                    .addSubCommands(getCommandBaseBuilder()
                            .setName("lookup")
                            .setPermission("playtime.lookup")
                            .setUsage("playtime lookup [player] <start_date> <end_date>")
                            .setExecutorType(PlaytimeLookupCommand.class)
                            .addExtraArgument(playerDataService)
                            .addExtraArgument(configurationService)
                            .addExtraArgument(langService)
                            .build())
                    .build());
        }};
    }

    @Override
    protected List<ConfigService> registerConfigurationsServices() {
        return new ArrayList<>() {{
            add(configurationService);
            add(langService);
        }};
    }

    @Override
    protected List<Listener> registerListeners() {
        return new ArrayList<>() {{
            add(new PlayerListener(playerService));
        }};
    }
}
