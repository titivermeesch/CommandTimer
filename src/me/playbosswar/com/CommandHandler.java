package me.playbosswar.com;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(args.length == 0) {
            if(sender instanceof Player) {
                Player p = (Player) sender;
                if(p.hasPermission("commandtimer.use") || p.isOp()) {
                    GUIHandler.listCommandsGUI(p);
                } else {
                    p.sendMessage(ChatColor.RED + "You don't have the right permission for this");
                }
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "Command can only be used in-game");
                return true;
            }
        } else if(args[0].equals("reload")) {
            CommandTimer.getPlugin().reloadConfig();
            Tools.reloadTaks();
            sender.sendMessage(ChatColor.GOLD + "CommandTimer reloaded");
            Tools.closeAllInventories();
            return true;
        }
        return true;
    }
}
