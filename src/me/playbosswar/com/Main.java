package me.playbosswar.com;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	private static Plugin plugin;
	public int t;

	public void onEnable() {
		plugin = this;
		registerCommands();
		Tools.initConfig();
		TaskRunner.startTasks();
		Tools.printDate();
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[CommandTimer] v2.4.2 loaded");
	}
	
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
