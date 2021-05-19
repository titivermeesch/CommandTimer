package me.playbosswar.com.gui.tasks;

import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.Main;
import me.playbosswar.com.tasks.Task;
import org.bukkit.entity.Player;

public class MainCommandsMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final Task task;

    public MainCommandsMenu(Task task) {
        this.task = task;
        INVENTORY = SmartInventory.builder()
                .id("task-commands")
                .provider(this)
                .manager(Main.getInventoryManager())
                .size(3, 9)
                .title("§9§lTask commands")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {

    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
