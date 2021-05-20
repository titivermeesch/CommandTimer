package me.playbosswar.com.tasks;

import me.playbosswar.com.utils.Gender;

public class TaskCommand {
    private String command;
    private Gender gender;
    private final Task task;

    public TaskCommand(Task task, String command, Gender gender) {
        this.task = task;
        this.command = command;
        this.gender = gender;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Task getTask() {
        return task;
    }
}
