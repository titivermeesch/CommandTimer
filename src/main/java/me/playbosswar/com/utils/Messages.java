package me.playbosswar.com.utils;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messages {
    private static final boolean debug = CommandTimerPlugin.getPlugin().getConfig().getBoolean("debug");

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Get plugin prefix
     *
     * @return prefix
     */
    private static String getPrefix() {
        return CommandTimerPlugin.getLanguageManager().get(LanguageKey.PREFIX);
    }

    /**
     * Send a message to a player
     *
     * @param player
     * @param message
     */
    public static void sendMessageToPlayer(Player player, String message) {
        player.sendMessage(colorize(getPrefix() + message));
    }

    /**
     * Send a message to the console
     *
     * @param message
     */
    public static void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(colorize(getPrefix() + message));
    }

    public static void sendDebugConsole(String message) {
        if(debug) {
            sendConsole(message);
        }
    }

    /**
     * Send a message to anyone
     *
     * @param sender
     * @param message
     */
    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(colorize(getPrefix() + message));
    }

    public static void sendMessage(CommandSender sender, LanguageKey key) {
        String message = CommandTimerPlugin.getLanguageManager().get(key);
        sender.sendMessage(colorize(getPrefix() + message));
    }

    /**
     * Send need to be a player message to sender
     *
     * @param sender
     */
    public static void sendNeedToBePlayer(CommandSender sender) {
        sender.sendMessage(colorize(getPrefix() + CommandTimerPlugin.getLanguageManager().get(LanguageKey.NO_CONSOLE)));
    }

    /**
     * Send message if player has no correct permission
     *
     * @param player
     */
    public static void sendNoPermission(Player player) {
        player.sendMessage(colorize(getPrefix() + CommandTimerPlugin.getLanguageManager().get(LanguageKey.NO_PERMISSION)));
    }

    public static void sendNoPermission(CommandSender sender) {
        sender.sendMessage(colorize(getPrefix() + CommandTimerPlugin.getLanguageManager().get(LanguageKey.NO_PERMISSION)));
    }

    /**
     * Send message if IO writing failed
     *
     * @param player
     */
    public static void sendFailedIO(Player player) {
        Messages.sendMessageToPlayer(player, CommandTimerPlugin.getLanguageManager().get(LanguageKey.IO_ERROR));
    }

    public static void sendHelpMessage(CommandSender sender) {
        Messages.sendMessage(sender, "§7--- §eCommandTimer Help §7---");
        Messages.sendMessage(sender, "§e/cmt help - §7Open help menu");
        Messages.sendMessage(sender, "§e/cmt activate <task> - §7Activate a task");
        Messages.sendMessage(sender, "§e/cmt deactivate <task> - §7Deactivate a task");
        Messages.sendMessage(sender, "§e/cmt execute <task> - §7Instantly execute a task");
        Messages.sendMessage(sender, "§e/cmt nextexecutions <task> - §7Show next 10 executions for a task");
        Messages.sendMessage(sender, "§e/cmt schedule [-after 1h10m5s] [-gender CONSOLE] <command> - §7Schedule a one-time command");
        Messages.sendMessage(sender, "§e/cmt reload - §7Reload the plugin");
        Messages.sendMessage(sender, "§e/cmt migrateToDatabase - §7Migrate JSON files to database");
    }
}
