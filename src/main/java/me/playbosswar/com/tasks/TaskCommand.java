package me.playbosswar.com.tasks;

import me.playbosswar.com.enums.Gender;

import java.util.UUID;

public class TaskCommand {
    private String command;
    private Gender gender;
    private transient Task task;

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
            setGender(Gender.PLAYER);
            return;
        }

        if(gender.equals(Gender.PLAYER)) {
            setGender(Gender.CONSOLE);
            return;
        }

        if(gender.equals(Gender.CONSOLE)) {
            setGender(Gender.CONSOLE_PER_USER);
            return;
        }

        if(gender.equals(Gender.CONSOLE_PER_USER)) {
            setGender(Gender.CONSOLE_PER_USER_OFFLINE);
            return;
        }

        if(gender.equals(Gender.CONSOLE_PER_USER_OFFLINE)) {
            setGender(Gender.OPERATOR);
        }
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
}
