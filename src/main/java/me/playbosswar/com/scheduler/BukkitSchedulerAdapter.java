package me.playbosswar.com.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class BukkitSchedulerAdapter implements SchedulerAdapter {
    private Plugin plugin;

    public BukkitSchedulerAdapter(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public BukkitTask runTaskTimer(Runnable runnable, long delay, long period) {
        return Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period);
    }

    @Override
    public BukkitTask runTask(Runnable runnable) {
        return Bukkit.getScheduler().runTask(plugin, runnable);
    }

    @Override
    public BukkitTask runTaskLater(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
    }
}
