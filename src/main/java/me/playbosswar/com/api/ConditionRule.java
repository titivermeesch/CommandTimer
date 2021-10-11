package me.playbosswar.com.api;

import org.jeasy.rules.api.Rule;

import java.util.HashMap;

public interface ConditionRule extends Rule {
    /**
     * Request values that need to be filled in by the user
     */
    HashMap<String, Class<?>> getNeededValues();
}
