package me.playbosswar.com.queue;

import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TaskCommand;
import me.playbosswar.com.tasks.TaskInterval;
import me.playbosswar.com.tasks.TasksManager;

public class CommandQueueRunner implements Runnable {
    private final Plugin plugin;
    private final TasksManager taskManager;
    private final CommandsQueueManager commandsQueueManager;

    public CommandQueueRunner(Plugin plugin, TasksManager taskManager, CommandsQueueManager commandsQueueManager) {
        this.plugin = plugin;
        this.taskManager = taskManager;
        this.commandsQueueManager = commandsQueueManager;
    }

    @Override
    public void run() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            List<QueuedCommand> queuedCommands = commandsQueueManager.getQueuedCommands();
            for (QueuedCommand queuedCommand : queuedCommands) {
                TaskCommand taskCommand = queuedCommand.getTaskCommand();
                TaskInterval delay = queuedCommand.getDelay();
                Date queuedAt = queuedCommand.getQueuedAt();

                int delayInSeconds = delay.getSeconds();
                int now = (int) (new Date().getTime() / 1000);
                if (now - (queuedAt.getTime() / 1000) < delayInSeconds) {
                    continue;
                }

                taskManager.processCommandExecution(null, taskCommand);
                commandsQueueManager.removeCommand(queuedCommand);
            }
        }, System.currentTimeMillis() % 20, 20);
    }
}
