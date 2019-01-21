package me.playbosswar.com;

import net.md_5.bungee.api.ProxyServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class CommandTimer extends JavaPlugin implements Listener {

	private static Plugin plugin;
	public int t;

	public void onEnable() {
		plugin = this;
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Bukkit.getPluginManager().registerEvents(this, this);
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[CommandTimer] PlaceholderAPI detected -> We will use it!");
        }

        try {
            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "BungeeCord MessagingChannel registered");
        } catch(Exception e) {
            e.printStackTrace();
        }



		//Register the GUI events
		Tools.registerEvents(this, new GUIListeners());
		//Register the commands
		registerCommands();
		//Load configuration file
		Tools.initConfig();
		//Load all tasks in config
		TaskRunner.startTasks();
		
		Tools.printDate();
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[CommandTimer] v2.1.3 loaded");
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

	public static void sendBungee(Player p) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("Command");
			out.writeUTF("ALL");
			out.writeUTF("/alert Testing command distance");
		} catch(Exception e) {
			e.printStackTrace();
		}

		p.sendPluginMessage(getPlugin(), "BungeeCord", b.toByteArray());
	}
}
