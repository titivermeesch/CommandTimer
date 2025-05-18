package me.playbosswar.com.tasks;

import io.sentry.ITransaction;
import io.sentry.Sentry;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.enums.CommandExecutionMode;
import me.playbosswar.com.utils.Messages;
import me.playbosswar.com.utils.TaskTimeUtils;
import me.playbosswar.com.utils.TaskUtils;
import me.playbosswar.com.utils.Tools;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.TimerTask;

/**
 * Runnable responsible for scheduling tasks
 */
public class TaskRunner implements Runnable {
    private final TasksManager tasksManager;

    public TaskRunner(TasksManager tasksManager) {
        this.tasksManager = tasksManager;
    }

    private void processTask(Task task) {
        if(tasksManager.stopRunner) {
            return;
        }

        Messages.sendDebugConsole("Checking if " + task.getName() + " can be executed");

        if(!task.isActive()) {
            Messages.sendDebugConsole("Task is not active");
            return;
        }

        if(!TaskUtils.checkTaskDaysContainToday(task)) {
            Messages.sendDebugConsole("Command can not be executed today");
            return;
        }

        if(task.getTimesExecuted() >= task.getExecutionLimit() && task.getExecutionLimit() != -1) {
            Messages.sendDebugConsole("Task reached the maximum execution limit");
            return;
        }

        if(!Tools.randomCheck(task.getRandom())) {
            Messages.sendDebugConsole("Task has a random execution chance");
            return;
        }

        boolean blockTime = true;
        if(!task.getTimes().isEmpty()) {
            Messages.sendDebugConsole("Task is time related, checking if can be executed now");

            for(TaskTime taskTime : task.getTimes()) {
                if(taskTime.isMinecraftTime()) {
                    Messages.sendDebugConsole("Task is using minecraft time");

                    World world = Bukkit.getWorld(taskTime.getWorld() == null ? "world" : taskTime.getWorld());
                    String minecraftTime = Tools.calculateWorldTime(world);

                    Messages.sendDebugConsole("Current minecraft time is " + minecraftTime);

                    LocalTime current = LocalTime.parse(minecraftTime);

                    if(taskTime.isRange()) {
                        LocalTime startRange = taskTime.getTime1();
                        LocalTime endRange = taskTime.getTime2();

                        if(current.isBefore(endRange) && current.isAfter(startRange)) {
                            boolean hasPassedInterval = TaskTimeUtils.hasPassedInterval(task);
                            if(hasPassedInterval) {
                                blockTime = false;
                            }
                        }
                    } else {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                        if(minecraftTime.equals(taskTime.getTime1().format(formatter))) {
                            blockTime = false;
                        }
                    }
                } else {
                    Messages.sendDebugConsole("Found time, checking if execution is needed");

                    LocalTime current = LocalTime.now().withNano(0);

                    if(taskTime.isRange()) {
                        Messages.sendDebugConsole("Found time range");

                        LocalTime startRange = taskTime.getTime1();
                        LocalTime endRange = taskTime.getTime2();

                        if(current.isBefore(endRange) && current.isAfter(startRange)) {
                            Messages.sendDebugConsole("Time is inside time range");
                            boolean hasPassedInterval = TaskTimeUtils.hasPassedInterval(task);
                            if(hasPassedInterval) {
                                Messages.sendDebugConsole("Interval has passed, time range can be executed");
                                blockTime = false;
                            }
                        }
                    } else {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

                        String currentFormat = current.format(formatter);
                        String time1Format = taskTime.getTime1().format(formatter);

                        if(!currentFormat.equals(time1Format)) {
                            Messages.sendDebugConsole("Time " + currentFormat + " does not correspond to " + time1Format);
                        } else {
                            blockTime = false;
                        }
                    }
                }
            }
        } else {
            if(task.getInterval().toSeconds() == 0 && !task.getEvents().isEmpty()) {
                Messages.sendDebugConsole("Timer has no interval set and uses events, skipping");
                return;
            }
            blockTime = false;
            boolean hasPassedInterval = TaskTimeUtils.hasPassedInterval(task);
            if(!hasPassedInterval) {
                Messages.sendDebugConsole("Timer has been executed before");
                return;
            }
        }

        if(blockTime) {
            Messages.sendDebugConsole("Execution has been blocked because times do not correspond");
            return;
        }

        CommandExecutionMode executionMode = task.getCommandExecutionMode();
        if(executionMode.equals(CommandExecutionMode.INTERVAL)) {
            CommandTimerPlugin.getScheduler().runTaskTimer(
                    new CommandIntervalExecutorRunnable(task), 1, task.getCommandExecutionInterval().toSeconds() * 20L);
            return;
        }

        boolean hasDelayedCommands = task.getCommands().stream().anyMatch(c -> c.getDelay().toSeconds() > 0);
        if(executionMode.equals(CommandExecutionMode.ORDERED) && hasDelayedCommands) {
            final int[] accumulatedDelaySeconds = {0};
            task.getCommands().forEach(command -> {
                CommandTimerPlugin.getScheduler().runTaskLater(
                        () -> tasksManager.processCommandExecution(task, command), (20L * accumulatedDelaySeconds[0]) + 1);
                accumulatedDelaySeconds[0] += command.getDelay().toSeconds();
            });
            return;
        }

        // If it remains -1, that means that all commands should be executed
        int selectedCommandIndex = tasksManager.getNextTaskCommandIndex(task);

        if(selectedCommandIndex == -1) {
            task.setLastExecutedCommandIndex(0);
            CommandTimerPlugin.getScheduler().runTask(
                    () -> task.getCommands().forEach(command -> tasksManager.processCommandExecution(task, command)));
        } else {
            TaskCommand taskCommand = task.getCommands().get(selectedCommandIndex);
            task.setLastExecutedCommandIndex(task.getCommands().indexOf(taskCommand));
            CommandTimerPlugin.getScheduler().runTask(
                    () -> tasksManager.processCommandExecution(task, taskCommand));
        }

        task.storeInstance();
    }

    @Override
    public void run() {
        CommandTimerPlugin.getScheduler().runTaskTimer(new TimerTask() {
            @Override
            public void run() {
                if(tasksManager.stopRunner) {
                    Messages.sendDebugConsole("Ignoring execution because manager has been stopped");
                    return;
                }

                List<Task> tasks = CommandTimerPlugin.getInstance().getTasksManager().getLoadedTasks();

                tasks.forEach(task -> {
                    ITransaction transaction = Sentry.startTransaction("processTask()", "task");
                    processTask(task);
                    transaction.finish();
                });
            }
            // Sync runner with the clock
        }, (System.currentTimeMillis() % 20) + 1, 20);
    }
}