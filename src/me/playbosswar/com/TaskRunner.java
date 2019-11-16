package me.playbosswar.com;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class TaskRunner {
    public static Plugin p = Main.getPlugin();

    public static void startTasks() {
        FileConfiguration c = p.getConfig();

        if (!c.contains("tasks")) {
            return;
        }

        for (String task : c.getConfigurationSection("tasks").getKeys(false)) {
            Bukkit.getConsoleSender().sendMessage("Analysing command..." + task);
            long seconds = 20L * c.getLong("tasks." + task + ".seconds");
            String gender = Tools.getGender(task);

            if (c.getBoolean("tasks." + task + ".onload")) {
                Bukkit.getConsoleSender().sendMessage("Executing command on load");
                Tools.simpleCommandRunner(task, gender);
                continue;
            }

            if (c.contains("tasks." + task + ".time") && c.getStringList("tasks." + task + ".time").size() > 0) {
                Bukkit.getConsoleSender().sendMessage("time is " + c.getStringList("tasks." + task + ".time"));
                Bukkit.getConsoleSender().sendMessage("Executing command on time");
                Tools.complexCommandRunner(task, gender);
                continue;
            }

            Bukkit.getConsoleSender().sendMessage("Executing command normally");
            Tools.easyCommandRunner(task, seconds, gender);
        }
    }
}
