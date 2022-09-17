package me.playbosswar.com;

import fr.minuskube.inv.InventoryManager;
import io.sentry.Sentry;
import io.sentry.protocol.User;
import me.playbosswar.com.commands.MainCommand;
import me.playbosswar.com.conditionsengine.ConditionEngineManager;
import me.playbosswar.com.events.JoinEvents;
import me.playbosswar.com.hooks.HooksManager;
import me.playbosswar.com.hooks.Metrics;
import me.playbosswar.com.tasks.TasksManager;
import me.playbosswar.com.updater.Updater;
import me.playbosswar.com.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandTimerPlugin extends JavaPlugin implements Listener {
    private static Plugin plugin;
    private static CommandTimerPlugin instance;
    private static HooksManager hooksManager;
    private static InventoryManager inventoryManager;
    private static TasksManager tasksManager;
    private static ConditionEngineManager conditionEngineManager;
    public static Metrics metrics;
    public static Updater updater;

    @Override
    public void onEnable() {
        plugin = this;
        instance = this;

        this.loadSentry();
        this.loadConfig();
        this.registerCommands();

        Files.createDataFolders();

        Bukkit.getPluginManager().registerEvents(new JoinEvents(), this);

        updater = new Updater(this);
        metrics = new Metrics(CommandTimerPlugin.getPlugin(), 9657);
        hooksManager = new HooksManager();
        tasksManager = new TasksManager();
        inventoryManager = new InventoryManager(this);
        conditionEngineManager = new ConditionEngineManager();
        inventoryManager.init();
        Files.deserializeJsonFilesIntoCommandTimers();

        Tools.printDate();
        Messages.sendConsole("&e" + getDescription().getVersion() + "&a loaded!");
    }

    @Override
    public void onDisable() {
        tasksManager.disable();
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

    private void loadSentry() {
        Sentry.init(options -> {
            options.setDsn("https://45383fac83f64e65a45d83c3059eb934@o1414814.ingest.sentry.io/6755132");
            options.setTracesSampleRate(1.0);
            options.setDebug(false);
        });

        Sentry.configureScope(scope -> {
            scope.setContexts("version", Bukkit.getVersion());
            scope.setContexts("pluginVersion", getDescription().getVersion());
            scope.setContexts("ip", getPlugin().getServer().getIp());
            scope.setContexts("loadedTasks", getTasksManager().getLoadedTasks().size());
        });
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    public HooksManager getHooksManager() {
        return hooksManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public TasksManager getTasksManager() {
        return tasksManager;
    }

    public ConditionEngineManager getConditionEngineManager() {
        return conditionEngineManager;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public static CommandTimerPlugin getInstance() {
        return instance;
    }

    public static Updater getUpdater() {
        return updater;
    }
}
