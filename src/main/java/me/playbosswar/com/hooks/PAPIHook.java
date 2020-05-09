package me.playbosswar.com.hooks;

import me.clip.placeholderapi.PlaceholderAPI;
import me.playbosswar.com.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class PAPIHook {
    public static String parsePAPI(String str, OfflinePlayer p) {
        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return str;
        }

        new PAPIPlaceholders(Main.getPlugin()).register();
        return PlaceholderAPI.setPlaceholders(p, str);
    }
}
