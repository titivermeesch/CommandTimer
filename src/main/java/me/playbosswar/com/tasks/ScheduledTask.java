package me.playbosswar.com.tasks;

import java.time.ZonedDateTime;

public class ScheduledTask {
    private final Task task;
    private final ZonedDateTime date;

    public ScheduledTask(Task task, ZonedDateTime date) {
        this.task = task;
        this.date = date;
    }

    public Task getTask() {
        return task;
    }
    
    public ZonedDateTime getDate() {
        return date;
    }
}
