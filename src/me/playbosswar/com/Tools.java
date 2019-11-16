package me.playbosswar.com;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Tools {
	static FileConfiguration c = CommandTimer.getPlugin().getConfig();

	public static void printDate() {
		LocalDate date = LocalDate.now();
		DayOfWeek dow = date.getDayOfWeek();
		if (CommandTimer.getPlugin().getConfig().getBoolean("timeonload")) {
			Bukkit.getConsoleSender().sendMessage("§aServer time : " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
			Bukkit.getConsoleSender().sendMessage("§aServer day : " + dow);
		}
	}

	public static void initConfig() {
		CommandTimer.getPlugin().saveDefaultConfig();
		CommandTimer.getPlugin().getConfig().options().copyDefaults(false);
	}

	public static void registerEvents(Plugin plugin, Listener... listeners) {
		for (final Listener listener : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
		}
	}

	public static void registerBungeeChannels() {
		CommandTimer.getPlugin().getServer().getMessenger().registerOutgoingPluginChannel(CommandTimer.getPlugin(), "ct:ct");
	}

	public static void closeAllInventories() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.closeInventory();
		}
	}

	public static void reloadTaks() {
		Bukkit.getScheduler().cancelTasks(CommandTimer.getPlugin());
		TaskRunner.startTasks();
	}

	public static void cancelTasks() {
		CommandTimer.getPlugin().getServer().getScheduler().cancelTasks(CommandTimer.getPlugin());
	}

	public static String getGender(final String task) {
		String configGender = Tools.c.getString("tasks." + task + ".gender").toLowerCase();
		if (configGender.equals("player") || configGender.equals("console") || configGender.equals("operator")) {
			return configGender;
		}
		return null;
	}

	public static void easyCommandRunner(final String task, final long ticks, final String gender) {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(CommandTimer.getPlugin(), new CommandTask(Tools.c.getStringList("tasks." + task + ".commands"), gender, task), ticks, ticks);
	}

	public static void simpleCommandRunner(String task, String gender) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(CommandTimer.getPlugin(), new Runnable() {

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
						Bukkit.getScheduler().scheduleSyncDelayedTask(CommandTimer.getPlugin(), (Runnable)new Runnable() {
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
					if (Tools.c.getBoolean("tasks." + task + ".bungee")) {
						sendToBungee(cmd);
						return;
					}
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
				}
			}
			else {
				if (Tools.c.getBoolean("tasks." + task + ".bungee")) {
					sendToBungee(cmd);
					return;
				}
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
						if (Tools.c.getBoolean("tasks." + task + ".bungee")) {
							sendToBungee(cmd);
							return;
						}
						p.performCommand(cmd);
					}
					else {
						if (Tools.c.getBoolean("tasks." + task + ".bungee")) {
							sendToBungee(cmd);
							return;
						}
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
						if (Tools.c.getBoolean("tasks." + task + ".bungee")) {
							sendToBungee(cmd);
						}
						else {
							p2.performCommand(cmd);
						}
					}
					else {
						if (Tools.c.getBoolean("tasks." + task + ".bungee")) {
							sendToBungee(cmd);
							return;
						}
						p2.performCommand(cmd);
					}
				}
				else if (Tools.c.getBoolean("tasks." + task + ".useRandom")) {
					final double d2 = Tools.c.getDouble("tasks." + task + ".random");
					if (!randomCheck(d2)) {
						continue;
					}
					if (Tools.c.getBoolean("tasks." + task + ".bungee")) {
						sendToBungee(cmd);
					}
					else {
						p2.performCommand(cmd);
					}
				}
				else {
					if (Tools.c.getBoolean("tasks." + task + ".bungee")) {
						sendToBungee(cmd);
						return;
					}
					p2.performCommand(cmd);
				}
			}
		}
	}



	public static void sendToBungee(String cmd) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("command");
		out.writeUTF(cmd);
		if (!Bukkit.getOnlinePlayers().toString().equals("[]")) {
			Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
			player.sendPluginMessage(CommandTimer.getPlugin(), "ct:ct", out.toByteArray());
		}
	}

	public static boolean randomCheck(double random) {
		final Random r = new Random();
		final float chance = r.nextFloat();
		return chance <= random;
	}
}
