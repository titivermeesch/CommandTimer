package me.playbosswar.com.updater;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;


public class Updater {
    private static final String USER_AGENT = "Updater by Stipess1";
    // Direct download link
    private String downloadLink;
    // Provided plugin
    private final Plugin plugin;
    // ID of a project
    private final int id = 24141;
    // return a page
    private int page = 1;
    // Set the update type
    private final UpdateType updateType = UpdateType.VERSION_CHECK;
    // Get the outcome result
    private Result result = Result.SUCCESS;
    // If next page is empty set it to true, and get info from previous page.
    private boolean emptyPage;
    // Updater thread
    private final Thread thread;

    private static final String DOWNLOAD = "/download";
    private static final String VERSIONS = "/versions";
    private static final String PAGE = "?page=";
    private static final String API_RESOURCE = "https://api.spiget.org/v2/resources/";

    public Updater(Plugin plugin) {
        this.plugin = plugin;

        downloadLink = API_RESOURCE + id;

        thread = new Thread(new UpdaterRunnable());
        thread.start();
    }

    public enum UpdateType {
        VERSION_CHECK,
    }

    public enum Result {
        UPDATE_FOUND,
        NO_UPDATE,
        SUCCESS,
        BAD_ID
    }

    /**
     * Get the result of the update.
     *
     * @return result of the update.
     * @see Result
     */
    public Result getResult() {
        waitThread();
        return result;
    }

    /**
     * Check if id of resource is valid
     *
     * @param link link of the resource
     * @return true if id of resource is valid
     */
    private boolean checkResource(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", USER_AGENT);

            int code = connection.getResponseCode();

            if (code != 200) {
                connection.disconnect();
                result = Result.BAD_ID;
                return false;
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Checks if there is any update available.
     */
    private void checkUpdate() {
        try {
            String page = Integer.toString(this.page);

            URL url = new URL(API_RESOURCE + id + VERSIONS + PAGE + page);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", USER_AGENT);

            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            JsonElement element = new JsonParser().parse(reader);
            JsonArray jsonArray = element.getAsJsonArray();

            if (jsonArray.size() == 10 && !emptyPage) {
                connection.disconnect();
                this.page++;
                checkUpdate();
            } else if (jsonArray.size() == 0) {
                emptyPage = true;
                this.page--;
                checkUpdate();
            } else if (jsonArray.size() < 10) {
                element = jsonArray.get(jsonArray.size() - 1);

                JsonObject object = element.getAsJsonObject();
                element = object.get("name");
                // Version returned from spigot
                String version = element.toString().replaceAll("\"", "").replace("v", "");
                // If true updater is going to log progress to the console.
                boolean logger = true;
                if (logger)
                    plugin.getLogger().info("Checking for update...");
                if (shouldUpdate(version, plugin.getDescription().getVersion()) && updateType == UpdateType.VERSION_CHECK) {
                    result = Result.UPDATE_FOUND;
                    if (logger)
                        plugin.getLogger().info("Update found!");
                } else {
                    if (logger)
                        plugin.getLogger().info("Update not found");
                    result = Result.NO_UPDATE;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if plugin should be updated
     *
     * @param newVersion remote version
     * @param oldVersion current version
     */
    private boolean shouldUpdate(String newVersion, String oldVersion) {
        return !newVersion.equalsIgnoreCase(oldVersion);
    }

    /**
     * Updater depends on thread's completion, so it is necessary to wait for thread to finish.
     */
    private void waitThread() {
        if (thread != null && thread.isAlive()) {
            this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
                try {
                    this.plugin.getLogger().log(Level.INFO, "Waiting for updater thread to finish.");
                    thread.join();
                    this.plugin.getLogger().log(Level.INFO, "Update done.");
                } catch (InterruptedException e) {
                    this.plugin.getLogger().log(Level.SEVERE, null, e);
                }
            });

        }
    }

    public class UpdaterRunnable implements Runnable {

        public void run() {
            if (checkResource(downloadLink)) {
                downloadLink = downloadLink + DOWNLOAD;
                checkUpdate();
            }
        }
    }
}
