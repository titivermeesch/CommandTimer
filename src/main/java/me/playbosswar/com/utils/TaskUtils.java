package me.playbosswar.com.utils;

import me.playbosswar.com.tasks.Task;
import org.bukkit.Bukkit;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class TaskUtils {
    public static boolean checkTaskDaysContainToday(Task task) {
        DayOfWeek today = LocalDate.now().getDayOfWeek();

        return task.getDays().contains(today.toString());
    }
}
