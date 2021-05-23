package me.playbosswar.com.tasks;

import me.playbosswar.com.Main;
import me.playbosswar.com.Tools;
import me.playbosswar.com.enums.CommandExecutionMode;
import me.playbosswar.com.enums.Gender;
import me.playbosswar.com.hooks.PAPIHook;
import me.playbosswar.com.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Runnable responsible for scheduling tasks
 */
public class TaskRunner implements Runnable {
    private static final boolean debug = Main.getPlugin().getConfig().getBoolean("debug");

    @Override
    public void run() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                List<Task> tasks = Main.getTasksManager().getLoadedTasks();

                for (Task timer : tasks) {
                    if (debug) {
                        Messages.sendConsole("Checking if " + timer.getName() + " can be executed");
                    }

                    DayOfWeek today = LocalDate.now().getDayOfWeek();

                    // Check if the command can be executed on this day
                    if (timer.getDays().size() > 0 && !timer.getDays().contains(today.toString())) {
                        if (debug) {
                            Messages.sendConsole("Command can not be executed today");
                        }

                        continue;
                    }

                    if (timer.getMinPlayers() != -1 && Bukkit.getOnlinePlayers().size() < timer.getMinPlayers()) {
                        if (debug) {
                            Messages.sendConsole("Timer has minPlayers limit which is not reached");
                        }
                        continue;
                    }

                    if (timer.getMaxPlayers() != -1 && Bukkit.getOnlinePlayers().size() > timer.getMaxPlayers()) {
                        if (debug) {
                            Messages.sendConsole("Timer has maxPlayers limit which went beyond limit");
                        }
                        continue;
                    }

                    // Check if the command can be executed at this time
                    if (timer.getTimes().size() > 0) {
                        if (debug) {
                            Messages.sendConsole("Timer is time related, checking if can be executed now");
                        }

                        Boolean shouldBlock = true;

                        // Handle minecraft world time
                        // if (timer.getUseMinecraftTime()) {
                        if (true) {
                            if (debug) {
                                Messages.sendConsole("Timer is using minecraft time");
                            }

                            for (String worldName : timer.getWorlds()) {
                                World world = Bukkit.getWorld(worldName == null ? "world" : worldName);
                                assert world != null;
                                String minecraftTime = Tools.calculateWorldTime(world);

                                if (debug) {
                                    Messages.sendConsole("Current minecraft time is " + minecraftTime);
                                }

                                for (TaskTime time : timer.getTimes()) {
                                    LocalTime current = LocalTime.parse(minecraftTime);

                                    if (time.getTime2() != null) {
                                        LocalTime startRange = time.getTime1();
                                        LocalTime endRange = time.getTime2();

                                        if (current.isAfter(startRange) && current.isBefore(endRange)) {
                                            shouldBlock = false;
                                        }
                                    } else if (minecraftTime.equals(time)) {
                                        shouldBlock = false;
                                    }
                                }
                            }
                        }

                        // Handle real world time
                        for (TaskTime time : timer.getTimes()) {
                            if (debug) {
                                Messages.sendConsole("Found time " + time + ", checking if execution is needed");
                            }

                            LocalTime current = LocalTime.now().withNano(0);

                            if (time.getTime2() != null) {
                                if (debug) {
                                    Messages.sendConsole("Found time range");
                                }

                                LocalTime startRange = time.getTime1();
                                LocalTime endRange = time.getTime2();

                                if (current.isAfter(startRange) && current.isBefore(endRange)) {
                                    if (debug) {
                                        Messages.sendConsole("Time is in range, continue processing...");
                                    }

                                    shouldBlock = false;
                                }
                            }

                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

                            if (debug) {
                                Messages.sendConsole("Time for timer is: " + time);
                                Messages.sendConsole("Current time is: " + current.format(formatter));
                            }

                            if (current.format(formatter).equals(time)) {
                                if (debug) {
                                    Messages.sendConsole("Times are equal, continue processing...");
                                }

                                shouldBlock = false;
                            }
                        }

                        if (shouldBlock) {
                            if (debug) {
                                Messages.sendConsole("Times are not equal, skipping");
                            }
                            continue;
                        }
                    }

                    // If timer has already been executed to much
                    if (timer.getExecutionLimit() != -1 && timer.getExecutionLimit() <= timer.getTimesExecuted()) {
                        if (debug) {
                            Messages.sendConsole("Timer execution limit reached");
                        }

                        continue;
                    }

                    LocalTime lastTimeExecuted = timer.getLastExecuted();
                    Duration secondsSinceLastExecution = Duration.between(lastTimeExecuted, LocalTime.now());

                    // If the last execution happened less that timer seconds ago

                    if (true) {
                        // if (secondsSinceLastExecution.getSeconds() < timer.getSeconds()) {
                        if (debug) {
                            Messages.sendConsole("Timer has been executed before");
                        }

                        continue;
                    }

                    if (!Tools.randomCheck(timer.getRandom())) {
                        if (debug) {
                            Messages.sendConsole("Timer has random value and didn't meet treshold");
                        }

                        continue;
                    }

                    // Gender timerGender = timer.getGender();
                    Gender timerGender = Gender.CONSOLE;
                    boolean selectRandomCommand = timer.getCommandExecutionMode().equals(CommandExecutionMode.RANDOM);
                    int selectedCommand = Tools.getRandomInt(0, timer.getCommands().size() - 1);

                    if (debug && selectRandomCommand) {
                        Messages.sendConsole("Timer has random command selection enabled");
                    }

                    if (timerGender.equals(Gender.CONSOLE)) {
                        int i = 0;
                        for (TaskCommand command : timer.getCommands()) {

                            if (selectRandomCommand && i != selectedCommand) {
                                i++;
                                continue;
                            }

                            // if (timer.getExecutePerUser()) {
                            if (true) {
                                for (Player p : Bukkit.getOnlinePlayers()) {
                                    if (timer.getRequiredPermission() != "" && p.hasPermission(timer.getRequiredPermission())) {
                                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                                                                           PAPIHook.parsePAPI(command.getCommand(), p));
                                    }
                                }
                            } else {
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                                                                   PAPIHook.parsePAPI(command.getCommand(), null));
                            }

                            i++;
                        }
                    } else if (timerGender.equals(Gender.PLAYER)) {
                        int i = 0;
                        for (TaskCommand command : timer.getCommands()) {
                            if (selectRandomCommand && i != selectedCommand) {
                                i++;
                                continue;
                            }

                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (!timer.getRequiredPermission().equals("") && p.hasPermission(timer.getRequiredPermission())) {
                                    p.performCommand(PAPIHook.parsePAPI(command.getCommand(), p));
                                }
                            }
                            i++;
                        }
                    } else if (timerGender.equals(Gender.OPERATOR)) {
                        int i = 0;
                        for (TaskCommand command : timer.getCommands()) {
                            if (selectRandomCommand && i != selectedCommand) {
                                i++;
                                continue;
                            }

                            for (Player p : Bukkit.getOnlinePlayers()) {
                                boolean isAlreadyOp = p.isOp();

                                try {

                                    if (!isAlreadyOp) {
                                        p.setOp(true);
                                    }

                                    p.performCommand(PAPIHook.parsePAPI(command.getCommand(), p));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    if (!isAlreadyOp) {
                                        p.setOp(false);
                                    }
                                }
                            }
                            i++;
                        }
                    }

                    final LocalTime lastExecuted = LocalTime.now();

                    timer.setLastExecuted(lastExecuted);
                    timer.setTimesExecuted(timer.getTimesExecuted() + 1);
                }
            }
        }, 1000);
    }
}
