package me.playbosswar.com.gui.tasks;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.Main;
import me.playbosswar.com.gui.MainMenu;
import me.playbosswar.com.gui.tasks.commands.AllCommandsMenu;
import me.playbosswar.com.gui.tasks.scheduler.MainScheduleMenu;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.ItemGeneratorHelpers;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EditTaskMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final Task task;

    public EditTaskMenu(Task task) {
        this.task = task;
        INVENTORY = SmartInventory.builder()
                .id("edit-task")
                .provider(this)
                .manager(Main.getInventoryManager())
                .size(4, 9)
                .title("§9§lEdit " + task.getName())
                .build();
    }

    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        String[] nameLore = new String[]{"", "§7Change the display name of this task"};
        ItemStack nameItem = ItemGeneratorHelpers.generateItem("§bTask name", XMaterial.PAPER, nameLore);
        ClickableItem clickableNameItem = ClickableItem.of(nameItem, e -> new TaskNameMenu(player, task).INVENTORY.open(player));
        contents.set(1, 1, clickableNameItem);

        String[] scheduleLore = new String[]{"", "§7Choose when this task will be executed"};
        ItemStack scheduleItem = ItemGeneratorHelpers.generateItem("§bSchedule settings", XMaterial.CLOCK, scheduleLore);
        ClickableItem clickableScheduleItem = ClickableItem.of(scheduleItem, e -> new MainScheduleMenu(task).INVENTORY.open(player));
        contents.set(1, 2, clickableScheduleItem);

        String[] commandsLore = new String[]{"", "§7Choose which commands to execute"};
        ItemStack commandsItem = ItemGeneratorHelpers.generateItem("§bCommands", XMaterial.COMMAND_BLOCK, commandsLore);
        ClickableItem clickableCommandsItem = ClickableItem.of(commandsItem, e -> new AllCommandsMenu(task).INVENTORY.open(player));
        contents.set(1, 3, clickableCommandsItem);

        String[] generalLimitsLore = new String[]{"",
                "§7Add more limits to this task to",
                "§7limit when the task can be executed.",
                "",
                "§bAvailable limits:",
                "§7  - Amount of players online",
                "§7  - "
        };

        ItemStack backItem = ItemGeneratorHelpers.getBackItem();
        ClickableItem clickableBack = ClickableItem.of(backItem, e -> new MainMenu().INVENTORY.open(player));
        contents.set(2, 7, clickableBack);
    }

    public void update(Player player, InventoryContents contents) {
    }
}
