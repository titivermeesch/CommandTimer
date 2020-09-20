package me.playbosswar.com.chat;

import me.playbosswar.com.utils.CommandTimer;
import me.playbosswar.com.utils.CommandsManager;
import me.playbosswar.com.utils.Messages;
import me.tom.sparse.spigot.chat.menu.ChatMenu;
import me.tom.sparse.spigot.chat.menu.element.ButtonElement;
import me.tom.sparse.spigot.chat.menu.element.InputElement;
import me.tom.sparse.spigot.chat.menu.element.TextElement;
import org.bukkit.entity.Player;

public class WorldsMenu {
    public static void openWorldsMenu(Player p, String timerName) {
        CommandTimer timer = CommandsManager.getCommandTimer(timerName);
        ChatMenu menu = new ChatMenu().pauseChat();

        menu.add(new TextElement(Messages.colorize("&6&lTimer worlds for: " + timer.getName()), 5, 1));

        int i = 3;
        for (String world : timer.getWorlds()) {
            if(world.length() > 50) {
                world = world.substring(0, 50) + "...";
            }

            if (i < 17) {
                int finalI = i;
                menu.add(new ButtonElement(5, i, Messages.colorize("&4\u2718"), player -> {
                    CommandsManager.removeWorldFromTimer(p, timer, finalI - 3);
                    menu.close(p);
                    openWorldsMenu(p, timerName);
                }));
                menu.add(new TextElement(world, 15, i));
                i++;
            }
        }

        InputElement commandInput = new InputElement(5, i + 3, 120, "Enter new world");
        commandInput.value.setChangeCallback(state -> {
            CommandsManager.addWorldToTimer(p, timer, state.getCurrent());
            menu.close(p);
            openWorldsMenu(p, timerName);
        });

        menu.add(commandInput);
        menu.add(new ButtonElement(5, i + 5, Messages.colorize("&c[Go back]"), player -> {
            menu.close(player);
            ChatMenus.openTimerMenu(player, timer.getName());
        }));
        menu.add(new ButtonElement(60, i + 5, Messages.colorize("&4[Close]"), menu::close));

        menu.openFor(p);
    }
}
