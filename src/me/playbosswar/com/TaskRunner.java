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
            System.out.println("No tasks found");
            return;
        }

        for (String task : c.getConfigurationSection("tasks").getKeys(false)) {
            System.out.println("Analysing task");
            System.out.println(task);
            final long seconds = 20L * c.getLong("tasks." + task + ".seconds");
            System.out.println(seconds);
            Gender gender = GenderHandler.getGender(task);
            System.out.println(gender);

            if(gender == null) {
                gender = Gender.CONSOLE;
            }

            if (c.getBoolean("tasks." + task + ".onload")) {
                System.out.println("SimplecommandRunner");
                Tools.simpleCommandRunner(task, gender);
                continue;
            }

            if (c.contains("tasks." + task + ".time") && c.getStringList("tasks." + task + ".time").size() > 0) {
                System.out.println("complex command runner");
                Tools.complexCommandRunner(task, gender);
                continue;
            }

            System.out.println("Scheduled task");
            Tools.easyCommandRunner(task, seconds, gender);
        }
    }
}
