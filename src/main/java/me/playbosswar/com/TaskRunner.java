package me.playbosswar.com;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.playbosswar.com.genders.GenderHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;

public class TaskRunner {
    private static Plugin p = Main.getPlugin();
    private static HashMap<String, Integer> tasksTimesExecuted = new HashMap<>();

    public static void startTasks() {
        final FileConfiguration c = p.getConfig();

        if (!c.contains("tasks")) {
            Tools.sendConsole("No tasks found");
            return;
        }

        for (String task : c.getConfigurationSection("tasks").getKeys(false)) {
            List<String> commands = p.getConfig().getStringList("tasks." + task + ".commands");
            final long seconds = 20L * c.getLong("tasks." + task + ".seconds");
            GenderHandler.Gender gender = GenderHandler.getGender(task);
            tasksTimesExecuted.put(task, 0);

            for (String cmd : commands) {
                if (c.getBoolean("tasks." + task + ".onload")) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> Tools.executeCommand(task, cmd, gender), 50L);
                    continue;
                }

                if (c.contains("tasks." + task + ".time") && !c.getStringList("tasks." + task + ".time").isEmpty()) {
                    Tools.complexCommandRunner(task, cmd, gender);
                    continue;
                }

                Bukkit.getScheduler().scheduleSyncRepeatingTask(p, () -> {
                    int timesExecuted = tasksTimesExecuted.get(task);

                    if (c.contains("tasks." + task + ".executionLimit") && timesExecuted >= c.getInt("tasks." + task + ".executionLimit")) {
                        return;
                    }

                    tasksTimesExecuted.replace(task, ++timesExecuted);
                    Tools.executeCommand(task, cmd, gender);
                }, seconds, seconds);
            }
        }
    }
}
