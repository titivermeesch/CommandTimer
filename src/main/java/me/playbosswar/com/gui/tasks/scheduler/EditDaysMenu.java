package me.playbosswar.com.gui.tasks.scheduler;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.Main;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;

public class EditDaysMenu implements InventoryProvider {
    public final SmartInventory INVENTORY;
    private final Task task;

    public EditDaysMenu(Task task) {
        this.task = task;
        INVENTORY = SmartInventory.builder()
                .id("task-days")
                .provider(this)
                .manager(Main.getInventoryManager())
                .size(3, 9)
                .title("§9§lTask days")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));




        contents.set(2, 8, ClickableItem.of(Items.getBackItem(),
                                            e -> new MainScheduleMenu(task).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
