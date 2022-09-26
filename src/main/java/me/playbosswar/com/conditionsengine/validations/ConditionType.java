package me.playbosswar.com.conditionsengine.validations;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;

public enum ConditionType {
    AND(LanguageKey.AND_CONDITION),
    OR(LanguageKey.OR_CONDITION),
    SIMPLE(LanguageKey.SIMPLE_CONDITION),
    NOT(LanguageKey.NOT_CONDITION);

    final String description;

    ConditionType(LanguageKey languageKey) {
        final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
        this.description = languageManager.get(languageKey);
    }

    public String getDescription() {
        return description;
    }
}
