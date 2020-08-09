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

        ChatMenu menu = new ChatMenu().pauseChat();

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
        VerticalSelectorElement genderSelector = new VerticalSelectorElement(50, 5, defaultIndex, genders);
        genderSelector.value.setChangeCallback(state -> {
            CommandsManager.changeCommandtimerData(p, timer.getName(), "gender", genders[state.getCurrent()]);
        });
        menu.add(genderSelector);

        menu.add(new TextElement("Execution chance: ", 5, 8));
        NumberSliderElement executionSlider = new NumberSliderElement(100, 8, 20, 5);
        executionSlider.value.setChangeCallback(state -> {
            int value = state.getCurrent() + 1;

            double pourcentage = value * 0.05;
            
            CommandsManager.changeCommandtimerData(p, timer.getName(), "random", pourcentage + "");
        });
        menu.add(executionSlider);

        menu.add(new TextElement("Execute for each user: ", 5, 9));
        menu.add(new BooleanElement(150, 9, timer.getExecutePerUser()));

        menu.add(new TextElement("Use Minecraft time: ", 5, 10));
        menu.add(new BooleanElement(150, 10, timer.getUseMinecraftTime()));

        menu.add(new ButtonElement(5, 12, Messages.colorize("&a&l[Commands]"), player -> {
            menu.close(player);
            openCommandsMenu(player, timer);
        }));
        menu.add(new ButtonElement(100, 12, Messages.colorize("&a&l[Hours]")));


        menu.openFor(p);
    }

    public static void openCommandsMenu(Player p, CommandTimer timer) {
        ChatMenu menu = new ChatMenu().pauseChat();

        menu.add(new TextElement(Messages.colorize("&6&lTimer commands for: " + timer.getName()), 5, 1));

        int i = 3;
        for(String command : timer.getCommands()) {
            if(i < 17) {
                menu.add(new TextElement(" - " + command, 5, i));
                i++;
            }
        }

        InputElement commandInput = new InputElement(5, i, 200, "Enter new command");
        commandInput.value.setChangeCallback(state -> {
            CommandsManager.addCommandToTimer(p, timer, state.getCurrent());
            menu.close(p);
            menu.openFor(p);
        });
        menu.add(commandInput);
        menu.add(new ButtonElement(5, i + 1, Messages.colorize("&c[Go back]"), player -> {
            menu.close(player);
            openTimerMenu(player, timer.getName());
        }));
        menu.add(new ButtonElement(100, i + 1, Messages.colorize("&4[Close]"), menu::close));

        menu.openFor(p);
    }
}
