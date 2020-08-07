package me.playbosswar.com.commands;

import me.playbosswar.com.Tools;
import me.playbosswar.com.utils.CommandsManager;
import me.playbosswar.com.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length == 1) {
            if (args[0].equals("reload")) {
                Tools.reloadTasks();
                Messages.sendMessage(sender, "Reloaded");
                return true;
            }
        }

        if(!(sender instanceof Player)) {
            Messages.sendNeedToBePlayer(sender);
            return true;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("commandtimer.use")) {
            Messages.sendNoPermission(p);
            return true;
        }

        if(args.length == 2) {
            if(args[0].equals("create")) {
                CommandsManager.createNewCommandTimer(p, args[1]);
                return true;
            }

            if(args[0].equals("remove") || args[0].equals("delete")) {
                CommandsManager.deleteCommandtimer(p, args[1]);
                return true;
            }

        }

        if(args.length == 4) {
            if(args[0].equals("set")) {
                CommandsManager.changeCommandtimerData(p, args[1], args[2], args[3]);
                return true;
            }
        }

        Messages.sendMessageToPlayer(p, "&9&lThanks for using CommandTimer.");
        Messages.sendMessageToPlayer(p, "&6Please look on the plugin page for extra help");
        Messages.sendMessageToPlayer(p, "https://github.com/titivermeesch/CommandTimer/wiki");
        Messages.sendMessageToPlayer(p, "https://www.spigotmc.org/resources/command-timer.24141/");
        return true;
    }
}
