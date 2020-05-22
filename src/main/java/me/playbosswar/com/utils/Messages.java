package me.playbosswar.com.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messages {
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendMessageToPlayer(Player p, String message) {
        p.sendMessage(colorize("&6&l[CommandTimer] &f" + message));
    }

    public static void sendConsole(String str) {
        Bukkit.getConsoleSender().sendMessage(colorize("&a[CommandTimer] " + str));
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(colorize("&a[CommandTimer] &f" + message));
    }
}
