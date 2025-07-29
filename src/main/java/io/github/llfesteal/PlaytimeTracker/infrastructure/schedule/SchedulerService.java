package io.github.llfesteal.PlaytimeTracker.infrastructure.schedule;

import org.bukkit.scheduler.BukkitTask;

public interface SchedulerService {
    BukkitTask runTaskTimerAsynchronously(Runnable task, long delay, long period);
}
