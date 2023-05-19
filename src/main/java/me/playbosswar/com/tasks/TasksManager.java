package me.playbosswar.com.tasks;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.conditionsengine.validations.Condition;
import me.playbosswar.com.enums.CommandExecutionMode;
import me.playbosswar.com.enums.Gender;
import me.playbosswar.com.hooks.PAPIHook;
import me.playbosswar.com.utils.Files;
import me.playbosswar.com.utils.Messages;
import me.playbosswar.com.utils.StringEnhancer;
import me.playbosswar.com.utils.Tools;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class TasksManager {
    private static final String CONDITION_NO_MATCH = "Conditions did not match";
    private final List<Task> loadedTasks = new ArrayList<>();
    private final List<TaskCommand> scheduledExecutions = new ArrayList<>();
    private Thread runnerThread;
    public boolean stopRunner = false;
    public int executionsSinceLastSync = 0;

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
        Optional<Task> optionalTask =
                loadedTasks.stream().filter(task -> task.getName().equalsIgnoreCase(name)).findFirst();
        return optionalTask.orElse(null);
    }

    public void removeTask(Task task) throws IOException {
        try {
            java.nio.file.Files.delete(Paths.get(Files.getTaskFile(task.getName())));
            loadedTasks.remove(task);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public List<Task> getLoadedTasks() {
        return loadedTasks;
    }

    private void startRunner() {
        Runnable runner = new TaskRunner(this);
        Thread thread = new Thread(runner);
        thread.start();
        this.runnerThread = thread;
    }

    private void runConsolePerUserCommand(TaskCommand taskCommand, List<UUID> scopedPlayers) throws CommandException {
        String command = taskCommand.getCommand();

        Collection<Player> affectedPlayers = (Collection<Player>) Bukkit.getOnlinePlayers();
        if(!scopedPlayers.isEmpty()) {
            affectedPlayers = scopedPlayers
                    .stream()
                    .map(Bukkit::getPlayer)
                    .filter(Objects::nonNull)
                    .toList();
        }

        for(Player p : affectedPlayers) {
            if(taskCommand.getTask().getCondition() != null) {
                boolean valid = TaskValidationHelpers.processCondition(taskCommand.getTask().getCondition(), p);
                if(!valid) {
                    Messages.sendDebugConsole(CONDITION_NO_MATCH);
                    continue;
                }
            }

            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, p));
            executionsSinceLastSync++;
        }
    }

    private void runConsolePerUserOfflineCommand(TaskCommand taskCommand) throws CommandException {
        String command = taskCommand.getCommand();

        // TODO: Caching could be used heres, Bukkit.getOfflinePlayers() is pretty expensive
        for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, p));
            executionsSinceLastSync++;
        }
    }

    private void runConsolePerUserCommand(TaskCommand taskCommand) throws CommandException {
        runConsolePerUserCommand(taskCommand, new ArrayList<>());
    }

    private void runConsoleCommand(TaskCommand taskCommand) throws CommandException {
        Condition condition = taskCommand.getTask().getCondition();
        if(condition != null && (!condition.getConditions().isEmpty() || (condition.getSimpleCondition() != null && condition.getSimpleCondition().getConditionGroup() != null))) {
            boolean valid = TaskValidationHelpers.processCondition(taskCommand.getTask().getCondition(), null);
            if(!valid) {
                Messages.sendDebugConsole(CONDITION_NO_MATCH);
                return;
            }
        }

        String command = taskCommand.getCommand();
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, null));
        executionsSinceLastSync++;
    }

    private void runPlayerCommand(TaskCommand taskCommand, List<UUID> scopedPlayers) {
        String command = taskCommand.getCommand();

        Collection<Player> affectedPlayers = (Collection<Player>) Bukkit.getOnlinePlayers();
        if(!scopedPlayers.isEmpty()) {
            affectedPlayers = scopedPlayers
                    .stream()
                    .map(Bukkit::getPlayer)
                    .filter(Objects::nonNull)
                    .toList();
        }

        for(Player p : affectedPlayers) {
            if(taskCommand.getTask().getCondition() != null) {
                boolean valid = TaskValidationHelpers.processCondition(taskCommand.getTask().getCondition(), p);
                if(!valid) {
                    Messages.sendDebugConsole(CONDITION_NO_MATCH);
                    continue;
                }
            }

            String parsedCommand = PAPIHook.parsePAPI(command, p);
            boolean executed = p.performCommand(parsedCommand);

            if(!executed) {
                String errorMessage = new StringEnhancer("Failed to execute command {command}").add("taskName",
                        command).parse();
                throw new CommandException(errorMessage);
            }
            executionsSinceLastSync++;
        }
    }

    private void runPlayerCommand(TaskCommand taskCommand) {
        runPlayerCommand(taskCommand, new ArrayList<>());
    }

    private void runOperatorCommand(TaskCommand taskCommand, List<UUID> scopedPlayers) {
        String command = taskCommand.getCommand();

        Collection<Player> affectedPlayers = (Collection<Player>) Bukkit.getOnlinePlayers();
        if(!scopedPlayers.isEmpty()) {
            affectedPlayers = scopedPlayers
                    .stream()
                    .map(Bukkit::getPlayer)
                    .filter(Objects::nonNull)
                    .toList();
        }

        for(Player p : affectedPlayers) {
            boolean wasAlreadyOp = p.isOp();

            try {
                if(taskCommand.getTask().getCondition() != null) {
                    boolean valid = TaskValidationHelpers.processCondition(taskCommand.getTask().getCondition(), p);
                    if(!valid) {
                        Messages.sendDebugConsole(CONDITION_NO_MATCH);

                        if(!wasAlreadyOp) {
                            p.setOp(false);
                        }
                        continue;
                    }
                }

                p.performCommand(PAPIHook.parsePAPI(command, p));
                executionsSinceLastSync++;
            } finally {
                if(!wasAlreadyOp) {
                    p.setOp(false);
                }
            }
        }
    }

    private void runOperatorCommand(TaskCommand taskCommand) {
        runOperatorCommand(taskCommand, new ArrayList<>());
    }

    public void addTaskCommandExecution(TaskCommand taskCommand) {
        scheduledExecutions.add(taskCommand);
    }

    public void processCommandExecution(TaskCommand taskCommand) {
        Gender gender = taskCommand.getGender();

        if(gender.equals(Gender.CONSOLE)) {
            runConsoleCommand(taskCommand);
        } else if(gender.equals(Gender.PLAYER)) {
            runPlayerCommand(taskCommand);
        } else if(gender.equals(Gender.OPERATOR)) {
            runOperatorCommand(taskCommand);
        } else if(gender.equals(Gender.CONSOLE_PER_USER)) {
            runConsolePerUserCommand(taskCommand);
        } else if(gender.equals(Gender.CONSOLE_PER_USER_OFFLINE)) {
            runConsolePerUserOfflineCommand(taskCommand);
        }
    }

    // Executes scheduled commands
    private void startCommandExecutor() {
        BukkitRunnable runnable = new BukkitRunnable() {

            @Override
            public void run() {
                final List<TaskCommand> tasksToRemove = new ArrayList<>(scheduledExecutions);

                tasksToRemove.forEach(taskCommand -> {
                    processCommandExecution(taskCommand);
                    scheduledExecutions.remove(taskCommand);
                });

            }
        };

        runnable.runTaskTimer(CommandTimerPlugin.getPlugin(), 20L, 20L);
    }

    public int getNextTaskCommandIndex(Task task) {
        // If it remains -1, that means that all commands should be executed
        int selectedCommandIndex = -1;
        if(task.getCommandExecutionMode().equals(CommandExecutionMode.RANDOM)) {
            selectedCommandIndex = Tools.getRandomInt(0, task.getCommands().size() - 1);
        } else if(task.getCommandExecutionMode().equals(CommandExecutionMode.ORDERED)) {
            int currentLatestCommandIndex = task.getLastExecutedCommandIndex();

            if(currentLatestCommandIndex == task.getCommands().size() - 1) {
                selectedCommandIndex = 0;
            } else {
                selectedCommandIndex = currentLatestCommandIndex + 1;
            }
        }

        return selectedCommandIndex;
    }

    public void disable() {
        loadedTasks.forEach(Task::storeInstance);
        stopRunner = true;
        runnerThread.stop();
    }
}
