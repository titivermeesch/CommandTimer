package me.playbosswar.com;

import me.playbosswar.com.genders.GenderHandler.Gender;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Tools {
    public static Plugin pl = Main.getPlugin();

    public static void printDate() {
        final LocalDate date = LocalDate.now();
        final DayOfWeek dow = date.getDayOfWeek();
        final String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        Bukkit.getConsoleSender().sendMessage("§aServer time : " + time);
        Bukkit.getConsoleSender().sendMessage("§aServer day : " + dow);
    }

    public static void initConfig() {
        pl.saveDefaultConfig();
        pl.getConfig().options().copyDefaults(false);
    }

    public static void reloadTaks() {
        Bukkit.getScheduler().cancelTasks(Main.getPlugin());
        pl.reloadConfig();
        TaskRunner.startTasks();
    }


    public static void easyCommandRunner(final String task, final long seconds, final Gender gender) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, new CommandTask(pl.getConfig()
                .getStringList("tasks." + task + ".commands"), gender, task), seconds, seconds);
    }

    public static void simpleCommandRunner(final String task, final Gender gender) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> {
            for (final String next : pl.getConfig().getStringList("tasks." + task + ".commands")) {
                Tools.executeCommand(task, next, gender);
            }
        }, 50L);
    }

    public static void complexCommandRunner(final String task, final Gender gender) {
        final FileConfiguration c = pl.getConfig();

        if (!c.contains("tasks." + task + ".time")) {
            return;
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                final Date date = new Date();
                final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                final String formattedDate = dateFormat.format(date);

                for (final String hour : c.getStringList("tasks." + task + ".time")) {
                    if (!formattedDate.equals(hour)) {
                        continue;
                    }

                    Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () -> {
                        for (final String next : c.getStringList("tasks." + task + ".commands")) {
                            Tools.executeCommand(task, next, gender);
                        }
                    }, 50L);
                }
            }
        }, 1L, 1000L);
    }


    public static void executeCommand(String task, String cmd, Gender gender) {
        final FileConfiguration c = pl.getConfig();

        if (gender.equals(Gender.CONSOLE)) {
            if (!c.contains("tasks." + task + ".random")) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }

            final double d = c.getDouble("tasks." + task + ".random");
            if (randomCheck(d)) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
            return;
        }

        if (gender.equals(Gender.OPERATOR)) {
            for (final Player p : Bukkit.getOnlinePlayers()) {
                Boolean isOp = p.isOp();

                try {
                    p.setOp(true);
                    if (!c.contains("tasks." + task + ".random")) {
                        p.performCommand(cmd);
                        return;
                    }

                    final double d = c.getDouble("tasks." + task + ".random");
                    if (randomCheck(d)) {
                        p.performCommand(cmd);
                    }
                } finally {
                    if (!isOp) {
                        p.setOp(false);
                    }
                }
            }
        }

        if (gender.equals(Gender.PLAYER)) {
            final String perm = c.getString("tasks." + task + ".permission");

            for (final Player p : Bukkit.getOnlinePlayers()) {
                if (perm == null) {
                    p.performCommand(cmd);
                    continue;
                }

                if (!p.hasPermission(perm)) {
                    continue;
                }

                if (pl.getConfig().contains("tasks." + task + ".random")) {
                    final double d = c.getDouble("tasks." + task + ".random");
                    if (randomCheck(d)) {
                        p.performCommand(cmd);
                    }
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
