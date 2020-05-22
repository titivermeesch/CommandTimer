package me.playbosswar.com.utils;

import java.util.ArrayList;

public class CommandsManager {
    private static ArrayList<CommandTimer> timers = new ArrayList<>();

    public static void addCommandTimer(CommandTimer t) {
        timers.add(t);
    }

    public static void removeCommandTimer(String name) {
        for(CommandTimer t : timers) {
            if(t.getName().equals(name)) {
                timers.remove(t);
                return;
            }
        }
    }

    public static CommandTimer getCommandTimer(String name) {
        for(CommandTimer t : timers) {
            return t;
        }
        return null;
    }
}
