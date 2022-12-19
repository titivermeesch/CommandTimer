package me.playbosswar.com.gui.events;

import com.cryptomorin.xseries.XMaterial;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.playbosswar.com.CommandTimerPlugin;
import me.playbosswar.com.api.ConditionExtension;
import me.playbosswar.com.api.events.EventExtension;
import me.playbosswar.com.language.LanguageKey;
import me.playbosswar.com.language.LanguageManager;
import me.playbosswar.com.tasks.Task;
import me.playbosswar.com.utils.Items;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ConfigureEventMenu implements InventoryProvider {
    public SmartInventory INVENTORY;
    private final LanguageManager languageManager = CommandTimerPlugin.getLanguageManager();
    private final ConditionExtension extension;
    private final EventExtension eventExtension;
    private final Task task;

    public ConfigureEventMenu(Task task, ConditionExtension extension, EventExtension eventExtension) {
        this.extension = extension;
        this.task = task;
        this.eventExtension = eventExtension;
        INVENTORY = SmartInventory.builder()
                .id("configure-event")
                .provider(this)
                .manager(CommandTimerPlugin.getInstance().getInventoryManager())
                .size(3, 9)
                .title(languageManager.get(LanguageKey.CONFIGURE_EVENT_GUI_TITLE))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(ClickableItem.empty(XMaterial.BLUE_STAINED_GLASS_PANE.parseItem()));

        ClickableItem conditionTypeItem = getConditionTypeItem(type -> {
            condition.setConditionType(type);
            this.INVENTORY.open(player);
        });
        contents.set(1, 1, conditionTypeItem);

        // 1. Item for condition type (SIMPLE, AND, OR, NOT)

        // 2. With simple/or, we show a second item to configure all the values

        // 3. If and/or, show menu to create subconditions. This should be very similar to conditions engine and
        // maybe we can reuse it

        String[] valuesLore =
                languageManager.getList(LanguageKey.CONFIGURE_EVENT_VALUES_ITEM_LORE).toArray(new String[]{});
        ItemStack valuesItem = Items.generateItem(
                LanguageKey.CONFIGURE_EVENT_VALUES_ITEM_TITLE,
                XMaterial.PAPER,
                valuesLore);
        contents.set(1, 2, ClickableItem.of(valuesItem, e -> new ConfigureEventValuesMenu(task, extension,
                eventExtension).INVENTORY.open(player)));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}
