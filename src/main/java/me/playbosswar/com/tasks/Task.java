package me.playbosswar.com.tasks;

import me.playbosswar.com.conditionsengine.validations.Condition;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.conditionsengine.validations.SimpleCondition;
import me.playbosswar.com.enums.CommandExecutionMode;
import me.playbosswar.com.utils.Files;
import me.playbosswar.com.utils.gson.GsonConverter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Task {
    private String name;
    private List<TaskCommand> commands = new ArrayList<>();
    private TaskInterval interval = new TaskInterval(this, 0, 0, 0, 5);
    private List<TaskTime> times = new ArrayList<>();
    private double random = 1.0;
    private List<String> days = new ArrayList<>();
    private int executionLimit = -1;
    private int timesExecuted = 0;
    private int lastExecutedCommandIndex = 0;
    private Date lastExecuted = new Date();
    private CommandExecutionMode commandExecutionMode = CommandExecutionMode.ALL;
    private TaskInterval commandExecutionInterval = new TaskInterval(this, 0, 0, 0, 1);
    private boolean active = false;
    private boolean resetExecutionsAfterRestart = false;
    private Condition condition;

    public Task(String name) {
        this.name = name;
        this.days.add("MONDAY");
        this.days.add("TUESDAY");
        this.days.add("WEDNESDAY");
        this.days.add("THURSDAY");
        this.days.add("FRIDAY");
        this.days.add("SATURDAY");
        this.days.add("SUNDAY");
        this.condition = new Condition(ConditionType.SIMPLE, new ArrayList<>(), new SimpleCondition(this), this);
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

    public List<TaskCommand> getCommands() {
        return commands;
    }

    public void setCommands(List<TaskCommand> commands) {
        this.commands = commands;
        storeInstance();
    }

    public int addCommand(TaskCommand command) {
        if(command.getCommand().startsWith("/")) {
            command.setCommand(command.getCommand().substring(1));
        }
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

    public List<TaskTime> getTimes() {
        return times;
    }

    public void setTimes(List<TaskTime> times) {
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

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
        storeInstance();
    }

    public void toggleDay(String day) {
        if (days.contains(day)) {
            days.remove(day);
        } else {
            days.add(day);
        }

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

    public Date getLastExecuted() {
        return lastExecuted;
    }

    public void setLastExecuted(Date lastExecuted) {
        this.lastExecuted = lastExecuted;
        storeInstance();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        this.lastExecuted = new Date();
        storeInstance();
    }

    public void toggleActive() {
        this.active = !this.active;
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
            commandExecutionMode = CommandExecutionMode.INTERVAL;
            storeInstance();
            return;
        }

        if (commandExecutionMode.equals(CommandExecutionMode.INTERVAL)) {
            commandExecutionMode = CommandExecutionMode.ALL;
            storeInstance();
        }
    }

    public boolean isResetExecutionsAfterRestart() {
        return resetExecutionsAfterRestart;
    }

    public void setResetExecutionsAfterRestart(boolean resetExecutionsAfterRestart) {
        this.resetExecutionsAfterRestart = resetExecutionsAfterRestart;
        storeInstance();
    }

    public void toggleResetExecutionAfterRestart() {
        this.resetExecutionsAfterRestart = !this.resetExecutionsAfterRestart;
        storeInstance();
    }

    public int getLastExecutedCommandIndex() {
        return lastExecutedCommandIndex;
    }

    public void setLastExecutedCommandIndex(int lastExecutedCommandIndex) {
        this.lastExecutedCommandIndex = lastExecutedCommandIndex;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public TaskInterval getCommandExecutionInterval() {
        return commandExecutionInterval;
    }

    public void setCommandExecutionInterval(TaskInterval commandExecutionInterval) {
        this.commandExecutionInterval = commandExecutionInterval;
    }

    public void storeInstance() {
        GsonConverter gson = new GsonConverter();
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
