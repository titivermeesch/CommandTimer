package me.playbosswar.com.tasks;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.enums.CommandExecutionMode;
import me.playbosswar.com.enums.Gender;
import me.playbosswar.com.hooks.PAPIHook;
import me.playbosswar.com.utils.Files;
import me.playbosswar.com.utils.Messages;
import me.playbosswar.com.utils.Tools;
import me.playbosswar.com.utils.WeatherConditions;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TasksManager {
    private final List<Task> loadedTasks = new ArrayList<>();
    private final List<TaskCommand> scheduledExecutions = new ArrayList<>();
    private Thread runnerThread;
    public boolean stopRunner = false;

    public TasksManager() {
        loadedTasks.addAll(Files.deserializeJsonFilesIntoCommandTimers());
        startRunner();
        startCommandExecutor();
    }

    public Task createTask() {
        String name = "Task_" + RandomStringUtils.randomAlphabetic(4);
        Task task = new Task(name);
        loadedTasks.add(task);

        return task;
    }

    @Nullable
    public Task getTaskByName(String name) {
        Optional<Task> optionalTask = loadedTasks.stream().filter(task -> task.getName().equalsIgnoreCase(name)).findFirst();
        return optionalTask.orElse(null);
    }

    public void removeTask(Task task) throws IOException {
        File file = new File(Files.getTaskFile(task.getName()));

        if (!file.exists()) {
            return;
        }

        boolean deleted = file.delete();

        if (!deleted) {
            throw new IOException();
        }

        loadedTasks.remove(task);
    }

    public List<Task> getLoadedTasks() {
        return loadedTasks;
    }

    private void startRunner() {
        Runnable runner = new TaskRunner();
        Thread thread = new Thread(runner);
        thread.start();
        this.runnerThread = thread;
    }

    private void runConsolePerUserCommand(TaskCommand taskCommand) {
        String command = taskCommand.getCommand();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!WeatherConditions.checkPlayerMatchesWeather(p, taskCommand.getWeatherConditions())) {
                Messages.sendDebugConsole("Stopped execution because player has wrong weather");
                continue;
            }

            if (taskCommand.getTask().getWorlds().size() > 0 && !taskCommand.getTask().getWorlds().contains(p.getWorld().getName())) {
                Messages.sendDebugConsole("Player is in a world that is not affected by task");
                continue;
            }

            if (taskCommand.getTask().getCondition() != null) {
                boolean valid = TaskValidationHelpers.processCondition(taskCommand.getTask().getCondition(), p);
                if (!valid) {
                    Messages.sendDebugConsole("Conditions did not match");
                    continue;
                }
            }

            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, p));
        }
    }

    private void runConsoleCommand(TaskCommand taskCommand) {
        if (taskCommand.getTask().getCondition() != null) {
            boolean valid = TaskValidationHelpers.processCondition(taskCommand.getTask().getCondition(), null);
            if (!valid) {
                Messages.sendDebugConsole("Conditions did not match");
                return;
            }
        }

        String command = taskCommand.getCommand();
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, null));
    }

    private void runPlayerCommand(TaskCommand taskCommand) {
        String command = taskCommand.getCommand();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!WeatherConditions.checkPlayerMatchesWeather(p, taskCommand.getWeatherConditions())) {
                Messages.sendDebugConsole("Player has incorrect weather for his current location");
                continue;
            }

            if (taskCommand.getTask().getWorlds().size() > 0 && !taskCommand.getTask().getWorlds().contains(p.getWorld().getName())) {
                Messages.sendDebugConsole("Player is in a world that is not affected by task");
                continue;
            }

            if (taskCommand.getTask().getCondition() != null) {
                boolean valid = TaskValidationHelpers.processCondition(taskCommand.getTask().getCondition(), p);
                if (!valid) {
                    continue;
                }
            }

            p.performCommand(PAPIHook.parsePAPI(command, p));
        }
    }

    private void runOperatorCommand(TaskCommand taskCommand) {
        String command = taskCommand.getCommand();

        for (Player p : Bukkit.getOnlinePlayers()) {
            boolean wasAlreadyOp = p.isOp();

            try {
                if (!wasAlreadyOp) {
                    p.setOp(true);
                }

                if (!WeatherConditions.checkPlayerMatchesWeather(p, taskCommand.getWeatherConditions())) {
                    if (!wasAlreadyOp) {
                        p.setOp(false);
                    }
                    continue;
                }

                if (taskCommand.getTask().getWorlds().size() > 0 && !taskCommand.getTask().getWorlds().contains(p.getWorld().getName())) {
                    Messages.sendDebugConsole("Player is in a world that is not affected by task");
                    if (!wasAlreadyOp) {
                        p.setOp(false);
                    }
                    continue;
                }

                if (taskCommand.getTask().getCondition() != null) {
                    boolean valid = TaskValidationHelpers.processCondition(taskCommand.getTask().getCondition(), p);
                    if (!valid) {
                        if (!wasAlreadyOp) {
                            p.setOp(false);
                        }
                        continue;
                    }
                }

                p.performCommand(PAPIHook.parsePAPI(command, p));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (!wasAlreadyOp) {
                    p.setOp(false);
                }
            }
        }
    }

    public void addTaskCommandExecution(TaskCommand taskCommand) {
        scheduledExecutions.add(taskCommand);
    }

    // Executes scheduled commands
    private void startCommandExecutor() {
        BukkitRunnable runnable = new BukkitRunnable() {

            @Override
            public void run() {
                final List<TaskCommand> tasksToRemove = new ArrayList<>(scheduledExecutions);

                tasksToRemove.forEach(taskCommand -> {
                    Task task = taskCommand.getTask();
                    Gender gender = taskCommand.getGender();

                    // Choose correct gender executor
                    if (gender.equals(Gender.CONSOLE)) {
                        runConsoleCommand(taskCommand);
                    } else if (gender.equals(Gender.PLAYER)) {
                        runPlayerCommand(taskCommand);
                    } else if (gender.equals(Gender.OPERATOR)) {
                        runOperatorCommand(taskCommand);
                    } else if (gender.equals(Gender.CONSOLE_PER_USER)) {
                        runConsolePerUserCommand(taskCommand);
                    }

                    task.setLastExecuted(new Date());
                    task.setTimesExecuted(task.getTimesExecuted() + 1);
                    task.setLastExecutedCommandIndex(task.getCommands().indexOf(taskCommand));
                    scheduledExecutions.remove(taskCommand);
                });

            }
        };

        runnable.runTaskTimer(CommandTimerPlugin.getPlugin(), 20L, 20L);
    }

    public int getNextTaskCommandIndex(Task task) {
        // If it remains -1, that means that all commands should be executed
        int selectedCommandIndex = -1;
        if (task.getCommandExecutionMode().equals(CommandExecutionMode.RANDOM)) {
            selectedCommandIndex = Tools.getRandomInt(0, task.getCommands().size() - 1);
        } else if (task.getCommandExecutionMode().equals(CommandExecutionMode.ORDERED)) {
            int currentLatestCommandIndex = task.getLastExecutedCommandIndex();

            if (currentLatestCommandIndex == task.getCommands().size() - 1) {
                selectedCommandIndex = 0;
            } else {
                selectedCommandIndex = currentLatestCommandIndex + 1;
            }
        }

        return selectedCommandIndex;
    }

    public void disable() {
        stopRunner = true;
        runnerThread.stop();
    }
}
