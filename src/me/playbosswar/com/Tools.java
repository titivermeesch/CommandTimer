package me.playbosswar.com;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class Tools {

	static FileConfiguration c = CommandTimer.getPlugin().getConfig();
	
	public static void printDate() { //Print the date
		LocalDate date = LocalDate.now();
		DayOfWeek dow = date.getDayOfWeek();
		if(CommandTimer.getPlugin().getConfig().getBoolean("timeonload")) {
			Bukkit.getConsoleSender().sendMessage("§aServer time : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
			Bukkit.getConsoleSender().sendMessage("§aServer day : " + dow);
		}
	}
	
	public static void initConfig() { //Load configuration file
		CommandTimer.getPlugin().saveDefaultConfig();
		CommandTimer.getPlugin().getConfig().options().copyDefaults(false);
	}
	
	public static void registerEvents(Plugin plugin, Listener... listeners) { //Register all events
		Listener[] arrayOfListener;
		int j = (arrayOfListener = listeners).length;
		for (int i = 0; i < j; i++) {
			Listener listener = arrayOfListener[i];
			Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
		}
	}
	
	public static void closeAllInventories() { //Close inventories to prevent errors
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.closeInventory();
		}
	}
	
	public static void reloadTaks() { //Reload all loaded tasks
		Bukkit.getScheduler().cancelTasks(CommandTimer.getPlugin());
		TaskRunner.startTasks();
	}

	public static void cancelTasks() { //Cancel all tasks
		CommandTimer.getPlugin().getServer().getScheduler().cancelTasks(CommandTimer.getPlugin());
	}

	public static String getGender(String task) { //Get how the command should be executed (OPERATOR, CONSOLE, PLAYER)
		String configGender = c.getString("settings.tasks." + task + ".gender").toLowerCase();
		if(configGender.equals("player") || configGender.equals("console") || configGender.equals("operator")) {
			return configGender;
		}
		return null;
	}

	public static void easyCommandRunner(String task, long ticks, String gender) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(CommandTimer.getPlugin(), new CommandTask(c.getStringList("settings.tasks." + task + ".commands"), gender, task), ticks, ticks);
	}

	public static void simpleCommandRunner(String task, String gender) {

		Bukkit.getScheduler().scheduleSyncDelayedTask(CommandTimer.getPlugin(), new Runnable() {
			@Override
			public void run() {
				for (String next : c.getStringList("settings.tasks." + task + ".commands")) { //Go through all commands
						executeCommand(task, next, gender);
				}
			}
		}, 50);
	}

	public static void complexCommandRunner(String task, String gender) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Date date = new Date();
				SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
				String formattedDate = df.format(date);

				for (String hour : c.getStringList("settings.tasks." + task + ".time")) {
					if (formattedDate.equals(hour)) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(CommandTimer.getPlugin(), new Runnable() {
							@Override
							public void run() {
								int i = c.getStringList("settings.tasks." + task + ".time").toArray().length;
								for (String next : c.getStringList("settings.tasks." + task + ".commands")) {
									if(i>0) {
											executeCommand(task, next, gender);
										i--;
									}
								}
							}
						}, 50);
					}
				}
			}
		}, 1, 1000);
	}

	public static void executeCommand(String task, String cmd, String gender) {
		if (gender.equals("console")) {
		    if(c.getDouble("settings.tasks." + task + ".random") != 0) {
		        double d = c.getDouble("settings.tasks." + task + ".random");
		        if(randomCheck(d)) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }
            }
		} else if (gender.equals("operator")) {
			for (Player p : Bukkit.getOnlinePlayers()) {
			    int i = 0;
			    if(p.isOp()) {
			        i = 1;
                }
				try {
					p.setOp(true);
					if(c.getString("settings.tasks." + task + ".random") != null) {
                        double d = c.getDouble("settings.tasks." + task + ".random");
                        if(randomCheck(d)) {
                            p.performCommand(cmd);
                        }
                    }
				} finally {
				    if(i == 0) {
                        p.setOp(false);
                    }
				}
			}
		} else if (gender.equals("player")) {
			String perm = c.getString("settings.tasks." + task + ".permission");
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (perm != null) {
					if (p.hasPermission(perm)) {
					    if(c.getDouble("settings.tasks." + task + ".random") != 0) {
                            double d = c.getDouble("settings.tasks." + task + ".random");
                            if(randomCheck(d)) {
                                p.performCommand(cmd);
                            }
                        }
					}
				}
			}
		}
	}

	public static boolean randomCheck(double random) {
	    Random r = new Random();
	    float chance = r.nextFloat();

	    if(chance <= random) {
            return true;
        }
        return false;
    }


}
