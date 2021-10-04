package me.playbosswar.com;

import fr.minuskube.inv.InventoryManager;
import me.playbosswar.com.commands.MainCommand;
import me.playbosswar.com.conditionsengine.ConditionEngineManager;
import me.playbosswar.com.hooks.HooksManager;
import me.playbosswar.com.hooks.Metrics;
import me.playbosswar.com.listeners.PlayerWorldTimeTracking;
import me.playbosswar.com.tasks.TasksManager;
import me.playbosswar.com.utils.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    private static Plugin plugin;
    private static HooksManager hooksManager;
    private static InventoryManager inventoryManager;
    private static TasksManager tasksManager;
    private static ConditionEngineManager conditionEngineManager;
    private static PlayerWorldTimeTracking playerWorldTimeTracking;
    public static Metrics metrics;

    @Override
    public void onEnable() {
        plugin = this;

        this.loadConfig();
        this.registerCommands();

        Files.createDataFolders();
        Files.deserializeJsonFilesIntoCommandTimers();

        metrics = new Metrics(Main.getPlugin(), 9657);
        hooksManager = new HooksManager();
        tasksManager = new TasksManager();
        inventoryManager = new InventoryManager(this);
        conditionEngineManager = new ConditionEngineManager();
        playerWorldTimeTracking = new PlayerWorldTimeTracking();
        inventoryManager.init();

        Tools.printDate();
        Messages.sendConsole("&e" + getDescription().getVersion() + "&a loaded!");
    }

    @Override
    public void onDisable() {
        tasksManager.disable();
        playerWorldTimeTracking.cancel();
        conditionEngineManager.onDisable();
        saveDefaultConfig();
        plugin = null;
    }

    public void registerCommands() {
        getCommand("commandtimer").setExecutor(new MainCommand());
    }

    public void loadConfig() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public static HooksManager getHooksManager() {
        return hooksManager;
    }

    public static InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public static TasksManager getTasksManager() {
        return tasksManager;
    }

    public static ConditionEngineManager getConditionEngineManager() {
        return conditionEngineManager;
    }

    public static PlayerWorldTimeTracking getPlayerWorldTimeTracking() {
        return playerWorldTimeTracking;
    }

    public static Metrics getMetrics() {
        return metrics;
    }
}
