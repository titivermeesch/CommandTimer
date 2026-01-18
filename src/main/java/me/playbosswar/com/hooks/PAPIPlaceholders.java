package me.playbosswar.com.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.utils.Tools;
import me.playbosswar.com.tasks.ScheduledTask;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TaskTime;
import me.playbosswar.com.utils.Messages;
import me.playbosswar.com.utils.TaskTimeUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.util.*;
import java.util.stream.Collectors;

public class PAPIPlaceholders extends PlaceholderExpansion {
    private final Plugin plugin;

    public PAPIPlaceholders(Plugin p) {
        plugin = p;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "commandtimer";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        PlaceholderConfiguration placeholder = new PlaceholderConfiguration(identifier);

        if(!placeholder.isValid()) {
            Messages.sendConsole("Used a CommandTimer placeholder wrong. Example: " +
                    "%commandtimer_testtask_nextExecutionFormat%");
            return "INVALID PLACEHOLDER";
        }

        Task task = CommandTimerPlugin.getInstance().getTasksManager().getTaskByName(placeholder.getTaskName());
        String fallbackMessage = placeholder.getFallbackMessage();

        if(placeholder.getTaskName().equals("ALLTASKS")) {
            ScheduledTask scheduledTask = CommandTimerPlugin.getInstance().getTasksManager().getNextScheduledTask();
            if(scheduledTask == null) {
                return fallbackMessage;
            }

            task = scheduledTask.getTask();
        }

        if(task == null) {
            if(CommandTimerPlugin.getInstance().getConfig().getBoolean("disablePapiPlaceholderWarnings")) {
                return fallbackMessage;
            }

            Messages.sendConsole("Tried to use PAPI placeholder for unknown task:" + placeholder.getTaskName());
            return fallbackMessage;
        }

        if(placeholder.getPlaceholderType().equals("nextTaskName")) {
            return task.getName();
        }


        if(placeholder.getPlaceholderType().equalsIgnoreCase("seconds")) {
            return getSecondsText(task, fallbackMessage, false);
        }

        if(placeholder.getPlaceholderType().equalsIgnoreCase("secondsFormat")) {
            return getSecondsText(task, fallbackMessage, true);
        }

        if(placeholder.getPlaceholderType().equalsIgnoreCase("nextExecution")) {
            return getNextExecutionText(task, fallbackMessage, false);
        }

        if(placeholder.getPlaceholderType().equalsIgnoreCase("nextExecutionFormat")) {
            return getNextExecutionText(task, fallbackMessage, true);
        }

        if(placeholder.getPlaceholderType().equalsIgnoreCase("lastExecution")) {
            return getLastExecutionText(task, fallbackMessage, false);
        }

        if(placeholder.getPlaceholderType().equalsIgnoreCase("lastExecutionFormat")) {
            return getLastExecutionText(task, fallbackMessage, true);
        }

        return getNextExecutionText(task, fallbackMessage, true, placeholder.getPlaceholderType());
    }

    private String getSecondsText(Task task, String fallbackMessage, boolean format) {
        int seconds = task.getInterval().toSeconds();

        if(seconds < 0 && fallbackMessage != null) {
            return fallbackMessage;
        }

        if(format) {
            return Tools.getTimeString(seconds);
        }

        return seconds + "";
    }

    // Get time in seconds before next task execution
    private long getNextExecution(Task task) {
        ScheduledTask taskScheduled = CommandTimerPlugin.getInstance().getTasksManager().getNextScheduledTaskForTask(task);
        if(taskScheduled == null) {
            return -1;
        }

        long now = System.currentTimeMillis();
        return (taskScheduled.getDate().toInstant().toEpochMilli() - now) / 1000;
    }

    private long getLastExecution(Task task) {
        if (task.getLastExecuted() == null || !task.isActive()) {
            return -1;
        }

        long now = new Date().getTime();
        Interval interval = new Interval(task.getLastExecuted().getTime(), now);
        Duration period = interval.toDuration();
        return period.getStandardSeconds();
    }

    private String getNextExecutionText(Task task, String fallbackMessage, boolean format, String timeFormat) {
        long seconds = getNextExecution(task);

        if(seconds == -1) {
            return fallbackMessage != null ? fallbackMessage : "";
        }

        if(format) {
            if(timeFormat == null) {
                timeFormat = seconds > 86400 ? "DD:HH:mm:ss" : "HH:mm:ss";
            }
            return Tools.getTimeString((int) seconds, timeFormat);
        }

        return seconds + "";
    }

    private String getLastExecutionText(Task task, String fallbackMessage, boolean format, String timeFormat) {
        long seconds = getLastExecution(task);

        if(seconds == -1) {
            return fallbackMessage != null ? fallbackMessage : "";
        }

        if(format) {
            if(timeFormat == null) {
                timeFormat = seconds > 86400 ? "DD:HH:mm:ss" : "HH:mm:ss";
            }
            return Tools.getTimeString((int) seconds, timeFormat);
        }

        return seconds + "";
    }

    private String getNextExecutionText(Task task, String fallbackMessage, boolean format) {
        return getNextExecutionText(task, fallbackMessage, format, null);
    }

    private String getLastExecutionText(Task task, String fallbackMessage, boolean format) {
        return getLastExecutionText(task, fallbackMessage, format, null);
    }
}