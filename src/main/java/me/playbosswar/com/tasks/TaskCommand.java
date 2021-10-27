package me.playbosswar.com.tasks;

import me.playbosswar.com.enums.Gender;

import java.util.UUID;

public class TaskCommand {
    private final UUID uuid;
    private String command;
    private Gender gender;
    private transient Task task;

    public TaskCommand(Task task, UUID uuid, String command, Gender gender) {
        this.task = task;
        this.uuid = uuid;
        this.command = command;
        this.gender = gender;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
        task.storeInstance();
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
        task.storeInstance();
    }

    public void toggleGender() {
        if(gender.equals(Gender.OPERATOR)) {
            gender = Gender.PLAYER;
            task.storeInstance();
            return;
        }

        if(gender.equals(Gender.PLAYER)) {
            gender = Gender.CONSOLE;
            task.storeInstance();
            return;
        }

        if(gender.equals(Gender.CONSOLE)) {
            gender = Gender.CONSOLE_PER_USER;
            task.storeInstance();
            return;
        }

        if(gender.equals(Gender.CONSOLE_PER_USER)) {
            gender = Gender.OPERATOR;
            task.storeInstance();
            return;
        }
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public UUID getUuid() {
        return uuid;
    }
}
