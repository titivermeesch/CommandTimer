package me.playbosswar.com.gui.tasks;

import me.playbosswar.com.Main;
import me.playbosswar.com.tasks.Task;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

public class TaskNameMenu {
    public AnvilGUI.Builder INVENTORY;

    public TaskNameMenu(Player p, Task task) {
        INVENTORY = new AnvilGUI.Builder()
                .onComplete((player, text) -> {
                    task.setName(text);
                    new EditTaskMenu(task).INVENTORY.open(player);
                    return AnvilGUI.Response.close();
                })
                .text(task.getName())
                .title("Write a new task name")
                .plugin(Main.getPlugin());
    }
}
