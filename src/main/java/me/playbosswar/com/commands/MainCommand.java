package me.playbosswar.com.commands;

import me.playbosswar.com.Tools;
import me.playbosswar.com.chat.ChatMenus;
import me.playbosswar.com.utils.CommandTimer;
import me.playbosswar.com.utils.TimerManager;
import me.playbosswar.com.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalTime;
import java.util.ArrayList;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            Messages.sendNeedToBePlayer(sender);
            return true;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("commandtimer.use")) {
            Messages.sendNoPermission(p);
            return true;
        }

        if (args.length == 1) {
            if (args[0].equals("reload")) {
                Tools.reloadTasks();
                Messages.sendMessage(sender, "Reloaded");
                return true;
            }

            if (args[0].equalsIgnoreCase("list")) {
                ArrayList<CommandTimer> timers = TimerManager.getAllTimers();

                if (timers.size() == 0) {
                    Messages.sendMessageToPlayer(p, "You don't have timers yet.");
                    Messages.sendMessageToPlayer(p, "Start with /cmt create <name>");
                    return true;
                }

                Messages.sendMessageToPlayer(p, "Here is a list of all your timers");
                Messages.sendMessageToPlayer(p, "See details with /cmt info <name>");

                int i = 1;
                for (CommandTimer timer : timers) {
                    Messages.sendMessageToPlayer(p, i + ": " + timer.getName());
                }
                return true;
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                TimerManager.createNewCommandTimer(p, args[1]);
                return true;
            }

            if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")) {
                TimerManager.deleteCommandtimer(p, args[1]);
                return true;
            }

            if (args[0].equalsIgnoreCase("info")) {
                ChatMenus.openTimerMenu(p, args[1]);
                return true;
            }

            if (args[0].equalsIgnoreCase("execute")) {
                CommandTimer timer = TimerManager.getCommandTimer(args[1]);
                LocalTime time = LocalTime.now();
                LocalTime newTime = time.minusSeconds(timer.getSeconds());
                timer.setLastExecuted(newTime);
                return true;
            }
        }

        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("set")) {
                TimerManager.changeCommandtimerData(p, args[1], args[2], args[3]);
                return true;
            }
        }

        Messages.sendMessageToPlayer(p, "&6/cmt list : See all your created timers");
        Messages.sendMessageToPlayer(p, "&6/cmt create <name> : Create a new timer");
        Messages.sendMessageToPlayer(p, "&6/cmt remove <name> : Remove an existing timer");
        Messages.sendMessageToPlayer(p, "&6/cmt info <name> : Get information and configure a timer");
        return true;
    }
}
