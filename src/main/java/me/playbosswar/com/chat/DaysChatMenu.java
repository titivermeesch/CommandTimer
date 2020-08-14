package me.playbosswar.com.chat;

import me.playbosswar.com.utils.CommandTimer;
import me.playbosswar.com.utils.CommandsManager;
import me.playbosswar.com.utils.Messages;
import me.tom.sparse.spigot.chat.menu.ChatMenu;
import me.tom.sparse.spigot.chat.menu.element.BooleanElement;
import me.tom.sparse.spigot.chat.menu.element.ButtonElement;
import me.tom.sparse.spigot.chat.menu.element.TextElement;
import org.bukkit.entity.Player;

public class DaysChatMenu {
    public static void openDaysMenu(Player p, String timerName) {
        CommandTimer timer = CommandsManager.getCommandTimer(timerName);
        ChatMenu menu = new ChatMenu().pauseChat();

        menu.add(new TextElement(Messages.colorize("&6&lTimer days for: " + timer.getName()), 5, 1));

        menu.add(new TextElement(5,  3, "Monday"));
        menu.add(new BooleanElement(100, 3, timer.getDays().contains("MONDAY")));

        menu.add(new TextElement(5,  4, "Tuesday"));
        menu.add(new BooleanElement(100, 4, timer.getDays().contains("TUESDAY")));

        menu.add(new TextElement(5,  5, "Wednesday"));
        menu.add(new BooleanElement(100, 5, timer.getDays().contains("WEDNESDAY")));

        menu.add(new TextElement(5,  6, "Thursday"));
        menu.add(new BooleanElement(100, 6, timer.getDays().contains("THURSDAY")));

        menu.add(new TextElement(5,  7, "Friday"));
        menu.add(new BooleanElement(100, 7, timer.getDays().contains("FRIDAY")));

        menu.add(new TextElement(5,  8, "Saturday"));
        menu.add(new BooleanElement(100, 8, timer.getDays().contains("SATURDAY")));

        menu.add(new TextElement(5,  9, "Sunday"));
        menu.add(new BooleanElement(100, 9, timer.getDays().contains("SUNDAY")));

        menu.add(new ButtonElement(5, 11, Messages.colorize("&c[Go back]"), player -> {
            menu.close(player);
            ChatMenus.openTimerMenu(player, timer.getName());
        }));
        menu.add(new ButtonElement(60, 11, Messages.colorize("&4[Close]"), menu::close));

        menu.openFor(p);

    }
}
