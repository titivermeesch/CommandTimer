package me.playbosswar.com.api;

// Class is used for both conditions engine and events engine
public class NeededValue<T> implements INeededValue<T> {
    // Name that will be saved in config (should be YAML compliant)
    private String name;
    // Visual name for the value
    private String label;
    // Default type
    private T defaultValue;
    private Class<T> type;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }
}
