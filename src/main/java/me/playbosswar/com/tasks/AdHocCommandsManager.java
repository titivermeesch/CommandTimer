package me.playbosswar.com.tasks;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.enums.Gender;
import me.playbosswar.com.utils.CommandExecutor;
import me.playbosswar.com.utils.Files;
import me.playbosswar.com.utils.Messages;
import me.playbosswar.com.utils.gson.GsonConverter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AdHocCommandsManager {
    private List<AdHocCommand> loadedCommands = new ArrayList<>();
    private Thread runnerThread;
    public boolean stopRunner = false;

    public AdHocCommandsManager(Plugin plugin) {
        loadedCommands.addAll(loadAdHocCommandsFromFiles());
        startRunner();
    }

    public AdHocCommand scheduleCommand(String command, Gender gender, ZonedDateTime scheduledTime) {
        AdHocCommand adHocCommand = new AdHocCommand(command, gender, scheduledTime);
        loadedCommands.add(adHocCommand);
        storeCommand(adHocCommand);
        return adHocCommand;
    }

    @Nullable
    public AdHocCommand getCommandById(UUID id) {
        return loadedCommands.stream()
                .filter(cmd -> cmd.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void removeCommand(AdHocCommand command) throws IOException {
        loadedCommands.remove(command);
        java.nio.file.Files.deleteIfExists(Paths.get(Files.getAdHocCommandFile(command.getId())));
    }

    public List<AdHocCommand> getLoadedCommands() {
        return loadedCommands;
    }

    public List<AdHocCommand> getPendingCommands() {
        return loadedCommands.stream()
                .filter(cmd -> !cmd.isExecuted())
                .collect(Collectors.toList());
    }

    private void startRunner() {
        Runnable runner = new AdHocCommandRunner(this);
        Thread thread = new Thread(runner);
        thread.start();
        this.runnerThread = thread;
    }

    public List<AdHocCommand> getCommandsToExecute() {
        ZonedDateTime now = ZonedDateTime.now();
        return loadedCommands.stream()
                .filter(cmd -> !cmd.isExecuted() && cmd.getScheduledTime().toInstant().toEpochMilli() <= now.toInstant().toEpochMilli())
                .collect(Collectors.toList());
    }

    public void processCommandExecution(AdHocCommand adHocCommand) {
        CommandTimerPlugin.getScheduler().runTaskAsynchronously(() -> {
            CommandExecutor.ExecutionContext context = new CommandExecutor.ExecutionContext.Builder()
                    .command(adHocCommand.getCommand())
                    .gender(adHocCommand.getGender())
                    .build();

            CommandExecutor.execute(context);

            adHocCommand.setExecuted(true);
            storeCommand(adHocCommand);
        });
    }

    private List<AdHocCommand> loadAdHocCommandsFromFiles() {
        List<AdHocCommand> commands = new ArrayList<>();
        File dir = new File(Files.getAdHocCommandsDirectory());
        if (!dir.exists()) {
            dir.mkdirs();
            return commands;
        }

        File[] directoryListing = dir.listFiles(file -> file.getName().endsWith(".json"));
        JSONParser jsonParser = new JSONParser();
        GsonConverter gson = new GsonConverter();

        if (directoryListing != null) {
            for (File file : directoryListing) {
                try {
                    FileReader fr = new FileReader(file.getPath());
                    AdHocCommand command = gson.fromJson(jsonParser.parse(fr).toString(), AdHocCommand.class);
                    if (command != null && !command.isExecuted()) {
                        commands.add(command);
                    }
                } catch (IOException | ParseException e) {
                    Messages.sendConsole("Failed to load ad-hoc command from " + file.getName() + ": " + e.getMessage());
                }
            }
        }

        return commands;
    }

    private void storeCommand(AdHocCommand command) {
        GsonConverter gson = new GsonConverter();
        String json = gson.toJson(command);
        try {
            String path;
            try {
                path = Files.getAdHocCommandFile(command.getId());
            } catch (IllegalStateException e) {
                path = Files.getNewAdHocCommandFile(command.getId());
            }
            try (FileWriter jsonFile = new FileWriter(path)) {
                jsonFile.write(json);
                jsonFile.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disable() {
        stopRunner = true;
        if (runnerThread != null && runnerThread.isAlive()) {
            runnerThread.interrupt();
        }
    }
}

