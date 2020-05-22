package me.playbosswar.com;

import me.playbosswar.com.commands.MainCommand;
import me.playbosswar.com.commands.WorldTimeCommand;
import me.playbosswar.com.utils.Files;
import me.playbosswar.com.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Timer;

public class Main extends JavaPlugin implements Listener {
    private static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        Files.createDataFolders();

        getCommand("worldtime").setExecutor(new WorldTimeCommand());
        getCommand("commandtimer").setExecutor(new MainCommand());

        saveDefaultConfig();
        getConfig().options().copyDefaults(true);

        // TaskRunner.startTasks();
        Tools.printDate();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getPluginManager().registerEvents(this, this);
            Messages.sendConsole("&a[CommandTimer] &e CommandTimer hooked in PlaceholderAPI");
        } else {
            Messages.sendConsole("&a[CommandTimer] &e CommandTimer could not find PlaceholderAPI, placeholders will not work");
        }
        Messages.sendConsole("&a[CommandTimer] &e" + getDescription().getVersion() + "&a loaded!");
    }

    @Override
    public void onDisable() {
        for(Timer t : Tools.timerList) {
            t.cancel();
        }

        saveDefaultConfig();
        plugin = null;
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
