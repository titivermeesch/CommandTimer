package me.playbosswar.com.gui.tasks.commands;

import me.playbosswar.com.Main;
import me.playbosswar.com.tasks.TaskCommand;
import net.wesjd.anvilgui.AnvilGUI;

public class EditCommandNameMenu {
    public final AnvilGUI.Builder INVENTORY;

    public EditCommandNameMenu(TaskCommand command) {
        INVENTORY = new AnvilGUI.Builder()
                .onComplete((player, text) -> {
                    command.setCommand(text);
                    new EditCommandMenu(command).INVENTORY.open(player);
                    return AnvilGUI.Response.close();
                })
                .text(command.getCommand())
                .title("Choose your command")
                .plugin(Main.getPlugin());
    }
}
