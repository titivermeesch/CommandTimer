package me.playbosswar.com.permissions;

import org.bukkit.command.CommandSender;

public class PermissionUtils {
    public static boolean playerHasSomeAccess(CommandSender sender) {
        return sender.hasPermission("commandtimer.manage")
                || sender.hasPermission("commandtimer.execute")
                || sender.hasPermission("commandtimer.activate")
                || sender.hasPermission("commandtimer.deactivate")
                || sender.hasPermission("commandtimer.update")
                || sender.hasPermission("commandtimer.schedule")
                || sender.hasPermission("commandtimer.toggle");
    }
}
