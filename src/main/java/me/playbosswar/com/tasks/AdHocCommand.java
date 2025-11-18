package me.playbosswar.com.tasks;

import me.playbosswar.com.enums.Gender;

import java.time.ZonedDateTime;
import java.util.UUID;

public class AdHocCommand {
    private UUID id;
    private String command;
    private Gender gender;
    private ZonedDateTime scheduledTime;
    private boolean executed;

    public AdHocCommand(String command, Gender gender, ZonedDateTime scheduledTime) {
        this.id = UUID.randomUUID();
        this.command = command;
        this.gender = gender;
        this.scheduledTime = scheduledTime;
        this.executed = false;
    }

    AdHocCommand() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public ZonedDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(ZonedDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }
}

