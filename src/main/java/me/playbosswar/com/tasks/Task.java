package me.playbosswar.com.tasks;

import me.playbosswar.com.enums.Gender;

import java.time.LocalTime;
import java.util.ArrayList;

public class Task {
    private String name;
    private ArrayList<TaskCommand> commands = new ArrayList<>();
    private Gender gender = Gender.CONSOLE;
    private int seconds = 5;
    private ArrayList<String> times = new ArrayList<>();
    private Boolean useMinecraftTime = false;
    private double random = 1.0;
    private Boolean executePerUser = false;
    private ArrayList<String> worlds = new ArrayList<>();
    private ArrayList<String> days = new ArrayList<>();
    private int executionLimit = -1;
    private int timesExecuted = 0;
    private LocalTime lastExecuted = LocalTime.now();
    private String requiredPermission = "";
    private int minPlayers = -1;
    private int maxPlayers = -1;
    private boolean selectRandomCommand = false;
    private boolean active = false;

    public Task(String name) {
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

    public ArrayList<TaskCommand> getCommands() {
        return commands;
    }

    public void setCommands(ArrayList<TaskCommand> commands) {
        this.commands = commands;
    }

    public int addCommand(TaskCommand command) {
        this.commands.add(command);
        return commands.indexOf(command);
    }

    public void removeCommand(TaskCommand command) {
        commands.remove(command);
    }

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

    public void addTime(String time) {
        this.times.add(time);
    }

    public void removeTime(int index) {
        this.times.remove(index);
    }

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

    public void addWorld(String world) {
        this.worlds.add(world);
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

    public String getRequiredPermission() {
        return requiredPermission;
    }

    public void setRequiredPermission(String requiredPermission) {
        this.requiredPermission = requiredPermission;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public boolean isSelectRandomCommand() {
        return selectRandomCommand;
    }

    public void setSelectRandomCommand(boolean selectRandomCommand) {
        this.selectRandomCommand = selectRandomCommand;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
