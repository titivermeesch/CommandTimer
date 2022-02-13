package me.playbosswar.com.commands;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.enums.CommandExecutionMode;
import me.playbosswar.com.gui.MainMenu;
import me.playbosswar.com.permissions.PermissionUtils;
import me.playbosswar.com.tasks.CommandIntervalExecutorRunnable;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TasksManager;
import me.playbosswar.com.utils.Messages;
import me.playbosswar.com.utils.Tools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        if (!PermissionUtils.playerHasSomeAccess(sender)) {
            Messages.sendNoPermission(sender);
            return true;
        }

        TasksManager tasksManager = CommandTimerPlugin.getInstance().getTasksManager();

        if (args.length == 0 && sender instanceof Player) {
            if (!sender.hasPermission("commandtimer.manage")) {
                Messages.sendNoPermission(sender);
                return true;
            }

            new MainMenu().INVENTORY.open((Player) sender);
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                Messages.sendHelpMessage(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("time")) {
                if (!(sender instanceof Player)) {
                    Messages.sendNeedToBePlayer(sender);
                    return true;
                }

                Player p = (Player) sender;
                Messages.sendMessage(sender, "Minecraft Time: " + Tools.calculateWorldTime(p.getWorld()));
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
                if (!sender.hasPermission("commandtimer.activate") && !sender.hasPermission("commandtimer.toggle")) {
                    Messages.sendNoPermission(sender);
                    return true;
                }

                task.setActive(true);
                Messages.sendMessage(sender, "§aTask has been activated");
                return true;
            }

            if (action.equalsIgnoreCase("deactivate")) {
                if (!sender.hasPermission("commandtimer.deactivate") && !sender.hasPermission("commandtimer.toggle")) {
                    Messages.sendNoPermission(sender);
                    return true;
                }

                task.setActive(false);
                Messages.sendMessage(sender, "§aTask has been deactivated");
                return true;
            }

            if (action.equalsIgnoreCase("execute")) {
                if (!sender.hasPermission("commandtimer.execute")) {
                    Messages.sendNoPermission(sender);
                    return true;
                }

                if (task.getCommandExecutionMode().equals(CommandExecutionMode.INTERVAL)) {
                    Bukkit.getScheduler().runTaskTimer(
                            CommandTimerPlugin.getPlugin(),
                            new CommandIntervalExecutorRunnable(task),
                            0,
                            task.getCommandExecutionInterval().toSeconds() * 20L);

                    Messages.sendMessage(sender, "§aTask is being executed. Not all commands may be executed at once.");
                    return true;
                }

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
