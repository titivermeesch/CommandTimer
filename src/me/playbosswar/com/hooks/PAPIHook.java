package me.playbosswar.com.hooks;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class PAPIHook {

    public static String parsePAPI(String str, OfflinePlayer p) {
        if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) return str;
        return PlaceholderAPI.setPlaceholders(p, str);
    }

}
