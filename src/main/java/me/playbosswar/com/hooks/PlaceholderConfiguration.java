package me.playbosswar.com.hooks;

public class PlaceholderConfiguration {
    private String taskName;
    private String fallbackMessage;
    private String placeholderType;
    private boolean valid = true;

    public PlaceholderConfiguration(String placeholder) {
        String[] parts = placeholder.split("_");

        if(parts.length < 1) {
            this.valid = false;
            return;
        }

        this.taskName = parts[0];
        this.placeholderType = parts[1];
        if(parts.length == 3) {
            this.fallbackMessage = parts[2];
        }
    }

    public String getTaskName() {
        return taskName;
    }

    public String getFallbackMessage() {
        return fallbackMessage;
    }

    public String getPlaceholderType() {
        return placeholderType;
    }

    public boolean isValid() {
        return valid;
    }
}
