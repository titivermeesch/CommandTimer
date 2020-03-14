package me.playbosswar.com;

import me.playbosswar.com.genders.GenderHandler.Gender;
import me.playbosswar.com.hooks.PAPIHook;
import org.apache.commons.lang.StringUtils;
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

class Tools {

    private Tools() {
        throw new AssertionError("Instantiating utility class.");
    }

    private static Plugin pl = Main.getPlugin();

    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static void sendConsole(String str) {
        Bukkit.getConsoleSender().sendMessage(color(str));
    }

    static void printDate() {
        final LocalDate date = LocalDate.now();
        final DayOfWeek dow = date.getDayOfWeek();
        final String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        sendConsole("&aServer time :&e " + time);
        sendConsole("&aServer day :&e " + dow);
    }

    static void initConfig() {
        pl.saveDefaultConfig();
        pl.getConfig().options().copyDefaults(false);
    }

    static void reloadTasks() {
        Bukkit.getScheduler().cancelTasks(Main.getPlugin());
        pl.reloadConfig();
        TaskRunner.startTasks();
    }


    static void easyCommandRunner(final String task, final long seconds, final Gender gender) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, new CommandTask(pl.getConfig()
                .getStringList("tasks." + task + ".commands"), gender, task), seconds, seconds);
    }

    static void simpleCommandRunner(final String task, final Gender gender) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> {
            for (final String next : pl.getConfig().getStringList("tasks." + task + ".commands")) {
                Tools.executeCommand(task, next, gender);
            }
        }, 50L);
    }

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


    static void executeCommand(final String task, String cmd, final Gender gender) {
        final FileConfiguration c = pl.getConfig();
        final String perm = c.getString("tasks." + task + ".permission");

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
                    if (cmd.contains("{player}")) {
                        cmd = StringUtils.replace(cmd, "{player}", p.getDisplayName());
                        cmd = PAPIHook.parsePAPI(cmd, p);
                    }

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
                final boolean isOp = p.isOp();

                try {
                    p.setOp(true);
                    cmd = PAPIHook.parsePAPI(cmd, p);
                    if (!c.contains("tasks." + task + ".random")) {
                        p.performCommand(cmd.replace("{player}", p.getDisplayName()));
                        return;
                    }

                    final double d = c.getDouble("tasks." + task + ".random");
                    if (randomCheck(d)) {
                        p.performCommand(cmd.replace("{player}", p.getDisplayName()));
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
                cmd = PAPIHook.parsePAPI(cmd, p);
                if (cmd.contains("{player}")) {
                    cmd = StringUtils.replace(cmd, "{player}", p.getDisplayName());
                }

                if (permission == null) {
                    p.performCommand(cmd.replace("{player}", p.getDisplayName()));
                    continue;
                }

                if (!p.hasPermission(permission)) {
                    continue;
                }

                if (pl.getConfig().contains("tasks." + task + ".random")) {
                    final double d = c.getDouble("tasks." + task + ".random");
                    if (randomCheck(d)) {
                        p.performCommand(cmd.replace("{player}", p.getDisplayName()));
                    }
                }
            }
        }
    }

    private static Random r = new Random();

    private static boolean randomCheck(double random) {
        final float chance = r.nextFloat();
        return chance <= random;
    }
}
