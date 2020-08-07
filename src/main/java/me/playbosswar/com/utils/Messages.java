package me.playbosswar.com.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messages {
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Get plugin prefix
     * @return prefix
     */
    private static String getPrefix() {
        return "&a[CommandTimer] &f";
    }

    /**
     * Send a message to a player
     * @param player
     * @param message
     */
    public static void sendMessageToPlayer(Player player, String message) {
        player.sendMessage(colorize(getPrefix() + message));
    }

    /**
     * Send a message to the console
     * @param message
     */
    public static void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(colorize(getPrefix() + message));
    }

    /**
     * Send a message to anyone
     * @param sender
     * @param message
     */
    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(colorize("&a[CommandTimer] &f" + message));
    }

    /**
     * Send need to be a player message to sender
     * @param sender
     */
    public static void sendNeedToBePlayer(CommandSender sender) {
        sender.sendMessage(colorize(getPrefix() + "&cYou need to be a player to do this"));
    }

    /**
     * Send message if player has no correct permission
     * @param player
     */
    public static void sendNoPermission(Player player) {
        player.sendMessage(colorize(getPrefix() + "&cYou don't have the right permission to do this."));
    }

    /**
     * Send message if IO writing failed
     * @param player
     */
    public static void sendFailedIO(Player player) {
        Messages.sendMessageToPlayer(player, "&cCould not update file on disk");
    }
}
