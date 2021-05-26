package me.playbosswar.com.gui.tasks.general;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.Main;
import me.playbosswar.com.gui.api.GenericNumberMenu;
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
                .manager(Main.getInventoryManager())
                .size(3, 9)
                .title("§9§lTask limits")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        String[] minPlayersLore = {
                "",
                "§7Define the minimum amount of players that",
                "§7are required to execute the task",
                "",
                "§b§lCurrent value: " + task.getMinPlayers()
        };
        ItemStack minPlayersItem = Items.generateItem("§bMinimum players", XMaterial.ZOMBIE_HEAD, minPlayersLore);
        ClickableItem clickableMinPlayersItem = ClickableItem.of(minPlayersItem, e ->
                new GenericNumberMenu(player, "§9§lMinimum players", task.getMinPlayers(), v -> {
                    task.setMinPlayers(v);
                    refresh(player);
                }));
        contents.set(1, 1, clickableMinPlayersItem);

        String[] maxPlayersLore = {
                "",
                "§7Define the maximum amount of players that",
                "§7can be online to execute the task",
                "",
                "§b§lCurrent value: " + task.getMaxPlayers()
        };
        ItemStack maxPlayersItem = Items.generateItem("§bMaximum players", XMaterial.ZOMBIE_HEAD, maxPlayersLore);
        ClickableItem clickableMaxPlayersItem = ClickableItem.of(maxPlayersItem, e ->
                new GenericNumberMenu(player, "§9§lMaximum players", task.getMaxPlayers(), v -> {
                    task.setMaxPlayers(v);
                    refresh(player);
                }));
        contents.set(1, 2, clickableMaxPlayersItem);

        String[] permissionLore = {
                "",
                "§7Configure a permission that players need",
                "§7for the command to be executed for them.",
                "",
                "§7You can start your permission with a - to",
                "§7Exclude a permission",
                "",
                "§b§lCurrent value: " + task.getRequiredPermission()
        };
        ItemStack permissionItem = Items.generateItem("§bRequired permission", XMaterial.DIAMOND_HELMET, permissionLore);
        ClickableItem clickablePermissionItem = ClickableItem.of(permissionItem, e -> new EditPermissionMenu(player, task));
        contents.set(1, 3, clickablePermissionItem);

        ItemStack executionLimitItem = Items.generateItem("§bExecution limit", XMaterial.DIAMOND_AXE);
        ClickableItem clickableExecutionLimitItem = ClickableItem.of(executionLimitItem, e -> new ExecutionLimitMenu(player,
                                                                                                                     task));
        contents.set(1, 4, clickableExecutionLimitItem);

        contents.set(1, 7, ClickableItem.of(Items.getBackItem(), e -> new EditTaskMenu(task).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private void refresh(Player player) {
        this.INVENTORY.open(player);
    }
}
