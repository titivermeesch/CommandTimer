package me.playbosswar.com.utils.migrations;

import com.google.gson.JsonObject;
import java.io.File;

public interface Migration {
    int getVersion();
    String getDescription();
    void migrate(File taskFile, JsonObject taskJson) throws Exception;
    void rollback(File taskFile, JsonObject taskJson) throws Exception;
}

