package me.playbosswar.com.gui.tasks.general;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.gui.tasks.EditTaskMenu;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GeneralLimitsMenu implements InventoryProvider {
    public final SmartInventory INVENTORY;
    private final Task task;

    public GeneralLimitsMenu(Task task) {
        this.task = task;
        INVENTORY = SmartInventory.builder()
                .id("task-limits")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(3, 9)
                .title("§9§lTask limits")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        String[] executionLimitLore = {
                "",
                "§7Set a limit on how many times",
                "§7a task can be executed in total",
                "",
                "§7Current limit: §e" + task.getExecutionLimit(),
                "§7Current times executed: §e" + task.getTimesExecuted(),
                "§7Reset on restart: §e" + task.isResetExecutionsAfterRestart(),
                "",
                "§aLeft-Click to edit",
                "§aRight-Click to reset the times executed",
        };
        ItemStack executionLimitItem = Items.generateItem("§bExecution limit", XMaterial.DIAMOND_AXE, executionLimitLore);
        ClickableItem clickableExecutionLimitItem = ClickableItem.of(executionLimitItem, e -> {
            if (e.isLeftClick()) {
                new ExecutionLimitMenu(player, task);
                return;
            }

            if(e.isRightClick()) {
                task.setTimesExecuted(0);
                this.INVENTORY.open(player);
            }
        });
        contents.set(1, 1, clickableExecutionLimitItem);

        contents.set(1, 7, ClickableItem.of(Items.getBackItem(), e -> new EditTaskMenu(task).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private void refresh(Player player) {
        this.INVENTORY.open(player);
    }
}
