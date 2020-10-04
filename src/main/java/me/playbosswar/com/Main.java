package me.playbosswar.com;

import me.playbosswar.com.commands.MainCommand;
import me.playbosswar.com.commands.WorldTimeCommand;
import me.playbosswar.com.hooks.PAPIPlaceholders;
import me.playbosswar.com.utils.*;
import me.tom.sparse.spigot.chat.menu.ChatMenuAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Main extends JavaPlugin implements Listener {
    private static Plugin plugin;

    @Override
    public void onEnable() {
        plugin = this;

        ChatMenuAPI.init(this);
        this.loadConfig();
        this.registerCommands();

        Files.createDataFolders();
        Files.deserializeJsonFilesIntoCommandTimers();

        try {
            Metrics metrics = new Metrics(this);
            boolean started = metrics.start();
            Messages.sendConsole("Hooked into Metrics");

            if(started) {
                Messages.sendConsole("Metrics started");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.loadPAPI();


        Tools.printDate();
        CommandExecutor.startRunner();
        Messages.sendConsole("&e" + getDescription().getVersion() + "&a loaded!");
    }

    @Override
    public void onDisable() {
        TimerManager.cancelAllTimers();

        saveDefaultConfig();
        ChatMenuAPI.disable();
        plugin = null;
    }

    public void registerCommands() {
        getCommand("worldtime").setExecutor(new WorldTimeCommand());
        getCommand("commandtimer").setExecutor(new MainCommand());
    }

    public void loadConfig() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
    }

    public void loadPAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PAPIPlaceholders(Main.getPlugin()).register();
            Bukkit.getPluginManager().registerEvents(this, this);
            Messages.sendConsole("&eCommandTimer hooked in PlaceholderAPI");
        } else {
            Messages.sendConsole("&eCommandTimer could not find PlaceholderAPI, placeholders will not work");
        }
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
