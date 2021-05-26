package me.playbosswar.com.gui.tasks.general;

import me.playbosswar.com.gui.api.GenericTextMenu;
import me.playbosswar.com.tasks.Task;
import org.bukkit.entity.Player;

public class EditPermissionMenu extends GenericTextMenu {
    public EditPermissionMenu(Player player, Task task) {
        super(player, "Edit task permission", task.getRequiredPermission(), text -> {
            task.setRequiredPermission(text);
            new GeneralLimitsMenu(task).INVENTORY.open(player);
        });
    }
}
