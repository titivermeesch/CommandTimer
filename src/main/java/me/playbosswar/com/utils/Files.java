package me.playbosswar.com.utils;

import me.playbosswar.com.Main;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;

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
     * @param name
     * @return
     */
    public static String getTimerFile(String name) {
        return pluginFolderPath + "/timers/" + name + ".json";
    }

    /**
     * Check if a file with name already exists
     * @param name
     * @return
     */
    public static Boolean commandTimerFileExists(String name) {
        File file = new File(getTimerFile(name));
        return file.exists();
    }

    /**
     * Create a new data file to store timer data
     * @param name
     * @throws IOException
     */
    public static void createNewCommandTimerDataFile(String name) throws IOException {
        if (commandTimerFileExists(name)) {
            throw new FileAlreadyExistsException(name);
        }

        FileWriter jsonFile = new FileWriter(getTimerFile(name));
        JSONObject commandTimer = new JSONObject();

        commandTimer.put("name", name);

        jsonFile.write(commandTimer.toJSONString());
        jsonFile.flush();
    }

    public static void removeExistingCommandTimer(String name) throws IOException {
        File file = new File(getTimerFile(name));

        if (!commandTimerFileExists(name)) {
            throw new FileNotFoundException();
        }

        Boolean deleted = file.delete();

        if(!deleted) {
            throw new IOException();
        }
    }

    public static void changeDataInFile(String timerName, String key, String value) throws IOException, ParseException {
        String timerFile = getTimerFile(timerName);

        Reader reader = new FileReader(timerFile);

        JSONParser parser = new JSONParser();
        JSONObject commandTimer = (JSONObject) parser.parse(reader);

        commandTimer.put(key, value);

        FileWriter jsonFile = new FileWriter(getTimerFile(timerName));
        jsonFile.write(commandTimer.toJSONString());
        jsonFile.flush();
    }

    public static void changeDataInFile(String timerName, String key, Integer value) throws IOException, ParseException {
        String timerFile = getTimerFile(timerName);

        Reader reader = new FileReader(timerFile);

        JSONParser parser = new JSONParser();
        JSONObject commandTimer = (JSONObject) parser.parse(reader);

        commandTimer.put(key, value);

        FileWriter jsonFile = new FileWriter(getTimerFile(timerName));
        jsonFile.write(commandTimer.toJSONString());
        jsonFile.flush();
    }

    public static void changeDataInFile(String timerName, String key, Boolean value) throws IOException, ParseException {
        String timerFile = getTimerFile(timerName);

        Reader reader = new FileReader(timerFile);

        JSONParser parser = new JSONParser();
        JSONObject commandTimer = (JSONObject) parser.parse(reader);

        commandTimer.put(key, value);

        FileWriter jsonFile = new FileWriter(getTimerFile(timerName));
        jsonFile.write(commandTimer.toJSONString());
        jsonFile.flush();
    }

    public static void changeDataInFile(String timerName, String key, Float value) throws IOException, ParseException {
        String timerFile = getTimerFile(timerName);

        Reader reader = new FileReader(timerFile);

        JSONParser parser = new JSONParser();
        JSONObject commandTimer = (JSONObject) parser.parse(reader);

        commandTimer.put(key, value);

        FileWriter jsonFile = new FileWriter(getTimerFile(timerName));
        jsonFile.write(commandTimer.toJSONString());
        jsonFile.flush();
    }

    public static void deserializeJsonFilesIntoCommandTimers() {
        File dir = new File(pluginFolderPath + "/timers");
        File[] directoryListing = dir.listFiles();
        JSONParser jsonParser = new JSONParser();

        try {
            if (directoryListing != null && directoryListing.length > 0) {
                for (File file : directoryListing) {
                    Messages.sendConsole("Loading existing CommandTimer " + file.getName());
                    if (!file.exists() || !file.getName().contains("json")) {
                        continue;
                    }

                    FileReader fr = new FileReader(file.getPath());
                    JSONObject o = (JSONObject) jsonParser.parse(fr);

                    System.out.println("Found data in file:");
                    System.out.println(o);

                    CommandTimer t = new CommandTimer((String) o.get("name"));
                    CommandsManager.addCommandTimer(t);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}