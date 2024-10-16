package me.playbosswar.com.utils;

import org.bukkit.World;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.time.*;
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

    private static String getTimeStringLegacy(int seconds, String format) {
        LocalDateTime local = LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds), ZoneId.of("GMT"));
        return local.format(DateTimeFormatter.ofPattern(format));
    }

    public static String getTimeString(int seconds, String format) {
        // If we don't have days, we don't need to handle it ourselves
        if(!format.contains("DD")) {
            return getTimeStringLegacy(seconds, format);
        }

        Duration duration = Duration.standardSeconds(seconds);

        int days = (int) duration.getStandardDays();
        int hours = (int) (duration.getStandardHours() % 48);
        int minutes = (int) (duration.getStandardMinutes() % 60);
        int remainingSeconds = (int) (duration.getStandardSeconds() % 60);

        return format
                .replace("DD", String.format("%02d", days))
                .replace("HH", String.format("%02d", hours))
                .replace("mm", String.format("%02d", minutes))
                .replace("ss", String.format("%02d", remainingSeconds));
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
