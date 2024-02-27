package utils;

import me.playbosswar.com.utils.Tools;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ToolsTest {
    @Test
    public void shouldReturnCorrectPlayer1() {
        String result = Tools.getTimeString(59, "HH:mm:ss");
        assertEquals("00:00:59", result);
    }

    @Test
    public void shouldReturnCorrectPlayer2() {
        String result = Tools.getTimeString(60, "HH:mm:ss");
        assertEquals("00:01:00", result);
    }

    @Test
    public void shouldReturnCorrectPlayer3() {
        String result = Tools.getTimeString(80, "mm:ss");
        assertEquals("01:20", result);
    }

    @Test
    public void shouldReturnCorrectPlayer4() {
        String result = Tools.getTimeString(20, "DD:HH:mm:ss");
        assertEquals("00:00:00:20", result);
    }

    @Test
    public void shouldReturnCorrectPlayer5() {
        String result = Tools.getTimeString(9, "HH:mm:ss");
        assertEquals("00:00:09", result);
    }

    @Test
    public void shouldReturnCorrectPlayer6() {
        String result = Tools.getTimeString(69, "HH:mm:ss");
        assertEquals("00:01:09", result);
    }

    @Test
    public void shouldReturnCorrectPlayer7() {
        String result = Tools.getTimeString(3669, "HH:mm:ss");
        assertEquals("01:01:09", result);
    }
}
