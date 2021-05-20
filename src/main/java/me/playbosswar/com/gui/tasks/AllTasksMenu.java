package me.playbosswar.com.gui.tasks;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import me.playbosswar.com.Main;
import me.playbosswar.com.gui.HorizontalIteratorWithBorder;
import me.playbosswar.com.gui.MainMenu;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.ItemGeneratorHelpers;
import me.playbosswar.com.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;

public class AllTasksMenu implements InventoryProvider {
    public SmartInventory INVENTORY;

    public AllTasksMenu() {
        INVENTORY = SmartInventory.builder()
                .id("all-tasks")
                .provider(this)
                .manager(Main.getInventoryManager())
                .size(6, 9)
                .title("§9§lAll tasks")
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));
        Pagination pagination = contents.pagination();

        pagination.setItems(getAllTaskItems(player));
        new HorizontalIteratorWithBorder(player, contents, INVENTORY);

        contents.set(5, 8, ClickableItem.of(ItemGeneratorHelpers.getBackItem(), e -> new MainMenu().INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private ClickableItem[] getAllTaskItems(Player p) {
        ArrayList<Task> tasks = Main.getTasksManager().getLoadedTasks();

        ClickableItem[] items = new ClickableItem[tasks.size()];
        String[] lore = new String[]{ "", "§aLeft-Click to edit", "§cRight-Click to delete" };

        for (int i = 0; i < items.length; i++) {
            Task task = tasks.get(i);
            ItemStack item = ItemGeneratorHelpers.generateItem("§b" + task.getName(), XMaterial.MAP, lore);
            items[i] = ClickableItem.of(item, e -> {
                if (e.getClick().equals(ClickType.LEFT)) {
                    new EditTaskMenu(task).INVENTORY.open(p);
                    return;
                }

                if (e.getClick().equals(ClickType.RIGHT)) {
                    try {
                        Main.getTasksManager().removeTask(task);
                        this.INVENTORY.open(p);
                    } catch (IOException ioException) {
                        Messages.sendFailedIO(p);
                        ioException.printStackTrace();
                    }
                }
            });
        }

        return items;
    }
}
