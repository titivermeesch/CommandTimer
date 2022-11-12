package me.playbosswar.com.gui.tasks.commands;

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

import java.util.List;

public class CommandSettingsMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final Task task;

    public CommandSettingsMenu(Task task) {
        this.task = task;
        INVENTORY = SmartInventory.builder()
                .id("command-settings")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(3, 9)
                .title(languageManager.get(LanguageKey.COMMAND_SETTINGS_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        List<String> resetIndexItemLore = languageManager.getList(LanguageKey.RESET_COMMAND_INDEX_LORE);
        ItemStack resetIndexItem = Items.generateItem(LanguageKey.RESET_COMMAND_INDEX_TITLE, XMaterial.YELLOW_DYE,
                resetIndexItemLore.toArray(new String[]{}));
        ClickableItem clickableResetIndexItem = ClickableItem.of(resetIndexItem, e -> {
            task.setLastExecutedCommandIndex(task.getCommands().size() - 1);
            new CommandSettingsMenu(task).INVENTORY.open(player);
        });
        contents.set(1, 1, clickableResetIndexItem);

        contents.set(2, 8, ClickableItem.of(Items.getBackItem(),
                e -> new AllCommandsMenu(task).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
