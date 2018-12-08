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
import org.bukkit.ChatColor;
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
		Tools.registerEvents(this, new GUIListeners());
		//Register the commands
		registerCommands();
		//Load configuration file
		Tools.initConfig();
		//Load all tasks in config
		TaskRunner.startTasks();
		
		Tools.printDate();
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[CommandTimer] v2.1.2 loaded");
	}
	
	public void onDisable() {
		Tools.closeAllInventories();
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