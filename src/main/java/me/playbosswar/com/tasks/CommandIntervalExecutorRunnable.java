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
        if(cancelled) {
            return;
        }

        tasksManager.processCommandExecution(task, task.getCommands().get(commandIndex)); // no need to run in scheduler, we are already on the main thread
        commandIndex++; // fix commandIndex incrementation

        if(commandIndex >= task.getCommands().size()) {
            cancelled = true;
        }
    }
}
