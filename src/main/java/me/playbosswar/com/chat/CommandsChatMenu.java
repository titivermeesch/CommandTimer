package me.playbosswar.com.chat;

import me.playbosswar.com.utils.CommandTimer;
import me.playbosswar.com.utils.TimerManager;
import me.playbosswar.com.utils.Messages;
import me.tom.sparse.spigot.chat.menu.ChatMenu;
import me.tom.sparse.spigot.chat.menu.element.BooleanElement;
import me.tom.sparse.spigot.chat.menu.element.ButtonElement;
import me.tom.sparse.spigot.chat.menu.element.InputElement;
import me.tom.sparse.spigot.chat.menu.element.TextElement;
import org.bukkit.entity.Player;

public class CommandsChatMenu {
    public static void openCommandsMenu(Player p, String timerName) {
        CommandTimer timer = TimerManager.getCommandTimer(timerName);
        ChatMenu menu = new ChatMenu().pauseChat();

        menu.add(new TextElement(Messages.colorize("&6&lTimer commands for: " + timer.getName()), 5, 1));

        int i = 3;
        for (String command : timer.getCommands()) {
            if(command.length() > 50) {
                command = command.substring(0, 50) + "...";
            }

            if (i < 17) {
                int finalI = i;
                menu.add(new ButtonElement(5, i, Messages.colorize("&4\u2718"), player -> {
                    TimerManager.removeCommandFromTimer(p, timer, finalI - 3);
                    menu.close(p);
                    openCommandsMenu(p, timerName);
                }));
                menu.add(new TextElement(command, 15, i));
                i++;
            }
        }

        menu.add(new TextElement("Select random command from list: ", 5, i + 1));
        BooleanElement selectRandomCommand = new BooleanElement(200, i + 1, timer.isSelectRandomCommand());
        selectRandomCommand.value.setChangeCallback(state -> TimerManager.changeCommandtimerData(p, timer.getName(), "selectRandomCommand", state.getCurrent().toString()));
        menu.add(selectRandomCommand);

        InputElement commandInput = new InputElement(5, i + 3, 120, "Enter new command");
        commandInput.value.setChangeCallback(state -> {
            TimerManager.addCommandToTimer(p, timer, state.getCurrent());
            menu.close(p);
            openCommandsMenu(p, timerName);
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
