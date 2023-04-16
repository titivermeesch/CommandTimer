package me.playbosswar.com.gui.tasks.scheduler;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.DayOfWeek;

public class EditDaysMenu implements InventoryProvider {
    public final SmartInventory INVENTORY;
    private final Task task;

    public EditDaysMenu(Task task) {
        this.task = task;
        LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
        INVENTORY = SmartInventory.builder()
                .id("task-days")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(3, 9)
                .title(languageManager.get(LanguageKey.EDIT_DAYS_GUI_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));
        
        int i = 1;
        for(DayOfWeek day : DayOfWeek.values()) {
            ItemStack item = Items.getToggleItem("§b" + day, new String[]{}, task.getDays().contains(day));
            ClickableItem clickableItem = ClickableItem.of(item, e -> {
                task.toggleDay(day);
                this.INVENTORY.open(player);
            });
            contents.set(1, i, clickableItem);
            i++;
        }

        contents.set(2, 8, ClickableItem.of(Items.getBackItem(),
                e -> new MainScheduleMenu(task).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
