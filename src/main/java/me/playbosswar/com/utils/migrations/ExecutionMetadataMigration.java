package me.playbosswar.com.utils.migrations;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.playbosswar.com.tasks.TaskExecutionMetadata;
import me.playbosswar.com.utils.Files;
import me.playbosswar.com.utils.gson.GsonConverter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ExecutionMetadataMigration implements Migration {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss. SSSXXX");

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "Move execution metadata to separate files";
    }

    @Override
    public void migrate(File taskFile, JsonObject taskJson) throws Exception {
        String idStr = taskJson.get("id").getAsString();
        UUID id = UUID.fromString(idStr);

        if (!taskJson.has("timesExecuted") || !taskJson.has("lastExecutedCommandIndex") || !taskJson.has("lastExecuted")) {
            throw new IllegalStateException("Missing required fields for migration: timesExecuted, lastExecutedCommandIndex, or lastExecuted");
        }

        int timesExecuted = taskJson.get("timesExecuted").getAsInt();
        int lastExecutedCommandIndex = taskJson.get("lastExecutedCommandIndex").getAsInt();
        Date lastExecuted = DATE_FORMAT.parse(taskJson.get("lastExecuted").getAsString());

        File metadataFile = new File(Files.getTaskLocalExecutionFile(id));
        if (!metadataFile.exists()) {
            TaskExecutionMetadata metadata = new TaskExecutionMetadata(id, timesExecuted, lastExecutedCommandIndex, lastExecuted);
            GsonConverter gson = new GsonConverter();
            try (FileWriter metaWriter = new FileWriter(metadataFile)) {
                metaWriter.write(gson.toJson(metadata));
                metaWriter.flush();
            }
        }

        taskJson.remove("timesExecuted");
        taskJson.remove("lastExecutedCommandIndex");
        taskJson.remove("lastExecuted");
    }

    @Override
    public void rollback(File taskFile, JsonObject taskJson) throws Exception {
        String idStr = taskJson.get("id").getAsString();
        UUID id = UUID.fromString(idStr);

        File metadataFile = new File(Files.getTaskLocalExecutionFile(id));
        if (!metadataFile.exists()) {
            taskJson.addProperty("timesExecuted", 0);
            taskJson.addProperty("lastExecutedCommandIndex", 0);
            taskJson.addProperty("lastExecuted", DATE_FORMAT.format(new Date()));
            return;
        }

        try (FileReader fr = new FileReader(metadataFile)) {
            JsonObject metadataJson = new JsonParser().parse(fr).getAsJsonObject();

            if (!metadataJson.has("timesExecuted") || !metadataJson.has("lastExecutedCommandIndex") || !metadataJson.has("lastExecuted")) {
                throw new IllegalStateException("Missing required fields in metadata file: timesExecuted, lastExecutedCommandIndex, or lastExecuted");
            }
            
            int timesExecuted = metadataJson.get("timesExecuted").getAsInt();
            int lastExecutedCommandIndex = metadataJson.get("lastExecutedCommandIndex").getAsInt();
            String lastExecuted = metadataJson.get("lastExecuted").getAsString();

            taskJson.addProperty("timesExecuted", timesExecuted);
            taskJson.addProperty("lastExecutedCommandIndex", lastExecutedCommandIndex);
            taskJson.addProperty("lastExecuted", lastExecuted);
        }

        metadataFile.delete();
    }
}

