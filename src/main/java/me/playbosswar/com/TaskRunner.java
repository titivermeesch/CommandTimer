package me.playbosswar.com;

import me.playbosswar.com.genders.GenderHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class TaskRunner {
    private static Plugin p = Main.getPlugin();

    public static void startTasks() {
        final FileConfiguration c = p.getConfig();

        if (!c.contains("tasks")) {
            Tools.sendConsole("[CommandTimer] No tasks found");
            return;
        }

        for (String task : c.getConfigurationSection("tasks").getKeys(false)) {
            final long seconds = 20L * c.getLong("tasks." + task + ".seconds");
            GenderHandler.Gender gender = GenderHandler.getGender(task);

            if (c.getBoolean("tasks." + task + ".onload")) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> {
                    for (final String cmd : p.getConfig().getStringList("tasks." + task + ".commands")) {
                        Tools.executeCommand(task, cmd, gender);
                    }
                }, 50L);
                continue;
            }

            if (c.contains("tasks." + task + ".time") && !c.getStringList("tasks." + task + ".time").isEmpty()) {
                Tools.complexCommandRunner(task, gender);
                continue;
            }

            Bukkit.getScheduler().scheduleSyncRepeatingTask(p, () -> {
                for (String cmd : p.getConfig().getStringList("tasks." + task + ".commands")) {
                    Tools.executeCommand(task, cmd, gender);
                }
            }, seconds, seconds);
        }
    }
}
