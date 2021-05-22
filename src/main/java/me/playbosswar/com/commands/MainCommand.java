package me.playbosswar.com.commands;

import me.playbosswar.com.gui.MainMenu;
import me.playbosswar.com.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        new MainMenu().INVENTORY.open(p);
        return true;
    }
}
