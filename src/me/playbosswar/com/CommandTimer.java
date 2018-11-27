package me.playbosswar.com;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandTimer extends JavaPlugin implements Listener {

	private static Plugin plugin;
	public int t;
	public static FileConfiguration config;
    static File cfile;
	ArrayList<String> done = new ArrayList<>();
	public HashMap<String, Integer> tasks = new HashMap<>();

	public void onEnable() {
		plugin = this;
		//Register the GUI events
		Tools.registerEvents(this, new GUIHandler());
		//Register the commands
		registerCommands();
		//Load configuration file
		Tools.initConfig();
		//Load all tasks in config
		startTasks();
		
		Tools.printDate();
		Bukkit.getConsoleSender().sendMessage("§a[CommandTimer] v2.0.4 loaded");
	}
	
	public void onDisable() {
		Tools.closeAllInventories();
		saveDefaultConfig();
		plugin = null;
	}
	
	private void registerCommands() {
		getCommand("commandtimer").setExecutor(new CommandHandler());
	}
	
	@SuppressWarnings("deprecation")
	public void startTasks() {
			getServer().getScheduler().cancelTasks(this); //Cancel all tasks that were still running
			if (getConfig().contains("settings.tasks")) { //Check if there is a task
				for (String task : getConfig().getConfigurationSection("settings.tasks").getKeys(false)) {
					if(getConfig().getBoolean("settings.tasks." + task + ".onday")) {
						LocalDate date = LocalDate.now();
						DayOfWeek dow = date.getDayOfWeek();
						if(getConfig().getStringList("settings.tasks." + task + ".days").contains(dow.toString())) {
							if (getConfig().getBoolean("settings.tasks." + task + ".onhour")) {
								Timer timer = new Timer();
								timer.schedule(new TimerTask() {
									@Override
									public void run() {
										Date date = new Date();
										SimpleDateFormat df = new SimpleDateFormat("HH:mm");
										String formattedDate = df.format(date);							

										if (formattedDate.equals(getConfig().getString("settings.tasks." + task + ".time"))) {
											Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
												@Override
												public void run() {
													for (String next : getConfig().getStringList("settings.tasks." + task + ".commands")) {
															Bukkit.dispatchCommand(Bukkit.getConsoleSender(), next);
													}
												}
											}, 50);
										}
									}
								}, TimeUnit.SECONDS.toMillis(1), TimeUnit.SECONDS.toMillis(1));
							} else {
								long ticks = 20L * getConfig().getLong("settings.tasks." + task + ".seconds");
								if (getConfig().getBoolean("settings.tasks." + task + ".onload")) {
									Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
										@Override
										public void run() {
											for (String next : getConfig().getStringList("settings.tasks." + task + ".commands")) {
													Bukkit.dispatchCommand(Bukkit.getConsoleSender(), next);
											}
										}
									}, 50);
								} else {
									Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new CommandTask(getConfig().getStringList("settings.tasks." + task + ".commands")), ticks, ticks);
								}
							}
						}
					} else if (getConfig().getBoolean("settings.tasks." + task + ".onhour")) {
						Timer timer = new Timer();
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								Date date = new Date();
								SimpleDateFormat df = new SimpleDateFormat("HH:mm");
								String formattedDate = df.format(date);			

								if (formattedDate.equals(getConfig().getStringList("settings.tasks." + task + ".time"))) {
									Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
										@Override
										public void run() {
											for (String next : getConfig().getStringList("settings.tasks." + task + ".commands")) {
													Bukkit.dispatchCommand(Bukkit.getConsoleSender(), next);
											}
										}
									}, 50);
								}
							}
						}, TimeUnit.SECONDS.toMillis(60), TimeUnit.SECONDS.toMillis(60));
					} else {
						long ticks = 20L * getConfig().getLong("settings.tasks." + task + ".seconds");
						if (getConfig().getBoolean("settings.tasks." + task + ".onload")) {
							Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
								@Override
								public void run() {
									for (String next : getConfig().getStringList("settings.tasks." + task + ".commands")) {
											Bukkit.dispatchCommand(Bukkit.getConsoleSender(), next);
									}
								}
							}, 50);
						} else {
							Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new CommandTask(getConfig().getStringList("settings.tasks." + task + ".commands")), ticks, ticks);
						}
					}
				}
			}
	}
		
	
	public static Plugin getPlugin() {
		return plugin;
	}
}
