package me.playbosswar.com;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1 && args[0].equals("reload")) {
            Main.getPlugin().reloadConfig();
            Tools.reloadTaks();
            sender.sendMessage(ChatColor.GOLD + "CommandTimer reloaded");
            return true;
        }
        return true;
    }
}