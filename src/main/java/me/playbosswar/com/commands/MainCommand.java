package me.playbosswar.com.commands;

import me.playbosswar.com.Tools;
import me.playbosswar.com.utils.CommandParam;
import me.playbosswar.com.utils.Files;
import me.playbosswar.com.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!sender.hasPermission("commandtimer.use")) {
            sender.sendMessage("§cYou don't have the right permissions to do this");
            return true;
        }

        if (args.length == 1) {
            if (args[0].equals("reload")) {
                Tools.reloadTasks();
                Messages.sendMessage(sender, "Reloaded");
                return true;
            }
        }

        if(!(sender instanceof Player)) {
            Messages.sendMessage(sender, "You need to be a player to do this");
            return true;
        }

        Player p = (Player) sender;

        if(args.length == 2) {
            if(args[0].equals("create")) {
                Files.createNewCommandTimerDataFile(p, args[1]);
                return true;
            }
        }

        // /cmt set <name> <param> <value>

        if(args.length == 4) {
            if(args[0].equals("set")) {
                if(!Files.commandTimerFileExists(args[1])) {
                    Messages.sendMessageToPlayer(p, "&cThis timer does not exist");
                    return true;
                }

                CommandParam param = CommandParam.valueOf(args[1]);
                Messages.sendMessageToPlayer(p, param.toString());

                if(param.equals(null)) {
                    Messages.sendMessageToPlayer(p, "&cThis param does not exist");
                    return true;
                }

                
            }
        }


        Messages.sendMessageToPlayer(p, "&9&lThanks for using CommandTimer.");
        Messages.sendMessageToPlayer(p, "&6Please look on the plugin page for extra help");
        Messages.sendMessageToPlayer(p, "&6Don't know where to start? Look in your server plugins folder and open the CommandTimer config.yml");
        return true;
    }
}
