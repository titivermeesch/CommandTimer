package me.playbosswar.com.gui.tasks.scheduler;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.Main;
import me.playbosswar.com.gui.tasks.EditTaskMenu;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        String[] secondsItemLore = new String[]{ "",
                "§7Set an interval in seconds for this task.",
                "",
                "§7This means that your set of commands will be",
                "§7executed every x seconds on a", "§7regular base." };
        ItemStack secondsItem = Items.generateItem("§bSeconds", XMaterial.CLOCK, secondsItemLore);
        ClickableItem clickableSecondsItem = ClickableItem.of(secondsItem, e -> {});
        contents.set(1, 1, clickableSecondsItem);

        String[] hoursLore = new String[]{ "",
                "§7Configure specific points in time at which",
                "§7your task should be executed.",
                "",
                "§7This could for example be 13:15:00, that would",
                "§7execute your task every day at that time" };
        ItemStack timesItem = Items.generateItem("§bSpecific time", XMaterial.CLOCK, hoursLore);
        ClickableItem clickableTimesItem = ClickableItem.of(timesItem, e -> {});
        contents.set(1, 2, clickableTimesItem);

        String[] daysLore = new String[]{ "",
                "§7Set a limit on which days the task",
                "§7can be executed.",
                "",
                "§7This works with both seconds and specific points in time.",
                "",
                "§7You can for example choose to only execute a task",
                "§7during the weekend, or only on monday,..." };
        ItemStack daysItem = Items.generateItem("§bDays", XMaterial.CLOCK, daysLore);
        ClickableItem clickableDaysItem = ClickableItem.of(daysItem, e -> {});
        contents.set(1, 3, clickableDaysItem);

        contents.set(1, 7, ClickableItem.of(Items.getBackItem(),
                                            e -> new EditTaskMenu(task).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
