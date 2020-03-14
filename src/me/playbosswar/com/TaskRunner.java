package me.playbosswar.com;

import me.playbosswar.com.genders.GenderHandler;
import me.playbosswar.com.genders.GenderHandler.Gender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

class TaskRunner {
    private static Plugin p = Main.getPlugin();

    static void startTasks() {
        final FileConfiguration c = p.getConfig();

        if (!c.contains("tasks")) {
            Tools.sendConsole("No tasks found");
            return;
        }

        for (String task : c.getConfigurationSection("tasks").getKeys(false)) {
            Tools.sendConsole("Analysing task");
            Tools.sendConsole(task);
            final long seconds = 20L * c.getLong("tasks." + task + ".seconds");
            Tools.sendConsole(seconds + "");
            Gender gender = GenderHandler.getGender(task);
            Tools.sendConsole(gender.name());

            if (c.getBoolean("tasks." + task + ".onload")) {
                Tools.sendConsole("SimplecommandRunner");
                Tools.simpleCommandRunner(task, gender);
                continue;
            }

            if (c.contains("tasks." + task + ".time") && !c.getStringList("tasks." + task + ".time").isEmpty()) {
                Tools.sendConsole("complex command runner");
                Tools.complexCommandRunner(task, gender);
                continue;
            }

            Tools.sendConsole("Scheduled task");
            Tools.easyCommandRunner(task, seconds, gender);
        }
    }
}
