package me.playbosswar.com.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.tasks.TaskTime;
import me.playbosswar.com.utils.Tools;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Messages;
import me.playbosswar.com.utils.TaskTimeUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        if(placeholder.getTaskName().equals("ALLTASKS")) {
            return getAllTasksNextExecutionText(placeholder.getFallbackMessage());
        }

        Task task = CommandTimerPlugin.getInstance().getTasksManager().getTaskByName(placeholder.getTaskName());

        if(task == null) {
            Messages.sendConsole("Tried to use PAPI placeholder for unknown task:" + placeholder.getTaskName());
            return null;
        }

        String fallbackMessage = placeholder.getFallbackMessage();

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

    private String getNextExecutionText(Task task, String fallbackMessage, boolean format, String timeFormat) {
        if(!task.getTimes().isEmpty()) {
            Date date = TaskTimeUtils.getSoonestTaskTime(task.getTimes());

            if(date == null || !task.isActive()) {
                return fallbackMessage != null ? fallbackMessage : "";
            }

            long seconds = (date.getTime() - new Date().getTime()) / 1000;

            if(format) {
                return Tools.getTimeString((int) seconds, timeFormat);
            }

            return seconds + "";
        }

        long timeLeft = getTaskTimeLeft(task);

        if((timeLeft < 0 || !task.isActive()) && fallbackMessage != null) {
            return fallbackMessage;
        }

        if(format) {
            return Tools.getTimeString((int) timeLeft, timeFormat);
        }

        return timeLeft + "";
    }

    private String getNextExecutionText(Task task, String fallbackMessage, boolean format) {
        return getNextExecutionText(task, fallbackMessage, format, "HH:mm:ss");
    }

    private String getAllTasksNextExecutionText(String fallbackMessage) {
        List<Task> tasks = CommandTimerPlugin.getInstance().getTasksManager().getLoadedTasks();
        List<TaskTime> taskTimes = new ArrayList<>();
        tasks.forEach(t -> {
            if(!t.isActive()) {
                return;
            }

            taskTimes.addAll(t.getTimes());
        });
        // Soonest date for all task times
        Date date = TaskTimeUtils.getSoonestTaskTime(taskTimes);

        // Next execution in seconds for tasks who don't have time
        final int[] seconds = {-1};
        tasks.forEach(t -> {
            if(!t.getTimes().isEmpty() || !t.isActive()) {
                return;
            }

            long timeLeft = getTaskTimeLeft(t);
            if((timeLeft < seconds[0] || seconds[0] == -1) && timeLeft >= 0) {
                seconds[0] = (int) timeLeft;
            }
        });

        if(seconds[0] == -1 && date == null && fallbackMessage != null) {
            return fallbackMessage;
        }

        if(date == null) {
            return seconds[0] + "";
        }

        long soonestTaskTimeExecution = (date.getTime() - new Date().getTime()) / 1000;
        int nextExecution = seconds[0] > soonestTaskTimeExecution ? (int) soonestTaskTimeExecution : seconds[0];

        return nextExecution + "";
    }

    private long getTaskTimeLeft(Task task) {
        Interval interval = new Interval(task.getLastExecuted().getTime(), new Date().getTime());
        Duration period = interval.toDuration();
        int seconds = task.getInterval().toSeconds();
        return seconds - period.getStandardSeconds();
    }
}