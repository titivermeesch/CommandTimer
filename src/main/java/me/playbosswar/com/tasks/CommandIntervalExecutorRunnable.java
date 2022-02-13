package me.playbosswar.com.tasks;

import me.playbosswar.com.CommandTimerPlugin;

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
        if (cancelled) {
            return;
        }

        tasksManager.addTaskCommandExecution(task.getCommands().get(commandIndex));
        commandIndex++;

        if (commandIndex == task.getCommands().size()) {
            cancelled = true;
        }
    }
}
