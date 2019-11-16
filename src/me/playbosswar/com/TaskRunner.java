package me.playbosswar.com;
import org.bukkit.configuration.file.FileConfiguration;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class TaskRunner {

    static FileConfiguration c = CommandTimer.getPlugin().getConfig();

    public static void startTasks() {
        Tools.cancelTasks(); //Cancel all tasks that may still be running
        if(c.contains("tasks")) { //Check if there are any tasks in the config file
            for (String task : c.getConfigurationSection("tasks").getKeys(false)) { //Loop throught all tasks that exist

                /* VARIABLE DECLARATION */
                long ticks = 20L * c.getLong("tasks." + task + ".seconds");
                LocalDate date = LocalDate.now();
                DayOfWeek dow = date.getDayOfWeek();
                String gender = Tools.getGender(task);

                if(c.getBoolean("tasks." + task + ".onday")) { //Checks if the onday function is enabled, if so, retreive date data and check if onhour is enabled too

                    if(c.getStringList("tasks." + task + ".days").contains(dow.toString())) { //Checks if the date today correspond
                        if (c.getBoolean("tasks." + task + ".onhour")) { //Once day correspond, checks if you have to execute it on hour too
                            Tools.complexCommandRunner(task, gender);
                        } else { //Check if it is onload and onday
                            if (c.getBoolean("tasks." + task + ".onload")) { //Check if onload
                                Tools.simpleCommandRunner(task, gender);
                            } else {
                                Tools.easyCommandRunner(task, ticks, gender);
                            }
                        }
                    }
                } else if (c.getBoolean("tasks." + task + ".onhour")) { //check if onhour only (not onday) {
                    Tools.complexCommandRunner(task, gender);
                } else {
                    if (c.getBoolean("tasks." + task + ".onload")) {
                        Tools.simpleCommandRunner(task, gender);
                    } else {
                        Tools.easyCommandRunner(task, ticks, gender);
                    }
                }
            }
        }
    }
}
