package me.playbosswar.com.hooks;

import me.playbosswar.com.CommandTimerPlugin;
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
    private final List<HookType> loadedHooks = new ArrayList<>();

    public HooksManager() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            hookIntoPAPI();
        }

        if(CommandTimerPlugin.getInstance().getMetrics().isEnabled()) {
            loadedHooks.add(HookType.METRICS);
        }
    }

    private void hookIntoPAPI() {
        Plugin plugin = CommandTimerPlugin.getPlugin();
        new PAPIPlaceholders(CommandTimerPlugin.getPlugin()).register();
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Messages.sendConsole("&eCommandTimer hooked in PlaceholderAPI");
        loadedHooks.add(HookType.PAPI);
    }

    public boolean isHookEnabled(HookType hookType) { return loadedHooks.contains(hookType); }

    public List<HookType> getLoadedHooks() { return loadedHooks; }
}
