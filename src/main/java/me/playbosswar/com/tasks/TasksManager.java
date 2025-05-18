package me.playbosswar.com.tasks;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.enums.CommandExecutionMode;
import me.playbosswar.com.enums.Gender;
import me.playbosswar.com.hooks.PAPIHook;
import me.playbosswar.com.utils.DatabaseUtils;
import me.playbosswar.com.utils.Files;
import me.playbosswar.com.utils.Messages;
import me.playbosswar.com.utils.StringEnhancer;
import me.playbosswar.com.utils.Tools;


public class TasksManager {
    private static final String CONDITION_NO_MATCH = "Conditions did not match";
    private List<Task> loadedTasks = new ArrayList<>();
    private Thread runnerThread;
    public boolean stopRunner = false;
    public int executionsSinceLastSync = 0;

    public TasksManager() {
        if(CommandTimerPlugin.getPlugin().getConfig().getBoolean("database.enabled")) {
            try {
                Messages.sendConsole("Loading all tasks from database");
                List<Task> tasks = DatabaseUtils.getAllTasksFromDatabase();
                loadedTasks.addAll(tasks);
                Messages.sendConsole("Loaded " + loadedTasks.size() + " tasks from database");
            } catch(SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            loadedTasks.addAll(Files.deserializeJsonFilesIntoCommandTimers());
        }

        loadedTasks.forEach(task -> {
            if(task.isResetExecutionsAfterRestart()) {
                task.setTimesExecuted(0);
                task.setLastExecuted(new Date());
                task.storeInstance();
            }
        });

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
        if(CommandTimerPlugin.getPlugin().getConfig().getBoolean("database.enabled")) {
            try {
                CommandTimerPlugin.getTaskDao().delete(task);
                java.nio.file.Files.delete(Paths.get(Files.getTaskLocalExecutionFile(task.getId())));
                loadedTasks.remove(task);
            } catch(SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                java.nio.file.Files.delete(Paths.get(Files.getTaskFile(task.getId())));
                loadedTasks.remove(task);
            } catch(IOException e) {
                e.printStackTrace();
            }
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

    private boolean runConsolePerUserCommand(Task task, TaskCommand taskCommand, List<UUID> scopedPlayers) throws CommandException {
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
            if(task.hasCondition()) {
                boolean valid = TaskValidationHelpers.processCondition(task.getCondition(), p);
                if(!valid) {
                    Messages.sendDebugConsole(CONDITION_NO_MATCH);
                    continue;
                }
            }
            willExecute = true;

            if(delayedExecutions) {
                CommandTimerPlugin.getScheduler().runTaskLater(() -> {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, p));
                    executionsSinceLastSync++;
                }, (20L * i * taskCommand.getInterval().toSeconds()) + 1);
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
                CommandTimerPlugin.getScheduler().runTaskLater(() -> {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, p));
                    executionsSinceLastSync++;
                }, (20L * i * taskCommand.getInterval().toSeconds()) + 1);
            } else {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, p));
                executionsSinceLastSync++;
            }
            i++;
        }

        return true;
    }

    private boolean runConsolePerUserCommand(Task task, TaskCommand taskCommand) throws CommandException {
        return runConsolePerUserCommand(task, taskCommand, new ArrayList<>());
    }

    private boolean runConsoleCommand(Task task, TaskCommand taskCommand) throws CommandException {
        if(task.hasCondition()) {
            boolean valid = TaskValidationHelpers.processCondition(task.getCondition(), null);
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

    private boolean runPlayerCommand(Task task, TaskCommand taskCommand, List<UUID> scopedPlayers) {
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
            if(task.hasCondition()) {
                boolean valid = TaskValidationHelpers.processCondition(task.getCondition(), p);
                if(!valid) {
                    Messages.sendDebugConsole(CONDITION_NO_MATCH);
                    continue;
                }
            }

            willExecute = true;
            if(delayedExecution) {
                CommandTimerPlugin.getScheduler().runTaskLater(() -> runForPlayer(p,
                        command), (20L * i * taskCommand.getInterval().toSeconds()) + 1);
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

    private boolean runPlayerCommand(Task task, TaskCommand taskCommand) {
        return runPlayerCommand(task, taskCommand, new ArrayList<>());
    }

    private boolean runOperatorCommand(Task task, TaskCommand taskCommand, List<UUID> scopedPlayers) {
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
                if(task.hasCondition()) {
                    boolean valid = TaskValidationHelpers.processCondition(task.getCondition(), p);
                    if(!valid) {
                        Messages.sendDebugConsole(CONDITION_NO_MATCH);

                        if(!wasAlreadyOp) {
                            p.setOp(false);
                        }
                        continue;
                    }
                }
                willExecute = true;

                if(delayedExecutions) {
                    CommandTimerPlugin.getScheduler().runTaskLater(() -> {
                        p.performCommand(PAPIHook.parsePAPI(command, p));
                        executionsSinceLastSync++;
                    }, (20L * i * taskCommand.getInterval().toSeconds()) + 1);
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

    private boolean runOperatorCommand(Task task, TaskCommand taskCommand) {
        return runOperatorCommand(task, taskCommand, new ArrayList<>());
    }

    private boolean runConsoleProxyCommand(Task task, TaskCommand taskCommand) {
        if(task.hasCondition()) {
            boolean valid = TaskValidationHelpers.processCondition(task.getCondition(), null);
            if(!valid) {
                Messages.sendDebugConsole(CONDITION_NO_MATCH);
                return false;
            }
        }

        String command = taskCommand.getCommand();
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("executeConsoleCommand");
            out.writeUTF(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bukkit.getServer().sendPluginMessage(CommandTimerPlugin.getPlugin(), "commandtimer:main", b.toByteArray()); 
        executionsSinceLastSync++;
        return true;
    }

    public void processCommandExecution(Task task, TaskCommand taskCommand) {
        if(!task.isActive()) {
            return;
        }

        Gender gender = taskCommand.getGender();

        boolean executed = false;
        if(gender.equals(Gender.CONSOLE)) {
            executed = runConsoleCommand(task, taskCommand);
        } else if(gender.equals(Gender.PLAYER)) {
            executed = runPlayerCommand(task, taskCommand);
        } else if(gender.equals(Gender.OPERATOR)) {
            executed = runOperatorCommand(task, taskCommand);
        } else if(gender.equals(Gender.CONSOLE_PER_USER)) {
            executed = runConsolePerUserCommand(task, taskCommand);
        } else if(gender.equals(Gender.CONSOLE_PER_USER_OFFLINE)) {
            executed = runConsolePerUserOfflineCommand(taskCommand);
        } else if(gender.equals(Gender.CONSOLE_PROXY)) {
            executed = runConsoleProxyCommand(task, taskCommand); 
        }

        if(executed) {
            task.setLastExecuted(new Date());
            task.setTimesExecuted(task.getTimesExecuted() + 1);
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
            runnerThread.interrupt();
        }
    }
}
