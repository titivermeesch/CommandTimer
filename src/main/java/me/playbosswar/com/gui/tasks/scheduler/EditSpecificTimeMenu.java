package me.playbosswar.com.gui.tasks.scheduler;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.Main;
import me.playbosswar.com.gui.worlds.WorldSelector;
import me.playbosswar.com.tasks.TaskTime;
import me.playbosswar.com.utils.Callback;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EditSpecificTimeMenu implements InventoryProvider {
    public final SmartInventory INVENTORY;
    private final TaskTime taskTime;

    public EditSpecificTimeMenu(TaskTime taskTime) {
        this.taskTime = taskTime;
        INVENTORY = SmartInventory.builder()
                .id("task-times-specific")
                .provider(this)
                .manager(Main.getInventoryManager())
                .size(3, 9)
                .title("§9§lEdit specific time")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        String[] time1Lore = new String[]{ "",
                "§b§lCurrent value: " + (taskTime.getTime1() == null ? "Not set" : taskTime.getTime1().toString()),
                "",
                "§aLeft-Click to edit"
        };
        ItemStack time1Item = Items.generateItem("§bTime 1", XMaterial.CLOCK, time1Lore);
        ClickableItem clickableTime1Item = ClickableItem.of(time1Item, e -> {});
        contents.set(1, 1, clickableTime1Item);

        String[] time2Lore = new String[]{ "",
                "§7This time can be used if you want a range",
                "§7instead of a specific hour. You could use this",
                "§7to only execute a task between a set of hours, like",
                "§7between 12:00:00 and 14:00:00",
                "",
                "§b§lCurrent value: " + (taskTime.getTime2() == null ? "Not set" : taskTime.getTime2().toString()),
                "",
                "§aLeft-Click to edit",
                "§cRight-Click to delete"
        };
        ItemStack time2Item = Items.generateItem("§bTime 2", XMaterial.CLOCK, time2Lore);
        ClickableItem clickableTime2Item = ClickableItem.of(time2Item, e -> {});
        contents.set(1, 2, clickableTime2Item);

        String[] minecraftTimeLore = new String[]{ "",
                "§7If enabled, the configured time will be",
                "§7the time on your Minecraft server"
        };
        ItemStack minecraftTimeItem = Items.getToggleItem("§bIs Minecraft time", minecraftTimeLore, taskTime.isMinecraftTime());
        contents.set(1, 3, ClickableItem.of(minecraftTimeItem, e -> {
            taskTime.toggleMinecraftTime();
            this.INVENTORY.open(player);
        }));

        if (taskTime.isMinecraftTime()) {
            String[] worldLore = new String[]{ "",
                    "§7When your time is the minecraft time, you need",
                    "§7to specify which world will be used for the time.",
                    "",
                    "§7This does not affect where the actual task is executed",
                    "",
                    "§b§lCurrent world: " + (taskTime.getWorld() == null ? "Not set" : taskTime.getWorld())
            };
            ItemStack worldItem = Items.generateItem("§bUsed world for Minecraft time", XMaterial.MAP, worldLore);
            Callback worldCallback = new Callback() {
                @Override
                public <T> void execute(T data) {
                    List<String> worlds = (List<String>) data;
                    taskTime.setWorld(worlds.get(0));
                    new EditSpecificTimeMenu(taskTime).INVENTORY.open(player);
                }
            };

            ArrayList<String> worlds = new ArrayList<>();
            worlds.add(taskTime.getWorld());
            contents.set(1, 4, ClickableItem.of(worldItem, e -> new WorldSelector(worldCallback,
                                                                                  worlds, false).INVENTORY.open(player)));
        }

        contents.set(1, 7, ClickableItem.of(Items.getBackItem(),
                                            e -> new EditTimesMenu(taskTime.getTask()).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
