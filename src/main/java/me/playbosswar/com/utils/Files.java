package me.playbosswar.com.utils;

import com.google.gson.Gson;
import me.playbosswar.com.Main;
import me.playbosswar.com.tasks.Task;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

public class Files {
    static String pluginFolderPath = Main.getPlugin().getDataFolder().getPath();

    /**
     * Create folder to store all the timers in
     */
    public static void createDataFolders() {
        File file = new File(pluginFolderPath + "/timers");
        Boolean created = file.mkdir();

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

    /**
     * Check if a file with name already exists
     *
     * @param name
     * @return
     */
    public static Boolean taskFileExists(String name) {
        File file = new File(getTaskFile(name));
        return file.exists();
    }

    /**
     * Create a new data file to store timer data
     *
     * @param name
     * @throws IOException
     */
    public static void createNewTaskFile(String name) throws IOException {
        if (taskFileExists(name)) {
            throw new FileAlreadyExistsException(name);
        }

        FileWriter jsonFile = new FileWriter(getTaskFile(name));
        JSONObject commandTimer = new JSONObject();

        ArrayList<String> days = new ArrayList<>();

        days.add("MONDAY");
        days.add("TUESDAY");
        days.add("WEDNESDAY");
        days.add("THURSDAY");
        days.add("FRIDAY");
        days.add("SATURDAY");
        days.add("SUNDAY");

        commandTimer.put("name", name);
        commandTimer.put("days", days);

        jsonFile.write(commandTimer.toJSONString());
        jsonFile.flush();
    }

    public static void removeExistingCommandTimer(String name) throws IOException {
        File file = new File(getTaskFile(name));

        if (!taskFileExists(name)) {
            throw new FileNotFoundException();
        }

        boolean deleted = file.delete();

        if (!deleted) {
            throw new IOException();
        }
    }

    public static void changeDataInFile(String timerName, String key, String value) throws IOException, ParseException {
        String timerFile = getTaskFile(timerName);

        Reader reader = new FileReader(timerFile);

        JSONParser parser = new JSONParser();
        JSONObject commandTimer = (JSONObject) parser.parse(reader);

        commandTimer.put(key, value);

        FileWriter jsonFile = new FileWriter(getTaskFile(timerName));
        jsonFile.write(commandTimer.toJSONString());
        jsonFile.flush();
    }

    public static void changeDataInFile(String timerName, String key, int value) throws IOException, ParseException {
        String timerFile = getTaskFile(timerName);

        Reader reader = new FileReader(timerFile);

        JSONParser parser = new JSONParser();
        JSONObject commandTimer = (JSONObject) parser.parse(reader);

        commandTimer.put(key, value);

        FileWriter jsonFile = new FileWriter(getTaskFile(timerName));
        jsonFile.write(commandTimer.toJSONString());
        jsonFile.flush();
    }

    public static void changeDataInFile(String timerName, String key, Boolean value) throws IOException, ParseException {
        String timerFile = getTaskFile(timerName);

        Reader reader = new FileReader(timerFile);

        JSONParser parser = new JSONParser();
        JSONObject commandTimer = (JSONObject) parser.parse(reader);

        commandTimer.put(key, value);

        FileWriter jsonFile = new FileWriter(getTaskFile(timerName));
        jsonFile.write(commandTimer.toJSONString());
        jsonFile.flush();
    }

    public static void changeDataInFile(String timerName, String key, ArrayList<String> values) throws IOException, ParseException {
        String timerFile = getTaskFile(timerName);

        Reader reader = new FileReader(timerFile);

        JSONParser parser = new JSONParser();
        JSONObject commandTimer = (JSONObject) parser.parse(reader);

        commandTimer.put(key, values);

        FileWriter jsonFile = new FileWriter(getTaskFile(timerName));
        jsonFile.write(commandTimer.toJSONString());
        jsonFile.flush();
    }

    public static void changeDataInFile(String timerName, String key, double value) throws IOException, ParseException {
        String timerFile = getTaskFile(timerName);

        Reader reader = new FileReader(timerFile);

        JSONParser parser = new JSONParser();
        JSONObject commandTimer = (JSONObject) parser.parse(reader);

        commandTimer.put(key, value);

        FileWriter jsonFile = new FileWriter(getTaskFile(timerName));
        jsonFile.write(commandTimer.toJSONString());
        jsonFile.flush();
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

                    Gson gson = new Gson();
                    Task task = gson.fromJson(jsonParser.parse(fr).toString(), Task.class);
                    if(task.getCommands() == null) {
                        task.setCommands(new ArrayList<>());
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