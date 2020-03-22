package me.playbosswar.com;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    private static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        registerCommands();
        Tools.initConfig();
        TaskRunner.startTasks();
        Tools.printDate();
        Tools.sendConsole("&a[CommandTimer] &e" + getDescription().getVersion() + "&a loaded!");
    }

    @Override
    public void onDisable() {
        saveDefaultConfig();
        plugin = null;
    }

    private void registerCommands() {
        getCommand("commandtimer").setExecutor(new CommandHandler());
    }


    public static Plugin getPlugin() {
        return plugin;
    }
}
