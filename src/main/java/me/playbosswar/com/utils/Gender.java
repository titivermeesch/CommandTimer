package me.playbosswar.com.utils;

public enum Gender {
    CONSOLE("console"),
    PLAYER("player"),
    OPERATOR("operator");

    final String key;

    private Gender(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
