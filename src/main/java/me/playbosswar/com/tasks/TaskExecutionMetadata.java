package me.playbosswar.com.tasks;

import java.util.Date;
import java.util.UUID;

public class TaskExecutionMetadata {
    private UUID taskId;
    private int timesExecuted = 0;
    private int lastExecutedCommandIndex = 0;
    private Date lastExecuted = new Date();

    public TaskExecutionMetadata(UUID taskId, int timesExecuted, int lastExecutedCommandIndex, Date lastExecuted) {
        this.taskId = taskId;
        this.timesExecuted = timesExecuted;
        this.lastExecutedCommandIndex = lastExecutedCommandIndex;
        this.lastExecuted = lastExecuted;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public int getTimesExecuted() {
        return timesExecuted;
    }

    public void setTimesExecuted(int timesExecuted) {
        this.timesExecuted = timesExecuted;
    }

    public int getLastExecutedCommandIndex() {
        return lastExecutedCommandIndex;
    }

    public void setLastExecutedCommandIndex(int lastExecutedCommandIndex) {
        this.lastExecutedCommandIndex = lastExecutedCommandIndex;
    }

    public Date getLastExecuted() {
        return lastExecuted;
    }

    public void setLastExecuted(Date lastExecuted) {
        this.lastExecuted = lastExecuted;
    }
}
