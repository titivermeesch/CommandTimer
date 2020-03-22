package me.playbosswar.com;

import me.playbosswar.com.genders.GenderHandler.Gender;
import me.playbosswar.com.hooks.PAPIHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    private static Plugin pl = Main.getPlugin();
    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
    public static void sendConsole(String str) {
        Bukkit.getConsoleSender().sendMessage(color(str));
    }

    /**
     * Show current time & day
     */
    public static void printDate() {
        final LocalDate date = LocalDate.now();
        final DayOfWeek dow = date.getDayOfWeek();
        final String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        sendConsole("&aServer time :&e " + time);
        sendConsole("&aServer day :&e " + dow);
    }

    /**
     * Load configuration file
     */
    public static void initConfig() {
        pl.saveDefaultConfig();
        pl.getConfig().options().copyDefaults(false);
    }

    /**
     * Reload all plugin tasks
     */
    static void reloadTasks() {
        Bukkit.getScheduler().cancelTasks(Main.getPlugin());
        pl.reloadConfig();
        TaskRunner.startTasks();
    }

    /**
     * Check the time before executing the actual command
     */
    static void complexCommandRunner(final String task, final Gender gender) {
        final FileConfiguration c = pl.getConfig();

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


    /**
     * Execute a command based on permission, random, gender, days
     */
    static void executeCommand(final String task, String cmd, final Gender gender) {
        final FileConfiguration c = pl.getConfig();
        final String perm = c.getString("tasks." + task + ".permission");

        if(c.contains("tasks." + task + ".days") && !c.getStringList("tasks." + task + ".days").isEmpty()) {
            final LocalDate date = LocalDate.now();
            final DayOfWeek dow = date.getDayOfWeek();
            if(!c.getStringList("tasks." + task + ".days").contains(dow.toString())) {
                return;
            }
        }

        if (gender.equals(Gender.CONSOLE)) {
            if (perm == null) {
                cmd = PAPIHook.parsePAPI(cmd, null);
                if (!c.contains("tasks." + task + ".random")) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }

                final double d = c.getDouble("tasks." + task + ".random");
                if (randomCheck(d)) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
                }
            } else {
                for (final Player p : Bukkit.getOnlinePlayers()) {
                    cmd = PAPIHook.parsePAPI(cmd, p);

                    if (!p.hasPermission(perm)) {
                        continue;
                    }

                    if (pl.getConfig().contains("tasks." + task + ".random")) {
                        final double d = c.getDouble("tasks." + task + ".random");
                        if (randomCheck(d)) {
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
                        }
                    }
                }
            }
        }

        if (gender.equals(Gender.OPERATOR)) {
            for (final Player p : Bukkit.getOnlinePlayers()) {
                cmd = PAPIHook.parsePAPI(cmd, p);
                final boolean isOp = p.isOp();

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
            final String permission = c.getString("tasks." + task + ".permission");

            for (final Player p : Bukkit.getOnlinePlayers()) {
                Tools.sendConsole(cmd);
                cmd = PAPIHook.parsePAPI(cmd, p);
                Tools.sendConsole(PAPIHook.parsePAPI(cmd, p));


                if (permission == null) {
                    p.performCommand(cmd);
                    continue;
                }

                if (!p.hasPermission(permission)) {
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

    /**
     * Returns a boolean value based on the value
     * @param random - value between 0 and 1
     * @return boolean
     */
    private static boolean randomCheck(double random) {
        final Random r = new Random();
        final float chance = r.nextFloat();
        return chance <= random;
    }
}
