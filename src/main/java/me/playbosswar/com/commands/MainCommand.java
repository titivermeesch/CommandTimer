package me.playbosswar.com.commands;

import me.playbosswar.com.Tools;
import me.playbosswar.com.utils.ChatMenus;
import me.playbosswar.com.utils.CommandTimer;
import me.playbosswar.com.utils.CommandsManager;
import me.playbosswar.com.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

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

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("list")) {
                ArrayList<CommandTimer> timers = CommandsManager.getAllTimers();
                Messages.sendMessageToPlayer(p, "Here is a list of all your timers:");
                int i = 1;
                for(CommandTimer timer : timers) {
                    Messages.sendMessageToPlayer(p, i + ": " + timer.getName() + " (every " + timer.getSeconds() + " seconds)");
                }
                return true;
            }
        }

        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("create")) {
                CommandsManager.createNewCommandTimer(p, args[1]);
                return true;
            }

            if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")) {
                CommandsManager.deleteCommandtimer(p, args[1]);
                return true;
            }

            if(args[0].equalsIgnoreCase("info")) {
                ChatMenus.openTimerMenu(p, args[1]);
                return true;
            }
        }

        if(args.length == 4) {
            if(args[0].equalsIgnoreCase("set")) {
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
