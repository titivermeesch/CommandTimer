package me.playbosswar.com.events;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.updater.Updater;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvents implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().isOp()) {
            return;
        }

        if (!CommandTimerPlugin.getUpdater().getResult().equals(Updater.Result.UPDATE_FOUND)) {
            return;
        }

        if (!CommandTimerPlugin.getPlugin().getConfig().getBoolean("showUpdateMessage")) {
            return;
        }

        e.getPlayer().sendMessage("§6A new version of CommandTimer is available. Please visit https://www.spigotmc.org/resources/command-timer.24141/ and download the latest version");
    }
}
