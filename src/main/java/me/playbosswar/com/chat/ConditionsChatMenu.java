package me.playbosswar.com.chat;

import me.playbosswar.com.utils.CommandTimer;
import me.playbosswar.com.utils.TimerManager;
import me.playbosswar.com.utils.Messages;
import me.tom.sparse.spigot.chat.menu.ChatMenu;
import me.tom.sparse.spigot.chat.menu.element.ButtonElement;
import me.tom.sparse.spigot.chat.menu.element.IncrementalElement;
import me.tom.sparse.spigot.chat.menu.element.TextElement;
import org.bukkit.entity.Player;

public class ConditionsChatMenu {
    public static void openConditionsMenu(Player p, String timerName) {
        CommandTimer timer = TimerManager.getCommandTimer(timerName);
        ChatMenu menu = new ChatMenu().pauseChat();

        menu.add(new TextElement(Messages.colorize("&6&lTimer conditions for: " + timer.getName()), 5, 1));

        menu.add(new TextElement("Min players: ", 5, 3));
        IncrementalElement minPlayers = new IncrementalElement(90, 3, -1, Integer.MAX_VALUE, timer.getMinPlayers());
        minPlayers.value.setChangeCallback(state -> {
            TimerManager.changeCommandtimerData(p, timer.getName(), "minPlayers", state.getCurrent().toString());
        });
        menu.add(minPlayers);

        menu.add(new TextElement("Max players: ", 5, 4));
        IncrementalElement maxPlayers = new IncrementalElement(90, 4, -1, Integer.MAX_VALUE, timer.getMaxPlayers());
        maxPlayers.value.setChangeCallback(state -> {
            TimerManager.changeCommandtimerData(p, timer.getName(), "maxPlayers", state.getCurrent().toString());
        });
        menu.add(maxPlayers);

        menu.add(new ButtonElement(5, 11, Messages.colorize("&c[Go back]"), player -> {
            menu.close(player);
            ChatMenus.openTimerMenu(player, timer.getName());
        }));
        menu.add(new ButtonElement(60, 11, Messages.colorize("&4[Close]"), menu::close));

        menu.openFor(p);
    }
}
