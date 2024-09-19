package me.playbosswar.com.tasks;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TaskTime {
    private LocalTime time1;
    private LocalTime time2;
    private boolean isMinecraftTime;
    private String world;

    public TaskTime(LocalTime time1, boolean isMinecraftTime) {
        this.time1 = time1;
        this.isMinecraftTime = isMinecraftTime;
    }

    public LocalTime getTime1() {
        return time1;
    }

    public void setTime1(LocalTime time1) {
        this.time1 = time1;
    }

    public LocalTime getTime2() {
        return time2;
    }

    public boolean isRange() { return getTime2() != null; }

    public void setTime2(LocalTime time2) {
        this.time2 = time2;
    }

    public boolean isMinecraftTime() {
        return isMinecraftTime;
    }

    public void setMinecraftTime(boolean minecraftTime) {
        if(!minecraftTime) {
            world = null;
        }

        isMinecraftTime = minecraftTime;
    }

    public void toggleMinecraftTime() {
        if(isMinecraftTime) {
            world = null;
        }

        isMinecraftTime = !isMinecraftTime;
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
    }
}
