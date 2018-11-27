package me.playbosswar.com;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {
	
	//Opens the GUI Menu, it's the only command of the plugin actualy
	public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args) {
		if(sender instanceof Player) {
			if(sender.hasPermission("commandtimer.use")) {
				GUIHandler.generateGUI((Player)sender);
			} else {
				sender.sendMessage("§cYou don't have the right permission for this");
			}
			
			return true;
		}
		return false;
	}
}
