package me.playbosswar.com.tasks;

import io.sentry.ITransaction;
import io.sentry.Sentry;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.enums.CommandExecutionMode;
import me.playbosswar.com.utils.Messages;
import me.playbosswar.com.utils.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;

/**
 * Runnable responsible for scheduling tasks
 */
public class TaskRunner implements Runnable {
    private final TasksManager tasksManager;

    public TaskRunner(TasksManager tasksManager) {
        this.tasksManager = tasksManager;
    }

    private void processTask(Task task) {
        if (tasksManager.stopRunner) {
            return;
        }

        Messages.sendDebugConsole("Checking if " + task.getName() + " can be executed");

        if (!Tools.randomCheck(task.getRandom())) {
            Messages.sendDebugConsole("Task has a random execution chance");
            return;
        }

        CommandExecutionMode executionMode = task.getCommandExecutionMode();
        if (executionMode.equals(CommandExecutionMode.INTERVAL)) {
            CommandTimerPlugin.getScheduler().runTaskTimer(new CommandIntervalExecutorRunnable(task), 1,
                    task.getCommandExecutionInterval().toSeconds() * 20L);
            return;
        }

        boolean hasDelayedCommands = task.getCommands().stream().anyMatch(c -> c.getDelay().toSeconds() > 0);
        if (executionMode.equals(CommandExecutionMode.ORDERED) && hasDelayedCommands) {
            final int[] accumulatedDelaySeconds = { 0 };
            task.getCommands().forEach(command -> {
                CommandTimerPlugin.getScheduler().runTaskLater(
                        () -> tasksManager.processCommandExecution(task, command),
                        (20L * accumulatedDelaySeconds[0]) + 1);
                accumulatedDelaySeconds[0] += command.getDelay().toSeconds();
            });
            return;
        }

        // If it remains -1, that means that all commands should be executed
        int selectedCommandIndex = tasksManager.getNextTaskCommandIndex(task);

        if (selectedCommandIndex == -1) {
            task.setLastExecutedCommandIndex(0);
            CommandTimerPlugin.getScheduler().runTask(
                    () -> task.getCommands().forEach(command -> tasksManager.processCommandExecution(task, command)));
        } else {
            TaskCommand taskCommand = task.getCommands().get(selectedCommandIndex);
            task.setLastExecutedCommandIndex(task.getCommands().indexOf(taskCommand));
            CommandTimerPlugin.getScheduler().runTask(() -> tasksManager.processCommandExecution(task, taskCommand));
        }

        task.storeInstance();
    }

    @Override
    public void run() {
        CommandTimerPlugin.getScheduler().runTaskTimer(new TimerTask() {
            @Override
            public void run() {
                if (tasksManager.stopRunner) {
                    Messages.sendDebugConsole("Ignoring execution because manager has been stopped");
                    return;
                }

                List<ScheduledTask> tasksToExecute = new ArrayList<>();
                Boolean exit = false;

                while(!exit) {
                    // First peek to aggregate a list of tasks to execute
                    ScheduledTask scheduledTask = tasksManager.getScheduledTasks().peek();
                    if(scheduledTask == null) {
                        exit = true;
                        continue;
                    }

                    if(scheduledTask.getDate().toInstant().toEpochMilli() > System.currentTimeMillis()) {
                        exit = true;
                        continue;
                    }

                    // Then poll to remove the tasks from the queue
                    tasksToExecute.add(tasksManager.getScheduledTasks().poll());
                }

                tasksToExecute.forEach(scheduledTask -> {
                    ITransaction transaction = Sentry.startTransaction("processTask()", "task");
                    processTask(scheduledTask.getTask());
                    transaction.finish();
                });
            }
        }, 1, 10);
    }
}