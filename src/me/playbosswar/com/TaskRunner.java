package me.playbosswar.com;
import org.bukkit.configuration.file.FileConfiguration;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class TaskRunner {

    static FileConfiguration c = Main.getPlugin().getConfig();

    public static void startTasks() {
        if(c.contains("tasks")) {
            for (String task : c.getConfigurationSection("tasks").getKeys(false)) {

                long ticks = 20L * c.getLong("tasks." + task + ".seconds");
                LocalDate date = LocalDate.now();
                DayOfWeek dow = date.getDayOfWeek();
                String gender = Tools.getGender(task);

                if(c.getBoolean("tasks." + task + ".onday")) {

                    if(c.getStringList("tasks." + task + ".days").contains(dow.toString())) {
                        if (c.getBoolean("tasks." + task + ".onhour")) {
                            Tools.complexCommandRunner(task, gender);
                        } else {
                            if (c.getBoolean("tasks." + task + ".onload")) {
                                Tools.simpleCommandRunner(task, gender);
                            } else {
                                Tools.easyCommandRunner(task, ticks, gender);
                            }
                        }
                    }
                } else if (c.getBoolean("tasks." + task + ".onhour")) {
                    Tools.complexCommandRunner(task, gender);
                } else {
                    if (c.getBoolean("tasks." + task + ".onload")) {
                        Tools.simpleCommandRunner(task, gender);
                    } else {
                        Tools.easyCommandRunner(task, ticks, gender);
                    }
                }
            }
        }
    }
}
