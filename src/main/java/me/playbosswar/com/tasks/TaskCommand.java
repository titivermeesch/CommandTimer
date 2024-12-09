package me.playbosswar.com.tasks;

import me.playbosswar.com.enums.Gender;

public class TaskCommand {
    private String command;
    private Gender gender;
    private TaskInterval interval;
    private TaskInterval delay;
    private String description = "";

    public TaskCommand(String command, Gender gender) {
        this.command = command;
        this.gender = gender;
        this.interval = new TaskInterval(0, 0, 0, 0);
        this.delay = new TaskInterval(0, 0, 0, 0);
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

    public void toggleGender() {
        if (gender.equals(Gender.OPERATOR)) {
            setGender(Gender.PLAYER);
            return;
        }

        if (gender.equals(Gender.PLAYER)) {
            setGender(Gender.CONSOLE);
            return;
        }

        if (gender.equals(Gender.CONSOLE)) {
            setGender(Gender.CONSOLE_PER_USER);
            return;
        }

        if (gender.equals(Gender.CONSOLE_PER_USER)) {
            setGender(Gender.CONSOLE_PER_USER_OFFLINE);
            return;
        }

        if (gender.equals(Gender.CONSOLE_PER_USER_OFFLINE)) {
            setGender(Gender.CONSOLE_PROXY);
            return;
        }

        if (gender.equals(Gender.CONSOLE_PROXY)) {
            setGender(Gender.OPERATOR);
        }
    }

    public TaskInterval getInterval() {
        return interval;
    }

    public void setInterval(TaskInterval interval) {
        this.interval = interval;
    }

    public TaskInterval getDelay() {
        return delay;
    }

    public void setDelay(TaskInterval delay) {
        this.delay = delay;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
