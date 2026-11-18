package me.playbosswar.com.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.ZoneId;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerTimeTest {
    private static final Logger LOGGER = Logger.getLogger("ServerTimeTest");

    @AfterEach
    void reset() {
        ServerTime.configure(config(false, null), LOGGER);
    }

    private static FileConfiguration config(boolean overwrite, String value) {
        FileConfiguration config = Mockito.mock(FileConfiguration.class);
        Mockito.when(config.getBoolean("timezoneOverwrite")).thenReturn(overwrite);
        Mockito.when(config.getString("timezoneOverwriteValue")).thenReturn(value);
        return config;
    }

    @Test
    void configure_appliesOverrideToClockAndTimeZone() {
        ServerTime.configure(config(true, "Asia/Tokyo"), LOGGER);

        assertEquals(ZoneId.of("Asia/Tokyo"), ServerTime.getZone());
        assertEquals(ZoneId.of("Asia/Tokyo"), ServerTime.now().getZone());
        assertEquals("Asia/Tokyo", ServerTime.getTimeZone().getID());
    }

    @Test
    void configure_withoutOverrideUsesSystemZone() {
        ServerTime.configure(config(false, "Asia/Tokyo"), LOGGER);

        assertEquals(ZoneId.systemDefault(), ServerTime.getZone());
    }

    @Test
    void disabled_usesSystemZone() {
        assertEquals(ZoneId.systemDefault(), ServerTime.resolveZone(false, "Asia/Tokyo", LOGGER));
    }

    @Test
    void enabled_usesConfiguredZone() {
        assertEquals(ZoneId.of("Asia/Tokyo"), ServerTime.resolveZone(true, "Asia/Tokyo", LOGGER));
    }

    @Test
    void enabled_acceptsDefaultGmtValue() {
        assertEquals(ZoneId.of("GMT"), ServerTime.resolveZone(true, "GMT", LOGGER));
    }

    @Test
    void enabled_acceptsLegacyShortIds() {
        assertEquals(ZoneId.of("America/Los_Angeles"), ServerTime.resolveZone(true, "PST", LOGGER));
    }

    @Test
    void enabled_invalidValueFallsBackToSystemZone() {
        assertEquals(ZoneId.systemDefault(), ServerTime.resolveZone(true, "Not/AZone", LOGGER));
    }

    @Test
    void enabled_emptyValueFallsBackToSystemZone() {
        assertEquals(ZoneId.systemDefault(), ServerTime.resolveZone(true, "", LOGGER));
        assertEquals(ZoneId.systemDefault(), ServerTime.resolveZone(true, null, LOGGER));
    }
}
