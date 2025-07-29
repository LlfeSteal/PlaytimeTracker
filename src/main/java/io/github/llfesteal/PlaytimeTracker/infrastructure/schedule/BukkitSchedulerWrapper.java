package io.github.llfesteal.PlaytimeTracker.infrastructure.schedule;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class BukkitSchedulerWrapper implements SchedulerService {

    private final Plugin plugin;

    public BukkitSchedulerWrapper(Plugin plugin) {
        this.plugin = plugin;
    }

    public BukkitTask runTaskTimerAsynchronously(Runnable task, long delay, long period) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, task, delay, period);
    }
}
