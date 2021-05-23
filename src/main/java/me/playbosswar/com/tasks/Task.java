package me.playbosswar.com.tasks;

import com.google.gson.Gson;
import me.playbosswar.com.enums.CommandExecutionMode;
import me.playbosswar.com.utils.Files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;

public class Task {
    private String name;
    private ArrayList<TaskCommand> commands = new ArrayList<>();
    private TaskInterval interval = new TaskInterval(this, 0, 0, 0, 5);
    private ArrayList<TaskTime> times = new ArrayList<>();
    private double random = 1.0;
    private ArrayList<String> worlds = new ArrayList<>();
    private ArrayList<String> days = new ArrayList<>();
    private int executionLimit = -1;
    private int timesExecuted = 0;
    private LocalTime lastExecuted;
    private String requiredPermission = "";
    private int minPlayers = -1;
    private int maxPlayers = -1;
    private CommandExecutionMode commandExecutionMode = CommandExecutionMode.ALL;
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
        storeInstance();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        File oldFile = new File(Files.getTaskFile(this.name));
        oldFile.delete();

        this.name = name;
        storeInstance();
    }

    public ArrayList<TaskCommand> getCommands() {
        return commands;
    }

    public void setCommands(ArrayList<TaskCommand> commands) {
        this.commands = commands;
        storeInstance();
    }

    public int addCommand(TaskCommand command) {
        this.commands.add(command);
        storeInstance();
        return commands.indexOf(command);
    }

    public void removeCommand(TaskCommand command) {
        commands.remove(command);
        storeInstance();
    }

    public TaskInterval getInterval() {
        return interval;
    }

    public void setInterval(TaskInterval interval) {
        this.interval = interval;
        storeInstance();
    }

    public ArrayList<TaskTime> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<TaskTime> times) {
        this.times = times;
        storeInstance();
    }

    public void addTime(TaskTime time) {
        this.times.add(time);
        storeInstance();
    }

    public void removeTime(TaskTime time) {
        times.remove(time);
        storeInstance();
    }

    public double getRandom() {
        return random;
    }

    public void setRandom(double random) {
        this.random = random;
        storeInstance();
    }

    public ArrayList<String> getWorlds() {
        return worlds;
    }

    public void setWorlds(ArrayList<String> worlds) {
        this.worlds = worlds;
        storeInstance();
    }

    public void addWorld(String world) {
        this.worlds.add(world);
        storeInstance();
    }

    public ArrayList<String> getDays() {
        return days;
    }

    public void setDays(ArrayList<String> days) {
        this.days = days;
        storeInstance();
    }

    public int getExecutionLimit() {
        return executionLimit;
    }

    public void setExecutionLimit(int executionLimit) {
        this.executionLimit = executionLimit;
        storeInstance();
    }

    public int getTimesExecuted() {
        return timesExecuted;
    }

    public void setTimesExecuted(int timesExecuted) {
        this.timesExecuted = timesExecuted;
        storeInstance();
    }

    public LocalTime getLastExecuted() {
        return lastExecuted;
    }

    public void setLastExecuted(LocalTime lastExecuted) {
        this.lastExecuted = lastExecuted;
        storeInstance();
    }

    public String getRequiredPermission() {
        return requiredPermission;
    }

    public void setRequiredPermission(String requiredPermission) {
        this.requiredPermission = requiredPermission;
        storeInstance();
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
        storeInstance();
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        storeInstance();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        storeInstance();
    }

    public void toggleActive() {
        active = !active;
        storeInstance();
    }

    public CommandExecutionMode getCommandExecutionMode() {
        return commandExecutionMode;
    }

    public void setCommandExecutionMode(CommandExecutionMode commandExecutionMode) {
        this.commandExecutionMode = commandExecutionMode;
    }

    public void switchCommandExecutionMode() {
        if (commandExecutionMode.equals(CommandExecutionMode.ALL)) {
            commandExecutionMode = CommandExecutionMode.ORDERED;
            storeInstance();
            return;
        }

        if (commandExecutionMode.equals(CommandExecutionMode.ORDERED)) {
            commandExecutionMode = CommandExecutionMode.RANDOM;
            storeInstance();
            return;
        }

        if (commandExecutionMode.equals(CommandExecutionMode.RANDOM)) {
            commandExecutionMode = CommandExecutionMode.ALL;
            storeInstance();
        }
    }

    public void storeInstance() {
        Gson gson = new Gson();
        String json = gson.toJson(this);

        try {
            FileWriter jsonFile = new FileWriter(Files.getTaskFile(name));
            jsonFile.write(json);
            jsonFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
