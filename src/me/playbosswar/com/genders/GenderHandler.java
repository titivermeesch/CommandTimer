package me.playbosswar.com.genders;

import me.playbosswar.com.Main;

public class GenderHandler {
    public enum Gender {
        CONSOLE,
        OPERATOR,
        PLAYER
    }

    public static Gender getGender(final String task) {
        final String gender = Main.getPlugin().getConfig().getString("tasks." + task + ".gender").toLowerCase();

        if(gender == "console") return Gender.CONSOLE;
        if(gender == "player") return Gender.PLAYER;
        if(gender == "operator") return Gender.OPERATOR;
        return Gender.CONSOLE;
    }
}
