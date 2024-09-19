package me.playbosswar.com.tasks;

import me.playbosswar.com.CommandTimerPlugin;
import org.bukkit.Bukkit;

public class CommandIntervalExecutorRunnable implements Runnable {
    private final Task task;
    private int commandIndex = 0;
    private boolean cancelled = false;
    private final TasksManager tasksManager = CommandTimerPlugin.getInstance().getTasksManager();

    public CommandIntervalExecutorRunnable(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        if(cancelled) {
            return;
        }

        Bukkit.getScheduler().runTask(CommandTimerPlugin.getInstance(),
                () -> tasksManager.processCommandExecution(task, task.getCommands().get(commandIndex)));
        commandIndex++;

        if(commandIndex == task.getCommands().size()) {
            cancelled = true;
        }
    }
}
