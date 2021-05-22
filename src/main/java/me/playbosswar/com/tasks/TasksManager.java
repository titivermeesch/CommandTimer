package me.playbosswar.com.tasks;

import me.playbosswar.com.utils.Files;
import org.apache.commons.lang.RandomStringUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TasksManager {
    private final ArrayList<Task> loadedTasks = new ArrayList<>();
    private Thread runnerThread;

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

    public ArrayList<Task> getLoadedTasks() {
        return loadedTasks;
    }

    private void startRunner() {
        Runnable runner = new TaskRunner();
        Thread thread = new Thread(runner);
        thread.start();
        this.runnerThread = thread;
    }

    public void disable() {
        runnerThread.stop();
    }
}
