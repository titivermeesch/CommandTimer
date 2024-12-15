package me.playbosswar.com.queue;

import java.util.Date;

import me.playbosswar.com.tasks.TaskCommand;
import me.playbosswar.com.tasks.TaskInterval;

public class QueuedCommand {
    private final TaskCommand taskCommand;
    private final TaskInterval delay;
    private final Date queuedAt;

    public QueuedCommand(TaskCommand taskCommand, TaskInterval delay) {
        this.taskCommand = taskCommand;
        this.delay = delay;
        this.queuedAt = new Date();
    }

    public TaskCommand getTaskCommand() {
        return taskCommand;
    }

    public TaskInterval getDelay() {
        return delay;
    }

    public Date getQueuedAt() {
        return queuedAt;
    }
}
