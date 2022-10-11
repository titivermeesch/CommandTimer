package me.playbosswar.com;

import fr.minuskube.inv.InventoryManager;
import io.sentry.Sentry;
import me.playbosswar.com.commands.MainCommand;
import me.playbosswar.com.conditionsengine.ConditionEngineManager;
import me.playbosswar.com.events.JoinEvents;
import me.playbosswar.com.hooks.HooksManager;
import me.playbosswar.com.hooks.Metrics;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.TasksManager;
import me.playbosswar.com.updater.Updater;
import me.playbosswar.com.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class CommandTimerPlugin extends JavaPlugin implements Listener {
    private static Plugin plugin;
    private static CommandTimerPlugin instance;
    private static HooksManager hooksManager;
    private static InventoryManager inventoryManager;
    private static TasksManager tasksManager;
    private static ConditionEngineManager conditionEngineManager;
    public static Metrics metrics;
    public static Updater updater;
    public static LanguageManager languageManager;

    @Override
    public void onEnable() {
        plugin = this;
        instance = this;

        // Load error handler as soon as possible
        this.loadSentry();

        this.loadConfig();
        languageManager = new LanguageManager(this, getConfig().getString("language"));
        this.registerCommands();

        Bukkit.getPluginManager().registerEvents(new JoinEvents(), this);

        updater = new Updater(this);
        metrics = new Metrics(CommandTimerPlugin.getPlugin(), 9657);
        hooksManager = new HooksManager();
        tasksManager = new TasksManager();
        inventoryManager = new InventoryManager(this);
        conditionEngineManager = new ConditionEngineManager();
        inventoryManager.init();

        metrics.addCustomChart(new Metrics.SingleLineChart("loaded_tasks", () -> tasksManager.getLoadedTasks().size()));
        metrics.addCustomChart(new Metrics.SingleLineChart("executed_tasks", () -> {
            int v = Integer.valueOf(tasksManager.executionsSinceLastSync);
            tasksManager.executionsSinceLastSync = 0;
            return v;
        }));

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
        Files.createDataFolders();
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        loadDefaults();
    }

    private void loadSentry() {
        Sentry.init(options -> {
            options.setDsn("https://45383fac83f64e65a45d83c3059eb934@o1414814.ingest.sentry.io/6755132");
            options.setTracesSampleRate(0.1);
            options.setRelease(getDescription().getVersion());
            options.addInAppInclude("me.playbosswar");
        });


        Sentry.configureScope(scope -> {
            scope.setContexts("version", Bukkit.getVersion());
            scope.setContexts("pluginVersion", getDescription().getVersion());
            try {
                scope.setContexts("ip", InetAddress.getLocalHost());
            } catch(UnknownHostException ignored) {
            }
        });
    }

    private void loadDefaults() {
        File existingConfigFile = new File(this.getDataFolder(), "config.yml");
        FileConfiguration existingFileConfiguration = YamlConfiguration.loadConfiguration(existingConfigFile);

        for(String section : getConfig().getConfigurationSection("").getKeys(true)) {
            if(existingFileConfiguration.get(section) != null) continue;

            existingFileConfiguration.set(section, getConfig().get(section));
        }

        try {
            existingFileConfiguration.save(existingConfigFile);
        } catch(IOException e) {
            e.printStackTrace();
        }
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

    public static LanguageManager getLanguageManager() {
        return languageManager;
    }
}
