package me.playbosswar.com.utils.migrations;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.utils.Messages;
import me.playbosswar.com.utils.gson.GsonConverter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MigrationManager {
    public static final int CURRENT_VERSION = 1;
    private final List<Migration> migrations = new ArrayList<>();
    private final String pluginFolderPath;

    public MigrationManager(CommandTimerPlugin plugin) {
        this.pluginFolderPath = plugin.getDataFolder().getPath();
        registerMigrations();
    }

    private void registerMigrations() {
        migrations.add(new ExecutionMetadataMigration());
    }

    public void runMigrations() {
        File dir = new File(pluginFolderPath + "/timers");
        File[] files = dir.listFiles(file -> file.getName().endsWith(".json"));
        if (files == null) return;

        for (File file : files) {
            try (FileReader fr = new FileReader(file)) {
                JsonObject json = new JsonParser().parse(fr).getAsJsonObject();

                int fileVersion = json.has("configVersion") ? json.get("configVersion").getAsInt() : 0;

                if (fileVersion >= CURRENT_VERSION) {
                    Messages.sendDebugConsole("Skipping migration for " + file.getName() + " (v" + fileVersion + " >= v" + CURRENT_VERSION + ")");
                    continue;
                }

                Messages.sendConsole("Migrating task file: " + file.getName() + " (v" + fileVersion + " -> v" + CURRENT_VERSION + ")");

                for (Migration migration : migrations.stream()
                        .filter(m -> m.getVersion() > fileVersion)
                        .sorted(Comparator.comparingInt(Migration::getVersion))
                        .toArray(Migration[]::new)) {
                    try {
                        migration.migrate(file, json);
                    } catch (Exception e) {
                        Messages.sendConsole("Migration v" + migration.getVersion() + " failed for " + file.getName() + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                Messages.sendConsole("Migration complete for " + file.getName() + " (v" + fileVersion + " -> v" + CURRENT_VERSION + ")");

                json.addProperty("configVersion", CURRENT_VERSION);
                GsonConverter gson = new GsonConverter();
                try (FileWriter fw = new FileWriter(file)) {
                    fw.write(gson.toJson(json));
                    fw.flush();
                }

            } catch (IOException e) {
                Messages.sendConsole("Failed to process " + file.getName() + ": " + e.getMessage());
            }
        }
    }

    public void rollbackToVersion(int targetVersion) {
        if (targetVersion < 0) {
            Messages.sendConsole("Invalid target version: " + targetVersion);
            return;
        }

        File dir = new File(pluginFolderPath + "/timers");
        File[] files = dir.listFiles(file -> file.getName().endsWith(".json"));
        if (files == null) return;

        for (File file : files) {
            try (FileReader fr = new FileReader(file)) {
                JsonObject json = new JsonParser().parse(fr).getAsJsonObject();

                int fileVersion = json.has("configVersion") ? json.get("configVersion").getAsInt() : 0;

                if (fileVersion <= targetVersion) {
                    continue;
                }

                Messages.sendConsole("Rolling back task file: " + file.getName() + " (v" + fileVersion + " -> v" + targetVersion + ")");

                for (Migration migration : migrations.stream()
                        .filter(m -> m.getVersion() <= fileVersion && m.getVersion() > targetVersion)
                        .sorted((a, b) -> Integer.compare(b.getVersion(), a.getVersion()))
                        .toArray(Migration[]::new)) {
                    try {
                        Messages.sendConsole("Rolling back migration v" + migration.getVersion() + ": " + migration.getDescription());
                        migration.rollback(file, json);
                    } catch (Exception e) {
                        Messages.sendConsole("Rollback v" + migration.getVersion() + " failed for " + file.getName() + ": " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                if (targetVersion == 0) {
                    json.remove("configVersion");
                } else {
                    json.addProperty("configVersion", targetVersion);
                }
                GsonConverter gson = new GsonConverter();
                try (FileWriter fw = new FileWriter(file)) {
                    fw.write(gson.toJson(json));
                    fw.flush();
                }

            } catch (IOException e) {
                Messages.sendConsole("Failed to process " + file.getName() + ": " + e.getMessage());
            }
        }

        Messages.sendConsole("Rollback to version " + targetVersion + " complete");
    }

    public int getCurrentVersion() {
        return CURRENT_VERSION;
    }

    public List<Migration> getMigrations() {
        return migrations;
    }
}

