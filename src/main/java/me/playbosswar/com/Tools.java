package me.playbosswar.com;

import me.playbosswar.com.utils.*;
import org.bukkit.World;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

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
     *
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
        long gameTime = w.getTime();
        long hours = gameTime / 1000 + 6;
        long minutes = (gameTime % 1000) * 60 / 1000;

        if (hours == 0) {
            hours = 12;
        }
        if (hours >= 24) {
            hours -= 24;
        }

        String mm = "0" + minutes;
        mm = mm.substring(mm.length() - 2);

        String realHours = String.valueOf(hours);
        if (hours < 10) {
            realHours = "0" + realHours;
        }

        return realHours + ":" + mm;
    }

    public static String getTimeString(int seconds) {
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        int s = seconds % 60;
        String sh = (h > 0 ? h + " " + "h" : "");
        String sm = (m < 10 && m > 0 && h > 0 ? "0" : "") + (m > 0 ? (h > 0 && s == 0 ? String.valueOf(m) : m + " " + "min") : "");
        String ss = (s == 0 && (h > 0 || m > 0) ? "" : (s < 10 && (h > 0 || m > 0) ? "0" : "") + s + " " + "sec");
        return sh + (h > 0 ? " " : "") + sm + (m > 0 ? " " : "") + ss;
    }
}
