package me.playbosswar.com.utils;

import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TaskTime;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.time.DayOfWeek;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class TaskTimeUtils {
    @Nullable
    public static Date getSoonestTaskTime(List<TaskTime> taskTimes) {
        List<Date> dates = new ArrayList<>();

        taskTimes.forEach(taskTime -> {
            Calendar cal = Calendar.getInstance();
            // TODO: Caching could be used here
            for(int i = 0; i <= 30; i++) {
                if(i != 0) {
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                }
                taskTime.getTask().getDays().forEach(day -> {
                    if(cal.get(Calendar.DAY_OF_WEEK) != transformDow(day)) {
                        return;
                    }

                    cal.set(Calendar.HOUR_OF_DAY, taskTime.getTime1().getHour());
                    cal.set(Calendar.MINUTE, taskTime.getTime1().getMinute());
                    cal.set(Calendar.SECOND, taskTime.getTime1().getSecond());
                    dates.add(cal.getTime());
                });
            }
        });

        if(dates.size() == 0) {
            return null;
        }

        final long now = System.currentTimeMillis();
        List<Date> futureDates = dates.stream().filter(date -> date.getTime() >= now).collect(Collectors.toList());

        if(futureDates.size() == 0) {
            return null;
        }

        return Collections.min(futureDates, (d1, d2) -> {
            long diff1 = Math.abs(d1.getTime() - now);
            long diff2 = Math.abs(d2.getTime() - now);
            return Long.compare(diff1, diff2);
        });
    }

    public static boolean hasPassedInterval(Task task) {
        try {
            Interval interval = new Interval(task.getLastExecuted().getTime(), new Date().getTime());
            Duration period = interval.toDuration();

            return period.getStandardSeconds() >= task.getInterval().toSeconds();
        } catch(IllegalArgumentException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Last executed time seems to be in the future!");
            e.printStackTrace();
        }

        return false;
    }

    private static int transformDow(DayOfWeek dow) {
        switch(dow) {
            case MONDAY:
                return 2;
            case TUESDAY:
                return 3;
            case WEDNESDAY:
                return 4;
            case THURSDAY:
                return 5;
            case FRIDAY:
                return 6;
            case SATURDAY:
                return 7;
            case SUNDAY:
                return 1;
        }
        return 0;
    }
}