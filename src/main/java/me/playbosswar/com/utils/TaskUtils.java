package me.playbosswar.com.utils;

import me.playbosswar.com.tasks.Task;
import org.bukkit.Bukkit;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TaskUtils {
    public static boolean checkTaskDaysContainToday(Task task) {
        DayOfWeek today = LocalDate.now().getDayOfWeek();

        return task.getDays().contains(today);
    }

    public static DayOfWeek getFirstNextDow(Task task) {
        if(task.getDays().isEmpty()) {
            return null;
        }

        List<Integer> dayValues = task.getDays().stream().map(DayOfWeek::getValue).collect(Collectors.toList());
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        // Monday = 1, Sunday = 7
        int todayValue = today.getValue();
        int foundDay = -1;

        while(foundDay == -1) {
            if(dayValues.contains(todayValue)) {
                foundDay = todayValue;
            } else {
                todayValue += 1;
                if(todayValue > 7) {
                    todayValue = 1;
                }
            }
        }

        return DayOfWeek.of(todayValue);
    }
}
