package me.playbosswar.com.commands;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.gui.MainMenu;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TasksManager;
import me.playbosswar.com.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        if (!sender.hasPermission("commandtimer.use")) {
            Messages.sendNoPermission(sender);
            return true;
        }

        TasksManager tasksManager = CommandTimerPlugin.getInstance().getTasksManager();

        if(args.length == 0 && sender instanceof Player) {
            new MainMenu().INVENTORY.open((Player) sender);
            return true;
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("help")) {
                Messages.sendHelpMessage(sender);
                return true;
            }
        }

        if (args.length == 2) {
            String action = args[0];
            String taskName = args[1];

            Task task = tasksManager.getTaskByName(taskName);
            if (task == null) {
                Messages.sendMessage(sender, "§cThere is no task with this name");
                return true;
            }

            if (action.equalsIgnoreCase("activate")) {
                task.setActive(true);
                Messages.sendMessage(sender, "§aTask has been activated");
                return true;
            }

            if (action.equalsIgnoreCase("deactivate")) {
                task.setActive(false);
                Messages.sendMessage(sender, "§aTask has been deactivated");
                return true;
            }

            if (action.equalsIgnoreCase("execute")) {
                int selectedCommandIndex = tasksManager.getNextTaskCommandIndex(task);
                if (selectedCommandIndex == -1) {
                    task.getCommands().forEach(tasksManager::addTaskCommandExecution);
                } else {
                    tasksManager.addTaskCommandExecution(task.getCommands().get(selectedCommandIndex));
                }

                Messages.sendMessage(sender, "§aTask has been executed");
                return true;
            }
        }

        Messages.sendHelpMessage(sender);
        return true;
    }
}
