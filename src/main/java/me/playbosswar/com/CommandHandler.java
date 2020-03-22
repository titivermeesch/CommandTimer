package me.playbosswar.com;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1 && args[0].equals("reload")) {
            Tools.reloadTasks();
            sender.sendMessage(Tools.color("&6CommandTimer reloaded!"));
            return true;
        }
        sender.sendMessage(Tools.color("&cYour command is invalid."));
        return true;
    }

}