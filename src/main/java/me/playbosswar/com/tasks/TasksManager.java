package me.playbosswar.com.tasks;

import me.playbosswar.com.utils.Files;
import org.apache.commons.lang.RandomStringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TasksManager {
    private final ArrayList<Task> loadedTasks = new ArrayList<>();

    public TasksManager() {
        loadedTasks.addAll(Files.deserializeJsonFilesIntoCommandTimers());
    }

    public Task createTask() {
        String name = "Task " + RandomStringUtils.randomAlphabetic(4);
        try {
            Files.createNewTaskFile(name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Task task = new Task(name);
        loadedTasks.add(task);
        return task;
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

    public ArrayList<Task> getLoadedTasks() {
        return loadedTasks;
    }
}
