package me.playbosswar.com;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandTimer extends JavaPlugin implements Listener {

	private static Plugin plugin;
	public int t;

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
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[CommandTimer] v2.2 loaded");
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
