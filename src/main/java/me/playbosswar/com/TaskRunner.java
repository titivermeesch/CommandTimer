package me.playbosswar.com;

import me.playbosswar.com.genders.GenderHandler;
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
                Tools.simpleCommandRunner(task, gender);
                continue;
            }

            if (c.contains("tasks." + task + ".time") && !c.getStringList("tasks." + task + ".time").isEmpty()) {
                Tools.complexCommandRunner(task, gender);
                continue;
            }

            Tools.easyCommandRunner(task, seconds, gender);
        }
    }
}
