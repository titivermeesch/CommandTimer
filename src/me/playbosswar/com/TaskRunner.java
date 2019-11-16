package me.playbosswar.com;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class TaskRunner {

    public static Plugin p = Main.getPlugin();

    public static void startTasks() {
        Main.getPlugin().saveDefaultConfig();
        Main.getPlugin().getConfig().options().copyDefaults(false);
        if(p.getConfig().contains("tasks")) {
            for (String task : Main.getPlugin().getConfig().getConfigurationSection("tasks").getKeys(false)) {

                Bukkit.getConsoleSender().sendMessage("Command is " + p.getConfig().getStringList("tasks." + task + ".commands"));
                Bukkit.getConsoleSender().sendMessage("other command is " + Main.getPlugin().getConfig().getStringList("tasks." + task + ".commands"));

                long ticks = 20L * p.getConfig().getLong("tasks." + task + ".seconds");
                LocalDate date = LocalDate.now();
                DayOfWeek dow = date.getDayOfWeek();
                String gender = Tools.getGender(task);

                if(p.getConfig().getBoolean("tasks." + task + ".onday")) {

                    if(p.getConfig().getStringList("tasks." + task + ".days").contains(dow.toString())) {
                        if (p.getConfig().getBoolean("tasks." + task + ".onhour")) {
                            Tools.complexCommandRunner(task, gender);
                        } else {
                            if (p.getConfig().getBoolean("tasks." + task + ".onload")) {
                                Tools.simpleCommandRunner(task, gender);
                            } else {
                                Tools.easyCommandRunner(task, ticks, gender);
                            }
                        }
                    }
                } else if (p.getConfig().getBoolean("tasks." + task + ".onhour")) {
                    Tools.complexCommandRunner(task, gender);
                } else {
                    if (p.getConfig().getBoolean("tasks." + task + ".onload")) {
                        Tools.simpleCommandRunner(task, gender);
                    } else {
                        Tools.easyCommandRunner(task, ticks, gender);
                    }
                }
            }
        }
    }
}
