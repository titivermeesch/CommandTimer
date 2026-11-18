package me.playbosswar.com.utils;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;

public final class ServerTime {
    private static volatile Clock clock = Clock.systemDefaultZone();

    private ServerTime() {
    }

    public static void configure(FileConfiguration config, Logger logger) {
        clock = Clock.system(resolveZone(config.getBoolean("timezoneOverwrite"),
                config.getString("timezoneOverwriteValue"), logger));
    }

    static ZoneId resolveZone(boolean overwrite, String value, Logger logger) {
        if (!overwrite) {
            return ZoneId.systemDefault();
        }
        if (value == null || value.trim().isEmpty()) {
            logger.warning("timezoneOverwrite is enabled but timezoneOverwriteValue is empty, using system timezone");
            return ZoneId.systemDefault();
        }
        try {
            return ZoneId.of(value.trim(), ZoneId.SHORT_IDS);
        } catch (DateTimeException e) {
            logger.warning("Invalid timezoneOverwriteValue '" + value + "', using system timezone");
            return ZoneId.systemDefault();
        }
    }

    public static Clock getClock() {
        return clock;
    }

    public static ZoneId getZone() {
        return clock.getZone();
    }

    public static TimeZone getTimeZone() {
        return TimeZone.getTimeZone(getZone());
    }

    public static ZonedDateTime now() {
        return ZonedDateTime.now(clock);
    }
}
