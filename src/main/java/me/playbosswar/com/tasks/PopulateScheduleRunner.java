package me.playbosswar.com.tasks;

import java.util.TimerTask;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.utils.Messages;

public class PopulateScheduleRunner implements Runnable {
    private final TasksManager tasksManager;

    public PopulateScheduleRunner(TasksManager tasksManager) {
        this.tasksManager = tasksManager;
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

                tasksManager.getLoadedTasks().forEach(task -> tasksManager.populateScheduleForTask(task));
            }
            // Run every 10 seconds
        }, 1, 20 * 10);
    }
}
