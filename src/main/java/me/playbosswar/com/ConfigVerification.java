package me.playbosswar.com;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigVerification {
    public static boolean checkConfigurationFileValidity() {
        final FileConfiguration c = Main.getPlugin().getConfig();
        Tools.sendConsole("&6Let's check if your configuration file is valid...");

        for(String task : c.getConfigurationSection("tasks").getKeys(false)) {
            if(task.contains(" ")) {
                Tools.sendConsole("&6Your configuration file use task names with spaces in it, please replace them with underscores");
                Tools.sendConsole("&cConfiguration file invalid, CommandTimer disabled...");
                Bukkit.getPluginManager().disablePlugin(Main.getPlugin());
                return false;
            }

            if(task.contains("1")) {
                Tools.sendConsole("&6Your configuration file use task names with a 1 in it, please remove it.");
                Tools.sendConsole("&cConfiguration file invalid, CommandTimer disabled...");
                Bukkit.getPluginManager().disablePlugin(Main.getPlugin());
                return false;
            }

            if(!c.contains("tasks." + task + ".gender")) {
                Tools.sendConsole("&6One of your tasks does not have a gender, please give each task a gender");
                Tools.sendConsole("&cConfiguration file invalid, CommandTimer disabled...");
                Bukkit.getPluginManager().disablePlugin(Main.getPlugin());
                return false;
            }

            if(c.getStringList("tasks." + task + ".commands").size() == 0) {
                Tools.sendConsole("&6One of your tasks does not have a commands. Please provide at least one command for each task");
                Tools.sendConsole("&cConfiguration file invalid, CommandTimer disabled...");
                Bukkit.getPluginManager().disablePlugin(Main.getPlugin());
                return false;
            }
        }

        Tools.sendConsole("&a[CommandTimer] Configuration valid!");
        return true;
    }
}
