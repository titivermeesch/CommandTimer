package me.playbosswar.com;

import me.playbosswar.com.hooks.PAPIHook;
import me.playbosswar.com.utils.Gender;
import me.playbosswar.com.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Tools {
    private static Plugin pl = Main.getPlugin();
    private static HashMap<String, Integer> tasksTimesExecuted = new HashMap<>();
    public static ArrayList<Timer> timerList = new ArrayList<>();



    /**
     * Show current time & day
     */
    public static void printDate() {
        final LocalDate date = LocalDate.now();
        final DayOfWeek dow = date.getDayOfWeek();
        final String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        Messages.sendConsole("&aServer time :&e " + time);
        Messages.sendConsole("&aServer day :&e " + dow);
    }

    public static String charRemoveAt(String str, int p) {
        return str.substring(0, p) + str.substring(p + 1);
    }

    /**
     * Get world time
     */
    public static String calculateWorldTime(World w) {
        long gameTime = w.getTime();
        long hours = gameTime / 1000 + 6;
        long minutes = (gameTime % 1000) * 60 / 1000;

        if (hours == 0) hours = 12;
        if (hours >= 24) hours -= 24;

        String mm = "0" + minutes;
        mm = mm.substring(mm.length() - 2);

        return hours + ":" + mm;
    }

    /**
     * Reload all plugin tasks
     */
    public static void reloadTasks() {
        Bukkit.getScheduler().cancelTasks(Main.getPlugin());
        pl.reloadConfig();
        TaskRunner.startTasks();
    }

    static void scheduleHourRange(String hour, String task, String command, Gender gender, String worldName) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () -> {
            String minecraftTime = Tools.calculateWorldTime(Bukkit.getWorld(worldName));
            String[] hourRange = charRemoveAt(charRemoveAt(hour, 0), hour.length() - 2).split("-");
            final boolean useMinecraftTime = Main.getPlugin().getConfig().getBoolean("tasks." + task + ".useMinecraftTime");
            final SimpleDateFormat timeFormat;

            if (useMinecraftTime) {
                timeFormat = new SimpleDateFormat("HH:mm");
            } else {
                timeFormat = new SimpleDateFormat("HH:mm:ss");
            }

            Date timeNow = null;
            Date startRange = null;
            Date endRange = null;

            try {
                if (useMinecraftTime) {
                    timeNow = timeFormat.parse(minecraftTime);
                } else {
                    timeNow = timeFormat.parse(timeFormat.format(new Date()));
                }

                startRange = timeFormat.parse(hourRange[0]);
                endRange = timeFormat.parse(hourRange[1]);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            assert timeNow != null;
            assert startRange != null;
            assert endRange != null;

            if (!timeNow.after(startRange) || !timeNow.before(endRange)) {
                return;
            }

            Tools.executeCommand(task, command, gender);
        }, Main.getPlugin().getConfig().getInt("tasks." + task + ".seconds") * 20L, Main.getPlugin().getConfig().getInt("tasks." + task + ".seconds") * 20L);
    }

    /**
     * Check the time before executing the actual command
     */
    static void complexCommandRunner(final String task, String command, final Gender gender) {
        final FileConfiguration c = pl.getConfig();
        boolean useMinecraftTime = c.getBoolean("tasks." + task + ".useMinecraftTime");
        Timer timer = new Timer();

        timerList.add(timer);
        tasksTimesExecuted.put(task, 0);

        if (useMinecraftTime) {
            for (String worldName : c.getStringList("tasks." + task + ".worlds")) {

                for (final String hour : c.getStringList("tasks." + task + ".time")) {
                    if (hour.contains("[")) {
                        scheduleHourRange(hour, task, command, gender, worldName);
                        return;
                    }

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            String minecraftTime = Tools.calculateWorldTime(Bukkit.getWorld(worldName));

                            if (!minecraftTime.equals(hour)) {
                                return;
                            }

                            int timesExecuted = tasksTimesExecuted.get(task);

                            if (c.contains("tasks." + task + ".executionLimit") && timesExecuted >= c.getInt("tasks." + task + ".executionLimit")) {
                                return;
                            }

                            tasksTimesExecuted.replace(task, ++timesExecuted);

                            if (c.contains("tasks." + task + ".seconds")) {
                                Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () -> Tools.executeCommand(task, command, gender), c.getInt("tasks." + task + ".seconds") * 20);
                                return;
                            }

                            Bukkit.getScheduler().scheduleSyncDelayedTask(pl, () -> Tools.executeCommand(task, command, gender), 1L);
                        }
                    }, 1L, 900L);
                }
            }
        } else {
            for (final String hour : c.getStringList("tasks." + task + ".time")) {
                if (hour.contains("[")) {
                    scheduleHourRange(hour, task, command, gender, "world");
                    return;
                }

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        final Date date = new Date();
                        final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                        final String formattedDate = dateFormat.format(date);

                        if (!formattedDate.equals(hour)) {
                            return;
                        }

                        int timesExecuted = tasksTimesExecuted.get(task);

                        if (c.contains("tasks." + task + ".executionLimit") && timesExecuted >= c.getInt("tasks." + task + ".executionLimit")) {
                            return;
                        }

                        tasksTimesExecuted.replace(task, ++timesExecuted);
                        Tools.executeCommand(task, command, gender);
                    }
                }, 1L, 1000L);
            }
        }
    }


    /**
     * Execute a command based on permission, random, gender, days
     */
    static void executeCommand(final String task, String cmd, final Gender gender) {
        final FileConfiguration c = pl.getConfig();
        final String perm = c.getString("tasks." + task + ".permission");
        final boolean perUser = c.getBoolean("tasks." + task + ".perUser");
        final List<String> worlds = c.getStringList("tasks." + task + ".worlds");
        final int playerCount = Bukkit.getOnlinePlayers().size();

        if(c.contains("tasks." + task + ".minPlayers")) {
            if(c.getInt("tasks." + task + ".minPlayers") > playerCount) {
                return;
            }
        }

        if(c.contains("tasks." + task + ".maxPlayers")) {
            if(c.getInt("tasks." + task + ".maxPlayers") < playerCount) {
                return;
            }
        }

        // Check if we are in a correct day.
        if (c.contains("tasks." + task + ".days") && !c.getStringList("tasks." + task + ".days").isEmpty()) {
            final LocalDate date = LocalDate.now();
            final DayOfWeek dow = date.getDayOfWeek();
            if (!c.getStringList("tasks." + task + ".days").contains(dow.toString())) {
                return;
            }
        }

        // Don't execute command if random failed
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
                if (worlds.size() > 0 && !worlds.contains(worldName)) {
                    return;
                }

                if (perm == null) {
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
                if (worlds.size() > 0 && !worlds.contains(worldName)) {
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
                if (worlds.size() > 0 && !worlds.contains(worldName)) {
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
     *
     * @param random - value between 0 and 1
     * @return boolean
     */
    private static boolean randomCheck(double random) {
        final Random r = new Random();
        final float chance = r.nextFloat();
        return chance <= random;
    }
}
