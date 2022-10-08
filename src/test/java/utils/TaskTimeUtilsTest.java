package utils;

import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TaskTime;

import java.time.LocalTime;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TaskTimeUtilsTest {
    private final Task task = new Task("test");

    @Test
    public void givenTaskTimes_returnSoonestDate() {
        List<TaskTime> taskTimes = new ArrayList<>();
        taskTimes.add(new TaskTime(task, LocalTime.of(14, 0, 0, 0), false));
    }
}
