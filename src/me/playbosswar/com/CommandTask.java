package me.playbosswar.com;

import me.playbosswar.com.genders.GenderHandler.Gender;
import me.playbosswar.com.hooks.PAPIHook;

import java.util.List;

public class CommandTask implements Runnable {
    private List<String> commands;
    private Gender gender;
    private String task;

    CommandTask(List<String> commands, Gender gender, String task) {
        this.commands = commands;
        this.gender = gender;
        this.task = task;
    }

    public void run() {
        for (String command : this.commands) {
            command = PAPIHook.parsePAPI(command, null);
            Tools.executeCommand(task, command, gender);
        }
    }
}
