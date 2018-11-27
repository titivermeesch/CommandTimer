package me.playbosswar.com;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
	
	//Opens the GUI Menu, it's the only command of the plugin actualy
	public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(p.hasPermission("commandtimer.use") || p.isOp()) {
				GUIHandler.generateGUI(p);
			} else {
				p.sendMessage(ChatColor.RED + "You don't have the right permission for this");
			}
			return true;
		}
        sender.sendMessage(ChatColor.RED + "Command can only be used in-game");
		return true;
	}
}
