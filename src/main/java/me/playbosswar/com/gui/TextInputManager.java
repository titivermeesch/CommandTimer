package me.playbosswar.com.gui;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.utils.Callback;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TextInputManager implements Listener {
    private static TextInputManager instance;
    private final Map<UUID, TextInputPrompt> activePrompts = new HashMap<>();
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();

    public static TextInputManager getInstance() {
        if (instance == null) {
            instance = new TextInputManager();
        }
        return instance;
    }

    private TextInputManager() {
        Bukkit.getPluginManager().registerEvents(this, CommandTimerPlugin.getPlugin());
    }

    public void startTextInput(Player player, String prompt, Callback<String> callback) {
        startTextInput(player, prompt, callback, null);
    }

    public void startTextInput(Player player, LanguageKey languageKey, Callback<String> callback) {
        startTextInput(player, languageManager.get(languageKey), callback, null);
    }

    public void startTextInput(Player player, String prompt, Callback<String> callback, Callback<Void> cancelCallback) {
        UUID playerId = player.getUniqueId();
        
        if (activePrompts.containsKey(playerId)) {
            activePrompts.remove(playerId);
        }

        TextInputPrompt textPrompt = new TextInputPrompt(prompt, callback, cancelCallback);
        activePrompts.put(playerId, textPrompt);

        player.sendMessage("§6" + prompt);
        player.sendMessage("§7" + languageManager.get(LanguageKey.TEXT_INPUT_CANCEL_HINT));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        if (!activePrompts.containsKey(playerId)) {
            return;
        }

        event.setCancelled(true);

        TextInputPrompt prompt = activePrompts.remove(playerId);
        String input = event.getMessage();

        if (input.equalsIgnoreCase("cancel") || input.equalsIgnoreCase("exit")) {
            if (prompt.getCancelCallback() != null) {
                prompt.getCancelCallback().execute(null);
            }
            player.sendMessage("§c" + languageManager.get(LanguageKey.TEXT_INPUT_CANCELLED));
            return;
        }

        CommandTimerPlugin.getScheduler().runTask(() -> prompt.getCallback().execute(input));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        activePrompts.remove(event.getPlayer().getUniqueId());
    }

    public boolean hasActivePrompt(Player player) {
        return activePrompts.containsKey(player.getUniqueId());
    }

    public void cancelPrompt(Player player) {
        activePrompts.remove(player.getUniqueId());
    }

    private static class TextInputPrompt {
        private final String prompt;
        private final Callback<String> callback;
        private final Callback<Void> cancelCallback;

        public TextInputPrompt(String prompt, Callback<String> callback, Callback<Void> cancelCallback) {
            this.prompt = prompt;
            this.callback = callback;
            this.cancelCallback = cancelCallback;
        }

        public String getPrompt() {
            return prompt;
        }

        public Callback<String> getCallback() {
            return callback;
        }

        public Callback<Void> getCancelCallback() {
            return cancelCallback;
        }
    }
}
