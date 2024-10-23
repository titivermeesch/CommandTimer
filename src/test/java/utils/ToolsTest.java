package utils;

import me.playbosswar.com.utils.Tools;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ToolsTest {
    @Test
    public void shouldReturnCorrectTime1() {
        String result = Tools.getTimeString(59, "HH:mm:ss");
        assertEquals("00:00:59", result);
    }

    @Test
    public void shouldReturnCorrectTime2() {
        String result = Tools.getTimeString(60, "HH:mm:ss");
        assertEquals("00:01:00", result);
    }

    @Test
    public void shouldReturnCorrectTime3() {
        String result = Tools.getTimeString(80, "mm:ss");
        assertEquals("01:20", result);
    }

    @Test
    public void shouldReturnCorrectTime4() {
        String result = Tools.getTimeString(20, "DD:HH:mm:ss");
        assertEquals("00:00:00:20", result);
    }

    @Test
    public void shouldReturnCorrectTime5() {
        String result = Tools.getTimeString(9, "HH:mm:ss");
        assertEquals("00:00:09", result);
    }

    @Test
    public void shouldReturnCorrectTime6() {
        String result = Tools.getTimeString(69, "HH:mm:ss");
        assertEquals("00:01:09", result);
    }

    @Test
    public void shouldReturnCorrectTime7() {
        String result = Tools.getTimeString(3669, "HH:mm:ss");
        assertEquals("01:01:09", result);
    }

    @Test
    public void shouldReturnCorrectTime8() {
        String result = Tools.getTimeString(172800, "DD:HH:mm:ss");
        assertEquals("02:00:00:00", result);
    }

    @Test
    public void shouldReturnCorrectTime9() {
        String result = Tools.getTimeString(7200, "DD:HH:mm:ss");
        assertEquals("00:02:00:00", result);
    }

    @Test
    public void shouldReturnCorrectTime10() {
        String result = Tools.getTimeString(7271, "DD:HH:mm:ss");
        assertEquals("00:02:01:11", result);
    }

    @Test
    public void shouldReturnCorrectTime11() {
        String result = Tools.getTimeString(172871, "DD:HH:mm:ss");
        assertEquals("02:00:01:11", result);
    }

    @Test
    public void shouldReturnCorrectTime12() {
        String result = Tools.getTimeString(86400, "DD:HH:mm:ss");
        assertEquals("01:00:00:00", result);
    }
}
