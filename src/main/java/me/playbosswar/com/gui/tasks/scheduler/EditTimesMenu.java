package me.playbosswar.com.gui.tasks.scheduler;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.gui.HorizontalIteratorWithBorder;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.tasks.TaskTime;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.time.LocalTime;
import java.util.List;

public class EditTimesMenu implements InventoryProvider {
    public final SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final Task task;

    public EditTimesMenu(Task task) {
        this.task = task;
        INVENTORY = SmartInventory.builder()
                .id("task-times")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(6, 9)
                .title(languageManager.get(LanguageKey.TASK_TIMES_GUI_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));
        Pagination pagination = contents.pagination();

        pagination.setItems(getAllTimes(player));
        new HorizontalIteratorWithBorder(player, contents, INVENTORY);

        String[] addItemLore = languageManager.getList(LanguageKey.ADD_SPECIFIC_TIME_LORE).toArray(new String[]{});
        ItemStack addItem = Items.generateItem(LanguageKey.ADD_SPECIFIC_TIME_TITLE, XMaterial.ANVIL, addItemLore);
        ClickableItem clickableAddItem = ClickableItem.of(addItem, e -> {
            TaskTime taskTime = new TaskTime(task, LocalTime.parse("14:00:00"), false);
            task.addTime(taskTime);
            new EditSpecificTimeMenu(taskTime).INVENTORY.open(player);
        });
        contents.set(0, 0, clickableAddItem);

        contents.set(5, 8, ClickableItem.of(Items.getBackItem(),
                e -> new MainScheduleMenu(task).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }

    private ClickableItem[] getAllTimes(Player p) {
        List<TaskTime> taskTimes = task.getTimes();

        ClickableItem[] items = new ClickableItem[taskTimes.size()];

        for(int i = 0; i < items.length; i++) {
            TaskTime taskTime = taskTimes.get(i);

            String[] lore = new String[]{"",
                    languageManager.get(LanguageKey.MINECRAFT_TIME_TITLE) + ": " + (taskTime.isMinecraftTime() ?
                            languageManager.get(LanguageKey.YES_LABEL) :
                            languageManager.get(LanguageKey.NO_LABEL)),
                    "",
                    languageManager.get(LanguageKey.LEFT_CLICK_EDIT),
                    languageManager.get(LanguageKey.RIGHT_CLICK_DELETE),
            };

            ItemStack item = Items.generateItem("§b" + taskTime, XMaterial.CLOCK, lore);
            items[i] = ClickableItem.of(item, e -> {
                if(e.getClick().equals(ClickType.LEFT)) {
                    new EditSpecificTimeMenu(taskTime).INVENTORY.open(p);
                    return;
                }

                if(e.getClick().equals(ClickType.RIGHT)) {
                    task.removeTime(taskTime);
                    this.INVENTORY.open(p);
                }
            });
        }

        return items;
    }
}
