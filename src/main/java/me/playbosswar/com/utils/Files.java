package me.playbosswar.com.utils;

import io.sentry.ITransaction;
import io.sentry.Sentry;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TaskExecutionMetadata;
import me.playbosswar.com.tasks.TaskInterval;
import me.playbosswar.com.utils.gson.GsonConverter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;

public class Files {
    static String pluginFolderPath = CommandTimerPlugin.getPlugin().getDataFolder().getPath();

    /**
     * Create folder to store all the timers in
     */
    public static void createDataFolders() {
        File timersFile = new File(pluginFolderPath + "/timers");
        File extensionsFolder = new File(pluginFolderPath + "/extensions");
        File adHocCommandsFolder = new File(pluginFolderPath + "/ad-hoc-commands");
        File executionDataFolder = new File(pluginFolderPath + "/execution-data");
        timersFile.mkdirs();
        extensionsFolder.mkdirs();
        adHocCommandsFolder.mkdirs();
        executionDataFolder.mkdirs();

        File dataFolder = CommandTimerPlugin.getPlugin().getDataFolder();
        File enLangFile = new File(dataFolder.getAbsoluteFile() + "/languages/en.json");
        if(!enLangFile.exists()) {
            Messages.sendDebugConsole("could not find languages/en.json, creating default");
            CommandTimerPlugin.getPlugin().saveResource("languages/en.json", false);
        }
        CommandTimerPlugin.getPlugin().saveResource("languages/default.json", true);
    }

    private static File findTaskFileByUuid(UUID id) {
        File dir = new File(pluginFolderPath + "/timers");
        File[] files = dir.listFiles(file -> file.getName().endsWith(".json"));
        if (files == null) return null;

        for (File file : files) {
            try (FileReader fr = new FileReader(file)) {
                JsonObject json = new JsonParser().parse(fr).getAsJsonObject();
                if (json.has("id")) {
                    UUID fileId = UUID.fromString(json.get("id").getAsString());
                    if (fileId.equals(id)) {
                        return file;
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }

    private static File findMetadataFileByUuid(UUID id) {
        File dir = new File(pluginFolderPath + "/execution-data");
        File[] files = dir.listFiles(file -> file.getName().endsWith(".json"));
        if (files == null) return null;

        for (File file : files) {
            try (FileReader fr = new FileReader(file)) {
                JsonObject json = new JsonParser().parse(fr).getAsJsonObject();
                if (json.has("taskId")) {
                    UUID fileId = UUID.fromString(json.get("taskId").getAsString());
                    if (fileId.equals(id)) {
                        return file;
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }

    private static File findAdHocCommandFileByUuid(UUID id) {
        File dir = new File(pluginFolderPath + "/ad-hoc-commands");
        File[] files = dir.listFiles(file -> file.getName().endsWith(".json"));
        if (files == null) return null;

        for (File file : files) {
            try (FileReader fr = new FileReader(file)) {
                JsonObject json = new JsonParser().parse(fr).getAsJsonObject();
                if (json.has("id")) {
                    UUID fileId = UUID.fromString(json.get("id").getAsString());
                    if (fileId.equals(id)) {
                        return file;
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }

    public static String getTaskFile(UUID id) {
        File file = findTaskFileByUuid(id);
        if (file != null) {
            return file.getAbsolutePath();
        }
        throw new IllegalStateException("Task file not found for UUID: " + id);
    }

    public static String getTaskLocalExecutionFile(UUID id) {
        File file = findMetadataFileByUuid(id);
        if (file != null) {
            return file.getAbsolutePath();
        }
        throw new IllegalStateException("Task metadata file not found for UUID: " + id);
    }

    public static String getNewTaskFile(UUID id) {
        return pluginFolderPath + "/timers/" + id + ".json";
    }

    public static String getNewTaskLocalExecutionFile(UUID id) {
        return pluginFolderPath + "/execution-data/" + id + ".json";
    }

    public static String getAdHocCommandsDirectory() {
        return pluginFolderPath + "/ad-hoc-commands";
    }

    public static String getAdHocCommandFile(UUID id) {
        File file = findAdHocCommandFileByUuid(id);
        if (file != null) {
            return file.getAbsolutePath();
        }
        throw new IllegalStateException("Ad-hoc command file not found for UUID: " + id);
    }

    public static String getNewAdHocCommandFile(UUID id) {
        return pluginFolderPath + "/ad-hoc-commands/" + id + ".json";
    }

    private static void healTask(Task task) {
        TaskInterval defaultInterval = new TaskInterval(0, 0, 0, 5);
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

    public static TaskExecutionMetadata getOrCreateTaskMetadata(Task task) {
        try {
            File file = findMetadataFileByUuid(task.getId());
            if (file == null || !file.exists()) {
                TaskExecutionMetadata metadata = new TaskExecutionMetadata(task.getId(),
                        task.getTimesExecuted(), task.getLastExecutedCommandIndex(), task.getLastExecuted());
                GsonConverter gson = new GsonConverter();
                String json = gson.toJson(metadata);
                String path = getNewTaskLocalExecutionFile(task.getId());
                try (FileWriter jsonFile = new FileWriter(path)) {
                    jsonFile.write(json);
                    jsonFile.flush();
                }
                return metadata;
            }

            FileReader fr = new FileReader(file);
            JSONParser jsonParser = new JSONParser();
            TaskExecutionMetadata metadata = new GsonConverter().fromJson(jsonParser.parse(fr).toString(),
                    TaskExecutionMetadata.class);
            return metadata;
        } catch(IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void updateLocalTaskMetadata(Task task) {
        TaskExecutionMetadata metadata = new TaskExecutionMetadata(task.getId(),
                task.getTimesExecuted(), task.getLastExecutedCommandIndex(), task.getLastExecuted());

        GsonConverter gson = new GsonConverter();
        String json = gson.toJson(metadata);
        File file = findMetadataFileByUuid(task.getId());
        String path = file != null ? file.getAbsolutePath() : getNewTaskLocalExecutionFile(task.getId());
        try (FileWriter jsonFile = new FileWriter(path)) {
            jsonFile.write(json);
            jsonFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Task> deserializeJsonFilesIntoCommandTimers() {
        ITransaction transaction = Sentry.startTransaction("deserializeJsonFilesIntoCommandTimers()",
                "initiation");
        File dir = new File(pluginFolderPath + "/timers");
        File[] directoryListing = dir.listFiles(file -> file.getName().endsWith(".json"));
        JSONParser jsonParser = new JSONParser();
        List<Task> tasks = new ArrayList<>();

        try {
            if(directoryListing != null) {
                for(File file : directoryListing) {
                    if(!file.exists() || !file.getName().contains(".json")) {
                        continue;
                    }

                    try {
                        Messages.sendConsole("Loading task " + file.getName());
                        FileReader fr = new FileReader(file.getPath());

                        GsonConverter gson = new GsonConverter();
                        Task task = gson.fromJson(jsonParser.parse(fr).toString(), Task.class);
                        healTask(task);

                        task.getCommands().forEach(command -> {
                            if(command.getInterval() == null) {
                                command.setInterval(new TaskInterval(0, 0, 0, 0));
                            }
                            if(command.getDelay() == null) {
                                command.setDelay(new TaskInterval(0, 0, 0, 0));
                            }
                        });
                        if(task.getEvents() == null) {
                            task.setEvents(new ArrayList<>());
                        }

                        TaskExecutionMetadata metadata = getOrCreateTaskMetadata(task);
                        if (metadata != null) {
                            task.setTimesExecuted(metadata.getTimesExecuted());
                            task.setLastExecutedCommandIndex(metadata.getLastExecutedCommandIndex());
                            task.setLastExecuted(metadata.getLastExecuted());
                        }

                        tasks.add(task);
                    } catch (JsonParseException e) {
                        Bukkit.getLogger().log(Level.SEVERE, "Failed to process " + file.getName() + " because of " + e.getMessage());
                    } catch(ParseException e) {
                        Bukkit.getLogger().log(Level.SEVERE, "Failed to process " + file.getName() + " because of " + e.getMessage());
                    }

                }
            }
        } catch(IOException e) {
            e.printStackTrace();
            transaction.setThrowable(e);
        } finally {
            transaction.finish();
        }

        return tasks;
    }

    @Nullable
    public static <T> Class<? extends T> findClass(@NotNull final File file, @NotNull final Class<T> clazz) throws
            IOException, ClassNotFoundException {
        if(!file.exists()) {
            return null;
        }

        final URL jar = file.toURI().toURL();
        final URLClassLoader loader = new URLClassLoader(new URL[]{jar}, clazz.getClassLoader());
        final List<String> matches = new ArrayList<>();
        final List<Class<? extends T>> classes = new ArrayList<>();

        try(final JarInputStream stream = new JarInputStream(jar.openStream())) {
            JarEntry entry;
            while((entry = stream.getNextJarEntry()) != null) {
                final String name = entry.getName();
                if(!name.endsWith(".class")) {
                    continue;
                }

                matches.add(name.substring(0, name.lastIndexOf('.')).replace('/', '.'));
            }

            for(final String match : matches) {
                try {
                    final Class<?> loaded = loader.loadClass(match);
                    if(clazz.isAssignableFrom(loaded)) {
                        classes.add(loaded.asSubclass(clazz));
                    }
                } catch(final NoClassDefFoundError ignored) {
                }
            }
        }
        if(classes.isEmpty()) {
            loader.close();
            return null;
        }
        return classes.get(0);
    }
}