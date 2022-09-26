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
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

/**
 * Take note that this in the only class where we cannot use our integrated logging system since LanguageManager may
 * not be fully loaded yet
 */
public class LanguageManager {
    private final Plugin plugin;
    private final String selectedLanguage;
    private final Map<LanguageKey, String> translations = new HashMap<>();

    public LanguageManager(Plugin plugin) {
        this(plugin, "en");
    }

    public LanguageManager(Plugin plugin, String language) {
        this.plugin = plugin;
        this.selectedLanguage = language;

        try {
            validateConfiguration();
            loadLanguage();
        } catch(Exception e) {
            e.printStackTrace();
            Sentry.captureException(e);
        }
    }

    private void validateConfiguration() throws Exception {
        boolean fileSaveRequired = false;
        JSONObject selectedLanguageObject = getLanguageObject(selectedLanguage);
        JSONObject defaultLanguageObject = getLanguageObject("default");
        for(LanguageKey languageKey : LanguageKey.values()) {
            if(selectedLanguageObject.get(languageKey.toString().toLowerCase()) == null) {
                Bukkit.getLogger().log(Level.WARNING,
                        "Translation file " + selectedLanguage + " is missing the key " + languageKey.toString().toLowerCase() + ", adding default value");
                selectedLanguageObject.put(
                        languageKey.toString().toLowerCase(),
                        defaultLanguageObject.get(languageKey.toString().toLowerCase()));
                fileSaveRequired = true;
            }
        }

        if(fileSaveRequired) {
            try {
                String filePath = getLanguageFilePath(selectedLanguage);
                FileWriter jsonFile = new FileWriter(filePath);
                jsonFile.write(selectedLanguageObject.toJSONString());
                jsonFile.flush();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void loadLanguage() throws Exception {
        JSONObject obj = getLanguageObject(selectedLanguage);

        obj.keySet().forEach(rawKey -> {
            String tag = (String) rawKey;
            LanguageKey languageKey = LanguageKey.getByTag(tag);
            if(languageKey == null) {
                try {
                    Bukkit.getPluginManager().disablePlugin(CommandTimerPlugin.getPlugin());
                    throw new Exception("Could not find translation for " + tag + " for language " + selectedLanguage);
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            }
            translations.put(languageKey, (String) obj.get(tag));
        });
    }

    private JSONObject getLanguageObject(String language) throws IOException, ParseException {
        String fileName = getLanguageFilePath(language);
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(new FileReader(fileName));
    }

    private String getLanguageFilePath(String language) {
        return plugin.getDataFolder().getAbsolutePath() + "/languages/" + language + ".json";
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
