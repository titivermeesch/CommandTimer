package me.playbosswar.com;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1 && args[0].equals("reload")) {
            Tools.reloadTasks();
            sender.sendMessage(ChatColor.GOLD + "CommandTimer reloaded");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Your command is invalid.");
        return true;
    }
}