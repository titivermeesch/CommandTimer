package me.playbosswar.com.utils;

import me.playbosswar.com.tasks.TaskTime;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class TaskTimeUtils {
    public static TaskTime getSoonestTaskTime(List<TaskTime> taskTimes) {
        LocalTime nowTime = LocalDateTime.ofInstant(new Date().toInstant(),
                                                    ZoneId.systemDefault()).toLocalTime();

        return taskTimes.stream().reduce(taskTimes.get(0), (acc, taskTime) -> {
            long seconds = ChronoUnit.SECONDS.between(nowTime, taskTime.getTime1());
            long accSeconds = ChronoUnit.SECONDS.between(nowTime, acc.getTime1());

            if(seconds < 0) {
                seconds = seconds + 86400;
            }

            if(accSeconds < 0) {
                accSeconds = accSeconds + 86400;
            }

            if (seconds < accSeconds) {
                return taskTime;
            }

            return acc;
        });
    }
}
