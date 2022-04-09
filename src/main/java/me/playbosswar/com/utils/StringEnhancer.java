package me.playbosswar.com.utils;

import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.Map;

public class StringEnhancer {
    private final Map<String, String> values = new HashMap<>();
    private final String initialString;

    public StringEnhancer(String initialString) {
        this.initialString = initialString;
    }

    public StringEnhancer add(String key, String value) {
        values.put(key, value);
        return this;
    }

    public String parse() {
        StringSubstitutor sub = new StringSubstitutor(values);

        return sub.replace(initialString);
    }
}
