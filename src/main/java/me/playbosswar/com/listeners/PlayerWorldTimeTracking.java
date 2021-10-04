package me.playbosswar.com.listeners;

import me.playbosswar.com.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class PlayerWorldTimeTracking implements Listener {
    private final BukkitRunnable runnable;
    private final Map<Player, Integer> secondsInWorld = new HashMap<>();

    public PlayerWorldTimeTracking() {
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                secondsInWorld.forEach((player, value) -> secondsInWorld.put(player, value + 1));
            }
        };

        runnable.runTaskTimer(Main.getPlugin(), 10L, 20L);
    }

    public void cancel() { runnable.cancel(); }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        secondsInWorld.put(e.getPlayer(), 0);
    }

    @EventHandler
    private void onLeave(PlayerQuitEvent e) {
        secondsInWorld.remove(e.getPlayer());
    }

    @EventHandler
    private void onWorldChange(PlayerTeleportEvent e) {
        if (e.getFrom().getWorld().equals(e.getTo().getWorld())) {
            return;
        }

        secondsInWorld.replace(e.getPlayer(), 0);
    }

    public Map<Player, Integer> getSecondsInWorld() {
        return secondsInWorld;
    }

    public int getSecondsInWorldForPlayer(Player p) { return secondsInWorld.get(p); }
}
