package me.playbosswar.com;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DataPersisterManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import fr.minuskube.inv.InventoryManager;
import io.sentry.ITransaction;
import io.sentry.Sentry;
import me.playbosswar.com.commands.MainCommand;
import me.playbosswar.com.conditionsengine.ConditionEngineManager;
import me.playbosswar.com.conditionsengine.EventsManager;
import me.playbosswar.com.events.JoinEvents;
import me.playbosswar.com.hooks.HooksManager;
import me.playbosswar.com.hooks.Metrics;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.scheduler.BukkitSchedulerAdapter;
import me.playbosswar.com.scheduler.FoliaSchedulerAdapter;
import me.playbosswar.com.scheduler.SchedulerAdapter;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TasksManager;
import me.playbosswar.com.tasks.persistors.*;
import me.playbosswar.com.updater.Updater;
import me.playbosswar.com.utils.Files;
import me.playbosswar.com.utils.Messages;
import me.playbosswar.com.utils.Tools;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CommandTimerPlugin extends JavaPlugin implements Listener {
    private static Plugin plugin;
    private static CommandTimerPlugin instance;
    private static HooksManager hooksManager;
    private static InventoryManager inventoryManager;
    private static TasksManager tasksManager;
    private static ConditionEngineManager conditionEngineManager;
    private static EventsManager eventsManager;
    public static Metrics metrics;
    public static Updater updater;
    public static LanguageManager languageManager;
    public static Dao<Task, Integer> taskDao;
    public static SchedulerAdapter schedulerAdapter;

    @Override
    public void onEnable() {
        plugin = this;
        instance = this;

        if(FoliaSchedulerAdapter.isSupported()) {
            schedulerAdapter = new FoliaSchedulerAdapter(this);
        } else {
            schedulerAdapter = new BukkitSchedulerAdapter(this);
        }

        Sentry.init(options -> {
            options.setDsn("https://45383fac83f64e65a45d83c3059eb934@o1414814.ingest.sentry.io/6755132");
            options.setTracesSampleRate(0.3);
            options.setRelease(getDescription().getVersion());
        });
        Sentry.configureScope(scope -> {
            scope.setExtra("bukkit_version", getServer().getBukkitVersion());
            scope.setExtra("server_name", getServer().getName());
        });

        ITransaction transaction = Sentry.startTransaction("server_startup", "initiation");

        this.loadConfig();
        languageManager = new LanguageManager(this, getConfig().getString("language"));
        this.registerCommands();

        Bukkit.getPluginManager().registerEvents(new JoinEvents(), this);

        Files.migrateFileNamesToFileUuids();
        if(getConfig().getBoolean("database.enabled")) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                ConnectionSource connectionSource = new JdbcConnectionSource(getConfig().getString("database.url"));
                DataPersisterManager.registerDataPersisters(TaskCommandPersistor.getSingleton(),
                        TaskIntervalPersistor.getSingleton(), TaskTimePersistor.getSingleton(),
                        ConditionPersistor.getSingleton(), EventConfigurationPersistor.getSingleton(),
                        DaysPersistor.getSingleton());
                taskDao = DaoManager.createDao(connectionSource, Task.class);
                TableUtils.createTableIfNotExists(connectionSource, Task.class);
            } catch(SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        updater = new Updater(this);
        metrics = new Metrics(CommandTimerPlugin.getPlugin(), 9657);
        hooksManager = new HooksManager();
        tasksManager = new TasksManager();
        inventoryManager = new InventoryManager(this);
        conditionEngineManager = new ConditionEngineManager();
        eventsManager = new EventsManager(tasksManager);
        inventoryManager.init();
        getServer().getMessenger().registerOutgoingPluginChannel(plugin, "commandtimer:main");
        loadMetrics();

        if(getConfig().getBoolean("timeonload")) {
            Tools.printDate();
        }
        Messages.sendConsole("&e" + getDescription().getVersion() + "&a loaded " + getTasksManager().getLoadedTasks().size() + " tasks!");
        transaction.finish();
    }

    @Override
    public void onDisable() {
        tasksManager.disable();
        conditionEngineManager.onDisable();
        saveDefaultConfig();
        plugin = null;
    }

    private void loadMetrics() {
        metrics.addCustomChart(new Metrics.SingleLineChart("loaded_tasks", () -> tasksManager.getLoadedTasks().size()));
        metrics.addCustomChart(new Metrics.SingleLineChart("executed_tasks", () -> {
            int v = Integer.valueOf(tasksManager.executionsSinceLastSync);
            tasksManager.executionsSinceLastSync = 0;
            return v;
        }));
        metrics.addCustomChart(new Metrics.MultiLineChart("loaded_extensions", () -> {
            Map<String, Integer> map = new HashMap<>();
            conditionEngineManager.getConditionExtensions().forEach(conditionExtension -> {
                map.put(conditionExtension.getConditionGroupName(), 1);
            });

            return map;
        }));
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

    public void setTasksManager(TasksManager tasksManager) {
        CommandTimerPlugin.tasksManager = tasksManager;
    }

    public ConditionEngineManager getConditionEngineManager() {
        return conditionEngineManager;
    }

    public void setConditionEngineManager(ConditionEngineManager conditionEngineManager) {
        CommandTimerPlugin.conditionEngineManager = conditionEngineManager;
    }

    public EventsManager getEventsManager() {
        return eventsManager;
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

    public static Dao<Task, Integer> getTaskDao() {
        return taskDao;
    }

    public static SchedulerAdapter getScheduler() {
        return schedulerAdapter;
    }
}
