package me.playbosswar.com.language;

import io.sentry.Sentry;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LanguageManager {
    private final Plugin plugin;
    private final String language;
    private final Map<LanguageKey, String> translations = new HashMap<>();

    public LanguageManager(Plugin plugin) {
        this(plugin, "en");
    }

    public LanguageManager(Plugin plugin, String language) {
        this.plugin = plugin;
        this.language = language;

        try {
            validateConfiguration();
            loadLanguage();
        } catch(Exception e) {
            e.printStackTrace();
            Sentry.captureException(e);
        }
    }

    private void validateConfiguration() throws Exception {
        JSONObject obj = getCurrentLanguage();
        for(LanguageKey languageKey : LanguageKey.values()) {
            if(obj.get(languageKey.toString().toLowerCase()) == null) {
                throw new Exception("Translation file " + language + " is missing the key " + languageKey.toString().toLowerCase());
            }
        }
    }

    private void loadLanguage() throws Exception {
        JSONObject obj = getCurrentLanguage();

        obj.keySet().forEach(rawKey -> {
            String tag = (String) rawKey;
            LanguageKey languageKey = LanguageKey.getByTag(tag);
            if(languageKey == null) {
                try {
                    Bukkit.getPluginManager().disablePlugin(CommandTimerPlugin.getPlugin());
                    throw new Exception("Could not find translation for " + tag + " for language " + language);
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            }
            translations.put(languageKey, (String) obj.get(tag));
        });
    }

    private JSONObject getCurrentLanguage() throws IOException, ParseException {
        String fileName = plugin.getDataFolder().getAbsolutePath() + "/languages/" + language + ".json";
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(new FileReader(fileName));
    }

    public String get(LanguageKey key) {
        return Messages.colorize(translations.get(key));
    }

    public String get(LanguageKey key, String... replacers) {
        String translation = Messages.colorize(translations.get(key));

        for(int i = 1; i <= replacers.length; i++) {
            String placeholder = "$" + i;
            translation = translation.replace(placeholder, replacers[i - 1]);
        }

        return translation;
    }

    public ArrayList<String> getList(LanguageKey key, String... replacers) {
        return new ArrayList<>(Arrays.asList(get(key, replacers).split("\n")));
    }

    public ArrayList<String> getList(LanguageKey key) {
        return new ArrayList<>(Arrays.asList(get(key).split("\n")));
    }
}
