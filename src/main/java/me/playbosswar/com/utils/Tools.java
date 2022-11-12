package me.playbosswar.com.utils;

import org.bukkit.World;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
        return df.format(d);
    }

    private static String getTenthNumeric(long val) {
        if(val < 9) {
            return "0" + val;
        }

        return val + "";
    }

    public static String getTimeString(int seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24L);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) * 60);

        if(day > 0) {
            return getTenthNumeric(day) + ":" + getTenthNumeric(hours) + ":" + getTenthNumeric(minute) + ":" + getTenthNumeric(second);
        }

        return getTenthNumeric(hours) + ":" + getTenthNumeric(minute) + ":" + getTenthNumeric(second);
    }
}
