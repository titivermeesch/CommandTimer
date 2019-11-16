package me.playbosswar.com;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Tools {
	static FileConfiguration c = Main.getPlugin().getConfig();

	public static void printDate() {
		LocalDate date = LocalDate.now();
		DayOfWeek dow = date.getDayOfWeek();
		if (Main.getPlugin().getConfig().getBoolean("timeonload")) {
			Bukkit.getConsoleSender().sendMessage("§aServer time : " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
			Bukkit.getConsoleSender().sendMessage("§aServer day : " + dow);
		}
	}

	public static void initConfig() {
		Main.getPlugin().saveDefaultConfig();
		Main.getPlugin().getConfig().options().copyDefaults(false);
	}

	public static void reloadTaks() {
		Bukkit.getScheduler().cancelTasks(Main.getPlugin());
		TaskRunner.startTasks();
	}

	public static void cancelTasks() {
		Main.getPlugin().getServer().getScheduler().cancelTasks(Main.getPlugin());
	}

	public static String getGender(final String task) {
		String configGender = Tools.c.getString("tasks." + task + ".gender").toLowerCase();
		if (configGender.equals("player") || configGender.equals("console") || configGender.equals("operator")) {
			return configGender;
		}
		return null;
	}

	public static void easyCommandRunner(final String task, final long ticks, final String gender) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new CommandTask(Tools.c.getStringList("tasks." + task + ".commands"), gender, task), ticks, ticks);
	}

	public static void simpleCommandRunner(String task, String gender) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {

			@Override
			public void run() {
				for (String next : Tools.c.getStringList("tasks." + task + ".commands")) {
					Tools.executeCommand(task, next, gender);
				}
			}
		}, 50L);
	}

	public static void complexCommandRunner(final String task, final String gender) {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				final Date date = new Date();
				final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
				final String formattedDate = df.format(date);
				for (final String hour : Tools.c.getStringList("tasks." + task + ".time")) {
					if (formattedDate.equals(hour)) {
						Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), (Runnable)new Runnable() {
							@Override
							public void run() {
								int i = Tools.c.getStringList("tasks." + task + ".time").toArray().length;
								for (final String next : Tools.c.getStringList("tasks." + task + ".commands")) {
									if (i > 0) {
										Tools.executeCommand(task, next, gender);
										--i;
									}
								}
							}
						}, 50L);
					}
				}
			}
		}, 1L, 1000L);
	}


	public static void executeCommand(String task, String cmd, String gender) {
		if (gender.equals("console")) {
			if (Tools.c.getBoolean("tasks." + task + ".useRandom")) {
				final double d = Tools.c.getDouble("tasks." + task + ".random");
				if (randomCheck(d)) {
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
				}
			}
			else {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
			}
		}
		else if (gender.equals("operator")) {
			for (final Player p : Bukkit.getOnlinePlayers()) {
				int i = 0;
				if (p.isOp()) {
					i = 1;
				}
				try {
					p.setOp(true);
					if (Tools.c.getBoolean("tasks." + task + ".useRandom")) {
						final double d2 = Tools.c.getDouble("tasks." + task + ".random");
						if (!randomCheck(d2)) {
							continue;
						}
						p.performCommand(cmd);
					}
					else {
						p.performCommand(cmd);
					}
				}
				finally {
					if (i == 0) {
						p.setOp(false);
					}
				}
			}
		}
		else if (gender.equals("player")) {
			final String perm = Tools.c.getString("tasks." + task + ".permission");
			for (final Player p2 : Bukkit.getOnlinePlayers()) {
				if (perm != null) {
					if (!p2.hasPermission(perm)) {
						continue;
					}
					if (Tools.c.getBoolean("tasks." + task + ".useRandom")) {
						final double d2 = Tools.c.getDouble("tasks." + task + ".random");
						if (!randomCheck(d2)) {
							continue;
						}
						else {
							p2.performCommand(cmd);
						}
					}
					else {
						p2.performCommand(cmd);
					}
				}
				else if (Tools.c.getBoolean("tasks." + task + ".useRandom")) {
					final double d2 = Tools.c.getDouble("tasks." + task + ".random");
					if (!randomCheck(d2)) {
						continue;
					}
					else {
						p2.performCommand(cmd);
					}
				}
				else {
					p2.performCommand(cmd);
				}
			}
		}
	}

	public static boolean randomCheck(double random) {
		final Random r = new Random();
		final float chance = r.nextFloat();
		return chance <= random;
	}
}
