package me.playbosswar.com.tasks;

import me.playbosswar.com.Main;
import me.playbosswar.com.enums.Gender;
import me.playbosswar.com.hooks.PAPIHook;
import me.playbosswar.com.utils.Files;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TasksManager {
    private final List<Task> loadedTasks = new ArrayList<>();
    private final List<TaskCommand> scheduledExecutions = new ArrayList<>();
    private Thread runnerThread;

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
    public Task getTaskByName(String name) { return loadedTasks.stream().filter(task -> task.getName().equalsIgnoreCase(name)).findFirst().get();}

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

    // Checks if any command should be scheduled for execution
    private void startRunner() {
        Runnable runner = new TaskRunner();
        Thread thread = new Thread(runner);
        thread.start();
        this.runnerThread = thread;
    }

    private void runConsolePerUserCommand(TaskCommand taskCommand) {
        String command = taskCommand.getCommand();
        String permission = taskCommand.getTask().getRequiredPermission();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!permission.equals("") && p.hasPermission(permission)) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, p));
            }
        }
    }

    private void runConsoleCommand(TaskCommand taskCommand) {
        String command = taskCommand.getCommand();

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, null));
    }

    private void runPlayerCommand(TaskCommand taskCommand) {
        String command = taskCommand.getCommand();
        String permission = taskCommand.getTask().getRequiredPermission();

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!permission.equals("") && p.hasPermission(permission)) {
                p.performCommand(PAPIHook.parsePAPI(command, p));
            }
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

    // Executes scheduled commands
    private void startCommandExecutor() {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                scheduledExecutions.forEach(taskCommand -> {
                    Task task = taskCommand.getTask();
                    Gender gender = taskCommand.getGender();

                    // Choose correct gender executor
                    if (gender.equals(Gender.CONSOLE)) {
                        runConsoleCommand(taskCommand);
                    } else if (gender.equals(Gender.PLAYER)) {
                        runPlayerCommand(taskCommand);
                    } else if (gender.equals(Gender.OPERATOR)) {
                        runOperatorCommand(taskCommand);
                    } else if(gender.equals(Gender.CONSOLE_PER_USER)) {
                        runConsolePerUserCommand(taskCommand);
                    }

                    final LocalTime lastExecuted = LocalTime.now();

                    task.setLastExecuted(lastExecuted);
                    task.setTimesExecuted(task.getTimesExecuted() + 1);
                });
            }
        };

        runnable.runTaskTimer(Main.getPlugin(), 20L, 20L);
    }

    public void disable() {
        runnerThread.stop();
    }
}
