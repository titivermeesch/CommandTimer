package me.playbosswar.com.tasks;

import me.playbosswar.com.CommandTimerPlugin;
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
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class TasksManager {
    private static final String CONDITION_NO_MATCH = "Conditions did not match";
    private List<Task> loadedTasks = new ArrayList<>();
    private Thread runnerThread;
    public boolean stopRunner = false;
    public int executionsSinceLastSync = 0;

    public TasksManager() {
        loadedTasks.addAll(Files.deserializeJsonFilesIntoCommandTimers());
        startRunner();
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

    public List<Task> getActiveTasks() {
        return loadedTasks.stream().filter(Task::isActive).collect(Collectors.toList());
    }

    public void setLoadedTasks(List<Task> loadedTasks) {
        this.loadedTasks = loadedTasks;
    }

    private void startRunner() {
        Runnable runner = new TaskRunner(this);
        Thread thread = new Thread(runner);
        thread.start();
        this.runnerThread = thread;
    }

    private boolean runConsolePerUserCommand(TaskCommand taskCommand, List<UUID> scopedPlayers) throws CommandException {
        String command = taskCommand.getCommand();

        Collection<Player> affectedPlayers = (Collection<Player>) Bukkit.getOnlinePlayers();
        if(!scopedPlayers.isEmpty()) {
            affectedPlayers =
                    scopedPlayers.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toList());
        }

        boolean delayedExecutions = taskCommand.getInterval().toSeconds() > 0;
        int i = 0;
        boolean willExecute = false;
        for(Player p : affectedPlayers) {
            if(taskCommand.getTask().hasCondition()) {
                boolean valid = TaskValidationHelpers.processCondition(taskCommand.getTask().getCondition(), p);
                if(!valid) {
                    Messages.sendDebugConsole(CONDITION_NO_MATCH);
                    continue;
                }
                willExecute = true;
            }

            if(delayedExecutions) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(CommandTimerPlugin.getPlugin(), () -> {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, p));
                    executionsSinceLastSync++;
                }, 20L * i * taskCommand.getInterval().toSeconds());
            } else {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, p));
                executionsSinceLastSync++;
            }
            i++;
        }

        return willExecute;
    }

    private boolean runConsolePerUserOfflineCommand(TaskCommand taskCommand) throws CommandException {
        String command = taskCommand.getCommand();
        boolean delayedExecutions = taskCommand.getInterval().toSeconds() > 0;

        int i = 0;
        // TODO: Caching could be used heres, Bukkit.getOfflinePlayers() is pretty expensive
        for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            if(delayedExecutions) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(CommandTimerPlugin.getPlugin(), () -> {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, p));
                    executionsSinceLastSync++;
                }, 20L * i * taskCommand.getInterval().toSeconds());
            } else {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, p));
                executionsSinceLastSync++;
            }
            i++;
        }

        return true;
    }

    private boolean runConsolePerUserCommand(TaskCommand taskCommand) throws CommandException {
        return runConsolePerUserCommand(taskCommand, new ArrayList<>());
    }

    private boolean runConsoleCommand(TaskCommand taskCommand) throws CommandException {
        if(taskCommand.getTask().hasCondition()) {
            boolean valid = TaskValidationHelpers.processCondition(taskCommand.getTask().getCondition(), null);
            if(!valid) {
                Messages.sendDebugConsole(CONDITION_NO_MATCH);
                return false;
            }
        }

        String command = taskCommand.getCommand();
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, null));
        executionsSinceLastSync++;
        return true;
    }

    private boolean runPlayerCommand(TaskCommand taskCommand, List<UUID> scopedPlayers) {
        String command = taskCommand.getCommand();

        Collection<Player> affectedPlayers = (Collection<Player>) Bukkit.getOnlinePlayers();
        if(!scopedPlayers.isEmpty()) {
            affectedPlayers =
                    scopedPlayers.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toList());
        }

        boolean delayedExecution = taskCommand.getInterval().toSeconds() > 0;
        int i = 0;
        boolean willExecute = false;
        for(Player p : affectedPlayers) {
            if(taskCommand.getTask().hasCondition()) {
                boolean valid = TaskValidationHelpers.processCondition(taskCommand.getTask().getCondition(), p);
                if(!valid) {
                    Messages.sendDebugConsole(CONDITION_NO_MATCH);
                    continue;
                }
                willExecute = true;
            }

            if(delayedExecution) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(CommandTimerPlugin.getPlugin(), () -> runForPlayer(p,
                        command), 20L * i * taskCommand.getInterval().toSeconds());
            } else {
                runForPlayer(p, command);
            }
            i++;
        }

        return willExecute;
    }

    private void runForPlayer(Player p, String command) {
        String parsedCommand = PAPIHook.parsePAPI(command, p);
        boolean executed = p.performCommand(parsedCommand);

        if(!executed) {
            String errorMessage =
                    new StringEnhancer("Failed to execute command {command}").add("taskName", command).parse();
            throw new CommandException(errorMessage);
        }
        executionsSinceLastSync++;
    }

    private boolean runPlayerCommand(TaskCommand taskCommand) {
        return runPlayerCommand(taskCommand, new ArrayList<>());
    }

    private boolean runOperatorCommand(TaskCommand taskCommand, List<UUID> scopedPlayers) {
        String command = taskCommand.getCommand();

        Collection<Player> affectedPlayers = (Collection<Player>) Bukkit.getOnlinePlayers();
        if(!scopedPlayers.isEmpty()) {
            affectedPlayers =
                    scopedPlayers.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toList());
        }

        boolean delayedExecutions = taskCommand.getInterval().toSeconds() > 0;
        int i = 0;
        boolean willExecute = false;
        for(Player p : affectedPlayers) {
            boolean wasAlreadyOp = p.isOp();

            try {
                p.setOp(true);
                if(taskCommand.getTask().hasCondition()) {
                    boolean valid = TaskValidationHelpers.processCondition(taskCommand.getTask().getCondition(), p);
                    if(!valid) {
                        Messages.sendDebugConsole(CONDITION_NO_MATCH);

                        if(!wasAlreadyOp) {
                            p.setOp(false);
                        }
                        continue;
                    }
                    willExecute = true;
                }

                if(delayedExecutions) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(CommandTimerPlugin.getPlugin(), () -> {
                        p.performCommand(PAPIHook.parsePAPI(command, p));
                        executionsSinceLastSync++;
                    }, 20L * i * taskCommand.getInterval().toSeconds());
                } else {
                    p.performCommand(PAPIHook.parsePAPI(command, p));
                    executionsSinceLastSync++;
                }

            } finally {
                if(!wasAlreadyOp) {
                    p.setOp(false);
                }
            }
            i++;
        }

        return willExecute;
    }

    private boolean runOperatorCommand(TaskCommand taskCommand) {
        return runOperatorCommand(taskCommand, new ArrayList<>());
    }

    public void processCommandExecution(TaskCommand taskCommand) {
        Task task = taskCommand.getTask();
        if(!task.isActive()) {
            return;
        }

        Gender gender = taskCommand.getGender();

        boolean executed = false;
        if(gender.equals(Gender.CONSOLE)) {
            executed = runConsoleCommand(taskCommand);
        } else if(gender.equals(Gender.PLAYER)) {
            executed = runPlayerCommand(taskCommand);
        } else if(gender.equals(Gender.OPERATOR)) {
            executed = runOperatorCommand(taskCommand);
        } else if(gender.equals(Gender.CONSOLE_PER_USER)) {
            executed = runConsolePerUserCommand(taskCommand);
        } else if(gender.equals(Gender.CONSOLE_PER_USER_OFFLINE)) {
            executed = runConsolePerUserOfflineCommand(taskCommand);
        }

        if(executed) {
            task.setLastExecuted(new Date());
            task.setTimesExecuted(taskCommand.getTask().getTimesExecuted() + 1);
            task.storeInstance();
        }
    }

    public int getNextTaskCommandIndex(Task task) {
        // If it remains -1, that means that all commands should be executed
        int selectedCommandIndex = -1;
        if(task.getCommandExecutionMode().equals(CommandExecutionMode.RANDOM)) {
            selectedCommandIndex = Tools.getRandomInt(0, task.getCommands().size() - 1);
        } else if(task.getCommandExecutionMode().equals(CommandExecutionMode.ORDERED)) {
            int currentLatestCommandIndex = task.getLastExecutedCommandIndex();

            if(currentLatestCommandIndex >= task.getCommands().size() - 1) {
                selectedCommandIndex = 0;
            } else {
                selectedCommandIndex = currentLatestCommandIndex + 1;
            }
        }

        return selectedCommandIndex;
    }

    public void disable() {
        List<Task> tasksToStore = loadedTasks.stream().filter(Task::isActive).collect(Collectors.toList());
        tasksToStore.forEach(Task::storeInstance);
        stopRunner = true;
        if(runnerThread != null && runnerThread.isAlive()) {
            runnerThread.stop();
        }
    }
}
