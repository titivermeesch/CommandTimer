package me.playbosswar.com.tasks;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.utils.gson.GsonConverter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Activating a task restarts its interval countdown. That must only happen when the task
 * actually changes state, otherwise a repeated /cmt activate (for example from a startup
 * routine) silently pushes the next execution of a long running task forward
 */
class TaskActivationAnchorTest {
    private static final long DAY_IN_MS = 24L * 60 * 60 * 1000;

    @TempDir
    static Path pluginFolder;

    private static CommandTimerPlugin mockPlugin;

    private TasksManager manager;

    @BeforeAll
    static void setUpPlugin() throws Exception {
        java.nio.file.Files.createDirectories(pluginFolder.resolve("timers"));
        java.nio.file.Files.createDirectories(pluginFolder.resolve("execution-data"));

        mockPlugin = Mockito.mock(CommandTimerPlugin.class);
        FileConfiguration mockConfig = Mockito.mock(FileConfiguration.class);
        Mockito.when(mockPlugin.getConfig()).thenReturn(mockConfig);
        Mockito.when(mockConfig.getBoolean(Mockito.anyString())).thenReturn(false);
        // getDataFolder() is final, so the backing field is set instead of stubbing it
        Field dataFolderField = JavaPlugin.class.getDeclaredField("dataFolder");
        dataFolderField.setAccessible(true);
        dataFolderField.set(mockPlugin, pluginFolder.toFile());

        // Task serialization pulls in enums that resolve their description through the language manager
        LanguageManager mockLanguageManager = Mockito.mock(LanguageManager.class);
        Mockito.when(mockLanguageManager.get(Mockito.any(LanguageKey.class))).thenReturn("");

        setStaticField("plugin", mockPlugin);
        setStaticField("instance", mockPlugin);
        setStaticField("languageManager", mockLanguageManager);
    }

    @AfterAll
    static void tearDownPlugin() throws Exception {
        setStaticField("plugin", null);
        setStaticField("instance", null);
        setStaticField("languageManager", null);
    }

    @BeforeEach
    void setUp() {
        manager = new TasksManager();
        Mockito.when(mockPlugin.getTasksManager()).thenReturn(manager);
    }

    // ========================= Tests =========================

    @Test
    void activatingAnAlreadyActiveTaskKeepsTheAnchor() {
        Date anchor = daysAgo(7);
        Task task = createTask(new TaskInterval(14, 0, 0, 0), anchor, true);

        task.setActive(true);

        assertEquals(anchor, task.getLastExecuted(),
                "Activating an already active task must not restart the countdown");
    }

    @Test
    void deactivatingKeepsTheAnchor() {
        Date anchor = daysAgo(7);
        Task task = createTask(new TaskInterval(14, 0, 0, 0), anchor, true);

        task.setActive(false);

        assertFalse(task.isActive());
        assertEquals(anchor, task.getLastExecuted(), "Deactivating must not touch the countdown");
    }

    @Test
    void activatingAnInactiveTaskRestartsTheCountdown() {
        Task task = createTask(new TaskInterval(14, 0, 0, 0), daysAgo(7), false);

        long before = System.currentTimeMillis();
        task.setActive(true);
        long after = System.currentTimeMillis();

        assertTrue(task.isActive());
        assertTrue(task.getLastExecuted().getTime() >= before && task.getLastExecuted().getTime() <= after,
                "Activating an inactive task should anchor the countdown to now");
    }

    @Test
    void restartedCountdownIsPersisted() {
        Task task = createTask(new TaskInterval(14, 0, 0, 0), daysAgo(7), false);

        task.setActive(true);

        assertEquals(task.getLastExecuted(), readStoredLastExecuted(task),
                "The new countdown anchor must be written to the execution data file");
    }

    @Test
    void activatingAnAlreadyActiveTaskKeepsTheNextExecutionDate() {
        Date anchor = daysAgo(7);
        Task task = createTask(new TaskInterval(14, 0, 0, 0), anchor, true);

        manager.populateScheduleForTask(task);
        Date expected = new Date(anchor.getTime() + 14 * DAY_IN_MS);
        assertEquals(expected, nextExecution(task));

        task.setActive(true);

        assertFalse(scheduledFor(task).isEmpty(), "A no-op activation should not clear the schedule");
        assertEquals(expected, nextExecution(task));

        // Also verify a freshly built schedule still lands on the same date
        manager.resetScheduleForTask(task);
        manager.populateScheduleForTask(task);
        assertEquals(expected, nextExecution(task),
                "Next execution should stay at lastExecuted + interval, not move to now + interval");
    }

    // ========================= Helpers =========================

    private static Date daysAgo(int days) {
        return new Date(System.currentTimeMillis() - days * DAY_IN_MS);
    }

    private Task createTask(TaskInterval interval, Date lastExecuted, boolean active) {
        Task task = new Task();
        setField(task, "id", UUID.randomUUID());
        setField(task, "name", "AnchorTestTask");
        setField(task, "active", active);
        setField(task, "interval", interval);
        setField(task, "times", new ArrayList<TaskTime>());
        setField(task, "days", allDays());
        setField(task, "executionLimit", -1);
        setField(task, "timesExecuted", 0);
        setField(task, "lastExecuted", lastExecuted);
        setField(task, "events", new ArrayList<>());
        setField(task, "commands", new ArrayList<>());
        return task;
    }

    private List<ScheduledTask> scheduledFor(Task task) {
        return manager.getScheduledTasks().stream()
                .filter(st -> st.getTask().getId().equals(task.getId()))
                .collect(Collectors.toList());
    }

    private Date nextExecution(Task task) {
        ScheduledTask next = manager.getNextScheduledTaskForTask(task);
        assertNotNull(next, "Task should have a scheduled execution");
        return Date.from(next.getDate().toInstant());
    }

    private static Date readStoredLastExecuted(Task task) {
        File file = pluginFolder.resolve("execution-data").resolve(task.getId() + ".json").toFile();
        assertTrue(file.exists(), "Execution data file should exist");
        try {
            String json = new String(java.nio.file.Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            TaskExecutionMetadata metadata = new GsonConverter().fromJson(json, TaskExecutionMetadata.class);
            return metadata.getLastExecuted();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read execution data file", e);
        }
    }

    private static Collection<DayOfWeek> allDays() {
        Collection<DayOfWeek> days = new ArrayList<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            days.add(day);
        }
        return days;
    }

    private static void setStaticField(String fieldName, Object value) throws Exception {
        Field field = CommandTimerPlugin.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, value);
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
}
