package me.playbosswar.com.scheduler;

import org.bukkit.scheduler.BukkitTask;

public interface SchedulerAdapter {
    BukkitTask runTaskTimer(Runnable runnable, long delay, long period);

    BukkitTask runTask(Runnable runnable);

    BukkitTask runTaskLater(Runnable runnable, long delay);
}
