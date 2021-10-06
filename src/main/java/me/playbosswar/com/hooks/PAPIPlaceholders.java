package me.playbosswar.com.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.Tools;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TaskTime;
import me.playbosswar.com.utils.Messages;
import me.playbosswar.com.utils.TaskTimeUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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

        if (identifierParts.length != 2) {
            return null;
        }

        String commandName = identifierParts[0];
        String commandField = identifierParts[1];

        Task task = CommandTimerPlugin.getInstance().getTasksManager().getTaskByName(commandName);

        if (task == null) {
            Messages.sendConsole("Tried to use PAPI placeholder for unknown command: %commandtimer_" + identifier);
            return null;
        }

        if (commandField.equalsIgnoreCase("seconds")) {
            return task.getInterval().toSeconds() + "";
        }

        if (commandField.equalsIgnoreCase("secondsFormat")) {
            return Tools.getTimeString(task.getInterval().toSeconds());
        }

        if (commandField.equalsIgnoreCase("nextExecution")) {
            boolean isTimeRelated = !task.getTimes().isEmpty();
            LocalTime nowTime = LocalDateTime.ofInstant(new Date().toInstant(),
                                                        ZoneId.systemDefault()).toLocalTime();

            if(isTimeRelated) {
                TaskTime taskTime = TaskTimeUtils.getSoonestTaskTime(task.getTimes());
                long seconds = ChronoUnit.SECONDS.between(nowTime, taskTime.getTime1());

                if(seconds < 0) {
                    // Amount of seconds in a day
                    seconds = seconds + 86400;
                }

                return seconds + "";
            }

            Interval interval = new Interval(task.getLastExecuted().getTime(), new Date().getTime());
            Duration period = interval.toDuration();

            return task.getInterval().toSeconds() - period.getStandardSeconds() + "";
        }

        if (commandField.equalsIgnoreCase("nextExecutionFormat")) {
            boolean isTimeRelated = !task.getTimes().isEmpty();
            LocalTime nowTime = LocalDateTime.ofInstant(new Date().toInstant(),
                                                        ZoneId.systemDefault()).toLocalTime();

            if(isTimeRelated) {
                TaskTime taskTime = TaskTimeUtils.getSoonestTaskTime(task.getTimes());
                long seconds = ChronoUnit.SECONDS.between(nowTime, taskTime.getTime1());

                if(seconds < 0) {
                    // Amount of seconds in a day
                    seconds = seconds + 86400;
                }

                return Tools.getTimeString((int) seconds);
            }

            Interval interval = new Interval(task.getLastExecuted().getTime(), new Date().getTime());
            Duration period = interval.toDuration();

            long timeLeft = task.getInterval().toSeconds() - period.getStandardSeconds();

            return Tools.getTimeString((int) timeLeft);
        }

        return "";
    }
}