package me.playbosswar.com.utils;

import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TaskTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TaskTimeUtils {
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

        final long now = System.currentTimeMillis();
        return Collections.min(dates, (d1, d2) -> {
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
