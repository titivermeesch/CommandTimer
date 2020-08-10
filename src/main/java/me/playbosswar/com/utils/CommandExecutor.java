package me.playbosswar.com.utils;

import me.playbosswar.com.Main;
import me.playbosswar.com.Tools;
import me.playbosswar.com.hooks.PAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class CommandExecutor {
    public static void startRunner() {
        BukkitRunnable timer = new BukkitRunnable() {
            @Override
            public void run() {
                ArrayList<CommandTimer> timers = CommandsManager.getAllTimers();

                for(CommandTimer timer : timers) {
                    DayOfWeek today = LocalDate.now().getDayOfWeek();

                    // Check if the command can be executed on this day
                    if(timer.getDays().size() > 0 && !timer.getDays().contains(today)) {
                        return;
                    }

                    // Check if the command can be executed at this time
                    if(timer.getTimes().size() > 0) {
                        Boolean shouldBlock = true;

                        // Handle minecraft world time
                        if(timer.getUseMinecraftTime()) {
                            for(String worldName : timer.getWorlds()) {
                                String minecraftTime = Tools.calculateWorldTime(Bukkit.getWorld(worldName));

                                for(String time : timer.getTimes()) {
                                    LocalTime current = LocalTime.parse(minecraftTime);

                                    if(time.contains("[")) {
                                        String[] hourRange = Tools.charRemoveAt(Tools.charRemoveAt(time, 0), time.length() - 2).split("-");

                                        LocalTime startRange = LocalTime.parse(hourRange[0]);
                                        LocalTime endRange = LocalTime.parse(hourRange[1]);

                                        if(current.isAfter(startRange) && current.isBefore(endRange)) {
                                            shouldBlock = false;
                                        }
                                    } else if(minecraftTime.equals(time)) {
                                        shouldBlock = false;
                                    }
                                }
                            }
                        }

                        // Handle real world time
                        for(String time : timer.getTimes()) {
                            LocalTime current = LocalTime.now();

                            if(time.contains("[")) {
                                String[] hourRange = Tools.charRemoveAt(Tools.charRemoveAt(time, 0), time.length() - 2).split("-");

                                LocalTime startRange = LocalTime.parse(hourRange[0]);
                                LocalTime endRange = LocalTime.parse(hourRange[1]);

                                if(current.isAfter(startRange) && current.isBefore(endRange)) {
                                    shouldBlock = false;
                                }
                            } else if(current.equals(time)) {
                                shouldBlock = false;
                            }
                        }

                        if(shouldBlock) {
                            return;
                        }
                    }

                    // If timer has already been executed to much
                    if(timer.getExecutionLimit() != -1 && timer.getExecutionLimit() >= timer.getTimesExecuted()) {
                        return;
                    }

                    LocalTime lastTimeExecuted = timer.getLastExecuted();
                    Duration secondsSinceLastExecution = Duration.between(lastTimeExecuted, LocalTime.now());

                    // If the last execution happened less that timer seconds ago

                    if(secondsSinceLastExecution.getSeconds() < timer.getSeconds()) {
                        return;
                    }

                    Gender timerGender = timer.getGender();

                    if(timerGender.equals(Gender.CONSOLE)) {
                        for(String command : timer.getCommands()) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(command, null));
                        }
                    }

                    timer.setLastExecuted(LocalTime.now());
                }
            }
        };

        timer.runTaskTimer(Main.getPlugin(), 20, 20);
    }
}
