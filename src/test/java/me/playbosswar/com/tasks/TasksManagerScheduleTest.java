package me.playbosswar.com.tasks;

import me.playbosswar.com.CommandTimerPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TasksManagerScheduleTest {

    // Fixed point: Monday 2024-01-15 at 08:00 UTC
    private static final ZoneId ZONE = ZoneId.of("UTC");
    private static final Instant FIXED_INSTANT = ZonedDateTime.of(2024, 1, 15, 8, 0, 0, 0, ZONE).toInstant();
    private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_INSTANT, ZONE);

    private TasksManager manager;

    @BeforeAll
    static void setUpPlugin() throws Exception {
        // Set a mock plugin so Messages static initializer doesn't NPE
        CommandTimerPlugin mockPlugin = Mockito.mock(CommandTimerPlugin.class);
        FileConfiguration mockConfig = Mockito.mock(FileConfiguration.class);
        Mockito.when(mockPlugin.getConfig()).thenReturn(mockConfig);
        Mockito.when(mockConfig.getBoolean(Mockito.anyString())).thenReturn(false);

        Field pluginField = CommandTimerPlugin.class.getDeclaredField("plugin");
        pluginField.setAccessible(true);
        pluginField.set(null, mockPlugin);
    }

    @AfterAll
    static void tearDownPlugin() throws Exception {
        Field pluginField = CommandTimerPlugin.class.getDeclaredField("plugin");
        pluginField.setAccessible(true);
        pluginField.set(null, null);
    }

    @BeforeEach
    void setUp() {
        manager = new TasksManager();
        manager.setClock(FIXED_CLOCK);
    }

    // ========================= Helpers =========================

    private static Task createTestTask() {
        Task task = new Task();
        setField(task, "id", UUID.randomUUID());
        setField(task, "name", "TestTask");
        setField(task, "active", true);
        setField(task, "interval", new TaskInterval(0, 0, 0, 30));
        setField(task, "times", new ArrayList<TaskTime>());
        setField(task, "days", allDays());
        setField(task, "executionLimit", -1);
        setField(task, "timesExecuted", 0);
        setField(task, "lastExecuted", Date.from(FIXED_INSTANT.minusSeconds(300)));
        setField(task, "events", new ArrayList<>());
        setField(task, "commands", new ArrayList<>());
        return task;
    }

    private static Collection<DayOfWeek> allDays() {
        Collection<DayOfWeek> days = new ArrayList<>();
        for (DayOfWeek d : DayOfWeek.values()) {
            days.add(d);
        }
        return days;
    }

    private static void setField(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field " + fieldName, e);
        }
    }

    @SuppressWarnings("unchecked")
    private static void addTime(Task task, TaskTime time) {
        try {
            Field f = Task.class.getDeclaredField("times");
            f.setAccessible(true);
            ((Collection<TaskTime>) f.get(task)).add(time);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ZonedDateTime fixedNow() {
        return ZonedDateTime.now(FIXED_CLOCK);
    }

    private List<ScheduledTask> scheduledFor(Task task) {
        return manager.getScheduledTasks().stream()
                .filter(st -> st.getTask().getId().equals(task.getId()))
                .collect(Collectors.toList());
    }

    // ========================= Tests =========================

    // --- Interval-only scheduling ---

    @Test
    void intervalOnly_schedulesFromLastExecuted() {
        Task task = createTestTask();
        // interval=30s, lastExecuted=now-300s, no times
        setField(task, "interval", new TaskInterval(0, 0, 0, 30));
        setField(task, "lastExecuted", Date.from(FIXED_INSTANT.minusSeconds(300)));

        manager.populateScheduleForTask(task);

        List<ScheduledTask> scheduled = scheduledFor(task);
        assertEquals(50, scheduled.size());

        // First entry is catch-up at lastExecuted + 30s (in the past)
        // Compare instants to avoid timezone mismatch (code uses system default zone)
        Instant catchUpInstant = FIXED_INSTANT.minusSeconds(270);
        assertTrue(scheduled.stream().anyMatch(st -> st.getDate().toInstant().equals(catchUpInstant)),
                "Should have catch-up entry at lastExecuted + interval");
    }

    @Test
    void intervalOnly_futureEntriesAreSpacedByInterval() {
        Task task = createTestTask();
        setField(task, "interval", new TaskInterval(0, 0, 1, 0)); // 60s
        setField(task, "lastExecuted", Date.from(FIXED_INSTANT.minusSeconds(60)));

        manager.populateScheduleForTask(task);

        List<ScheduledTask> scheduled = scheduledFor(task);
        // Get entries at or after now
        List<ZonedDateTime> futureDates = scheduled.stream()
                .map(ScheduledTask::getDate)
                .filter(d -> !d.isBefore(fixedNow()))
                .sorted()
                .collect(Collectors.toList());

        assertTrue(futureDates.size() >= 2, "Should have multiple future entries");

        // Check spacing between consecutive future entries
        for (int i = 1; i < futureDates.size(); i++) {
            long gap = java.time.Duration.between(futureDates.get(i - 1), futureDates.get(i)).getSeconds();
            assertEquals(60, gap, "Entries should be 60s apart");
        }
    }

    @Test
    void intervalOnly_respectsExecutionLimit() {
        Task task = createTestTask();
        setField(task, "interval", new TaskInterval(0, 0, 0, 30));
        setField(task, "executionLimit", 5);
        setField(task, "timesExecuted", 0);

        manager.populateScheduleForTask(task);

        List<ScheduledTask> scheduled = scheduledFor(task);
        assertEquals(5, scheduled.size(), "Should respect execution limit");
    }

    @Test
    void intervalOnly_accountsForAlreadyExecuted() {
        Task task = createTestTask();
        setField(task, "interval", new TaskInterval(0, 0, 0, 30));
        setField(task, "executionLimit", 10);
        setField(task, "timesExecuted", 7);

        manager.populateScheduleForTask(task);

        List<ScheduledTask> scheduled = scheduledFor(task);
        assertEquals(3, scheduled.size(), "Should schedule remaining = limit - executed");
    }

    // --- Single fixed time ---

    @Test
    void singleFixedTime_schedulesDaily() {
        Task task = createTestTask();
        TaskTime time = new TaskTime(LocalTime.of(14, 0), false);
        addTime(task, time);

        manager.populateScheduleForTask(task);

        List<ScheduledTask> scheduled = scheduledFor(task);
        assertEquals(50, scheduled.size());

        // All entries should be at 14:00
        for (ScheduledTask st : scheduled) {
            assertEquals(LocalTime.of(14, 0), st.getDate().toLocalTime(),
                    "All entries should be at 14:00");
        }

        // Entries should be on consecutive days
        List<ZonedDateTime> dates = scheduled.stream()
                .map(ScheduledTask::getDate)
                .sorted()
                .collect(Collectors.toList());
        for (int i = 1; i < dates.size(); i++) {
            assertEquals(1, java.time.Duration.between(dates.get(i - 1), dates.get(i)).toDays(),
                    "Should be on consecutive days");
        }
    }

    @Test
    void singleFixedTime_firstEntryIsToday() {
        Task task = createTestTask();
        // fixedNow is 08:00, so 14:00 today is in the future
        TaskTime time = new TaskTime(LocalTime.of(14, 0), false);
        addTime(task, time);

        manager.populateScheduleForTask(task);

        List<ZonedDateTime> dates = scheduledFor(task).stream()
                .map(ScheduledTask::getDate)
                .sorted()
                .collect(Collectors.toList());

        assertEquals(fixedNow().toLocalDate(), dates.get(0).toLocalDate(),
                "First entry should be today");
    }

    @Test
    void singleFixedTime_pastTimeStartsTomorrow() {
        Task task = createTestTask();
        // fixedNow is 08:00, so 06:00 today is in the past
        TaskTime time = new TaskTime(LocalTime.of(6, 0), false);
        addTime(task, time);

        manager.populateScheduleForTask(task);

        List<ZonedDateTime> dates = scheduledFor(task).stream()
                .map(ScheduledTask::getDate)
                .sorted()
                .collect(Collectors.toList());

        assertTrue(dates.get(0).isAfter(fixedNow()),
                "All entries should be after now");
    }

    // --- Multiple fixed times (independent budgets) ---

    @Test
    void multipleFixedTimes_eachGetsOwnBudget() {
        Task task = createTestTask();
        TaskTime time1 = new TaskTime(LocalTime.of(10, 0), false);
        TaskTime time2 = new TaskTime(LocalTime.of(14, 0), false);
        addTime(task, time1);
        addTime(task, time2);

        manager.populateScheduleForTask(task);

        List<ScheduledTask> scheduled = scheduledFor(task);
        long count10 = scheduled.stream()
                .filter(st -> st.getDate().toLocalTime().equals(LocalTime.of(10, 0)))
                .count();
        long count14 = scheduled.stream()
                .filter(st -> st.getDate().toLocalTime().equals(LocalTime.of(14, 0)))
                .count();

        assertEquals(50, count10, "10:00 time should get its own 50 slots");
        assertEquals(50, count14, "14:00 time should get its own 50 slots");
        assertEquals(100, scheduled.size(), "Total should be 100 (50+50)");
    }

    @Test
    void multipleFixedTimes_eachHasCorrectTaskTimeRef() {
        Task task = createTestTask();
        TaskTime time1 = new TaskTime(LocalTime.of(10, 0), false);
        TaskTime time2 = new TaskTime(LocalTime.of(14, 0), false);
        addTime(task, time1);
        addTime(task, time2);

        manager.populateScheduleForTask(task);

        List<ScheduledTask> scheduled = scheduledFor(task);
        long ref1 = scheduled.stream().filter(st -> st.getTaskTime() == time1).count();
        long ref2 = scheduled.stream().filter(st -> st.getTaskTime() == time2).count();

        assertEquals(50, ref1, "time1 entries should reference time1");
        assertEquals(50, ref2, "time2 entries should reference time2");
    }

    // --- Range with interval ---

    @Test
    void singleRange_schedulesWithinWindow() {
        Task task = createTestTask();
        setField(task, "interval", new TaskInterval(0, 0, 30, 0)); // 30 min

        TaskTime range = new TaskTime(LocalTime.of(10, 0), false);
        range.setTime2(LocalTime.of(12, 0)); // 10:00-12:00 range
        addTime(task, range);

        manager.populateScheduleForTask(task);

        List<ScheduledTask> scheduled = scheduledFor(task);
        assertTrue(scheduled.size() > 0, "Should schedule entries");

        // All entries must be within the 10:00-12:00 window
        for (ScheduledTask st : scheduled) {
            LocalTime t = st.getDate().toLocalTime();
            assertTrue(!t.isBefore(LocalTime.of(10, 0)) && !t.isAfter(LocalTime.of(12, 0)),
                    "Entry at " + t + " should be within 10:00-12:00");
        }
    }

    @Test
    void singleRange_entriesSpacedByInterval() {
        Task task = createTestTask();
        setField(task, "interval", new TaskInterval(0, 0, 30, 0)); // 30 min

        TaskTime range = new TaskTime(LocalTime.of(10, 0), false);
        range.setTime2(LocalTime.of(12, 0));
        addTime(task, range);

        manager.populateScheduleForTask(task);

        // Get entries for one specific day
        List<ZonedDateTime> todayEntries = scheduledFor(task).stream()
                .map(ScheduledTask::getDate)
                .filter(d -> d.toLocalDate().equals(fixedNow().toLocalDate()))
                .sorted()
                .collect(Collectors.toList());

        assertTrue(todayEntries.size() >= 2, "Should have multiple entries today");

        for (int i = 1; i < todayEntries.size(); i++) {
            long gap = java.time.Duration.between(todayEntries.get(i - 1), todayEntries.get(i)).toMinutes();
            assertEquals(30, gap, "Entries should be 30 min apart within the same day");
        }
    }

    @Test
    void singleRange_fills50Slots() {
        Task task = createTestTask();
        setField(task, "interval", new TaskInterval(0, 0, 30, 0));

        TaskTime range = new TaskTime(LocalTime.of(10, 0), false);
        range.setTime2(LocalTime.of(12, 0));
        addTime(task, range);

        manager.populateScheduleForTask(task);

        assertEquals(50, scheduledFor(task).size(), "Should fill all 50 slots");
    }

    // --- Range + fixed time together ---

    @Test
    void rangeAndFixedTime_bothGetScheduled() {
        Task task = createTestTask();
        setField(task, "interval", new TaskInterval(0, 1, 0, 0)); // 1h interval for range

        TaskTime range = new TaskTime(LocalTime.of(10, 0), false);
        range.setTime2(LocalTime.of(12, 0)); // 3 entries/day (10:00, 11:00, 12:00)
        addTime(task, range);

        TaskTime fixed = new TaskTime(LocalTime.of(14, 0), false);
        addTime(task, fixed);

        manager.populateScheduleForTask(task);

        List<ScheduledTask> scheduled = scheduledFor(task);
        long rangeCount = scheduled.stream().filter(st -> st.getTaskTime() == range).count();
        long fixedCount = scheduled.stream().filter(st -> st.getTaskTime() == fixed).count();

        assertEquals(50, rangeCount, "Range should get 50 slots");
        assertEquals(50, fixedCount, "Fixed should get 50 slots");
        assertEquals(100, scheduled.size(), "Total should be 100");
    }

    // --- No duplicates on repopulation ---

    @Test
    void repopulation_doesNotDuplicateFixedTime() {
        Task task = createTestTask();
        TaskTime time = new TaskTime(LocalTime.of(14, 0), false);
        addTime(task, time);

        manager.populateScheduleForTask(task);
        assertEquals(50, scheduledFor(task).size());

        // Simulate repopulation (as PopulateScheduleRunner does every 10s)
        manager.populateScheduleForTask(task);
        assertEquals(50, scheduledFor(task).size(),
                "Repopulation should NOT add duplicates");
    }

    @Test
    void repopulation_doesNotDuplicateInterval() {
        Task task = createTestTask();
        setField(task, "interval", new TaskInterval(0, 0, 1, 0)); // 60s

        manager.populateScheduleForTask(task);
        int firstCount = scheduledFor(task).size();
        assertEquals(50, firstCount);

        manager.populateScheduleForTask(task);
        assertEquals(50, scheduledFor(task).size(),
                "Repopulation should NOT add duplicates for interval tasks");
    }

    @Test
    void repopulation_doesNotDuplicateRange() {
        Task task = createTestTask();
        setField(task, "interval", new TaskInterval(0, 0, 30, 0));

        TaskTime range = new TaskTime(LocalTime.of(10, 0), false);
        range.setTime2(LocalTime.of(12, 0));
        addTime(task, range);

        manager.populateScheduleForTask(task);
        assertEquals(50, scheduledFor(task).size());

        manager.populateScheduleForTask(task);
        assertEquals(50, scheduledFor(task).size(),
                "Repopulation should NOT add duplicates for range tasks");
    }

    // --- Day of week filtering ---

    @Test
    void fixedTime_respectsDayFilter() {
        Task task = createTestTask();
        Collection<DayOfWeek> mondayOnly = new ArrayList<>();
        mondayOnly.add(DayOfWeek.MONDAY);
        setField(task, "days", mondayOnly);

        TaskTime time = new TaskTime(LocalTime.of(14, 0), false);
        addTime(task, time);

        manager.populateScheduleForTask(task);

        List<ScheduledTask> scheduled = scheduledFor(task);
        assertTrue(scheduled.size() > 0);

        for (ScheduledTask st : scheduled) {
            assertEquals(DayOfWeek.MONDAY, st.getDate().getDayOfWeek(),
                    "All entries should be on Monday");
        }
    }

    @Test
    void intervalOnly_respectsDayFilter() {
        Task task = createTestTask();
        setField(task, "interval", new TaskInterval(0, 1, 0, 0)); // 1h
        // fixedNow is Monday 2024-01-15 08:00 UTC
        Collection<DayOfWeek> wednesdayOnly = new ArrayList<>();
        wednesdayOnly.add(DayOfWeek.WEDNESDAY);
        setField(task, "days", wednesdayOnly);

        manager.populateScheduleForTask(task);

        List<ScheduledTask> scheduled = scheduledFor(task);
        for (ScheduledTask st : scheduled) {
            // Catch-up entry may be on a non-matching day, future entries should match
            if (!st.getDate().isBefore(fixedNow())) {
                assertEquals(DayOfWeek.WEDNESDAY, st.getDate().getDayOfWeek(),
                        "Future entries should be on Wednesday");
            }
        }
    }

    @Test
    void range_respectsDayFilter() {
        Task task = createTestTask();
        setField(task, "interval", new TaskInterval(0, 0, 30, 0));
        Collection<DayOfWeek> tuesdayOnly = new ArrayList<>();
        tuesdayOnly.add(DayOfWeek.TUESDAY);
        setField(task, "days", tuesdayOnly);

        TaskTime range = new TaskTime(LocalTime.of(10, 0), false);
        range.setTime2(LocalTime.of(12, 0));
        addTime(task, range);

        manager.populateScheduleForTask(task);

        List<ScheduledTask> scheduled = scheduledFor(task);
        assertTrue(scheduled.size() > 0);

        for (ScheduledTask st : scheduled) {
            assertEquals(DayOfWeek.TUESDAY, st.getDate().getDayOfWeek(),
                    "All range entries should be on Tuesday");
        }
    }

    // --- Inactive task ---

    @Test
    void inactiveTask_producesNoSchedule() {
        Task task = createTestTask();
        setField(task, "active", false);

        TaskTime time = new TaskTime(LocalTime.of(14, 0), false);
        addTime(task, time);

        manager.populateScheduleForTask(task);

        assertEquals(0, scheduledFor(task).size(),
                "Inactive task should produce no scheduled entries");
    }

    // --- Execution limit with times ---

    @Test
    void fixedTime_respectsExecutionLimit() {
        Task task = createTestTask();
        setField(task, "executionLimit", 3);
        setField(task, "timesExecuted", 0);

        TaskTime time = new TaskTime(LocalTime.of(14, 0), false);
        addTime(task, time);

        manager.populateScheduleForTask(task);

        List<ScheduledTask> scheduled = scheduledFor(task);
        assertTrue(scheduled.size() <= 3,
                "Should not exceed execution limit, got: " + scheduled.size());
    }

    @Test
    void multipleFixedTimes_executionLimitCapsTotal() {
        Task task = createTestTask();
        setField(task, "executionLimit", 5);
        setField(task, "timesExecuted", 0);

        TaskTime time1 = new TaskTime(LocalTime.of(10, 0), false);
        TaskTime time2 = new TaskTime(LocalTime.of(14, 0), false);
        addTime(task, time1);
        addTime(task, time2);

        manager.populateScheduleForTask(task);

        List<ScheduledTask> scheduled = scheduledFor(task);
        assertTrue(scheduled.size() <= 5,
                "Total scheduled should not exceed execution limit, got: " + scheduled.size());
    }

    @Test
    void executionLimitAlreadyReached_skipsScheduling() {
        Task task = createTestTask();
        setField(task, "executionLimit", 10);
        setField(task, "timesExecuted", 10);

        TaskTime time = new TaskTime(LocalTime.of(14, 0), false);
        addTime(task, time);

        manager.populateScheduleForTask(task);

        assertEquals(0, scheduledFor(task).size(),
                "Should not schedule when limit already reached");
    }

    // --- Per-TaskTime budget cap at 50 ---

    @Test
    void singleTaskTime_capsAt50() {
        Task task = createTestTask();
        TaskTime time = new TaskTime(LocalTime.of(14, 0), false);
        addTime(task, time);

        manager.populateScheduleForTask(task);

        long count = scheduledFor(task).stream()
                .filter(st -> st.getTaskTime() == time)
                .count();
        assertEquals(50, count, "Single TaskTime should cap at 50");
    }

    // --- Edge cases ---

    @Test
    void noTimesNoInterval_withEvents_skipsScheduling() {
        Task task = createTestTask();
        setField(task, "interval", new TaskInterval(0, 0, 0, 0)); // 0 seconds
        Collection<Object> events = new ArrayList<>();
        events.add(new Object()); // simulate non-empty events
        setField(task, "events", events);

        manager.populateScheduleForTask(task);

        assertEquals(0, scheduledFor(task).size(),
                "Should skip scheduling for event-only tasks with no interval");
    }

    // ========================= Aligned interval (grid-locked) =========================

    @Test
    void alignedInterval_schedulesAtCorrectGridTimes() {
        // startTime=15:00, interval=15min, now=Monday 08:00
        // Grid for today: 08:00, 08:15, 08:30, ...
        Task task = createTestTask();
        setField(task, "interval", new TaskInterval(0, 0, 15, 0)); // 15 min
        setField(task, "intervalStartTime", LocalTime.of(15, 0));


        manager.populateScheduleForTask(task);

        List<ScheduledTask> scheduled = scheduledFor(task);
        assertTrue(scheduled.size() > 0, "Should have scheduled entries");

        // All entries should fall on 15-minute grid aligned to 15:00
        for (ScheduledTask st : scheduled) {
            LocalTime t = st.getDate().toLocalTime();
            long minutesSinceMidnight = t.getHour() * 60 + t.getMinute();
            // 15:00 = 900 minutes; grid: any time where (minutes - 900) % 15 == 0
            long offset = ((minutesSinceMidnight - 900) % 15 + 15) % 15;
            assertEquals(0, offset,
                    "Entry at " + t + " should be on 15-min grid anchored at 15:00");
            assertEquals(0, t.getSecond(), "Seconds should be 0");
        }
    }

    @Test
    void alignedInterval_firstEntryIsNextGridPointAfterNow() {
        // startTime=15:00, interval=15min, now=08:00
        // Grid: ...07:45, 08:00, 08:15... → first should be 08:00
        Task task = createTestTask();
        setField(task, "interval", new TaskInterval(0, 0, 15, 0));
        setField(task, "intervalStartTime", LocalTime.of(15, 0));


        manager.populateScheduleForTask(task);

        List<ZonedDateTime> dates = scheduledFor(task).stream()
                .map(ScheduledTask::getDate)
                .sorted()
                .collect(Collectors.toList());

        // 08:00 is exactly on the grid, so it should be the first entry
        assertEquals(LocalTime.of(8, 0), dates.get(0).toLocalTime(),
                "First entry should be at 08:00 (on the grid)");
    }

    @Test
    void alignedInterval_rollsOverToNextDay() {
        // startTime=23:00, interval=2h, now=08:00
        // Grid today: 09:00, 11:00, 13:00, 15:00, 17:00, 19:00, 21:00, 23:00
        // Should eventually schedule entries on the next day too
        Task task = createTestTask();
        setField(task, "interval", new TaskInterval(0, 2, 0, 0)); // 2h
        setField(task, "intervalStartTime", LocalTime.of(23, 0));


        manager.populateScheduleForTask(task);

        List<ZonedDateTime> dates = scheduledFor(task).stream()
                .map(ScheduledTask::getDate)
                .sorted()
                .collect(Collectors.toList());

        assertTrue(dates.size() >= 2, "Should have multiple entries");

        // Should have entries on multiple days
        long distinctDays = dates.stream()
                .map(d -> d.toLocalDate())
                .distinct()
                .count();
        assertTrue(distinctDays > 1, "Should schedule across multiple days");
    }

    @Test
    void alignedInterval_respectsDayFilter() {
        Task task = createTestTask();
        setField(task, "interval", new TaskInterval(0, 1, 0, 0)); // 1h
        setField(task, "intervalStartTime", LocalTime.of(12, 0));

        // Only Wednesday (now is Monday)
        Collection<DayOfWeek> wednesdayOnly = new ArrayList<>();
        wednesdayOnly.add(DayOfWeek.WEDNESDAY);
        setField(task, "days", wednesdayOnly);

        manager.populateScheduleForTask(task);

        List<ScheduledTask> scheduled = scheduledFor(task);
        assertTrue(scheduled.size() > 0);
        for (ScheduledTask st : scheduled) {
            assertEquals(DayOfWeek.WEDNESDAY, st.getDate().getDayOfWeek(),
                    "All entries should be on Wednesday");
        }
    }

    @Test
    void alignedInterval_respectsExecutionLimit() {
        Task task = createTestTask();
        setField(task, "interval", new TaskInterval(0, 0, 15, 0));
        setField(task, "intervalStartTime", LocalTime.of(12, 0));

        setField(task, "executionLimit", 5);
        setField(task, "timesExecuted", 0);

        manager.populateScheduleForTask(task);

        assertEquals(5, scheduledFor(task).size(), "Should respect execution limit");
    }

    @Test
    void alignedInterval_repopulationDoesNotDuplicate() {
        Task task = createTestTask();
        setField(task, "interval", new TaskInterval(0, 0, 15, 0));
        setField(task, "intervalStartTime", LocalTime.of(12, 0));


        manager.populateScheduleForTask(task);
        int firstCount = scheduledFor(task).size();
        assertEquals(50, firstCount);

        manager.populateScheduleForTask(task);
        assertEquals(50, scheduledFor(task).size(),
                "Repopulation should NOT add duplicates for aligned interval tasks");
    }

    @Test
    void nullStartTime_fallsBackToExistingBehavior() {
        // No intervalStartTime set — should behave identically to the original test
        Task task = createTestTask();
        setField(task, "interval", new TaskInterval(0, 0, 0, 30));
        setField(task, "lastExecuted", Date.from(FIXED_INSTANT.minusSeconds(300)));
        // intervalStartTime is null by default

        manager.populateScheduleForTask(task);

        List<ScheduledTask> scheduled = scheduledFor(task);
        assertEquals(50, scheduled.size());

        // First entry should be catch-up at lastExecuted + 30s
        Instant catchUpInstant = FIXED_INSTANT.minusSeconds(270);
        assertTrue(scheduled.stream().anyMatch(st -> st.getDate().toInstant().equals(catchUpInstant)),
                "Should have catch-up entry at lastExecuted + interval (null startTime = existing behavior)");
    }

    @Test
    void allEntriesHaveCorrectTaskReference() {
        Task task = createTestTask();
        TaskTime time = new TaskTime(LocalTime.of(14, 0), false);
        addTime(task, time);

        manager.populateScheduleForTask(task);

        for (ScheduledTask st : scheduledFor(task)) {
            assertSame(task, st.getTask(), "ScheduledTask should reference the original task");
        }
    }
}
