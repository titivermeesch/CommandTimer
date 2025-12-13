package me.playbosswar.com.tasks;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.enums.CommandExecutionMode;
import me.playbosswar.com.utils.CommandExecutor;
import me.playbosswar.com.utils.DatabaseUtils;
import me.playbosswar.com.utils.Files;
import me.playbosswar.com.utils.Messages;
import me.playbosswar.com.utils.Tools;

public class TasksManager {
    private static final Random RANDOM = new Random();
    private static final String ALPHA_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private List<Task> loadedTasks = new ArrayList<>();
    private Queue<ScheduledTask> scheduledTasks = new PriorityQueue<>(Comparator.comparing(ScheduledTask::getDate));
    private Thread runnerThread;
    private Thread populateScheduleRunnerThread;
    public boolean stopRunner = false;
    public int executionsSinceLastSync = 0;

    public TasksManager(Plugin plugin) {
        if (plugin.getConfig().getBoolean("database.enabled")) {
            try {
                Messages.sendConsole("Loading all tasks from database");
                List<Task> tasks = DatabaseUtils.getAllTasksFromDatabase();
                loadedTasks.addAll(tasks);
                Messages.sendConsole("Loaded " + loadedTasks.size() + " tasks from database");
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else {
            loadedTasks.addAll(Files.deserializeJsonFilesIntoCommandTimers());
        }

        loadedTasks.forEach(task -> {
            if (task.isResetExecutionsAfterRestart()) {
                task.setTimesExecuted(0);
                task.setLastExecuted(new Date());
            }
        });

        startPopulateScheduleRunner();
        startRunner();
    }

    public Task createTask() {
        String name = "Task_" + generateRandomAlphabetic(4);
        Task task = new Task(name);
        loadedTasks.add(task);

        return task;
    }

    private static String generateRandomAlphabetic(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHA_CHARS.charAt(RANDOM.nextInt(ALPHA_CHARS.length())));
        }
        return sb.toString();
    }

    @Nullable
    public Task getTaskByName(String name) {
        Optional<Task> optionalTask = loadedTasks.stream().filter(task -> task.getName().equalsIgnoreCase(name))
                .findFirst();
        return optionalTask.orElse(null);
    }

    public void removeTask(Task task) throws IOException {
        resetScheduleForTask(task);
        if (CommandTimerPlugin.getPlugin().getConfig().getBoolean("database.enabled")) {
            try {
                CommandTimerPlugin.getTaskDao().delete(task);
                java.nio.file.Files.delete(Paths.get(Files.getTaskLocalExecutionFile(task.getId())));
                loadedTasks.remove(task);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                loadedTasks.remove(task);
                java.nio.file.Files.delete(Paths.get(Files.getTaskFile(task.getId())));
                java.nio.file.Files.delete(Paths.get(Files.getTaskLocalExecutionFile(task.getId())));
            } catch (IOException e) {
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

    private void startPopulateScheduleRunner() {
        Runnable runner = new PopulateScheduleRunner(this);
        Thread thread = new Thread(runner);
        thread.start();
        this.populateScheduleRunnerThread = thread;
    }


    public void processCommandExecution(Task task, TaskCommand taskCommand) {
        if (!task.isActive()) {
            return;
        }

        CommandTimerPlugin.getScheduler().runTaskAsynchronously(() -> {
            CommandExecutor.ExecutionContext.Builder builder = new CommandExecutor.ExecutionContext.Builder()
                    .command(taskCommand.getCommand())
                    .gender(taskCommand.getGender())
                    .interval(taskCommand.getInterval())
                    .executionCounter(count -> executionsSinceLastSync++);

            if (task.hasCondition()) {
                builder.conditionChecker(p -> TaskValidationHelpers.processCondition(task.getCondition(), p));
            }

            boolean executed = CommandExecutor.execute(builder.build());

            if (executed) {
                task.setLastExecuted(new Date());
                task.setTimesExecuted(task.getTimesExecuted() + 1);
            }
        });
    }
    
    public Queue<ScheduledTask> getScheduledTasks() {
        return scheduledTasks;
    }

    public void resetScheduleForTask(Task task) {
        scheduledTasks.removeIf(scheduledTask -> scheduledTask.getTask().getId().equals(task.getId()));
    }


    public void populateScheduleForTask(Task task) {
        Messages.sendDebugConsole("Populating schedule for task: " + task.getName());
        if (!task.isActive()) {
            Messages.sendDebugConsole("Task " + task.getName() + " is not active, skipping scheduling");
            return;
        }

        boolean usesMinecraftTime = task.getTimes().stream().anyMatch(TaskTime::isMinecraftTime);
        if (usesMinecraftTime) {
            scheduledTasks.removeIf(st -> st.getTask().getId().equals(task.getId()));
        }

        int executionLimit = task.getExecutionLimit();
        int timesExecuted = task.getTimesExecuted();
        long alreadyScheduled = scheduledTasks.stream()
                .filter(scheduledTask -> scheduledTask.getTask().getId().equals(task.getId())).count();

        if (alreadyScheduled >= 50) {
            Messages.sendDebugConsole(
                    "Task " + task.getName() + " has already been scheduled 50 times, skipping scheduling");
            return;
        }

        int maxToSchedule = 50;
        if (executionLimit != -1) {
            int remaining = executionLimit - timesExecuted - (int) alreadyScheduled;
            if (remaining <= 0) {
                Messages.sendDebugConsole(
                        "Task " + task.getName() + " has reached execution limit, skipping scheduling");
                return;
            }
            maxToSchedule = Math.min(maxToSchedule, remaining);
        }

        ZonedDateTime latestScheduledDate = scheduledTasks.stream()
                .filter(scheduledTask -> scheduledTask.getTask().getId().equals(task.getId()))
                .map(ScheduledTask::getDate).max(ZonedDateTime::compareTo).orElse(null);

        if (latestScheduledDate == null) {
            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime lastExecuted = task.getLastExecuted().toInstant().atZone(ZoneId.systemDefault());
            latestScheduledDate = lastExecuted.isAfter(now) ? lastExecuted : now;
        }

        if (maxToSchedule <= 0) {
            return;
        }

        if (!task.getTimes().isEmpty()) {
            for (TaskTime taskTime : task.getTimes()) {
                if (taskTime.isMinecraftTime()) {
                    World world = Bukkit.getWorld(taskTime.getWorld() == null ? "world" : taskTime.getWorld());
                    if (world == null) {
                        continue;
                    }

                    if (taskTime.isRange()) {
                        LocalTime startRange = taskTime.getTime1();
                        LocalTime endRange = taskTime.getTime2();

                        LocalTime currentMcTime = Tools.getMinecraftTimeAt(world, ZonedDateTime.now());
                        boolean currentlyInWindow = isTimeInRange(currentMcTime, startRange, endRange);

                        int mcDay = 0;
                        boolean firstIteration = true;
                        while (maxToSchedule > 0) {
                            ZonedDateTime windowStart;
                            ZonedDateTime windowEnd;

                            if (firstIteration && currentlyInWindow) {
                                windowStart = ZonedDateTime.now();
                                windowEnd = Tools.getNextMinecraftTime(world, endRange, 0);
                                if (windowEnd.isBefore(windowStart)) {
                                    windowEnd = Tools.getNextMinecraftTime(world, endRange, 1);
                                }
                            } else {
                                int dayOffset = (firstIteration && currentlyInWindow) ? 1 : mcDay;
                                if (firstIteration && !currentlyInWindow) {
                                    dayOffset = 0;
                                }
                                windowStart = Tools.getNextMinecraftTime(world, startRange, dayOffset);
                                windowEnd = Tools.getNextMinecraftTime(world, endRange, dayOffset);
                                if (windowEnd.isBefore(windowStart)) {
                                    windowEnd = Tools.getNextMinecraftTime(world, endRange, dayOffset + 1);
                                }
                            }
                            firstIteration = false;

                            if (!task.getDays().contains(windowStart.toLocalDate().getDayOfWeek())) {
                                mcDay++;
                                continue;
                            }

                            if (windowEnd.isBefore(latestScheduledDate)) {
                                mcDay++;
                                continue;
                            }

                            ZonedDateTime execTime = windowStart.isBefore(latestScheduledDate) ? latestScheduledDate : windowStart;
                            long intervalSeconds = task.getInterval().toSeconds();
                            if (intervalSeconds <= 0) intervalSeconds = 1;

                            while (maxToSchedule > 0 && !execTime.isAfter(windowEnd)) {
                                if (!execTime.isBefore(latestScheduledDate)) {
                                    scheduledTasks.add(new ScheduledTask(task, execTime));
                                    maxToSchedule--;
                                }
                                execTime = execTime.plusSeconds(intervalSeconds);
                            }
                            mcDay++;
                        }
                    } else {
                        LocalTime time = taskTime.getTime1();

                        int i = 0;
                        while (maxToSchedule > 0) {
                            ZonedDateTime nextMinecraftTime = Tools.getNextMinecraftTime(world, time, i);
                            if (!task.getDays().contains(nextMinecraftTime.getDayOfWeek())) {
                                i++;
                                continue;
                            }

                            if (nextMinecraftTime.isBefore(latestScheduledDate)) {
                                i++;
                                continue;
                            }

                            scheduledTasks.add(new ScheduledTask(task, nextMinecraftTime));
                            maxToSchedule--;
                            i++;
                        }
                    }
                } else if (taskTime.isRange()) {
                    LocalTime startRange = taskTime.getTime1();
                    LocalTime endRange = taskTime.getTime2();

                    int i = 0;
                    while (maxToSchedule > 0) {
                        ZonedDateTime date = ZonedDateTime.of(LocalDate.now(), startRange, ZoneId.systemDefault())
                                .plusSeconds(i * task.getInterval().toSeconds());
                        if (!task.getDays().contains(date.getDayOfWeek())) {
                            i++;
                            continue;
                        }

                        if (!(date.toLocalTime().isAfter(startRange) && date.toLocalTime().isBefore(endRange))) {
                            i++;
                            continue;
                        }

                        if (date.isBefore(latestScheduledDate)) {
                            i++;
                            continue;
                        }

                        scheduledTasks.add(new ScheduledTask(task, date));
                        maxToSchedule--;
                        i++;
                    }
                } else {
                    LocalTime time = taskTime.getTime1();

                    int i = 0;
                    while (maxToSchedule > 0) {
                        ZonedDateTime date = ZonedDateTime.of(LocalDate.now(), time, ZoneId.systemDefault())
                                .plusDays(i);
                        if (!task.getDays().contains(date.getDayOfWeek())) {
                            i++;
                            continue;
                        }

                        if (date.isBefore(latestScheduledDate)) {
                            i++;
                            continue;
                        }

                        scheduledTasks.add(new ScheduledTask(task, date));
                        maxToSchedule--;
                        i++;
                    }
                }
            }
            return;
        }

        if (task.getInterval().toSeconds() == 0 && !task.getEvents().isEmpty()) {
            Messages.sendDebugConsole(
                    "Task " + task.getName() + "has no interval set and uses events, skipping scheduling");
            return;
        }

        int i = 0;
        while (maxToSchedule > 0) {
            ZonedDateTime date = latestScheduledDate.plusSeconds(i * task.getInterval().toSeconds());
            if (!task.getDays().contains(date.getDayOfWeek())) {
                i++;
                continue;
            }

            scheduledTasks.add(new ScheduledTask(task, date));
            maxToSchedule--;
            i++;
        }
    }

    public ScheduledTask getNextScheduledTaskForTask(Task task) {
        return scheduledTasks.stream().filter(scheduledTask -> scheduledTask.getTask().getId().equals(task.getId())).min(Comparator.comparing(ScheduledTask::getDate)).orElse(null);
    }

    public int getNextTaskCommandIndex(Task task) {
        // If it remains -1, that means that all commands should be executed
        int selectedCommandIndex = -1;
        if (task.getCommandExecutionMode().equals(CommandExecutionMode.RANDOM)) {
            selectedCommandIndex = Tools.getRandomInt(0, task.getCommands().size() - 1);
        } else if (task.getCommandExecutionMode().equals(CommandExecutionMode.ORDERED)) {
            int currentLatestCommandIndex = task.getLastExecutedCommandIndex();

            if (currentLatestCommandIndex >= task.getCommands().size() - 1) {
                selectedCommandIndex = 0;
            } else {
                selectedCommandIndex = currentLatestCommandIndex + 1;
            }
        }

        return selectedCommandIndex;
    }

    private boolean isTimeInRange(LocalTime time, LocalTime start, LocalTime end) {
        if (start.isBefore(end)) {
            return !time.isBefore(start) && !time.isAfter(end);
        } else {
            return !time.isBefore(start) || !time.isAfter(end);
        }
    }

    public void disable() {
        stopRunner = true;
        if (runnerThread != null && runnerThread.isAlive()) {
            runnerThread.interrupt();
        }
        if (populateScheduleRunnerThread != null && populateScheduleRunnerThread.isAlive()) {
            populateScheduleRunnerThread.interrupt();
        }
    }
}
