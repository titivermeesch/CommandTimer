package me.playbosswar.com.utils;

import me.playbosswar.com.Main;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.time.LocalTime;
import java.util.ArrayList;

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
    public static String getTimerFile(String name) {
        return pluginFolderPath + "/timers/" + name + ".json";
    }

    /**
     * Check if a file with name already exists
     *
     * @param name
     * @return
     */
    public static Boolean commandTimerFileExists(String name) {
        File file = new File(getTimerFile(name));
        return file.exists();
    }

    /**
     * Create a new data file to store timer data
     *
     * @param name
     * @throws IOException
     */
    public static void createNewCommandTimerDataFile(String name) throws IOException {
        if (commandTimerFileExists(name)) {
            throw new FileAlreadyExistsException(name);
        }

        FileWriter jsonFile = new FileWriter(getTimerFile(name));
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
        File file = new File(getTimerFile(name));

        if (!commandTimerFileExists(name)) {
            throw new FileNotFoundException();
        }

        Boolean deleted = file.delete();

        if (!deleted) {
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

    public static void changeDataInFile(String timerName, String key, int value) throws IOException, ParseException {
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

    public static void changeDataInFile(String timerName, String key, ArrayList<String> values) throws IOException, ParseException {
        String timerFile = getTimerFile(timerName);

        Reader reader = new FileReader(timerFile);

        JSONParser parser = new JSONParser();
        JSONObject commandTimer = (JSONObject) parser.parse(reader);

        commandTimer.put(key, values);

        FileWriter jsonFile = new FileWriter(getTimerFile(timerName));
        jsonFile.write(commandTimer.toJSONString());
        jsonFile.flush();
    }

    public static void changeDataInFile(String timerName, String key, double value) throws IOException, ParseException {
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
                    if (!file.exists() || !file.getName().contains("json")) {
                        continue;
                    }

                    FileReader fr = new FileReader(file.getPath());
                    JSONObject o = (JSONObject) jsonParser.parse(fr);

                    CommandTimer t = new CommandTimer((String) o.get("name"));

                    t.setExecutionLimit(((Number) o.getOrDefault("executionLimit", -1)).intValue());

                    t.setExecutePerUser((Boolean) o.getOrDefault("executePerUser", false));

                    t.setUseMinecraftTime((Boolean) o.getOrDefault("useMinecraftTime", false));

                    t.setSelectRandomCommand((Boolean) o.getOrDefault("selectRandomCommand", false));

                    t.setSeconds(((Number) o.getOrDefault("seconds", 5)).intValue());

                    t.setMinPlayers(((Number) o.getOrDefault("minPlayers", -1)).intValue());

                    t.setMaxPlayers(((Number) o.getOrDefault("maxPlayers", -1)).intValue());

                    t.setGender(Gender.valueOf((String) o.getOrDefault("gender", "OPERATOR")));

                    t.setRandom((Double) o.getOrDefault("random", 1.0));

                    t.setTimesExecuted(0);

                    ArrayList<String> commands = (ArrayList<String>) o.getOrDefault("commands", new ArrayList<String>());
                    t.setCommands(commands);

                    ArrayList<String> days = (ArrayList<String>) o.getOrDefault("days", new ArrayList<String>());
                    t.setDays(days);

                    ArrayList<String> times = (ArrayList<String>) o.getOrDefault("times", new ArrayList<String>());
                    t.setTimes(times);

                    ArrayList<String> worlds = (ArrayList<String>) o.getOrDefault("worlds", new ArrayList<String>());
                    t.setWorlds(worlds);

                    String permission = (String) o.getOrDefault("requiredPermission", "");
                    t.setRequiredPermission(permission);

                    String lastExecuted = (String) o.getOrDefault("lastExecuted", "");

                    if (!lastExecuted.equals("")) {
                        LocalTime lastExecutedTime = LocalTime.parse(lastExecuted);
                        t.setLastExecuted(lastExecutedTime);
                    }

                    CommandsManager.addCommandTimer(t);
                    Messages.sendConsole("Timer " + t.getName() + " has been loaded");
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}