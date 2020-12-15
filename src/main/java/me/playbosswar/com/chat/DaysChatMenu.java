package me.playbosswar.com.chat;

import me.playbosswar.com.utils.CommandTimer;
import me.playbosswar.com.utils.TimerManager;
import me.playbosswar.com.utils.Messages;
import me.playbosswar.com.chat.api.menu.ChatMenu;
import me.playbosswar.com.chat.api.menu.element.BooleanElement;
import me.playbosswar.com.chat.api.menu.element.ButtonElement;
import me.playbosswar.com.chat.api.menu.element.TextElement;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class DaysChatMenu {
    public static void openDaysMenu(Player p, String timerName) {
        CommandTimer timer = TimerManager.getCommandTimer(timerName);
        ChatMenu menu = new ChatMenu().pauseChat();

        menu.add(new TextElement(Messages.colorize("&6&lTimer days for: " + timer.getName()), 5, 1));

        menu.add(new TextElement(5,  3, "Monday"));
        BooleanElement monday = new BooleanElement(100, 3, timer.getDays().contains("MONDAY"));
        monday.value.setChangeCallback(s -> handleDayChange(timer, "MONDAY"));
        menu.add(monday);

        menu.add(new TextElement(5,  4, "Tuesday"));
        BooleanElement tuesday = new BooleanElement(100, 4, timer.getDays().contains("TUESDAY"));
        tuesday.value.setChangeCallback(s -> handleDayChange(timer, "TUESDAY"));
        menu.add(tuesday);

        menu.add(new TextElement(5,  5, "Wednesday"));
        BooleanElement wednesday = new BooleanElement(100, 5, timer.getDays().contains("WEDNESDAY"));
        wednesday.value.setChangeCallback(s -> handleDayChange(timer, "WEDNESDAY"));
        menu.add(wednesday);

        menu.add(new TextElement(5,  6, "Thursday"));
        BooleanElement thursday = new BooleanElement(100, 6, timer.getDays().contains("THURSDAY"));
        thursday.value.setChangeCallback(s -> handleDayChange(timer, "THURSDAY"));
        menu.add(thursday);

        menu.add(new TextElement(5,  7, "Friday"));
        BooleanElement friday = new BooleanElement(100, 7, timer.getDays().contains("FRIDAY"));
        friday.value.setChangeCallback(s -> handleDayChange(timer, "FRIDAY"));
        menu.add(friday);

        menu.add(new TextElement(5,  8, "Saturday"));
        BooleanElement saturday = new BooleanElement(100, 8, timer.getDays().contains("SATURDAY"));
        saturday.value.setChangeCallback(s -> handleDayChange(timer, "SATURDAY"));
        menu.add(saturday);

        menu.add(new TextElement(5,  9, "Sunday"));
        BooleanElement sunday = new BooleanElement(100, 9, timer.getDays().contains("SUNDAY"));
        sunday.value.setChangeCallback(s -> handleDayChange(timer, "SUNDAY"));
        menu.add(sunday);

        menu.add(new ButtonElement(5, 11, Messages.colorize("&c[Go back]"), player -> {
            menu.close(player);
            ChatMenus.openTimerMenu(player, timer.getName());
        }));
        menu.add(new ButtonElement(60, 11, Messages.colorize("&4[Close]"), menu::close));

        menu.openFor(p);
    }

    private static void handleDayChange(CommandTimer timer, String toggledDay) {
        ArrayList<String> days = timer.getDays();

        if(days.contains(toggledDay)) {
            days.remove(toggledDay);
        } else {
            days.add(toggledDay);
        }

        timer.setDays(days);
    }
}
