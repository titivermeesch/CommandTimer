package me.playbosswar.com.utils;

import me.playbosswar.com.Main;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Files {
    static String pluginFolderPath = Main.getPlugin().getDataFolder().getPath();

    public static void createDataFolders() {
        File file = new File(pluginFolderPath + "/timers");
        Boolean created = file.mkdir();

        if(created) {
            Messages.sendConsole("Data folder has been created");
        } else {
            Messages.sendConsole("We could not create the data folder. If it already exists ignore this.");
        }
    }

    public static String getTimerFile(String name) {
        return pluginFolderPath + "/timers/" + name + ".json";
    }

    public static Boolean commandTimerFileExists(String name) {
        File file = new File(getTimerFile(name));
        return file.exists();
    }

    public static void createNewCommandTimerDataFile(Player p, String name) {
        if (commandTimerFileExists(name)) {
            Messages.sendMessageToPlayer(p, "&cThis name has already been used");
            return;
        }

        try {
            FileWriter jsonFile = new FileWriter(getTimerFile(name));
            JSONObject commandTimer = new JSONObject();

            commandTimer.put("name", name);

            jsonFile.write(commandTimer.toJSONString());
            jsonFile.flush();

            CommandTimer t = new CommandTimer(name);
            CommandsManager.addCommandTimer(t);

            Messages.sendMessageToPlayer(p, "Command &a" + t.getName() + " &fhas been create");
        } catch (IOException e) {
            Messages.sendMessageToPlayer(p, "&cSomething went wrong");
            e.printStackTrace();
        }
    }

    public static void removeExistingCommandTimer(Player p, String name) {
        File file = new File(getTimerFile(name));

        if (!commandTimerFileExists(name)) {
            Messages.sendMessageToPlayer(p, "&cThis command timer does not exist");
            return;
        }

        if (file.delete()) {
            CommandsManager.removeCommandTimer(name);
            Messages.sendMessageToPlayer(p, "Deleted");
            return;
        }

        Messages.sendMessageToPlayer(p, "&cSomething went wrong");
    }

    public static void deserializeJsonFilesIntoCommandTimers() {
        File dir = new File(pluginFolderPath + "/timers");
        File[] directoryListing = dir.listFiles();
        JSONParser jsonParser = new JSONParser();

        try {
            if (directoryListing != null && directoryListing.length > 0) {
                for (File file : directoryListing) {
                    Messages.sendConsole("Loading command timer " + file.getName());
                    if (!file.exists() || !file.getName().contains("json")) {
                        continue;
                    }

                    FileReader fr = new FileReader(file.getPath());
                    JSONObject o = (JSONObject) jsonParser.parse(fr);

                    CommandTimer t = new CommandTimer((String) o.get("name"));
                    CommandsManager.addCommandTimer(t);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}