package me.playbosswar.com.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.utils.Tools;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Messages;
import me.playbosswar.com.utils.TaskTimeUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.util.Date;
import java.util.Objects;

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
    public String onPlaceholderRequest(Player player, String identifier) {
        String[] identifierParts = identifier.split("_");

        if(identifierParts.length < 2) {
            Messages.sendConsole("Used a CommandTimer placeholder wrong. Example: " +
                    "%commandtimer_testtask_nextExecutionFormat%");
            return null;
        }

        String commandName = identifierParts[0];
        String commandField = identifierParts[1];
        String fallbackMessage = null;

        if(identifierParts.length == 3) {
            fallbackMessage = identifierParts[2];
        }

        Task task = CommandTimerPlugin.getInstance().getTasksManager().getTaskByName(commandName);

        if(task == null) {
            Messages.sendConsole("Tried to use PAPI placeholder for unknown command:" + identifier);
            return null;
        }

        if(commandField.equalsIgnoreCase("seconds")) {
            int seconds = task.getInterval().toSeconds();

            if(seconds < 0 && fallbackMessage != null) {
                return fallbackMessage;
            }

            return seconds + "";
        }

        if(commandField.equalsIgnoreCase("secondsFormat")) {
            int seconds = task.getInterval().toSeconds();

            if(seconds < 0 && fallbackMessage != null) {
                return fallbackMessage;
            }

            return Tools.getTimeString(seconds);
        }

        if(commandField.equalsIgnoreCase("nextExecution")) {
            if(!task.getTimes().isEmpty()) {
                Date date = TaskTimeUtils.getSoonestTaskTime(task.getTimes());

                if(date == null) {
                    return fallbackMessage != null ? fallbackMessage : "";
                }

                long seconds = (date.getTime() - new Date().getTime()) / 1000;

                return seconds + "";
            }

            Interval interval = new Interval(task.getLastExecuted().getTime(), new Date().getTime());
            Duration period = interval.toDuration();

            return task.getInterval().toSeconds() - period.getStandardSeconds() + "";
        }

        if(commandField.equalsIgnoreCase("nextExecutionFormat")) {
            if(!task.getTimes().isEmpty()) {
                Date date = TaskTimeUtils.getSoonestTaskTime(task.getTimes());

                if(date == null) {
                    return fallbackMessage != null ? fallbackMessage : "";
                }

                long seconds = (date.getTime() - new Date().getTime()) / 1000;

                return Tools.getTimeString((int) seconds);
            }

            Interval interval = new Interval(task.getLastExecuted().getTime(), new Date().getTime());
            Duration period = interval.toDuration();

            long timeLeft = task.getInterval().toSeconds() - period.getStandardSeconds();

            return Tools.getTimeString((int) timeLeft);
        }

        if(!task.getTimes().isEmpty()) {
            Date date = TaskTimeUtils.getSoonestTaskTime(task.getTimes());

            if(date == null) {
                return fallbackMessage != null ? fallbackMessage : "";
            }

            long seconds = (date.getTime() - new Date().getTime()) / 1000;

            return Tools.getTimeString((int) seconds, commandField);
        }

        Interval interval = new Interval(task.getLastExecuted().getTime(), new Date().getTime());
        Duration period = interval.toDuration();

        long timeLeft = task.getInterval().toSeconds() - period.getStandardSeconds();

        return Tools.getTimeString((int) timeLeft, commandField);
    }
}