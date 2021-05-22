package me.playbosswar.com.enums;

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
