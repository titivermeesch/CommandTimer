package me.playbosswar.com.gui.tasks.commands;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import me.playbosswar.com.Main;
import me.playbosswar.com.gui.HorizontalIteratorWithBorder;
import me.playbosswar.com.gui.tasks.EditTaskMenu;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TaskCommand;
import me.playbosswar.com.enums.Gender;
import me.playbosswar.com.utils.ItemGeneratorHelpers;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class AllCommandsMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final Task task;

    public AllCommandsMenu(Task task) {
        this.task = task;
        INVENTORY = SmartInventory.builder()
                .id("task-commands")
                .provider(this)
                .manager(Main.getInventoryManager())
                .size(6, 9)
                .title("§9§lTask commands")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        Pagination pagination = contents.pagination();

        pagination.setItems(getAllCommands(player));
        new HorizontalIteratorWithBorder(player, contents, INVENTORY);

        contents.set(5, 8, ClickableItem.of(ItemGeneratorHelpers.getBackItem(), e -> new EditTaskMenu(task).INVENTORY.open(player)));

        String[] addItemLore = new String[]{ "",
                "§7Add a new command that will be",
                "§7executed on your specified schedule" };
        ItemStack addItem = ItemGeneratorHelpers.generateItem("§bAdd command", XMaterial.LIME_DYE, addItemLore);
        ClickableItem clickableAddItem = ClickableItem.of(addItem, e -> {
            TaskCommand taskCommand = new TaskCommand(task, "my command", Gender.CONSOLE);
            task.addCommand(taskCommand);
            new EditCommandMenu(taskCommand).INVENTORY.open(player);
        });
        contents.set(0, 0, clickableAddItem);
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private ClickableItem[] getAllCommands(Player p) {
        ArrayList<TaskCommand> commands = task.getCommands();

        if(commands == null) {
            return new ClickableItem[0];
        }

        ClickableItem[] items = new ClickableItem[commands.size()];
        String[] lore = new String[]{ "", "§aLeft-Click to edit", "§cRight-Click to delete" };

        for (int i = 0; i < items.length; i++) {
            TaskCommand taskCommand = commands.get(i);
            String command = taskCommand.getCommand();
            ItemStack item = ItemGeneratorHelpers.generateItem("§b" + command, XMaterial.LEVER, lore);

            items[i] = ClickableItem.of(item, e -> {
                if (e.getClick().equals(ClickType.LEFT)) {
                    new EditCommandMenu(taskCommand).INVENTORY.open(p);
                    return;
                }

                if (e.getClick().equals(ClickType.RIGHT)) {
                    task.removeCommand(taskCommand);
                    this.INVENTORY.open(p);
                }
            });
        }

        return items;
    }
}
