package me.playbosswar.com.gui.tasks.general;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.Main;
import me.playbosswar.com.gui.api.GenericNumberMenu;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ExecutionLimitMenu implements InventoryProvider {
    private final SmartInventory inventory;
    private final Player player;
    private final Task task;

    public ExecutionLimitMenu(Player player, Task task) {
        this.player = player;
        this.task = task;
        this.inventory = SmartInventory.builder()
                .id(UUID.randomUUID().toString())
                .provider(this)
                .manager(Main.getInventoryManager())
                .size(3, 9)
                .title("§9§lTask execution limit")
                .build();

        this.inventory.open(player);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        String[] limitLore = {
                "",
                "§7Set a limit on how many times a task",
                "§7can be executed in total",
                "",
                "§b§lCurrent value: " + task.getExecutionLimit()
        };
        ItemStack limitItem = Items.generateItem("§bLimit", XMaterial.STICK, limitLore);
        ClickableItem clickableLimitItem = ClickableItem.of(limitItem, e ->
                new GenericNumberMenu(player, "Execution limit", task.getExecutionLimit(), limit -> {
                    task.setExecutionLimit(limit);
                    this.inventory.open(player);
                }));
        contents.set(1, 1, clickableLimitItem);

        String[] toggleResetLore = {
                "",
                "§7If enabled, the number of executions",
                "§7will reset after a restart. The actual limit",
                "§7you configured will stay but we will start",
                "§7counting from 0 again.",
                "",
                "§7This can be very helpful if you want a task",
                "§7to happen on server start/reload only"
        };
        ItemStack toggleResetItem = Items.getToggleItem("§bReset executions after restart", toggleResetLore, task.isResetExecutionsAfterRestart());
        ClickableItem clickableToggleResetItem = ClickableItem.of(toggleResetItem, e -> {
            task.toggleResetExecutionAfterRestart();
            this.inventory.open(player);
        });
        contents.set(1, 2, clickableToggleResetItem);

        contents.set(1, 7, ClickableItem.of(Items.getBackItem(), e -> new GeneralLimitsMenu(task).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
