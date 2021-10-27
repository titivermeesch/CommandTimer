package me.playbosswar.com.gui.tasks.scheduler;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.tasks.TaskInterval;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;

public class EditIntervalMenu implements InventoryProvider {
    public final SmartInventory INVENTORY;
    private final TaskInterval interval;

    public EditIntervalMenu(TaskInterval interval) {
        this.interval = interval;
        INVENTORY = SmartInventory.builder()
                .id("task-interval")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(5, 9)
                .title("§9§lTask interval")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        // Days
        contents.set(1, 1, ClickableItem.of(Items.getAddItem(), e -> {
            interval.incrementDays();
            refresh(player);
        }));
        contents.set(2, 1, ClickableItem.empty(Items.generateItem("§b" + interval.getDays() + " days", XMaterial.CLOCK)));
        contents.set(3, 1, ClickableItem.of(Items.getSubstractItem(), e -> {
            interval.decrementDays();
            refresh(player);
        }));

        // Hours
        contents.set(1, 3, ClickableItem.of(Items.getAddItem(), e -> {
            interval.incrementHours();
            refresh(player);
        }));
        contents.set(2, 3, ClickableItem.empty(Items.generateItem("§b" + interval.getHours() + " hours", XMaterial.CLOCK)));
        contents.set(3, 3, ClickableItem.of(Items.getSubstractItem(), e -> {
            interval.decrementHours();
            refresh(player);
        }));

        // Minutes
        contents.set(1, 5, ClickableItem.of(Items.getAddItem(), e -> {
            interval.incrementMinutes();
            refresh(player);
        }));
        contents.set(2, 5, ClickableItem.empty(Items.generateItem("§b" + interval.getMinutes() + " minutes", XMaterial.CLOCK)));
        contents.set(3, 5, ClickableItem.of(Items.getSubstractItem(), e -> {
            interval.decrementMinutes();
            refresh(player);
        }));

        // Seconds
        contents.set(1, 7, ClickableItem.of(Items.getAddItem(), e -> {
            interval.incrementSeconds();
            refresh(player);
        }));
        contents.set(2, 7, ClickableItem.empty(Items.generateItem("§b" + interval.getSeconds() + " seconds", XMaterial.CLOCK)));
        contents.set(3, 7, ClickableItem.of(Items.getSubstractItem(), e -> {
            interval.decrementSeconds();
            refresh(player);
        }));

        contents.set(4, 8, ClickableItem.of(Items.getBackItem(),
                                            e -> new MainScheduleMenu(interval.getTask()).INVENTORY.open(player)));

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private void refresh(Player player) { this.INVENTORY.open(player); }
}
