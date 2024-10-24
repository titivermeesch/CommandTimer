package utils;

import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TaskTime;

import java.time.DayOfWeek;
import java.time.LocalTime;

import me.playbosswar.com.utils.TaskTimeUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskTimeUtilsTest {

    @Test
    public void givenTaskTimes_shouldHandleMultipleWeeks_returnSoonestDate() {
        final Task task = new Task("test");
        List<TaskTime> taskTimes = new ArrayList<>();

        List<DayOfWeek> days = new ArrayList<>();
        days.add(DayOfWeek.MONDAY);
        task.setDays(days);

        taskTimes.add(new TaskTime(LocalTime.of(14, 0, 0, 0), false));

        Date soonestDate = TaskTimeUtils.getSoonestTaskTime(task, taskTimes);
    }
}
