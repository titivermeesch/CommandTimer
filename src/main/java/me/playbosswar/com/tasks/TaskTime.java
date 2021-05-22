package me.playbosswar.com.tasks;

public class TaskTime {
    private transient Task task;
    private String time;
    private boolean isMinecraftTime;

    public TaskTime(Task task, String time, boolean isMinecraftTime) {
        this.task = task;
        this.time = time;
        this.isMinecraftTime = isMinecraftTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        task.storeInstance();
    }

    public boolean isMinecraftTime() {
        return isMinecraftTime;
    }

    public void setMinecraftTime(boolean minecraftTime) {
        isMinecraftTime = minecraftTime;
        task.storeInstance();
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
