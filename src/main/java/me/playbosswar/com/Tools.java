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
import java.util.*;

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
    static void complexCommandRunner(final String task, String command, final Gender gender) {
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

                    Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () -> Tools.executeCommand(task, command, gender), 50L);
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
        final boolean perUser = c.getBoolean("tasks." + task + ".perUser");
        final List<String> worlds = c.getStringList("tasks." + task + ".worlds");

        if(c.contains("tasks." + task + ".days") && !c.getStringList("tasks." + task + ".days").isEmpty()) {
            final LocalDate date = LocalDate.now();
            final DayOfWeek dow = date.getDayOfWeek();
            if(!c.getStringList("tasks." + task + ".days").contains(dow.toString())) {
                return;
            }
        }

        if (pl.getConfig().contains("tasks." + task + ".random")) {
            final double randomValue = c.getDouble("tasks." + task + ".random");
            if (!randomCheck(randomValue)) {
                return;
            }
        }

        if (gender.equals(Gender.CONSOLE)) {
            if (!perUser) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(cmd, null));
                return;
            }

            for (final Player p : Bukkit.getOnlinePlayers()) {
                String worldName = p.getWorld().getName();
                if(worlds.size() > 0 && !worlds.contains(worldName)) {
                    return;
                }

                if(perm == null) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(cmd, p));
                    return;
                }

                if (!p.hasPermission(perm)) {
                    continue;
                }

                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), PAPIHook.parsePAPI(cmd, p));
            }
        }

        if (gender.equals(Gender.OPERATOR)) {
            for (final Player p : Bukkit.getOnlinePlayers()) {
                String worldName = p.getWorld().getName();
                if(worlds.size() > 0 && !worlds.contains(worldName)) {
                    return;
                }

                final boolean isOp = p.isOp();

                try {
                    p.setOp(true);
                    p.performCommand(PAPIHook.parsePAPI(cmd, p));
                } finally {
                    if (!isOp) {
                        p.setOp(false);
                    }
                }
            }
        }

        if (gender.equals(Gender.PLAYER)) {
            for (final Player p : Bukkit.getOnlinePlayers()) {
                String worldName = p.getWorld().getName();
                if(worlds.size() > 0 && !worlds.contains(worldName)) {
                    return;
                }

                cmd = PAPIHook.parsePAPI(cmd, p);

                if (perm == null) {
                    p.performCommand(cmd);
                    continue;
                }

                if (!p.hasPermission(perm)) {
                    continue;
                }

                p.performCommand(cmd);
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
