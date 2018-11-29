package me.playbosswar.com;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args) {
		if(args[0].equals("reload")) {
			CommandTimer.getPlugin().reloadConfig();
			Tools.reloadTaks();
			sender.sendMessage(ChatColor.GOLD + "CommandTimer reloaded");
			Tools.closeAllInventories();
			return true;
		}

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
