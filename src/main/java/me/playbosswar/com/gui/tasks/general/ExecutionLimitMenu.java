package me.playbosswar.com.gui.tasks.general;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.gui.api.GenericNumberMenu;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class ExecutionLimitMenu implements InventoryProvider {
    private final SmartInventory inventory;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final Task task;

    public ExecutionLimitMenu(Player player, Task task) {
        this.task = task;
        this.inventory = SmartInventory.builder()
                .id(UUID.randomUUID().toString())
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(3, 9)
                .title(languageManager.get(LanguageKey.TASK_EXECUTION_LIMIT_GUI_TITLE))
                .build();

        this.inventory.open(player);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        List<String> limitLore = languageManager.getList(LanguageKey.TASK_EXECUTION_LIMIT_LORE);
        limitLore.add(languageManager.get(LanguageKey.GUI_CURRENT, String.valueOf(task.getExecutionLimit())));

        ItemStack limitItem = Items.generateItem("§bLimit", XMaterial.STICK, limitLore.toArray(new String[]{}));
        ClickableItem clickableLimitItem = ClickableItem.of(limitItem, e ->
                new GenericNumberMenu(player, languageManager.get(LanguageKey.EXECUTION_LIMIT_NUMBER_GUI_TITLE),
                        task.getExecutionLimit(), limit -> {
                    task.setExecutionLimit(limit);
                    this.inventory.open(player);
                }));
        contents.set(1, 1, clickableLimitItem);

        ItemStack toggleResetItem = Items.getToggleItem(LanguageKey.EXECUTION_LIMIT_RESET,
                languageManager.getList(LanguageKey.TOGGLE_RESET_LORE).toArray(new String[]{}),
                task.isResetExecutionsAfterRestart());
        ClickableItem clickableToggleResetItem = ClickableItem.of(toggleResetItem, e -> {
            task.toggleResetExecutionAfterRestart();
            this.inventory.open(player);
        });
        contents.set(1, 2, clickableToggleResetItem);

        contents.set(1, 7, ClickableItem.of(Items.getBackItem(),
                e -> new GeneralLimitsMenu(task).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
