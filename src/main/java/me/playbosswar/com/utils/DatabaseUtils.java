package me.playbosswar.com.utils;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TaskExecutionMetadata;

import java.sql.SQLException;
import java.util.List;

public class DatabaseUtils {
    public static List<Task> getAllTasksFromDatabase() throws SQLException {
        List<Task> tasks = CommandTimerPlugin.getTaskDao().queryForAll();
        tasks.forEach(task -> {
            TaskExecutionMetadata metadata = Files.getOrCreateTaskMetadata(task);
            if(metadata != null) {
                task.setTimesExecuted(metadata.getTimesExecuted());
                task.setLastExecuted(metadata.getLastExecuted());
                task.setLastExecutedCommandIndex(metadata.getLastExecutedCommandIndex());
            }
        });
        return tasks;
    }
}
