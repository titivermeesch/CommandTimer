package me.playbosswar.com.utils;

import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.utils.*;
import org.bukkit.World;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class Tools {
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

    /**
     * Returns a boolean value based on the value
     *
     * @param random - value between 0 and 1
     * @return boolean
     */
    public static boolean randomCheck(double random) {
        final Random r = new Random();
        final float chance = r.nextFloat();
        return chance <= random;
    }

    public static int getRandomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    /**
     * Get world time
     */
    public static String calculateWorldTime(World w) {
        if(w == null) {
            return "00:00";
        }

        long gameTime = w.getTime();
        long hours = gameTime / 1000 + 6;
        long minutes = (gameTime % 1000) * 60 / 1000;

        if(hours == 0) {
            hours = 12;
        }
        if(hours >= 24) {
            hours -= 24;
        }

        String mm = "0" + minutes;
        mm = mm.substring(mm.length() - 2);

        String realHours = String.valueOf(hours);
        if(hours < 10) {
            realHours = "0" + realHours;
        }

        return realHours + ":" + mm;
    }

    public static String getTimeString(int seconds, String format) {
        Date d = new Date(seconds * 1000L);
        SimpleDateFormat df = new SimpleDateFormat(format);
        Calendar now = Calendar.getInstance();
        now.getTimeZone().setRawOffset(0);
        TimeZone timezone = now.getTimeZone();
        if(CommandTimerPlugin.getPlugin().getConfig().getBoolean("timezoneOverwrite")) {
            timezone = TimeZone.getTimeZone(CommandTimerPlugin.getPlugin().getConfig().getString(
                    "timezoneOverwriteValue"));
        }
        df.setTimeZone(timezone);
        return df.format(d);
    }

    public static String getTimeString(int seconds) {
        if(seconds >= 86400) {
            return getTimeString(seconds, "dd:HH:mm:ss");
        }

        return getTimeString(seconds, "HH:mm:ss");
    }
}
