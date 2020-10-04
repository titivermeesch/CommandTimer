package me.playbosswar.com.chat;

import me.playbosswar.com.utils.CommandTimer;
import me.playbosswar.com.utils.TimerManager;
import me.playbosswar.com.utils.Messages;
import me.tom.sparse.spigot.chat.menu.ChatMenu;
import me.tom.sparse.spigot.chat.menu.element.ButtonElement;
import me.tom.sparse.spigot.chat.menu.element.InputElement;
import me.tom.sparse.spigot.chat.menu.element.TextElement;
import org.bukkit.entity.Player;

public class TimesChatMenu {
    public static void openTimesMenu(Player p, String timerName) {
        CommandTimer timer = TimerManager.getCommandTimer(timerName);
        ChatMenu menu = new ChatMenu().pauseChat();

        menu.add(new TextElement(Messages.colorize("&6&lTimer times for: " + timer.getName()), 5, 1));

        int i = 3;
        for (String time : timer.getTimes()) {
            if (i < 17) {
                int finalI = i;
                menu.add(new ButtonElement(5, i, Messages.colorize("&4\u2718"), player -> {
                    TimerManager.removeTimeFromTimer(p, timer, finalI - 3);
                    menu.close(p);
                    openTimesMenu(p, timerName);
                }));
                menu.add(new TextElement(time, 15, i));
                i++;
            }
        }

        InputElement timeInput = new InputElement(5, i + 1, 120, "Enter new time");
        timeInput.value.setChangeCallback(state -> {
            TimerManager.addTimeToTimer(p, timer, state.getCurrent());
            menu.close(p);
            openTimesMenu(p, timerName);
        });

        menu.add(timeInput);
        menu.add(new ButtonElement(5, i + 3, Messages.colorize("&c[Go back]"), player -> {
            menu.close(player);
            ChatMenus.openTimerMenu(player, timer.getName());
        }));
        menu.add(new ButtonElement(60, i + 3, Messages.colorize("&4[Close]"), menu::close));

        menu.openFor(p);
    }
}
