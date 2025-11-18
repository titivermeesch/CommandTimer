package me.playbosswar.com.gui.tasks.general;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.gui.tasks.EditTaskMenu;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GeneralLimitsMenu implements InventoryProvider {
    public final SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final Task task;

    public GeneralLimitsMenu(Task task) {
        this.task = task;
        INVENTORY = SmartInventory.builder()
                .id("task-limits")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(3, 9)
                .title(languageManager.get(LanguageKey.TASK_LIMIT_GUI_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        List<String> executionLimitLore = languageManager.getList(
                LanguageKey.EXECUTION_LIMIT_LORE,
                String.valueOf(task.getExecutionLimit()),
                String.valueOf(task.getTimesExecuted()),
                String.valueOf(task.isResetExecutionsAfterRestart()));
        executionLimitLore.add("");
        executionLimitLore.add(languageManager.get(LanguageKey.LEFT_CLICK_EDIT));
        executionLimitLore.add(languageManager.get(LanguageKey.RIGHT_CLICK_RESET_EXECUTIONS));

        ItemStack executionLimitItem = Items.generateItem(LanguageKey.EXECUTION_LIMIT_TITLE, XMaterial.DIAMOND_AXE,
                executionLimitLore.toArray(new String[]{}));
        ClickableItem clickableExecutionLimitItem = ClickableItem.of(executionLimitItem, e -> {
            if(e.isLeftClick()) {
                new ExecutionLimitMenu(player, task);
                return;
            }

            if(e.isRightClick()) {
                task.setTimesExecuted(0);
                task.storeInstance();
                CommandTimerPlugin.getInstance().getTasksManager().resetScheduleForTask(task);
                this.INVENTORY.open(player);
            }
        });
        contents.set(1, 1, clickableExecutionLimitItem);

        contents.set(2, 8, ClickableItem.of(Items.getBackItem(), e -> new EditTaskMenu(task).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
