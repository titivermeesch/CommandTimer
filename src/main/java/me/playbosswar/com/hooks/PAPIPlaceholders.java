package me.playbosswar.com.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.playbosswar.com.Main;
import me.playbosswar.com.Tools;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class PAPIPlaceholders extends PlaceholderExpansion {
    private Plugin plugin;
    public PAPIPlaceholders(Plugin p){
        plugin = p;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier(){
        return "commandtimer";
    }

    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier){
        String[] identifierParts = identifier.split("_");

        if(identifierParts.length != 2) {
            return null;
        }

        String commandName = identifierParts[0];
        String commandField = identifierParts[1];

        Task task = Main.getTasksManager().getTaskByName(commandName);

        if(task == null) {
            Messages.sendConsole("Tried to use PAPI placeholder for unknown command: %commandtimer_" + identifier);
            return null;
        }

        if(commandField.equalsIgnoreCase("seconds")) {
            return "";
            // return task.getSeconds() + "";
        }

        if(commandField.equalsIgnoreCase("secondsFormat")) {
            return "";
            // return Tools.getTimeString(task.getSeconds());
        }

        if(commandField.equalsIgnoreCase("nextExecution")) {
            Interval interval = new Interval(task.getLastExecuted().getTime(), new Date().getTime());
            Duration period = interval.toDuration();

            return task.getInterval().toSeconds() - period.getStandardSeconds() + "";
        }

        if(commandField.equalsIgnoreCase("nextExecutionFormat")) {
            Interval interval = new Interval(task.getLastExecuted().getTime(), new Date().getTime());
            Duration period = interval.toDuration();

             long timeLeft = task.getInterval().toSeconds() - period.getStandardSeconds();

             return Tools.getTimeString((int) timeLeft);
        }

        return null;
    }
}