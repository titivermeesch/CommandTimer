package me.playbosswar.com.api;

public class NeededValue<T> implements INeededValue<T> {
    // Name that will be saved in config (should be YAML compliant)
    private final String name;
    // Visual name for the value
    private final String label;
    // Default type
    private final T defaultValue;
    private final Class<T> type;

    public NeededValue(String name, String label, Class<T> type, T defaultValue) {
        this.name = name;
        this.label = label;
        this.defaultValue = defaultValue;
        this.type = type;
    }

    @Override
    public boolean inputIsValid(T value) {
        return true;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public Class<T> getType() {
        return type;
    }
}
