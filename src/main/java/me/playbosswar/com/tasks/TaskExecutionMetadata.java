package me.playbosswar.com.tasks;

import java.util.Date;

// Data class only used to store metadata in local json files
public class TaskExecutionMetadata {
    private int timesExecuted = 0;
    private int lastExecutedCommandIndex = 0;
    private Date lastExecuted = new Date();

    public TaskExecutionMetadata(int timesExecuted, int lastExecutedCommandIndex, Date lastExecuted) {
        this.timesExecuted = timesExecuted;
        this.lastExecutedCommandIndex = lastExecutedCommandIndex;
        this.lastExecuted = lastExecuted;
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
