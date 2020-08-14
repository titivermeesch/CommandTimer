package me.playbosswar.com.chat;

import me.playbosswar.com.utils.CommandTimer;
import me.playbosswar.com.utils.CommandsManager;
import me.playbosswar.com.utils.Messages;
import me.tom.sparse.spigot.chat.menu.ChatMenu;
import me.tom.sparse.spigot.chat.menu.element.ButtonElement;
import me.tom.sparse.spigot.chat.menu.element.InputElement;
import me.tom.sparse.spigot.chat.menu.element.TextElement;
import org.bukkit.entity.Player;

public class CommandsChatMenu {
    public static void openCommandsMenu(Player p, String timerName) {
        CommandTimer timer = CommandsManager.getCommandTimer(timerName);
        ChatMenu menu = new ChatMenu().pauseChat();

        menu.add(new TextElement(Messages.colorize("&6&lTimer commands for: " + timer.getName()), 5, 1));

        int i = 3;
        for (String command : timer.getCommands()) {
            if (i < 17) {
                int finalI = i;
                menu.add(new ButtonElement(5, i, Messages.colorize("&4\u2718"), player -> {
                    CommandsManager.removeCommandFromTimer(p, timer, finalI - 3);
                    menu.close(p);
                    openCommandsMenu(p, timerName);
                }));
                menu.add(new TextElement(command, 15, i));
                i++;
            }
        }

        InputElement commandInput = new InputElement(5, i + 1, 120, "Enter new command");
        commandInput.value.setChangeCallback(state -> {
            CommandsManager.addCommandToTimer(p, timer, state.getCurrent());
            menu.close(p);
            openCommandsMenu(p, timerName);
        });

        menu.add(commandInput);
        menu.add(new ButtonElement(5, i + 3, Messages.colorize("&c[Go back]"), player -> {
            menu.close(player);
            ChatMenus.openTimerMenu(player, timer.getName());
        }));
        menu.add(new ButtonElement(60, i + 3, Messages.colorize("&4[Close]"), menu::close));

        menu.openFor(p);
    }
}
