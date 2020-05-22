package me.playbosswar.com.commands;

import me.playbosswar.com.Tools;
import me.playbosswar.com.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldTimeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage("This command needs to be executed by a player");
            return true;
        }

        Player p = (Player) sender;

        if(!p.hasPermission("commandtimer.worldtime")) {
            Messages.sendMessageToPlayer(p, "&cYou don't have the right permission to do this");
            return true;
        }

        World w = p.getWorld();

        Messages.sendMessageToPlayer(p, "World time: " + Tools.calculateWorldTime(w));
        return true;
    }
}
