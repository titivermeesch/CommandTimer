package me.playbosswar.com.hooks;

import me.playbosswar.com.Main;
import me.playbosswar.com.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Main manager that handles all external API's
 */
public class HooksManager implements Listener {
    private List<HookType> loadedHooks = new ArrayList<>();

    public HooksManager() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            hookIntoPAPI();
        }
    }

    private void hookIntoPAPI() {
        Plugin plugin = Main.getPlugin();
        new PAPIPlaceholders(Main.getPlugin()).register();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Messages.sendConsole("&eCommandTimer hooked in PlaceholderAPI");
        loadedHooks.add(HookType.PAPI);
    }

    public boolean isHookEnabled(HookType hookType) { return loadedHooks.contains(hookType); }

    public List<HookType> getLoadedHooks() { return loadedHooks; }
}
