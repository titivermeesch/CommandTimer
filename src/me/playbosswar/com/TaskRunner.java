package me.playbosswar.com;

import me.playbosswar.com.genders.GenderHandler;
import me.playbosswar.com.genders.GenderHandler.Gender;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

class TaskRunner {
    private static Plugin p = Main.getPlugin();

    static void startTasks() {
        final FileConfiguration c = p.getConfig();

        if (!c.contains("tasks")) {
            return;
        }

        for (String task : c.getConfigurationSection("tasks").getKeys(false)) {
            final long seconds = 20L * c.getLong("tasks." + task + ".seconds");
            final Gender gender = GenderHandler.getGender(task);

            if (c.getBoolean("tasks." + task + ".onload")) {
                Tools.simpleCommandRunner(task, gender);
                continue;
            }

            if (c.contains("tasks." + task + ".time") && c.getStringList("tasks." + task + ".time").size() > 0) {
                Tools.complexCommandRunner(task, gender);
                continue;
            }
            Tools.easyCommandRunner(task, seconds, gender);
        }
    }
}
