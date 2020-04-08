package me.playbosswar.com;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(!sender.hasPermission("commandtimer.use")) {
            sender.sendMessage("§cYou don't have the right permissions to do this");
            return true;
        }

        if (args.length == 1 ) {
            if(args[0].equals("reload")) {
                Tools.reloadTasks();
                sender.sendMessage(Tools.color("&6CommandTimer reloaded!"));
                return true;
            }

            if(args[0].equals("worldtime")) {
                if(!(sender instanceof Player)) {
                    Bukkit.getConsoleSender().sendMessage("This message needs to be executed by a player");
                    return true;
                }

                Player p = (Player) sender;

                World w = p.getWorld();

                p.sendMessage("World time: " + Tools.calculateWorldTime(w) + " and world time raw is " + w.getTime());
            }

            return true;
        }
        sender.sendMessage(Tools.color("&cYour command is invalid."));
        return true;
    }

}