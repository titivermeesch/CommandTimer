package me.playbosswar.com.gui.tasks;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.Main;
import me.playbosswar.com.tasks.Task;
import org.bukkit.entity.Player;

public class MainScheduleMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final Task task;

    public MainScheduleMenu(Task task) {
        this.task = task;
        INVENTORY = SmartInventory.builder()
                .id("task-scheduler")
                .provider(this)
                .manager(Main.getInventoryManager())
                .size(3, 9)
                .title("§9§lTask scheduler")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.ORANGE_STAINED_GLASS_PANE.parseItem()));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
