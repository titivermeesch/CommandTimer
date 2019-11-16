package me.playbosswar.com;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIHandler implements Listener {
	public static void listCommandsGUI(Player p) { //GUI that shows all the loaded commands
		if(CommandTimer.getPlugin().getConfig().getString("tasks.1") != null) {
			Inventory allCommands = Bukkit.getServer().createInventory(p, 54, "All loaded timers");
			FileConfiguration c = CommandTimer.getPlugin().getConfig();
			for (final String path : CommandTimer.getPlugin().getConfig().getConfigurationSection("tasks").getKeys(false)) {
				//Generate an unique item for each command
				ItemStack genStack = new ItemStack(Material.WOOL);
		        ItemMeta genStackMeta = genStack.getItemMeta();
		        ArrayList<String> lore= new ArrayList<String>();

				lore.add(ChatColor.GOLD + "Commands : " + c.getStringList("tasks." + path + ".commands"));
				lore.add(ChatColor.GOLD + "On hour : " + c.getBoolean("tasks." + path + ".onhour"));
				lore.add(ChatColor.GOLD + "Time : " + c.getStringList("tasks." + path + ".time"));
				lore.add(ChatColor.GOLD + "On load : " + c.getBoolean("tasks." + path + ".onload"));
				lore.add(ChatColor.GOLD + "On day : " + c.getBoolean("tasks." + path + ".onday"));
				lore.add(ChatColor.GOLD + "Days : " + c.getStringList("tasks." + path + ".days"));
				lore.add(ChatColor.GOLD + "Seconds : " + c.getInt("tasks." + path + ".seconds"));
				lore.add(ChatColor.GOLD + "UseRandom : " + c.getBoolean("tasks." + path + ".useRandom"));
				lore.add(ChatColor.GOLD + "Random : " + c.getDouble("tasks." + path + ".random"));
				lore.add(ChatColor.GOLD + "Gender : " + c.getString("tasks." + path + ".gender"));
				lore.add(ChatColor.GOLD + "Bungee : " + c.getBoolean("tasks." + path + ".bungee"));
				lore.add(ChatColor.GOLD + "Permission : " + c.getString("tasks." + path + ".permissoin"));
				lore.add(ChatColor.RED + "Left-Click to delete this command");
		        
		        genStackMeta.setLore(lore);
		        genStackMeta.setDisplayName(path + "");
		        
		        genStack.setItemMeta(genStackMeta);
		        
		        allCommands.setItem(Integer.parseInt(path)-1, genStack); //Set item in GUI
			}
			ItemStack close = new ItemStack(Material.BARRIER);
	        ItemMeta closeMeta = close.getItemMeta();
	        
	        closeMeta.setDisplayName(ChatColor.GOLD + "Close");
	        close.setItemMeta(closeMeta);
	        allCommands.setItem(53, close);
			
			p.openInventory(allCommands);
		} else {
			p.sendMessage(ChatColor.RED + "You don't have any loaded commands");
		}
	}
}