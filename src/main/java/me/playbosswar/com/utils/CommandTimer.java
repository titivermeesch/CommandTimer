package me.playbosswar.com.utils;

import org.bukkit.World;

import java.time.LocalTime;
import java.util.ArrayList;

public class CommandTimer {
    private String name;
    private ArrayList<String> commands = new ArrayList<>();
    private Gender gender = Gender.CONSOLE;
    private int seconds = 5;
    private ArrayList<String> times = new ArrayList<>();
    private Boolean useMinecraftTime = false;
    private double random = 1;
    private Boolean executePerUser = false;
    private ArrayList<String> worlds = new ArrayList<>();
    private ArrayList<String> days = new ArrayList<>();
    private int executionLimit = -1;
    private int timesExecuted = 0;
    private LocalTime lastExecuted = LocalTime.now();

    public CommandTimer(String name) {
        this.name = name;
        this.days.add("MONDAY");
        this.days.add("TUESDAY");
        this.days.add("WEDNESDAY");
        this.days.add("THURSDAY");
        this.days.add("FRIDAY");
        this.days.add("SATURDAY");
        this.days.add("SUNDAY");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getCommands() {
        return commands;
    }

    public void setCommands(ArrayList<String> commands) {
        this.commands = commands;
    }

    public void addCommand(String command) { this.commands.add(command); }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public ArrayList<String> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<String> times) {
        this.times = times;
    }

    public void addTime(String time) { this.times.add(time); }

    public Boolean getUseMinecraftTime() {
        return useMinecraftTime;
    }

    public void setUseMinecraftTime(Boolean useMinecraftTime) {
        this.useMinecraftTime = useMinecraftTime;
    }

    public double getRandom() {
        return random;
    }

    public void setRandom(double random) {
        this.random = random;
    }

    public Boolean getExecutePerUser() {
        return executePerUser;
    }

    public void setExecutePerUser(Boolean executePerUser) {
        this.executePerUser = executePerUser;
    }

    public ArrayList<String> getWorlds() {
        return worlds;
    }

    public void setWorlds(ArrayList<String> worlds) {
        this.worlds = worlds;
    }

    public ArrayList<String> getDays() {
        return days;
    }

    public void setDays(ArrayList<String> days) {
        this.days = days;
    }

    public int getExecutionLimit() {
        return executionLimit;
    }

    public void setExecutionLimit(int executionLimit) {
        this.executionLimit = executionLimit;
    }

    public int getTimesExecuted() {
        return timesExecuted;
    }

    public void setTimesExecuted(int timesExecuted) {
        this.timesExecuted = timesExecuted;
    }

    public LocalTime getLastExecuted() {
        return lastExecuted;
    }

    public void setLastExecuted(LocalTime lastExecuted) {
        this.lastExecuted = lastExecuted;
    }
}
