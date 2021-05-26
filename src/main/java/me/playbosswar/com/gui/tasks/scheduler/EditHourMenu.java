package me.playbosswar.com.gui.tasks.scheduler;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.Main;
import me.playbosswar.com.tasks.TaskTime;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;

import java.time.LocalTime;

public class EditHourMenu implements InventoryProvider {
    public final SmartInventory INVENTORY;
    private final TaskTime taskTime;
    private final boolean isTime2;

    public EditHourMenu(TaskTime taskTime, boolean isTime2) {
        this.taskTime = taskTime;
        this.isTime2 = isTime2;
        INVENTORY = SmartInventory.builder()
                .id("edit-hour")
                .provider(this)
                .manager(Main.getInventoryManager())
                .size(3, 9)
                .title("§9§lEdit hour")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fill(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        LocalTime usedTime = isTime2 ? taskTime.getTime2() : taskTime.getTime1();

        if (usedTime == null) {
            if (isTime2) {
                taskTime.setTime2(LocalTime.of(14, 0, 0));
                usedTime = taskTime.getTime2();
            } else {
                taskTime.setTime1(LocalTime.of(14, 0, 0));
                usedTime = taskTime.getTime1();
            }
        }

        LocalTime finalUsedTime = usedTime;
        // Hours
        contents.set(0, 2, ClickableItem.of(Items.getAddItem(), e -> {
            setTime(finalUsedTime.plusHours(1));
            refresh(player);
        }));
        contents.set(1, 2, ClickableItem.empty(Items.generateItem("§b" + usedTime.getHour() + " hours", XMaterial.CLOCK)));
        contents.set(2, 2, ClickableItem.of(Items.getSubstractItem(), e -> {
            setTime(finalUsedTime.minusHours(1));
            refresh(player);
        }));

        // Minutes
        contents.set(0, 4, ClickableItem.of(Items.getAddItem(), e -> {
            setTime(finalUsedTime.plusMinutes(1));
            refresh(player);
        }));
        contents.set(1, 4, ClickableItem.empty(Items.generateItem("§b" + usedTime.getMinute() + " minutes", XMaterial.CLOCK)));
        contents.set(2, 4, ClickableItem.of(Items.getSubstractItem(), e -> {
            setTime(finalUsedTime.minusMinutes(1));
            refresh(player);
        }));

        // Seconds
        contents.set(0, 6, ClickableItem.of(Items.getAddItem(), e -> {
            setTime(finalUsedTime.plusSeconds(1));
            refresh(player);
        }));
        contents.set(1, 6, ClickableItem.empty(Items.generateItem("§b" + usedTime.getSecond() + " seconds", XMaterial.CLOCK)));
        contents.set(2, 6, ClickableItem.of(Items.getSubstractItem(), e -> {
            setTime(finalUsedTime.minusSeconds(1));
            refresh(player);
        }));

        contents.set(2, 8, ClickableItem.of(Items.getBackItem(), e -> new EditSpecificTimeMenu(taskTime).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private void refresh(Player player) { this.INVENTORY.open(player); }

    private void setTime(LocalTime time) {
        if (isTime2) {
            taskTime.setTime2(time);
        } else {
            taskTime.setTime1(time);
        }
    }
}
