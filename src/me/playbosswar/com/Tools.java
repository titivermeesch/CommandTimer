package me.playbosswar.com;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class Tools {
	
	public static void printDate() {
		LocalDate date = LocalDate.now();
		DayOfWeek dow = date.getDayOfWeek();
		if(CommandTimer.getPlugin().getConfig().getBoolean("timeonload")) {
			Bukkit.getConsoleSender().sendMessage("§aServer time : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
			Bukkit.getConsoleSender().sendMessage("§aServer day : " + dow);
		}
	}
	
	public static void initConfig() {
		CommandTimer.getPlugin().saveDefaultConfig();
		CommandTimer.getPlugin().getConfig().options().copyDefaults(false);
	}
	
	public static void registerEvents(Plugin plugin, Listener... listeners) {
		Listener[] arrayOfListener;
		int j = (arrayOfListener = listeners).length;
		for (int i = 0; i < j; i++) {
			Listener listener = arrayOfListener[i];
			Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
		}
	}
	
	public static void closeAllInventories() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.closeInventory();
		}
	}
	
	public static void reloadTaks() {
		Bukkit.getScheduler().cancelTasks(CommandTimer.getPlugin());
		((CommandTimer) CommandTimer.getPlugin()).startTasks();
	}


}
