package me.playbosswar.com.tasks;

import java.time.ZonedDateTime;

import org.jetbrains.annotations.Nullable;

public class ScheduledTask {
    private final Task task;
    private final ZonedDateTime date;
    @Nullable
    private final TaskTime taskTime;

    public ScheduledTask(Task task, ZonedDateTime date) {
        this(task, date, null);
    }

    public ScheduledTask(Task task, ZonedDateTime date, @Nullable TaskTime taskTime) {
        this.task = task;
        this.date = date;
        this.taskTime = taskTime;
    }

    public Task getTask() {
        return task;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    @Nullable
    public TaskTime getTaskTime() {
        return taskTime;
    }
}
