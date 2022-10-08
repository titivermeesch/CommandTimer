package me.playbosswar.com.utils;

import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TaskTime;
import org.jetbrains.annotations.Nullable;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

public class TaskTimeUtils {
    @Nullable
    public static Date getSoonestTaskTime(List<TaskTime> taskTimes) {
        List<Date> dates = new ArrayList<>();

        taskTimes.forEach(taskTime -> {
            taskTime.getTask().getDays().forEach(day -> {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, taskTime.getTime1().getHour());
                cal.set(Calendar.MINUTE, taskTime.getTime1().getMinute());
                cal.set(Calendar.SECOND, taskTime.getTime1().getSecond());
                cal.set(Calendar.DAY_OF_WEEK, DayOfWeek.valueOf(day).getValue());
                dates.add(cal.getTime());
            });
        });

        if(dates.size() == 0) {
            return null;
        }

        final long now = System.currentTimeMillis();
        List<Date> futureDates = dates.stream().filter(date -> date.getTime() >= now).collect(Collectors.toList());
        return Collections.min(futureDates, (d1, d2) -> {
            long diff1 = Math.abs(d1.getTime() - now);
            long diff2 = Math.abs(d2.getTime() - now);
            return Long.compare(diff1, diff2);
        });
    }

    public static boolean hasPassedInterval(Task task) {
        Interval interval = new Interval(task.getLastExecuted().getTime(), new Date().getTime());
        Duration period = interval.toDuration();

        return period.getStandardSeconds() >= task.getInterval().toSeconds();
    }
}
