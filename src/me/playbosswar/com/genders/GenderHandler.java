package me.playbosswar.com.genders;

import me.playbosswar.com.Main;
import org.bukkit.Bukkit;

public class GenderHandler {
    public enum Gender {
        CONSOLE,
        OPERATOR,
        PLAYER
    }

    public static Gender getGender(final String task) {
        final String gender = Main.getPlugin().getConfig().getString("tasks." + task + ".gender").toLowerCase();
        Bukkit.getConsoleSender().sendMessage("Found fender is " + gender);

        if(gender.equals("console")) return Gender.CONSOLE;
        if(gender.equals("player")) return Gender.PLAYER;
        if(gender.equals("operator")) return Gender.OPERATOR;
        return Gender.CONSOLE;
    }
}
