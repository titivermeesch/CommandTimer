package me.playbosswar.com.commands;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.enums.CommandExecutionMode;
import me.playbosswar.com.gui.MainMenu;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.permissions.PermissionUtils;
import me.playbosswar.com.enums.Gender;
import me.playbosswar.com.tasks.AdHocCommandsManager;
import me.playbosswar.com.tasks.CommandIntervalExecutorRunnable;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TaskInterval;
import me.playbosswar.com.tasks.TasksManager;
import me.playbosswar.com.utils.Files;
import me.playbosswar.com.utils.Messages;
import me.playbosswar.com.utils.Tools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor {
    private final Plugin plugin;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();

    public MainCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        if(!PermissionUtils.playerHasSomeAccess(sender)) {
            Messages.sendNoPermission(sender);
            return true;
        }

        TasksManager tasksManager = CommandTimerPlugin.getInstance().getTasksManager();
        AdHocCommandsManager adHocCommandsManager = CommandTimerPlugin.getInstance().getAdHocCommandsManager();

        if(args.length >= 2 && args[0].equalsIgnoreCase("schedule")) {
            if(!sender.hasPermission("commandtimer.schedule")) {
                Messages.sendNoPermission(sender);
                return true;
            }

            TaskInterval delay = new TaskInterval(0, 0, 0, 0);
            Gender gender = Gender.CONSOLE;
            List<String> commandParts = new ArrayList<>();

            for(int i = 1; i < args.length; i++) {
                String arg = args[i];
                if(arg.equals("-after")) {
                    if(i + 1 < args.length) {
                        String timeStr = args[i + 1];
                        delay = Tools.parseTimeString(timeStr);
                        i++;
                    } else {
                        Messages.sendMessage(sender, "&cMissing value for -after parameter.");
                        Messages.sendMessage(sender, "&7Usage: /cmt schedule [-after 1h10m5s] [-gender CONSOLE] <command>");
                        return true;
                    }
                } else if(arg.equals("-gender")) {
                    if(i + 1 < args.length) {
                        String genderStr = args[i + 1].toUpperCase();
                        try {
                            gender = Gender.valueOf(genderStr);
                            i++;
                        } catch(IllegalArgumentException e) {
                            Messages.sendMessage(sender, "&cInvalid gender: " + genderStr + ". Valid options: CONSOLE, PLAYER, OPERATOR, CONSOLE_PER_USER, CONSOLE_PER_USER_OFFLINE, CONSOLE_PROXY");
                            return true;
                        }
                    } else {
                        Messages.sendMessage(sender, "&cMissing value for -gender parameter.");
                        Messages.sendMessage(sender, "&7Usage: /cmt schedule [-after 1h10m5s] [-gender CONSOLE] <command>");
                        return true;
                    }
                } else {
                    commandParts.add(arg);
                }
            }

            if(commandParts.isEmpty()) {
                Messages.sendMessage(sender, "&cYou must provide a command to schedule.");
                Messages.sendMessage(sender, "&7Usage: /cmt schedule [-after 1h10m5s] [-gender CONSOLE] <command>");
                return true;
            }

            String command = String.join(" ", commandParts);
            if(command.startsWith("/")) {
                command = command.substring(1);
            }

            ZonedDateTime scheduledTime = ZonedDateTime.now().plusSeconds(delay.toSeconds());
            adHocCommandsManager.scheduleCommand(command, gender, scheduledTime);

            Messages.sendMessage(sender, "&aCommand scheduled successfully");
            return true;
        }

        if(args.length == 0 && sender instanceof Player) {
            if(!sender.hasPermission("commandtimer.manage")) {
                Messages.sendNoPermission(sender);
                return true;
            }

            new MainMenu().INVENTORY.open((Player) sender);
            return true;
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("help")) {
                Messages.sendHelpMessage(sender);
                return true;
            }

            if(args[0].equalsIgnoreCase("migrateToDatabase")) {
                if(!sender.hasPermission("commandtimer.manage")) {
                    Messages.sendNoPermission(sender);
                    return true;
                }

                if(!CommandTimerPlugin.getPlugin().getConfig().getBoolean("database.enabled")) {
                    Messages.sendMessage(sender, "Please enable the database in the config file");
                    return true;
                }

                Messages.sendMessage(sender, "Migrating files to database");
                List<Task> tasks = Files.deserializeJsonFilesIntoCommandTimers();
                try {
                    CommandTimerPlugin.getTaskDao().create(tasks);
                } catch(SQLException e) {
                    throw new RuntimeException(e);
                }
                tasks.forEach(t -> {
                    try {
                        java.nio.file.Files.delete(Paths.get(Files.getTaskFile(t.getId())));
                    } catch(IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                Messages.sendMessage(sender, "Migration completed successfully");
                return true;
            }

            if(args[0].equalsIgnoreCase("time")) {
                if(!(sender instanceof Player)) {
                    Messages.sendNeedToBePlayer(sender);
                    return true;
                }

                Player p = (Player) sender;
                String worldTime = Tools.calculateWorldTime(p.getWorld());
                Messages.sendMessage(sender, languageManager.get(LanguageKey.MINECRAFT_TIME, worldTime));
                return true;
            }

            if(args[0].equalsIgnoreCase("reload")) {
                if(!sender.hasPermission("commandtimer.manage")) {
                    Messages.sendNoPermission(sender);
                    return true;
                }

                Messages.sendMessage(sender, "&4IT IS NOT RECOMMENDED TO RELOAD COMMANDTIMER");
                CommandTimerPlugin pl = CommandTimerPlugin.getInstance();
                pl.getTasksManager().disable();
                pl.saveDefaultConfig();

                pl.setTasksManager(new TasksManager(plugin));

                Messages.sendMessage(sender, CommandTimerPlugin.getLanguageManager().get(LanguageKey.PLUGIN_RELOADED));
                return true;
            }
        }

        if(args.length == 2) {
            String action = args[0];
            String taskName = args[1];

            Task task = tasksManager.getTaskByName(taskName);
            if(task == null) {
                Messages.sendMessage(sender, languageManager.get(LanguageKey.NO_TASK));
                return true;
            }

            if(action.equalsIgnoreCase("activate")) {
                if(!sender.hasPermission("commandtimer.activate") && !sender.hasPermission("commandtimer.toggle")) {
                    Messages.sendNoPermission(sender);
                    return true;
                }

                task.setActive(true);
                Messages.sendMessage(sender, languageManager.get(LanguageKey.TASK_ACTIVATED));
                return true;
            }

            if(action.equalsIgnoreCase("deactivate")) {
                if(!sender.hasPermission("commandtimer.deactivate") && !sender.hasPermission("commandtimer.toggle")) {
                    Messages.sendNoPermission(sender);
                    return true;
                }

                task.setActive(false);
                Messages.sendMessage(sender, languageManager.get(LanguageKey.TASK_DEACTIVATED));
                return true;
            }

            if(action.equalsIgnoreCase("execute")) {
                if(!sender.hasPermission("commandtimer.execute")) {
                    Messages.sendNoPermission(sender);
                    return true;
                }

                if(task.getCommandExecutionMode().equals(CommandExecutionMode.INTERVAL)) {
                    CommandTimerPlugin.getScheduler().runTaskTimer(
                            new CommandIntervalExecutorRunnable(task),
                            1,
                            task.getCommandExecutionInterval().toSeconds() * 20L);

                    Messages.sendMessage(sender, languageManager.get(LanguageKey.TASK_EXECUTION_ONGOING));
                    return true;
                }

                if(task.getCommandExecutionMode().equals(CommandExecutionMode.ORDERED)) {
                    final int[] accumulatedDelaySeconds = {0};
                    task.getCommands().forEach(command -> {
                        CommandTimerPlugin.getScheduler().runTaskLater(
                                () -> tasksManager.processCommandExecution(task, command),
                                (20L * accumulatedDelaySeconds[0]) + 1);
                        accumulatedDelaySeconds[0] += command.getDelay().toSeconds();
                    });
                    Messages.sendMessage(sender, languageManager.get(LanguageKey.TASK_EXECUTION_ONGOING));
                    return true;
                }

                int selectedCommandIndex = tasksManager.getNextTaskCommandIndex(task);
                if(selectedCommandIndex == -1) {
                    CommandTimerPlugin.getScheduler().runTask(() ->
                            task.getCommands().forEach(command -> tasksManager.processCommandExecution(task, command)));
                } else {
                    CommandTimerPlugin.getScheduler().runTask(() ->
                            tasksManager.processCommandExecution(task, task.getCommands().get(selectedCommandIndex)));
                }

                Messages.sendMessage(sender, languageManager.get(LanguageKey.TASK_EXECUTED));
                return true;
            }
        }

        Messages.sendHelpMessage(sender);
        return true;
    }
}
