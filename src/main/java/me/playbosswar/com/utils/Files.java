package me.playbosswar.com.utils;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.conditionsengine.validations.Condition;
import me.playbosswar.com.conditionsengine.validations.ConditionType;
import me.playbosswar.com.conditionsengine.validations.SimpleCondition;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TaskInterval;
import me.playbosswar.com.utils.gson.GsonConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class Files {
    static String pluginFolderPath = CommandTimerPlugin.getPlugin().getDataFolder().getPath();

    /**
     * Create folder to store all the timers in
     */
    public static void createDataFolders() {
        File timersFile = new File(pluginFolderPath + "/timers");
        File extensionsFolder = new File(pluginFolderPath + "/extensions");
        boolean created = timersFile.mkdir();
        boolean created2 = extensionsFolder.mkdir();

        if (created && created2) {
            Messages.sendConsole("Data folder has been created");
        } else {
            Messages.sendConsole("We could not create the data folder. If it already exists ignore this.");
        }
    }

    /**
     * Returns timer json file
     *
     * @param name
     *
     * @return
     */
    public static String getTaskFile(String name) {
        return pluginFolderPath + "/timers/" + name + ".json";
    }

    private static void setTaskOnConditions(Task task, List<Condition> conditions) {
        conditions.forEach(condition -> {
            condition.setTask(task);
            if (condition.getConditionType().equals(ConditionType.SIMPLE) || condition.getConditionType().equals(ConditionType.NOT)) {
                condition.getSimpleCondition().setTask(task);
            } else {
                setTaskOnConditions(task, condition.getConditions());
            }
        });
    }

    private static void healTask(Task task) {
        TaskInterval defaultInterval = new TaskInterval(task, 0, 0, 0, 5);
        if(task.getCommands() == null) {
            task.setCommands(new ArrayList<>());
        }

        if(task.getInterval() == null) {
            task.setInterval(defaultInterval);
        }

        if(task.getTimes() == null) {
            task.setTimes(new ArrayList<>());
        }

        if(task.getCommandExecutionInterval() == null) {
            task.setCommandExecutionInterval(defaultInterval);
        }
    }

    public static List<Task> deserializeJsonFilesIntoCommandTimers() {
        File dir = new File(pluginFolderPath + "/timers");
        File[] directoryListing = dir.listFiles();
        JSONParser jsonParser = new JSONParser();
        List<Task> tasks = new ArrayList<>();

        try {
            if (directoryListing != null && directoryListing.length > 0) {
                for (File file : directoryListing) {
                    if (!file.exists() || !file.getName().contains("json")) {
                        continue;
                    }

                    FileReader fr = new FileReader(file.getPath());

                    GsonConverter gson = new GsonConverter();
                    Task task = gson.fromJson(jsonParser.parse(fr).toString(), Task.class);
                    healTask(task);
                    // We relink the tasks to commands and times because we lose this structure during serializing
                    task.getCommands().forEach(command -> command.setTask(task));
                    task.getTimes().forEach(time -> time.setTask(task));
                    task.getInterval().setTask(task);
                    task.getCommandExecutionInterval().setTask(task);
                    Condition condition = task.getCondition();
                    condition.setTask(task);

                    SimpleCondition simpleCondition = condition.getSimpleCondition();
                    if (simpleCondition != null) {
                        simpleCondition.setTask(task);
                    }

                    if (condition.getConditionType().equals(ConditionType.OR) || condition.getConditionType().equals(ConditionType.AND)) {
                        setTaskOnConditions(task, condition.getConditions());
                    }

                    if (task.isResetExecutionsAfterRestart()) {
                        task.setTimesExecuted(0);
                    }

                    tasks.add(task);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    @Nullable
    public static <T> Class<? extends T> findClass(@NotNull final File file,
                                                   @NotNull final Class<T> clazz) throws IOException, ClassNotFoundException {
        if (!file.exists()) {
            return null;
        }

        final URL jar = file.toURI().toURL();
        final URLClassLoader loader = new URLClassLoader(new URL[]{ jar }, clazz.getClassLoader());
        final List<String> matches = new ArrayList<>();
        final List<Class<? extends T>> classes = new ArrayList<>();

        try (final JarInputStream stream = new JarInputStream(jar.openStream())) {
            JarEntry entry;
            while ((entry = stream.getNextJarEntry()) != null) {
                final String name = entry.getName();
                if (name.isEmpty() || !name.endsWith(".class")) {
                    continue;
                }

                matches.add(name.substring(0, name.lastIndexOf('.')).replace('/', '.'));
            }

            for (final String match : matches) {
                try {
                    final Class<?> loaded = loader.loadClass(match);
                    if (clazz.isAssignableFrom(loaded)) {
                        classes.add(loaded.asSubclass(clazz));
                    }
                } catch (final NoClassDefFoundError ignored) {
                }
            }
        }
        if (classes.isEmpty()) {
            loader.close();
            return null;
        }
        return classes.get(0);
    }
}