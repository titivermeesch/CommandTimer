package me.playbosswar.com.utils;

import me.playbosswar.com.tasks.TaskInterval;
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
        int hours = (int) (duration.getStandardHours() % 24);
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

    public static TaskInterval parseTimeString(String timeString) {
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        String remaining = timeString.toLowerCase();
        
        if (remaining.contains("d")) {
            int dIndex = remaining.indexOf("d");
            int dEnd = dIndex;
            while (dEnd > 0 && Character.isDigit(remaining.charAt(dEnd - 1))) {
                dEnd--;
            }
            if (dEnd < dIndex) {
                days = Integer.parseInt(remaining.substring(dEnd, dIndex));
                remaining = remaining.substring(0, dEnd) + remaining.substring(dIndex + 1);
            }
        }
        
        if (remaining.contains("h")) {
            int hIndex = remaining.indexOf("h");
            int hEnd = hIndex;
            while (hEnd > 0 && Character.isDigit(remaining.charAt(hEnd - 1))) {
                hEnd--;
            }
            if (hEnd < hIndex) {
                hours = Integer.parseInt(remaining.substring(hEnd, hIndex));
                remaining = remaining.substring(0, hEnd) + remaining.substring(hIndex + 1);
            }
        }
        
        if (remaining.contains("m")) {
            int mIndex = remaining.indexOf("m");
            int mEnd = mIndex;
            while (mEnd > 0 && Character.isDigit(remaining.charAt(mEnd - 1))) {
                mEnd--;
            }
            if (mEnd < mIndex) {
                minutes = Integer.parseInt(remaining.substring(mEnd, mIndex));
                remaining = remaining.substring(0, mEnd) + remaining.substring(mIndex + 1);
            }
        }
        
        if (remaining.contains("s")) {
            int sIndex = remaining.indexOf("s");
            int sEnd = sIndex;
            while (sEnd > 0 && Character.isDigit(remaining.charAt(sEnd - 1))) {
                sEnd--;
            }
            if (sEnd < sIndex) {
                seconds = Integer.parseInt(remaining.substring(sEnd, sIndex));
            }
        }

        return new TaskInterval(days, hours, minutes, seconds);
    }

    public static ZonedDateTime getNextMinecraftTime(World world, LocalTime targetMcTime, int occurrence) {
        long currentTicks = world.getTime();
        long targetTicks = minecraftTimeToTicks(targetMcTime);

        long ticksUntilTarget = targetTicks - currentTicks;
        if (ticksUntilTarget < 0) {
            ticksUntilTarget += 24000;
        }

        ticksUntilTarget += occurrence * 24000;

        long realTimeSeconds = (long) (ticksUntilTarget * 0.05);
        return ZonedDateTime.now().plusSeconds(realTimeSeconds);
    }

    public static LocalTime getMinecraftTimeAt(World world, ZonedDateTime realTime) {
        long timeDiff = java.time.Duration.between(ZonedDateTime.now(), realTime).getSeconds();
        long ticksDiff = (long) (timeDiff / 0.05);
        long futureTicks = (world.getTime() + ticksDiff) % 24000;

        long hours = (futureTicks / 1000 + 6) % 24;
        long minutes = (futureTicks % 1000) * 60 / 1000;

        return LocalTime.of((int) hours, (int) minutes);
    }

    public static long minecraftTimeToTicks(LocalTime mcTime) {
        int hours = mcTime.getHour();
        int minutes = mcTime.getMinute();

        if (hours < 6) {
            hours += 24;
        }

        long ticks = (hours - 6) * 1000L + (minutes * 1000L / 60);
        return ticks % 24000;
    }
}
