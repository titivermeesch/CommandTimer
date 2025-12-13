package me.playbosswar.com.tasks;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import io.sentry.ITransaction;
import io.sentry.Sentry;
import io.sentry.SpanStatus;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.api.events.EventConfiguration;
import me.playbosswar.com.conditionsengine.validations.Condition;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.conditionsengine.validations.SimpleCondition;
import me.playbosswar.com.enums.CommandExecutionMode;
import me.playbosswar.com.tasks.persistors.*;
import me.playbosswar.com.utils.Files;
import me.playbosswar.com.utils.gson.GsonConverter;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@DatabaseTable(tableName = "tasks")
public class Task {
    @DatabaseField(id = true)
    private UUID id;
    @DatabaseField
    private String name = "";
    @DatabaseField(persisterClass = TaskCommandPersistor.class)
    private Collection<TaskCommand> commands = new ArrayList<>();
    @DatabaseField(persisterClass = TaskIntervalPersistor.class)
    private TaskInterval interval = new TaskInterval(0, 0, 0, 5);
    @DatabaseField(persisterClass = TaskTimePersistor.class)
    private Collection<TaskTime> times = new ArrayList<>();
    @DatabaseField
    private double random = 1.0;
    @DatabaseField(persisterClass = DaysPersistor.class)
    private Collection<DayOfWeek> days = new ArrayList<>();
    @DatabaseField
    private int executionLimit = -1;
    private transient int timesExecuted = 0;
    private transient int lastExecutedCommandIndex = 0;
    private transient Date lastExecuted = new Date();
    @DatabaseField
    private CommandExecutionMode commandExecutionMode = CommandExecutionMode.ALL;
    @DatabaseField(persisterClass = TaskIntervalPersistor.class)
    private TaskInterval commandExecutionInterval = new TaskInterval(0, 0, 0, 1);
    @DatabaseField
    private boolean active = false;
    @DatabaseField
    private boolean resetExecutionsAfterRestart = false;
    @DatabaseField(persisterClass = ConditionPersistor.class)
    private Condition condition;
    @DatabaseField(persisterClass = EventConfigurationPersistor.class)
    private Collection<EventConfiguration> events = new ArrayList<>();

    Task() {
        // all persisted classes must define a no-arg constructor with at least package visibility
    }

    public Task(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.days.add(DayOfWeek.MONDAY);
        this.days.add(DayOfWeek.TUESDAY);
        this.days.add(DayOfWeek.WEDNESDAY);
        this.days.add(DayOfWeek.THURSDAY);
        this.days.add(DayOfWeek.FRIDAY);
        this.days.add(DayOfWeek.SATURDAY);
        this.days.add(DayOfWeek.SUNDAY);
        this.condition = new Condition(ConditionType.SIMPLE, new ArrayList<>(), new SimpleCondition());
        storeInstance();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.replaceAll(" ", "");
        storeInstance();
    }

    public List<TaskCommand> getCommands() {
        return commands.stream().collect(Collectors.toList());
    }

    public void setCommands(List<TaskCommand> commands) {
        this.commands = commands;
        storeInstance();
    }

    public void addCommand(TaskCommand command) {
        if(command.getCommand().startsWith("/")) {
            command.setCommand(command.getCommand().substring(1));
        }
        this.commands.add(command);
        storeInstance();
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
        CommandTimerPlugin.getInstance().getTasksManager().resetScheduleForTask(this);
    }

    public List<TaskTime> getTimes() {
        return times.stream().collect(Collectors.toList());
    }

    public void setTimes(List<TaskTime> times) {
        this.times = times;
        storeInstance();
        CommandTimerPlugin.getInstance().getTasksManager().resetScheduleForTask(this);
    }

    public void addTime(TaskTime time) {
        this.times.add(time);
        storeInstance();
        CommandTimerPlugin.getInstance().getTasksManager().resetScheduleForTask(this);
    }

    public void removeTime(TaskTime time) {
        times.remove(time);
        storeInstance();
        CommandTimerPlugin.getInstance().getTasksManager().resetScheduleForTask(this);
    }

    public double getRandom() {
        return random;
    }

    public void setRandom(double random) {
        this.random = random;
        storeInstance();
        CommandTimerPlugin.getInstance().getTasksManager().resetScheduleForTask(this);
    }

    public List<DayOfWeek> getDays() {
        return days.stream().collect(Collectors.toList());
    }

    public void setDays(List<DayOfWeek> days) {
        this.days = days;
        storeInstance();
        CommandTimerPlugin.getInstance().getTasksManager().resetScheduleForTask(this);
    }

    public void toggleDay(DayOfWeek day) {
        if(days.contains(day)) {
            days.remove(day);
        } else {
            days.add(day);
        }

        storeInstance();
        CommandTimerPlugin.getInstance().getTasksManager().resetScheduleForTask(this);
    }

    public int getExecutionLimit() {
        return executionLimit;
    }

    public void setExecutionLimit(int executionLimit) {
        this.executionLimit = executionLimit;
        storeInstance();
        CommandTimerPlugin.getInstance().getTasksManager().resetScheduleForTask(this);
    }

    public int getTimesExecuted() {
        return timesExecuted;
    }

    public void setTimesExecuted(int timesExecuted) {
        this.timesExecuted = timesExecuted;
        storeExecutionMetadata();
    }

    public Date getLastExecuted() {
        return lastExecuted;
    }

    public void setLastExecuted(Date lastExecuted) {
        this.lastExecuted = lastExecuted;
        storeExecutionMetadata();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        this.lastExecuted = new Date();
        storeInstance();
        CommandTimerPlugin.getInstance().getTasksManager().resetScheduleForTask(this);
    }

    public void toggleActive() {
        this.active = !this.active;
        storeInstance();
        CommandTimerPlugin.getInstance().getTasksManager().resetScheduleForTask(this);
    }

    public CommandExecutionMode getCommandExecutionMode() {
        return commandExecutionMode;
    }

    public void setCommandExecutionMode(CommandExecutionMode commandExecutionMode) {
        this.commandExecutionMode = commandExecutionMode;
    }

    public void switchCommandExecutionMode() {
        if(commandExecutionMode.equals(CommandExecutionMode.ALL)) {
            commandExecutionMode = CommandExecutionMode.ORDERED;
            storeInstance();
            return;
        }

        if(commandExecutionMode.equals(CommandExecutionMode.ORDERED)) {
            commandExecutionMode = CommandExecutionMode.RANDOM;
            storeInstance();
            return;
        }

        if(commandExecutionMode.equals(CommandExecutionMode.RANDOM)) {
            commandExecutionMode = CommandExecutionMode.INTERVAL;
            storeInstance();
            return;
        }

        if(commandExecutionMode.equals(CommandExecutionMode.INTERVAL)) {
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
        storeExecutionMetadata();
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

    public List<EventConfiguration> getEvents() {
        // Convert events collection to list
        return events.stream().collect(Collectors.toList());
    }

    public void setEvents(List<EventConfiguration> events) {
        this.events = events;
    }

    public boolean hasCondition() {
        if(this.condition == null) {
            return false;
        }

        if(!this.condition.getConditions().isEmpty()) {
            return true;
        }

        if(this.condition.getSimpleCondition() == null) {
            return false;
        }

        if(this.getCondition().getSimpleCondition().getConditionGroup() == null) {
            return false;
        }


        return true;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void storeExecutionMetadata() {
        Files.updateLocalTaskMetadata(this);
    }

    public void storeInstance() {
        if(CommandTimerPlugin.getInstance().getConfig().getBoolean("database.enabled")) {
            try {
                CommandTimerPlugin.getTaskDao().createOrUpdate(this);
                Files.updateLocalTaskMetadata(this);
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        ITransaction transaction = Sentry.startTransaction("storeInstance()", "task");
        GsonConverter gson = new GsonConverter();
        String json = gson.toJson(this);
        transaction.setContext("task", json);

        try (FileWriter jsonFile = new FileWriter(Files.getTaskFile(id))) {
            jsonFile.write(json);
            jsonFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
            transaction.setThrowable(e);
            transaction.setStatus(SpanStatus.INTERNAL_ERROR);
        } finally {
            transaction.finish();
        }
    }
}
