package me.playbosswar.com.utils;

import me.tom.sparse.spigot.chat.menu.ChatMenu;
import me.tom.sparse.spigot.chat.menu.element.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatMenus {
    public static void openTimerMenu(Player p, String name) {
        CommandTimer timer = CommandsManager.getCommandTimer(name);

        if (timer == null) {
            Messages.sendMessageToPlayer(p, "&cWe could not find this timer");
            return;
        }

        ChatMenu menu = new ChatMenu();

        menu.add(new TextElement(Messages.colorize("&6&lTimer: " + timer.getName()), 5, 1));

        menu.add(new TextElement("Seconds: ", 5, 3));
        IncrementalElement secondsInc = new IncrementalElement(60, 3, 1, Integer.MAX_VALUE, timer.getSeconds());
        secondsInc.value.setChangeCallback(state -> {
            CommandsManager.changeCommandtimerData(p, timer.getName(), "seconds", state.getCurrent().toString());
        });
        menu.add(secondsInc);

        String[] genders = new String[3];

        genders[0] = "CONSOLE";
        genders[1] = "OPERATOR";
        genders[2] = "PLAYER";

        int defaultIndex = 0;
        if (timer.getGender().equals(Gender.OPERATOR)) {
            defaultIndex = 1;
        }
        if (timer.getGender().equals(Gender.PLAYER)) {
            defaultIndex = 2;
        }

        menu.add(new TextElement("Gender: ", 5, 5));
        menu.add(new VerticalSelectorElement(50, 5, defaultIndex, genders));

        menu.add(new TextElement("Execution change: ", 5, 8));
        menu.add(new NumberSliderElement(100, 8, 20, 5));

        menu.add(new TextElement("Execute for each user: ", 5, 9));
        menu.add(new BooleanElement(150, 9, timer.getExecutePerUser()));

        menu.add(new TextElement("Use Minecraft time: ", 5, 10));
        menu.add(new BooleanElement(150, 10, timer.getUseMinecraftTime()));

        menu.add(new ButtonElement(5, 12, Messages.colorize("&a&l[Commands]")));
        menu.add(new ButtonElement(100, 12, Messages.colorize("&a&l[Hours]")));


        menu.openFor(p);
    }
}
