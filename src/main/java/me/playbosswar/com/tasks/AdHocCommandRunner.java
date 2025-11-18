package me.playbosswar.com.tasks;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.utils.Messages;

import java.util.List;
import java.util.TimerTask;

public class AdHocCommandRunner implements Runnable {
    private final AdHocCommandsManager manager;

    public AdHocCommandRunner(AdHocCommandsManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        CommandTimerPlugin.getScheduler().runTaskTimer(new TimerTask() {
            @Override
            public void run() {
                if (manager.stopRunner) {
                    Messages.sendDebugConsole("Ignoring ad-hoc command execution because manager has been stopped");
                    return;
                }

                List<AdHocCommand> commandsToExecute = manager.getCommandsToExecute();
                commandsToExecute.forEach(command -> {
                    Messages.sendDebugConsole("Executing ad-hoc command: " + command.getCommand());
                    manager.processCommandExecution(command);
                });
            }
        }, 1, 10);
    }
}

