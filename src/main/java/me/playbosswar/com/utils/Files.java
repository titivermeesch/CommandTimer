package me.playbosswar.com.utils;

import me.playbosswar.com.Main;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.gson.GsonConverter;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Files {
    static String pluginFolderPath = Main.getPlugin().getDataFolder().getPath();

    /**
     * Create folder to store all the timers in
     */
    public static void createDataFolders() {
        File file = new File(pluginFolderPath + "/timers");
        boolean created = file.mkdir();

        if (created) {
            Messages.sendConsole("Data folder has been created");
        } else {
            Messages.sendConsole("We could not create the data folder. If it already exists ignore this.");
        }
    }

    /**
     * Returns timer json file
     *
     * @param name
     * @return
     */
    public static String getTaskFile(String name) {
        return pluginFolderPath + "/timers/" + name + ".json";
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
                    // We relink the tasks to commands and times because we lose this structure during serializing
                    task.getCommands().forEach(command -> command.setTask(task));
                    task.getTimes().forEach(time -> time.setTask(task));
                    task.getInterval().setTask(task);

                    if(task.isResetExecutionsAfterRestart()) {
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
}