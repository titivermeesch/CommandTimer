package me.playbosswar.com.gui.tasks.commands;

import me.playbosswar.com.gui.api.GenericTextMenu;
import me.playbosswar.com.tasks.TaskCommand;
import org.bukkit.entity.Player;

public class EditCommandNameMenu extends GenericTextMenu {
    public EditCommandNameMenu(Player player, TaskCommand command) {
        super(player, "Choose your command", command.getCommand(), text -> {
            command.setCommand(text);
            new EditCommandMenu(command).INVENTORY.open(player);
        });
    }


}
