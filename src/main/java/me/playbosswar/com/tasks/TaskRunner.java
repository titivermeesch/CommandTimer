package me.playbosswar.com.tasks;

import me.playbosswar.com.Main;
import me.playbosswar.com.Tools;
import me.playbosswar.com.enums.CommandExecutionMode;
import me.playbosswar.com.utils.Messages;
import me.playbosswar.com.utils.TaskUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Runnable responsible for scheduling tasks
 */
public class TaskRunner implements Runnable {
    private void processTask(Task task) {
        if(Main.getTasksManager().stopRunner) {
            return;
        }

        Messages.sendDebugConsole("Checking if " + task.getName() + " can be executed");

        if (!task.isActive()) {
            Messages.sendDebugConsole("Task is not active");
            return;
        }

        if (!TaskUtils.checkTaskDaysContainToday(task)) {
            Messages.sendDebugConsole("Command can not be executed today");
            return;
        }

        if (!TaskUtils.checkServerHasEnoughPlayers(task)) {
            Messages.sendDebugConsole("Server did not reach player limits on task");
            return;
        }

        if (task.getTimesExecuted() >= task.getExecutionLimit() && task.getExecutionLimit() != -1) {
            Messages.sendDebugConsole("Task reached the maximum execution limit");
            return;
        }

        if (!Tools.randomCheck(task.getRandom())) {
            Messages.sendDebugConsole("Task has a random execution chance");
            return;
        }

        if (task.getTimes().size() > 0) {
            Messages.sendDebugConsole("Task is time related, checking if can be executed now");

            for (TaskTime taskTime : task.getTimes()) {
                if (taskTime.isMinecraftTime()) {
                    Messages.sendDebugConsole("Task is using minecraft time");

                    World world = Bukkit.getWorld(taskTime.getWorld() == null ? "world" : taskTime.getWorld());
                    assert world != null;
                    String minecraftTime = Tools.calculateWorldTime(world);

                    Messages.sendDebugConsole("Current minecraft time is " + minecraftTime);

                    LocalTime current = LocalTime.parse(minecraftTime);

                    if (taskTime.isRange()) {
                        LocalTime startRange = taskTime.getTime1();
                        LocalTime endRange = taskTime.getTime2();

                        if (current.isAfter(endRange) || current.isBefore(startRange)) {
                            return;
                        }
                    } else {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                        if (!minecraftTime.equals(taskTime.getTime1().format(formatter))) {
                            return;
                        }
                    }
                } else {
                    Messages.sendDebugConsole("Found time, checking if execution is needed");

                    LocalTime current = LocalTime.now().withNano(0);

                    if (taskTime.isRange()) {
                        Messages.sendDebugConsole("Found time range");

                        LocalTime startRange = taskTime.getTime1();
                        LocalTime endRange = taskTime.getTime2();

                        if (current.isAfter(endRange) || current.isBefore(startRange)) {
                            return;
                        }
                    } else {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

                        if (!current.format(formatter).equals(taskTime.getTime1().format(formatter))) {
                            return;
                        }
                    }
                }
            }
        } else {
            Interval interval = new Interval(task.getLastExecuted().getTime(), new Date().getTime());
            Duration period = interval.toDuration();

            if (period.getStandardSeconds() < task.getInterval().toSeconds()) {
                Messages.sendDebugConsole("Timer has been executed before. Last execution " + period.getStandardSeconds() + "s ago");
                return;
            }
        }

        // If it remains -1, that means that all commands should be executed
        int selectedCommandIndex = -1;
        if(task.getCommandExecutionMode().equals(CommandExecutionMode.RANDOM)) {
            selectedCommandIndex = Tools.getRandomInt(0, task.getCommands().size() - 1);
        } else if(task.getCommandExecutionMode().equals(CommandExecutionMode.ORDERED)) {
            int currentLatestCommandIndex = task.getLastExecutedCommandIndex();

            if(currentLatestCommandIndex == task.getCommands().size() - 1) {
                selectedCommandIndex = 0;
            } else {
                selectedCommandIndex = currentLatestCommandIndex + 1;
            }
        }

        if(selectedCommandIndex == -1) {
            task.getCommands().forEach(taskCommand -> Main.getTasksManager().addTaskCommandExecution(taskCommand));
        } else {
            Main.getTasksManager().addTaskCommandExecution(task.getCommands().get(selectedCommandIndex));
        }
    }

    @Override
    public void run() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<Task> tasks = Main.getTasksManager().getLoadedTasks();

                tasks.forEach(task -> processTask(task));
            }
            // Sync runner with the clock
        }, System.currentTimeMillis() % 1000, 1000);
    }
}
