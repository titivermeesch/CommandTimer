package me.playbosswar.com.language;

import io.sentry.Sentry;
import me.playbosswar.com.utils.Messages;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            loadLanguage();
        } catch(IOException | ParseException e) {
            e.printStackTrace();
            Sentry.captureException(e);
        }
    }

    private void loadLanguage() throws IOException, ParseException {
        String fileName = plugin.getDataFolder().getAbsolutePath() + "/languages/" + language + ".json";
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(new FileReader(fileName));

        obj.keySet().forEach(rawKey -> {
            String tag = (String) rawKey;
            LanguageKey languageKey = LanguageKey.getByTag(tag);
            translations.put(languageKey, tag);
        });
    }

    public String get(LanguageKey key) {
        return Messages.colorize(translations.get(key));
    }

    public String get(LanguageKey key, String... replacers) {
        String translation = Messages.colorize(translations.get(key));

        for(int i = 1; i <= replacers.length; i++) {
            String placeholder = "$" + i;
            translation = translation.replace(placeholder, replacers[i]);
        }

        return translation;
    }

    public List<String> getList(LanguageKey key, String... replacers) {
        return List.of(get(key, replacers).split("\n"));
    }

    public List<String> getList(LanguageKey key) {
        return List.of(get(key).split("\n"));
    }
}
