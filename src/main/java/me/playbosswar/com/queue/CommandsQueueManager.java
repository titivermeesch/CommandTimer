package me.playbosswar.com.queue;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;

import me.playbosswar.com.tasks.TasksManager;

public class CommandsQueueManager {
    private final Plugin plugin;
    private final List<QueuedCommand> queuedCommands = new ArrayList<>();
    private final TasksManager tasksManager;

    // TODO: implement thread stop when plugin is disabled
    private Thread runnerThread;

    public CommandsQueueManager(Plugin plugin, TasksManager tasksManager) {
        this.plugin = plugin;
        this.tasksManager = tasksManager;
        startRunner();
    }

    private void startRunner() {
        Runnable runner = new CommandQueueRunner(plugin, tasksManager, this);
        Thread thread = new Thread(runner);
        thread.start();
        this.runnerThread = thread;
    }

    public void addCommand(QueuedCommand command) {
        queuedCommands.add(command);
    }

    public void removeCommand(QueuedCommand command) {
        queuedCommands.remove(command);
    }

    public List<QueuedCommand> getQueuedCommands() {
        return queuedCommands;
    }
}
