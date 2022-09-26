package me.playbosswar.com.hooks;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;

import java.util.List;

public enum HookType {
    PAPI(LanguageKey.PAPI_INTEGRATION_LABEL, LanguageKey.PAPI_INTEGRATION_DESCRIPTION),
    METRICS(LanguageKey.METRICS_INTEGRATION_LABEL, LanguageKey.METRICS_INTEGRATION_DESCRIPTION);

    private final LanguageKey displayName;
    private final LanguageKey loreDescription;

    HookType(LanguageKey displayName, LanguageKey loreDescription) {
        this.displayName = displayName;
        this.loreDescription = loreDescription;
    }

    public String getDisplayName() {
        LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
        return languageManager.get(displayName);
    }

    public List<String> getLoreDescription() {
        LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
        return languageManager.getList(loreDescription);
    }
}
