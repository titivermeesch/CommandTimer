package me.playbosswar.com.tasks;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TaskTime {
    private transient Task task;
    private LocalTime time1;
    private LocalTime time2;
    private boolean isMinecraftTime;
    private String world;

    public TaskTime(Task task, LocalTime time1, boolean isMinecraftTime) {
        this.task = task;
        this.time1 = time1;
        this.isMinecraftTime = isMinecraftTime;
    }

    public LocalTime getTime1() {
        return time1;
    }

    public void setTime1(LocalTime time1) {
        this.time1 = time1;
        task.storeInstance();
    }

    public LocalTime getTime2() {
        return time2;
    }

    public boolean isRange() { return getTime2() != null; }

    public void setTime2(LocalTime time2) {
        this.time2 = time2;
        task.storeInstance();
    }

    public boolean isMinecraftTime() {
        return isMinecraftTime;
    }

    public void setMinecraftTime(boolean minecraftTime) {
        if(!minecraftTime) {
            world = null;
        }

        isMinecraftTime = minecraftTime;
        task.storeInstance();
    }

    public void toggleMinecraftTime() {
        if(isMinecraftTime) {
            world = null;
        }

        isMinecraftTime = !isMinecraftTime;
        task.storeInstance();
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        if (time2 != null) {
            return "[" + time1.format(formatter) + ":" + time2.format(formatter) + "]";
        }


        return time1.format(formatter);
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
        task.storeInstance();
    }
}
